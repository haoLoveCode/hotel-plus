package cn.common.service.impl.biz;

import cn.common.repository.entity.biz.OrderData;
import cn.common.repository.repository.biz.OrderDataRepository;
import cn.common.req.biz.platform.OrderDataAddReq;
import cn.common.req.biz.platform.OrderDataReq;
import cn.common.req.biz.platform.OrderDataUpdateReq;
import cn.common.resp.biz.openBiz.OrderDataExportResp;
import cn.common.resp.biz.openBiz.OrderDataResp;
import cn.common.service.biz.OrderDataService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 订单信息相关服务方法实现
 * @date 2024-03-25
 */
@Service("orderDataService")
@Slf4j
public class OrderDataServiceImpl implements OrderDataService {

    @Resource
    private OrderDataRepository orderDataRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;

    /**
     * 导出数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(OrderDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<OrderData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,OrderData::getCreateTime);
        List<OrderData> pageList = orderDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<OrderDataExportResp> respList =
            mapperFacade.mapAsList(pageList, OrderDataExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), OrderDataExportResp.class).build();
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
     * 新增
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(OrderDataAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        OrderData entity = mapperFacade.map(addReq, OrderData.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setOrderDataId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+ StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        orderDataRepository.insert(entity);
    }

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<OrderData> entityList = orderDataRepository.selectList(
            new LambdaQueryWrapper<OrderData>().in(OrderData::getOrderDataId,mainIdList));
        entityList.stream().forEach(item -> {
            orderDataRepository.deleteById(item);
        });
    }

    /**
     * 查询单个的订单数据
     * @author: Singer
     * @date 2024-03-25
     * @param outTradeNo 交易订单号
     * @return java.util.List
     */
    @Override
    public OrderDataResp queryByTradeNo(String outTradeNo){
        OrderData entity = orderDataRepository.selectOne(new LambdaQueryWrapper<OrderData>()
                .eq(OrderData::getOutTradeNo, outTradeNo));
        if(CheckParam.isNull(entity)){
            return new OrderDataResp();
        }
        return mapperFacade.map(entity,OrderDataResp.class);
    }

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @Override
    public List<OrderDataResp> queryAllOrderData(){
        List<OrderData> entityList = orderDataRepository.selectList(new LambdaQueryWrapper<>());
            if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,OrderDataResp.class);
    }

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<OrderDataResp> queryByPage(
        OrderDataReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<OrderData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,OrderData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<OrderData> pageList = orderDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<OrderDataResp> respList =
            mapperFacade.mapAsList(pageList, OrderDataResp.class);
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
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<OrderData> pageWrapper, OrderDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getTradeOrderId())){
            pageWrapper.eq(OrderData::getTradeOrderId,pageReq.getTradeOrderId());
        }

        if(!CheckParam.isNull(pageReq.getOutTradeNo())){
            pageWrapper.eq(OrderData::getOutTradeNo,pageReq.getOutTradeNo());
        }

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            pageWrapper.eq(OrderData::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getItemId())){
            pageWrapper.eq(OrderData::getItemId,pageReq.getItemId());
        }

        if(!CheckParam.isNull(pageReq.getItemData())){
            pageWrapper.like(OrderData::getItemData,pageReq.getItemData());
        }

        if(!CheckParam.isNull(pageReq.getItemNum())){
            pageWrapper.like(OrderData::getItemNum,pageReq.getItemNum());
        }

        if(!CheckParam.isNull(pageReq.getOrderRemark())){
            pageWrapper.like(OrderData::getOrderRemark,pageReq.getOrderRemark());
        }
    }

    /**
     * 设置分页条件
     * @author: Singer
     * @date 2024-03-25
     * @param wrapper 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setCriteria(MPJLambdaWrapper<OrderData> wrapper, OrderDataReq pageReq){

        if(!CheckParam.isNull(pageReq.getTradeOrderId())){
            wrapper.eq(OrderData::getTradeOrderId,pageReq.getTradeOrderId());
        }

        if(!CheckParam.isNull(pageReq.getOutTradeNo())){
            wrapper.eq(OrderData::getOutTradeNo,pageReq.getOutTradeNo());
        }

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            wrapper.eq(OrderData::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getItemId())){
            wrapper.eq(OrderData::getItemId,pageReq.getItemId());
        }

        if(!CheckParam.isNull(pageReq.getItemData())){
            wrapper.like(OrderData::getItemData,pageReq.getItemData());
        }

        if(!CheckParam.isNull(pageReq.getItemNum())){
            wrapper.like(OrderData::getItemNum,pageReq.getItemNum());
        }

        if(!CheckParam.isNull(pageReq.getOrderRemark())){
            wrapper.like(OrderData::getOrderRemark,pageReq.getOrderRemark());
        }
    }

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(OrderDataUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getOrderDataId();
        OrderData result = orderDataRepository.selectOne(new LambdaQueryWrapper<OrderData>()
                    .eq(OrderData::getOrderDataId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        orderDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(OrderData entity,
        OrderDataUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTradeOrderId())){
            entity.setTradeOrderId(updateReq.getTradeOrderId());
        }
        if(!CheckParam.isNull(updateReq.getOutTradeNo())){
            entity.setOutTradeNo(updateReq.getOutTradeNo());
        }
        if(!CheckParam.isNull(updateReq.getAuthAppUserId())){
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if(!CheckParam.isNull(updateReq.getItemId())){
            entity.setItemId(updateReq.getItemId());
        }
        if(!CheckParam.isNull(updateReq.getItemData())){
            entity.setItemData(updateReq.getItemData());
        }
        if(!CheckParam.isNull(updateReq.getItemNum())){
            entity.setItemNum(updateReq.getItemNum());
        }
        if(!CheckParam.isNull(updateReq.getOrderRemark())){
            entity.setOrderRemark(updateReq.getOrderRemark());
        }
    }
}
