package cn.common.service.impl.biz.platform;

import cn.common.req.biz.RoomDataAddReq;
import cn.common.req.biz.RoomDataReq;
import cn.common.req.biz.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataResp;
import cn.common.resp.biz.RoomDataExportResp;
import cn.common.service.biz.platform.RoomDataService;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.service.platform.AuthUserService;
import cn.hutool.extra.validation.ValidationUtil;
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
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import pro.skywalking.utils.SnowflakeIdWorker;
import com.google.common.collect.Lists;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间信息相关服务方法实现
 * @date 2024/1/25
 */
@Service("roomDataService")
@Slf4j
public class RoomDataServiceImpl implements RoomDataService {

    @Resource
    private RoomDataRepository roomDataRepository;

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
     * 导出房间信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(RoomDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>房间信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomData::getCreateTime);
        List<RoomData> pageList = roomDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomDataExportResp> respList =
            mapperFacade.mapAsList(pageList, RoomDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomDataExportResp.class).build();
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
     * 新增房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增房间信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomDataAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        RoomData entity = mapperFacade.map(addReq, RoomData.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomDataId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增房间信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        roomDataRepository.insert(entity);
    }

    /**
     * 批量删除房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的房间信息信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomData> entityList = roomDataRepository.selectList(
            new LambdaQueryWrapper<RoomData>().in(RoomData::getRoomDataId,mainIdList));
        entityList.stream().forEach(item -> {
            roomDataRepository.deleteById(item);
        });
    }

    /**
     * 查询房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomDataResp> queryRoomData(RoomDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomData::getCreateTime);
        List<RoomData> entityList = roomDataRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,RoomDataResp.class);
    }

    /**
     * 查询单个房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public RoomDataResp queryOneRoomData(RoomDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomData::getCreateTime);
        RoomData entity = roomDataRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomDataResp();
        }
        return mapperFacade.map(entity,RoomDataResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/1/25
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<RoomData> wrapper,
                        RoomDataReq req){

        if(!CheckParam.isNull(req.getRoomTitle())){
            wrapper.like(RoomData::getRoomTitle,req.getRoomTitle());
        }

        if(!CheckParam.isNull(req.getBriefData())){
            wrapper.like(RoomData::getBriefData,req.getBriefData());
        }

        if(!CheckParam.isNull(req.getRoomNo())){
            wrapper.like(RoomData::getRoomNo,req.getRoomNo());
        }

        if(!CheckParam.isNull(req.getRoomImg())){
            wrapper.like(RoomData::getRoomImg,req.getRoomImg());
        }

        if(!CheckParam.isNull(req.getRoomStatus())){
            wrapper.like(RoomData::getRoomStatus,req.getRoomStatus());
        }

        if(!CheckParam.isNull(req.getRoomFloor())){
            wrapper.like(RoomData::getRoomFloor,req.getRoomFloor());
        }

        if(!CheckParam.isNull(req.getRoomType())){
            wrapper.like(RoomData::getRoomType,req.getRoomType());
        }

        if(!CheckParam.isNull(req.getUnitPrice())){
            wrapper.like(RoomData::getUnitPrice,req.getUnitPrice());
        }

        if(!CheckParam.isNull(req.getRoomArea())){
            wrapper.like(RoomData::getRoomArea,req.getRoomArea());
        }

        if(!CheckParam.isNull(req.getBedNum())){
            wrapper.like(RoomData::getBedNum,req.getBedNum());
        }
    }

    /**
     * 分页查询房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询房间信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomDataResp> queryByPage(
        RoomDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询房间信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomData> pageList = roomDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<RoomDataResp> respList =
            mapperFacade.mapAsList(pageList, RoomDataResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<RoomData> pageWrapper,
                        RoomDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getRoomTitle())){
            pageWrapper.like(RoomData::getRoomTitle,pageReq.getRoomTitle());
        }

        if(!CheckParam.isNull(pageReq.getBriefData())){
            pageWrapper.like(RoomData::getBriefData,pageReq.getBriefData());
        }

        if(!CheckParam.isNull(pageReq.getRoomNo())){
            pageWrapper.like(RoomData::getRoomNo,pageReq.getRoomNo());
        }

        if(!CheckParam.isNull(pageReq.getRoomImg())){
            pageWrapper.like(RoomData::getRoomImg,pageReq.getRoomImg());
        }

        if(!CheckParam.isNull(pageReq.getRoomStatus())){
            pageWrapper.like(RoomData::getRoomStatus,pageReq.getRoomStatus());
        }

        if(!CheckParam.isNull(pageReq.getRoomFloor())){
            pageWrapper.like(RoomData::getRoomFloor,pageReq.getRoomFloor());
        }

        if(!CheckParam.isNull(pageReq.getRoomType())){
            pageWrapper.like(RoomData::getRoomType,pageReq.getRoomType());
        }

        if(!CheckParam.isNull(pageReq.getUnitPrice())){
            pageWrapper.like(RoomData::getUnitPrice,pageReq.getUnitPrice());
        }

        if(!CheckParam.isNull(pageReq.getRoomArea())){
            pageWrapper.like(RoomData::getRoomArea,pageReq.getRoomArea());
        }

        if(!CheckParam.isNull(pageReq.getBedNum())){
            pageWrapper.like(RoomData::getBedNum,pageReq.getBedNum());
        }
    }

    /**
     * 更新房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新房间信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新房间信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomDataId();
        RoomData result = roomDataRepository.selectOne(new LambdaQueryWrapper<RoomData>()
                    .eq(RoomData::getRoomDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomData entity,
        RoomDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRoomTitle())){
            entity.setRoomTitle(updateReq.getRoomTitle());
        }
        if(!CheckParam.isNull(updateReq.getBriefData())){
            entity.setBriefData(updateReq.getBriefData());
        }
        if(!CheckParam.isNull(updateReq.getRoomNo())){
            entity.setRoomNo(updateReq.getRoomNo());
        }
        if(!CheckParam.isNull(updateReq.getRoomImg())){
            entity.setRoomImg(updateReq.getRoomImg());
        }
        if(!CheckParam.isNull(updateReq.getRoomStatus())){
            entity.setRoomStatus(updateReq.getRoomStatus());
        }
        if(!CheckParam.isNull(updateReq.getRoomFloor())){
            entity.setRoomFloor(updateReq.getRoomFloor());
        }
        if(!CheckParam.isNull(updateReq.getRoomType())){
            entity.setRoomType(updateReq.getRoomType());
        }
        if(!CheckParam.isNull(updateReq.getUnitPrice())){
            entity.setUnitPrice(updateReq.getUnitPrice());
        }
        if(!CheckParam.isNull(updateReq.getRoomArea())){
            entity.setRoomArea(updateReq.getRoomArea());
        }
        if(!CheckParam.isNull(updateReq.getBedNum())){
            entity.setBedNum(updateReq.getBedNum());
        }
    }
}
