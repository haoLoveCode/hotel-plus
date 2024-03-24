package cn.common.service.impl.biz.platform;

import cn.common.req.biz.FeedbackDataAddReq;
import cn.common.req.biz.FeedbackDataReq;
import cn.common.req.biz.FeedbackDataUpdateReq;
import cn.common.resp.biz.FeedbackDataResp;
import cn.common.resp.biz.FeedbackDataExportResp;
import cn.common.service.biz.platform.FeedbackDataService;
import cn.common.repository.entity.biz.FeedbackData;
import cn.common.repository.repository.biz.FeedbackDataRepository;
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
 * @Description: 投诉建议信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("feedbackDataService")
@Slf4j
public class FeedbackDataServiceImpl implements FeedbackDataService {

    @Resource
    private FeedbackDataRepository feedbackDataRepository;

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
     * 导出投诉建议信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(FeedbackDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>投诉建议信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<FeedbackData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,FeedbackData::getCreateTime);
        List<FeedbackData> pageList = feedbackDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<FeedbackDataExportResp> respList =
            mapperFacade.mapAsList(pageList, FeedbackDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), FeedbackDataExportResp.class).build();
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
     * 新增投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增投诉建议信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(FeedbackDataAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增投诉建议信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        FeedbackData entity = mapperFacade.map(addReq, FeedbackData.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setFeedbackDataId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增投诉建议信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        feedbackDataRepository.insert(entity);
    }

    /**
     * 批量删除投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的投诉建议信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<FeedbackData> entityList = feedbackDataRepository.selectList(
            new LambdaQueryWrapper<FeedbackData>().in(FeedbackData::getFeedbackDataId,mainIdList));
        entityList.stream().forEach(item -> {
            feedbackDataRepository.deleteById(item);
        });
    }

    /**
     * 查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<FeedbackDataResp> queryFeedbackData(FeedbackDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询投诉建议信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<FeedbackData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,FeedbackData::getCreateTime);
        List<FeedbackData> entityList = feedbackDataRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,FeedbackDataResp.class);
    }

    /**
     * 查询单个投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public FeedbackDataResp queryOneFeedbackData(FeedbackDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个投诉建议信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<FeedbackData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,FeedbackData::getCreateTime);
        FeedbackData entity = feedbackDataRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new FeedbackDataResp();
        }
        return mapperFacade.map(entity,FeedbackDataResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/3/24
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<FeedbackData> wrapper,
                        FeedbackDataReq req){

        if(!CheckParam.isNull(req.getDataTitle())){
            wrapper.like(FeedbackData::getDataTitle,req.getDataTitle());
        }

        if(!CheckParam.isNull(req.getDataValue())){
            wrapper.like(FeedbackData::getDataValue,req.getDataValue());
        }

        if(!CheckParam.isNull(req.getSubmitterId())){
            wrapper.like(FeedbackData::getSubmitterId,req.getSubmitterId());
        }

        if(!CheckParam.isNull(req.getHandleStatus())){
            wrapper.like(FeedbackData::getHandleStatus,req.getHandleStatus());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(FeedbackData::getRemarkData,req.getRemarkData());
        }
    }

    /**
     * 分页查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询投诉建议信息Req
     * @return Pagination
     */
    @Override
    public Pagination<FeedbackDataResp> queryByPage(
        FeedbackDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询投诉建议信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<FeedbackData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,FeedbackData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<FeedbackData> pageList = feedbackDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<FeedbackDataResp> respList =
            mapperFacade.mapAsList(pageList, FeedbackDataResp.class);
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
     * @date 2024/3/24
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<FeedbackData> pageWrapper,
                        FeedbackDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getDataTitle())){
            pageWrapper.like(FeedbackData::getDataTitle,pageReq.getDataTitle());
        }

        if(!CheckParam.isNull(pageReq.getDataValue())){
            pageWrapper.like(FeedbackData::getDataValue,pageReq.getDataValue());
        }

        if(!CheckParam.isNull(pageReq.getSubmitterId())){
            pageWrapper.like(FeedbackData::getSubmitterId,pageReq.getSubmitterId());
        }

        if(!CheckParam.isNull(pageReq.getHandleStatus())){
            pageWrapper.like(FeedbackData::getHandleStatus,pageReq.getHandleStatus());
        }

        if(!CheckParam.isNull(pageReq.getRemarkData())){
            pageWrapper.like(FeedbackData::getRemarkData,pageReq.getRemarkData());
        }
    }

    /**
     * 更新投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新投诉建议信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(FeedbackDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新投诉建议信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getFeedbackDataId();
        FeedbackData result = feedbackDataRepository.selectOne(new LambdaQueryWrapper<FeedbackData>()
                    .eq(FeedbackData::getFeedbackDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        feedbackDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(FeedbackData entity,
        FeedbackDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getDataTitle())){
            entity.setDataTitle(updateReq.getDataTitle());
        }
        if(!CheckParam.isNull(updateReq.getDataValue())){
            entity.setDataValue(updateReq.getDataValue());
        }
        if(!CheckParam.isNull(updateReq.getSubmitterId())){
            entity.setSubmitterId(updateReq.getSubmitterId());
        }
        if(!CheckParam.isNull(updateReq.getHandleStatus())){
            entity.setHandleStatus(updateReq.getHandleStatus());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
    }
}
