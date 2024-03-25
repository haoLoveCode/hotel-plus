package cn.common.service.impl.biz.platform;

import cn.common.req.biz.openBiz.NoticeDataAddReq;
import cn.common.req.biz.openBiz.NoticeDataReq;
import cn.common.req.biz.openBiz.NoticeDataUpdateReq;
import cn.common.resp.biz.openBiz.NoticeDataResp;
import cn.common.resp.biz.openBiz.NoticeDataExportResp;
import cn.common.service.biz.platform.NoticeDataService;
import cn.common.repository.entity.biz.NoticeData;
import cn.common.repository.repository.biz.NoticeDataRepository;
import cn.common.service.platform.AuthUserService;
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
import com.google.common.collect.Lists;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * @Description: 公告信息相关服务方法实现
 * @date 2024-03-25
 */
@Service("noticeDataService")
@Slf4j
public class NoticeDataServiceImpl implements NoticeDataService {

    @Resource
    private NoticeDataRepository noticeDataRepository;

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
     * 导出公告信息数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(NoticeDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>公告信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<NoticeData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,NoticeData::getCreateTime);
        List<NoticeData> pageList = noticeDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<NoticeDataExportResp> respList =
            mapperFacade.mapAsList(pageList, NoticeDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), NoticeDataExportResp.class).build();
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
     * 新增公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增公告信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(NoticeDataAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增公告信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authUserService.currentAuthUserId();
        NoticeData entity = mapperFacade.map(addReq, NoticeData.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setNoticeDataId(mainId);
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增公告信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        noticeDataRepository.insert(entity);
    }

    /**
     * 批量删除公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的公告信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<NoticeData> entityList = noticeDataRepository.selectList(
            new LambdaQueryWrapper<NoticeData>().in(NoticeData::getNoticeDataId,mainIdList));
        entityList.stream().forEach(item -> {
            noticeDataRepository.deleteById(item);
        });
    }

    /**
     * 查询公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @Override
    public List<NoticeDataResp> queryNoticeData(NoticeDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询公告信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<NoticeData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,NoticeData::getCreateTime);
        List<NoticeData> entityList = noticeDataRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,NoticeDataResp.class);
    }

    /**
     * 查询单个公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @Override
    public NoticeDataResp queryOneNoticeData(NoticeDataReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个公告信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<NoticeData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,NoticeData::getCreateTime);
        NoticeData entity = noticeDataRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new NoticeDataResp();
        }
        return mapperFacade.map(entity,NoticeDataResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-25
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<NoticeData> wrapper,
                        NoticeDataReq req){

        if(!CheckParam.isNull(req.getNoticeDataId())){
            wrapper.eq(NoticeData::getNoticeDataId,req.getNoticeDataId());
        }

        if(!CheckParam.isNull(req.getDataTitle())){
            wrapper.like(NoticeData::getDataTitle,req.getDataTitle());
        }

        if(!CheckParam.isNull(req.getDataValue())){
            wrapper.like(NoticeData::getDataValue,req.getDataValue());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(NoticeData::getRemarkData,req.getRemarkData());
        }

        if(!CheckParam.isNull(req.getDataStatus())){
            wrapper.eq(NoticeData::getDataStatus,req.getDataStatus());
        }
    }

    /**
     * 分页查询公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询公告信息Req
     * @return Pagination
     */
    @Override
    public Pagination<NoticeDataResp> queryByPage(
        NoticeDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询公告信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<NoticeData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,NoticeData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<NoticeData> pageList = noticeDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<NoticeDataResp> respList =
            mapperFacade.mapAsList(pageList, NoticeDataResp.class);
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
     * @date 2024-03-25
     * @param pageWrapper 查询条件
     * @param pageReq 查询参数
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<NoticeData> pageWrapper,
                        NoticeDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getNoticeDataId())){
            pageWrapper.eq(NoticeData::getNoticeDataId,pageReq.getNoticeDataId());
        }

        if(!CheckParam.isNull(pageReq.getDataTitle())){
            pageWrapper.like(NoticeData::getDataTitle,pageReq.getDataTitle());
        }

        if(!CheckParam.isNull(pageReq.getDataValue())){
            pageWrapper.like(NoticeData::getDataValue,pageReq.getDataValue());
        }

        if(!CheckParam.isNull(pageReq.getRemarkData())){
            pageWrapper.like(NoticeData::getRemarkData,pageReq.getRemarkData());
        }

        if(!CheckParam.isNull(pageReq.getDataStatus())){
            pageWrapper.eq(NoticeData::getDataStatus,pageReq.getDataStatus());
        }
    }

    /**
     * 更新公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新公告信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(NoticeDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新公告信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getNoticeDataId();
        NoticeData result = noticeDataRepository.selectOne(new LambdaQueryWrapper<NoticeData>()
                    .eq(NoticeData::getNoticeDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        noticeDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(NoticeData entity,
        NoticeDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getDataTitle())){
            entity.setDataTitle(updateReq.getDataTitle());
        }
        if(!CheckParam.isNull(updateReq.getDataValue())){
            entity.setDataValue(updateReq.getDataValue());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
        if(!CheckParam.isNull(updateReq.getDataStatus())){
            entity.setDataStatus(updateReq.getDataStatus());
        }
    }
}
