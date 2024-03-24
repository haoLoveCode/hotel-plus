package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.repository.biz.RoomBookingRepository;
import cn.common.req.biz.RoomBookingAddReq;
import cn.common.req.biz.RoomBookingReq;
import cn.common.req.biz.RoomBookingUpdateReq;
import cn.common.resp.biz.RoomBookingExportResp;
import cn.common.resp.biz.RoomBookingResp;
import cn.common.service.biz.platform.RoomBookingService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.excel.ExportExcelHandler;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间预订信息相关服务方法实现
 * @date 2024/1/25
 */
@Service("roomBookingService")
@Slf4j
public class RoomBookingServiceImpl implements RoomBookingService {

    @Resource
    private RoomBookingRepository roomBookingRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    /**
     * 导出房间预订信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(RoomBookingReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>房间预订信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomBooking> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomBooking::getCreateTime);
        List<RoomBooking> pageList = roomBookingRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomBookingExportResp> respList =
            mapperFacade.mapAsList(pageList, RoomBookingExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomBookingExportResp.class).build();
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
                excelWriter.write(respList,writeSheet);
            } finally {
                // 关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>导出数据异常:{},{}<<<<<<<<<<<<<<<<<<<<<<",e.getMessage(),e);
            ExportExcelHandler.setExportErrorResponse(response);
            Map<String, String> map = Maps.newHashMap();
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                log.error(">>>>>>>>>>>>>>>>>导出数据发生异常:{},{}<<<<<<<<<<<<<<<<<",e.getMessage(),e);
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 新增房间预订信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增房间预订信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomBookingAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增房间预订信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        RoomBooking entity = mapperFacade.map(addReq, RoomBooking.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomBookingId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增房间预订信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        roomBookingRepository.insert(entity);
    }

    /**
     * 批量删除房间预订信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的房间预订信息信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomBooking> entityList = roomBookingRepository.selectList(
            new LambdaQueryWrapper<RoomBooking>().in(RoomBooking::getRoomBookingId,mainIdList));
        entityList.stream().forEach(item -> {
            roomBookingRepository.deleteById(item);
        });
    }

    /**
     * 查询房间预订信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomBookingResp> queryRoomBooking(RoomBookingReq req){
        log.info(">>>>>>>>>>>>>>>>>查询房间预订信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomBooking> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomBooking::getCreateTime);
        List<RoomBooking> entityList = roomBookingRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,RoomBookingResp.class);
    }

    /**
     * 查询单个房间预订信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public RoomBookingResp queryOneRoomBooking(RoomBookingReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个房间预订信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomBooking> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomBooking::getCreateTime);
        RoomBooking entity = roomBookingRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomBookingResp();
        }
        return mapperFacade.map(entity,RoomBookingResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/1/25
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<RoomBooking> wrapper,
                        RoomBookingReq req){

        if(!CheckParam.isNull(req.getSubscriberId())){
            wrapper.like(RoomBooking::getSubscriberId,req.getSubscriberId());
        }

        if(!CheckParam.isNull(req.getBookingNo())){
            wrapper.like(RoomBooking::getBookingNo,req.getBookingNo());
        }

        if(!CheckParam.isNull(req.getRoomDataId())){
            wrapper.like(RoomBooking::getRoomDataId,req.getRoomDataId());
        }

        if(!CheckParam.isNull(req.getRemark())){
            wrapper.like(RoomBooking::getRemark,req.getRemark());
        }

        if(!CheckParam.isNull(req.getBookingTime())){
            wrapper.like(RoomBooking::getBookingTime,req.getBookingTime());
        }

        if(!CheckParam.isNull(req.getCheckInBegin())){
            wrapper.like(RoomBooking::getCheckInBegin,req.getCheckInBegin());
        }

        if(!CheckParam.isNull(req.getCheckInEnd())){
            wrapper.like(RoomBooking::getCheckInEnd,req.getCheckInEnd());
        }

        if(!CheckParam.isNull(req.getBookingStatus())){
            wrapper.like(RoomBooking::getBookingStatus,req.getBookingStatus());
        }
    }

    /**
     * 分页查询房间预订信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询房间预订信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomBookingResp> queryByPage(
        RoomBookingReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询房间预订信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomBooking> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomBooking::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomBooking> pageList = roomBookingRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<RoomBookingResp> respList =
            mapperFacade.mapAsList(pageList, RoomBookingResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 设置分页条件
     * @author: Singer
     * @date 2024/1/25
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<RoomBooking> pageWrapper,
                        RoomBookingReq pageReq){

        if(!CheckParam.isNull(pageReq.getSubscriberId())){
            pageWrapper.eq(RoomBooking::getSubscriberId,pageReq.getSubscriberId());
        }

        if(!CheckParam.isNull(pageReq.getBookingNo())){
            pageWrapper.like(RoomBooking::getBookingNo,pageReq.getBookingNo());
        }

        if(!CheckParam.isNull(pageReq.getRoomDataId())){
            pageWrapper.eq(RoomBooking::getRoomDataId,pageReq.getRoomDataId());
        }

        if(!CheckParam.isNull(pageReq.getRemark())){
            pageWrapper.like(RoomBooking::getRemark,pageReq.getRemark());
        }

        if(!CheckParam.isNull(pageReq.getBookingTime())){
            pageWrapper.ge(RoomBooking::getBookingTime,pageReq.getBookingTime());
        }

        if(!CheckParam.isNull(pageReq.getCheckInBegin())){
            pageWrapper.ge(RoomBooking::getCheckInBegin,pageReq.getCheckInBegin());
        }

        if(!CheckParam.isNull(pageReq.getCheckInEnd())){
            pageWrapper.le(RoomBooking::getCheckInEnd,pageReq.getCheckInEnd());
        }

        if(!CheckParam.isNull(pageReq.getBookingStatus())){
            pageWrapper.eq(RoomBooking::getBookingStatus,pageReq.getBookingStatus());
        }
    }

    /**
     * 更新房间预订信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新房间预订信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomBookingUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新房间预订信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomBookingId();
        RoomBooking result = roomBookingRepository.selectOne(new LambdaQueryWrapper<RoomBooking>()
                    .eq(RoomBooking::getRoomBookingId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomBookingRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomBooking entity,
        RoomBookingUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getSubscriberId())){
            entity.setSubscriberId(updateReq.getSubscriberId());
        }
        if(!CheckParam.isNull(updateReq.getBookingNo())){
            entity.setBookingNo(updateReq.getBookingNo());
        }
        if(!CheckParam.isNull(updateReq.getRoomDataId())){
            entity.setRoomDataId(updateReq.getRoomDataId());
        }
        if(!CheckParam.isNull(updateReq.getRemark())){
            entity.setRemark(updateReq.getRemark());
        }
        if(!CheckParam.isNull(updateReq.getBookingTime())){
            entity.setBookingTime(updateReq.getBookingTime());
        }
        if(!CheckParam.isNull(updateReq.getCheckInBegin())){
            entity.setCheckInBegin(updateReq.getCheckInBegin());
        }
        if(!CheckParam.isNull(updateReq.getCheckInEnd())){
            entity.setCheckInEnd(updateReq.getCheckInEnd());
        }
        if(!CheckParam.isNull(updateReq.getBookingStatus())){
            entity.setBookingStatus(updateReq.getBookingStatus());
        }
    }
}
