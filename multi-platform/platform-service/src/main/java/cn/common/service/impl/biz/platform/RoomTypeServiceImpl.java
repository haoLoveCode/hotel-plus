package cn.common.service.impl.biz.platform;

import cn.common.req.biz.RoomTypeAddReq;
import cn.common.req.biz.RoomTypeReq;
import cn.common.req.biz.RoomTypeUpdateReq;
import cn.common.resp.biz.RoomTypeResp;
import cn.common.resp.biz.RoomTypeExportResp;
import cn.common.service.biz.platform.RoomTypeService;
import cn.common.repository.entity.biz.RoomType;
import cn.common.repository.repository.biz.RoomTypeRepository;
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
 * @Description: 房间类型信息相关服务方法实现
 * @date 2024/1/25
 */
@Service("roomTypeService")
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {

    @Resource
    private RoomTypeRepository roomTypeRepository;

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
     * 导出房间类型信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(RoomTypeReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>房间类型信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomType> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomType::getCreateTime);
        List<RoomType> pageList = roomTypeRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomTypeExportResp> respList =
            mapperFacade.mapAsList(pageList, RoomTypeExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomTypeExportResp.class).build();
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
     * 新增房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增房间类型信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomTypeAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增房间类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        RoomType entity = mapperFacade.map(addReq, RoomType.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomTypeId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增房间类型信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        roomTypeRepository.insert(entity);
    }

    /**
     * 批量删除房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的房间类型信息信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomType> entityList = roomTypeRepository.selectList(
            new LambdaQueryWrapper<RoomType>().in(RoomType::getRoomTypeId,mainIdList));
        entityList.stream().forEach(item -> {
            roomTypeRepository.deleteById(item);
        });
    }

    /**
     * 查询房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomTypeResp> queryRoomType(RoomTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询房间类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomType::getCreateTime);
        List<RoomType> entityList = roomTypeRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,RoomTypeResp.class);
    }

    /**
     * 查询单个房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @Override
    public RoomTypeResp queryOneRoomType(RoomTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个房间类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomType::getCreateTime);
        RoomType entity = roomTypeRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomTypeResp();
        }
        return mapperFacade.map(entity,RoomTypeResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/1/25
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<RoomType> wrapper,
                        RoomTypeReq req){

        if(!CheckParam.isNull(req.getTypeName())){
            wrapper.like(RoomType::getTypeName,req.getTypeName());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(RoomType::getRemarkData,req.getRemarkData());
        }
    }

    /**
     * 分页查询房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询房间类型信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomTypeResp> queryByPage(
        RoomTypeReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询房间类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomType> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomType::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomType> pageList = roomTypeRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<RoomTypeResp> respList =
            mapperFacade.mapAsList(pageList, RoomTypeResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<RoomType> pageWrapper,
                        RoomTypeReq pageReq){

        if(!CheckParam.isNull(pageReq.getTypeName())){
            pageWrapper.like(RoomType::getTypeName,pageReq.getTypeName());
        }

        if(!CheckParam.isNull(pageReq.getRemarkData())){
            pageWrapper.like(RoomType::getRemarkData,pageReq.getRemarkData());
        }
    }

    /**
     * 更新房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新房间类型信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomTypeUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新房间类型信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomTypeId();
        RoomType result = roomTypeRepository.selectOne(new LambdaQueryWrapper<RoomType>()
                    .eq(RoomType::getRoomTypeId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomTypeRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomType entity,
        RoomTypeUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTypeName())){
            entity.setTypeName(updateReq.getTypeName());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
    }
}
