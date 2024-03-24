package cn.common.service.impl.biz.platform;

import cn.common.enums.BizErrorCode;
import cn.common.repository.entity.biz.OrderAddress;
import cn.common.repository.entity.biz.SalesItem;
import cn.common.repository.repository.biz.OrderAddressRepository;
import cn.common.req.biz.platform.SetTradeOrderReq;
import cn.common.req.biz.platform.TradeOrderAddReq;
import cn.common.req.biz.platform.TradeOrderReq;
import cn.common.req.biz.platform.TradeOrderUpdateReq;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.resp.biz.platform.TradeOrderExportResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.platform.TradeOrderService;
import cn.common.repository.entity.biz.TradeOrder;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.excel.ExportExcelHandler;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * @Description: 交易订单信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("tradeOrderService")
@Slf4j
public class TradeOrderServiceImpl implements TradeOrderService {

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private OrderAddressRepository orderAddressRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthAppUserService authAppUserService;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;


    /**
     * 导出数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(TradeOrderReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        MPJLambdaWrapper<TradeOrder> pageWrapper = new MPJLambdaWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,TradeOrder::getCreateTime);
        List<TradeOrder> pageList = tradeOrderRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<TradeOrderExportResp> respList =
            mapperFacade.mapAsList(pageList, TradeOrderExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), TradeOrderExportResp.class).build();
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
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(TradeOrderAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String takeAddressId = addReq.getTakeAddressId();
        TradeOrder entity = mapperFacade.map(addReq, TradeOrder.class);
        entity.setId(null);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setTradeOrderId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+ StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        tradeOrderRepository.insert(entity);
        //插入收货地址信息
        insertOrderAddress(entity,takeAddressId);
    }

    /**
     *
     * @description: 插入收货地址信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/3/7
     * @param tradeOrder  交易订单信息
     * @param takeAddressId  收货地址ID
     * @return
     */
    private void insertOrderAddress(TradeOrder tradeOrder,String takeAddressId){
        OrderAddress entity = new OrderAddress();
        entity.setAuthAppUserId(tradeOrder.getAuthAppUserId());
        entity.setOrderId(tradeOrder.getTradeOrderId());
        entity.setTakeAddressId(takeAddressId);
        entity.setOrderAddressId(SnowflakeIdWorker.uniqueMainId());
        try{
            //防止字段为空
            BaseUtil.setFieldValueNotNull(entity);
            orderAddressRepository.insert(entity);
        }catch (Exception e){
            log.error(">>>>>>>>>>>下单出现异常 : {} , {} <<<<<<<<<",e.getMessage(),e);
            throw new BusinessException(BizErrorCode.WX_PAY_SIGN_ERROR.getCode(),
                    BizErrorCode.WX_PAY_SIGN_ERROR.getMessage());
        }
    }

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<TradeOrder> entityList = tradeOrderRepository.selectList(
            new MPJLambdaWrapper<TradeOrder>().in(TradeOrder::getTradeOrderId,mainIdList));
        entityList.stream().forEach(item -> {
            //删除原来的收货地址信息
            orderAddressRepository.delete(new LambdaQueryWrapper<OrderAddress>()
                    .eq(OrderAddress::getOrderId,item.getTradeOrderId()));
            tradeOrderRepository.deleteById(item);
        });
    }

     /**
       *
       * @description: 查询单个交易订单数据
       * @author: create by singer - Singer email:singer-coder@qq.com
       * @date 2024-03-06
       * @param  outTradeNo 外部交易订单号
       * @return cn.common.resp.biz.openBiz.TradeOrderResp
       */
    @Override
    public TradeOrderResp queryByOrderNo(String outTradeNo){
         String authUserId = authAppUserService.authAppUserId();
         TradeOrder entity = tradeOrderRepository.selectOne(new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getOutTradeNo, outTradeNo)
                .eq(TradeOrder::getAuthAppUserId, authUserId));
        if(CheckParam.isNull(entity)){
            return new TradeOrderResp();
        }
        return mapperFacade.map(entity,TradeOrderResp.class);
    }

     /**
       *
       * @description: 查询当前用户的所有订单信息
       * @author: create by singer - Singer email:singer-coder@qq.com
       * @date 2024-03-06
       * @return java.util.List
       */
    @Override
    public List<TradeOrderResp> queryOrderList(){
        String authUserId = authAppUserService.authAppUserId();
        MPJLambdaWrapper<TradeOrder> queryWrapper = new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getAuthAppUserId, authUserId)
                .orderBy(true, false, TradeOrder::getCreateTime);
        queryWrapper.leftJoin(OrderAddress.class,OrderAddress::getOrderId,TradeOrder::getTradeOrderId);
        queryWrapper.leftJoin(SalesItem.class,SalesItem::getSalesItemId,TradeOrder::getItemId);
        queryWrapper.selectAll(TradeOrder.class);
        queryWrapper.selectAs(OrderAddress::getTakeAddressId,TradeOrderResp::getTakeAddressId);
        queryWrapper.selectAs(SalesItem::getSalesItemId,TradeOrderResp::getSalesItemId);
        queryWrapper.selectAs(SalesItem::getPublisherId,TradeOrderResp::getPublisherId);
        queryWrapper.selectAs(SalesItem::getTypeItemId,TradeOrderResp::getTypeItemId);
        queryWrapper.selectAs(SalesItem::getItemName,TradeOrderResp::getItemName);
        queryWrapper.selectAs(SalesItem::getItemSummary,TradeOrderResp::getItemSummary);
        queryWrapper.selectAs(SalesItem::getItemTitle,TradeOrderResp::getItemTitle);
        queryWrapper.selectAs(SalesItem::getSalePrice,TradeOrderResp::getSalePrice);
        queryWrapper.selectAs(SalesItem::getOriginalPrice,TradeOrderResp::getOriginalPrice);
        queryWrapper.selectAs(SalesItem::getMainImg,TradeOrderResp::getMainImg);
        queryWrapper.selectAs(SalesItem::getItemStatus,TradeOrderResp::getItemStatus);
        queryWrapper.selectAs(SalesItem::getRemarkData,TradeOrderResp::getRemarkData);
        List<TradeOrderResp> respList = tradeOrderRepository.selectJoinList(TradeOrderResp.class,queryWrapper);
        if(CollectionUtils.isEmpty(respList)){
            return Lists.newArrayList();
        }
        return respList;
    }

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<TradeOrderResp> queryAllTradeOrder(){
        List<TradeOrder> entityList = tradeOrderRepository.selectList(new MPJLambdaWrapper<>());
            if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,TradeOrderResp.class);
    }

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<TradeOrderResp> queryByPage(
        TradeOrderReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        MPJLambdaWrapper<TradeOrder> pageWrapper = new MPJLambdaWrapper<>();
        pageWrapper.leftJoin(OrderAddress.class,OrderAddress::getOrderId,TradeOrder::getTradeOrderId);
        pageWrapper.leftJoin(SalesItem.class,SalesItem::getSalesItemId,TradeOrder::getItemId);
        pageWrapper.selectAll(TradeOrder.class);
        pageWrapper.selectAs(OrderAddress::getTakeAddressId,TradeOrderResp::getTakeAddressId);
        pageWrapper.selectAs(SalesItem::getSalesItemId,TradeOrderResp::getSalesItemId);
        pageWrapper.selectAs(SalesItem::getPublisherId,TradeOrderResp::getPublisherId);
        pageWrapper.selectAs(SalesItem::getTypeItemId,TradeOrderResp::getTypeItemId);
        pageWrapper.selectAs(SalesItem::getItemName,TradeOrderResp::getItemName);
        pageWrapper.selectAs(SalesItem::getItemSummary,TradeOrderResp::getItemSummary);
        pageWrapper.selectAs(SalesItem::getItemTitle,TradeOrderResp::getItemTitle);
        pageWrapper.selectAs(SalesItem::getSalePrice,TradeOrderResp::getSalePrice);
        pageWrapper.selectAs(SalesItem::getOriginalPrice,TradeOrderResp::getOriginalPrice);
        pageWrapper.selectAs(SalesItem::getMainImg,TradeOrderResp::getMainImg);
        pageWrapper.selectAs(SalesItem::getItemStatus,TradeOrderResp::getItemStatus);
        pageWrapper.selectAs(SalesItem::getRemarkData,TradeOrderResp::getRemarkData);
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,TradeOrder::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<TradeOrderResp> respList = tradeOrderRepository.selectJoinList(TradeOrderResp.class,pageWrapper);
        if (CollectionUtils.isEmpty(respList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
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
     * @date 2024-03-06
     * @param pageWrapper 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(MPJLambdaWrapper<TradeOrder> pageWrapper, TradeOrderReq pageReq){

        if(!CheckParam.isNull(pageReq.getItemId())){
            pageWrapper.eq(TradeOrder::getItemId,pageReq.getItemId());
        }

        if(!CheckParam.isNull(pageReq.getAuthAppUserId())){
            pageWrapper.eq(TradeOrder::getAuthAppUserId,pageReq.getAuthAppUserId());
        }

        if(!CheckParam.isNull(pageReq.getOrderType())){
            pageWrapper.eq(TradeOrder::getOrderType,pageReq.getOrderType());
        }

        if(!CheckParam.isNull(pageReq.getOrderRemark())){
            pageWrapper.like(TradeOrder::getOrderRemark,pageReq.getOrderRemark());
        }

        if(!CheckParam.isNull(pageReq.getOutTradeNo())){
            pageWrapper.like(TradeOrder::getOutTradeNo,pageReq.getOutTradeNo());
        }

        if(!CheckParam.isNull(pageReq.getOrderAmount())){
            pageWrapper.eq(TradeOrder::getOrderAmount,pageReq.getOrderAmount());
        }

        if(!CheckParam.isNull(pageReq.getPayType())){
            pageWrapper.eq(TradeOrder::getPayType,pageReq.getPayType());
        }

        if(!CheckParam.isNull(pageReq.getExtraData())){
            pageWrapper.like(TradeOrder::getExtraData,pageReq.getExtraData());
        }

        if(!CheckParam.isNull(pageReq.getOrderStatus())){
            pageWrapper.eq(TradeOrder::getOrderStatus,pageReq.getOrderStatus());
        }
    }

    /**
     * 更新订单状态
     * @author: Singer
     * @date 2024-03-06
     * @param req 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void setOrderStatus(SetTradeOrderReq req){
        log.info(">>>>>>>>>>>>>>>>>更新订单状态 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String mainId = req.getTradeOrderId();
        MPJLambdaWrapper<TradeOrder> lambdaWrapper = new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getTradeOrderId, mainId);
        lambdaWrapper.selectAll(TradeOrder.class);
        TradeOrder result = tradeOrderRepository.selectOne(lambdaWrapper);
        if (CheckParam.isNull(result)) {
            return;
        }
        if(!CheckParam.isNull(req.getOrderStatus())){
            result.setOrderStatus(req.getOrderStatus());
        }
        tradeOrderRepository.updateById(result);
    }

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(TradeOrderUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String takeAddressId = updateReq.getTakeAddressId();
        String mainId = updateReq.getTradeOrderId();
        MPJLambdaWrapper<TradeOrder> lambdaWrapper = new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getTradeOrderId, mainId);
        lambdaWrapper.selectAll(TradeOrder.class);
        TradeOrder result = tradeOrderRepository.selectOne(lambdaWrapper);
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        tradeOrderRepository.updateById(result);
        //删除原来的收货地址信息
        orderAddressRepository.delete(new LambdaQueryWrapper<OrderAddress>()
                .eq(OrderAddress::getOrderId,result.getTradeOrderId()));
        //插入收货地址信息
        insertOrderAddress(result,takeAddressId);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(TradeOrder entity,
        TradeOrderUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getItemId())){
            entity.setItemId(updateReq.getItemId());
        }
        if(!CheckParam.isNull(updateReq.getAuthAppUserId())){
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if(!CheckParam.isNull(updateReq.getOrderType())){
            entity.setOrderType(updateReq.getOrderType());
        }
        if(!CheckParam.isNull(updateReq.getOrderRemark())){
            entity.setOrderRemark(updateReq.getOrderRemark());
        }
        if(!CheckParam.isNull(updateReq.getOutTradeNo())){
            entity.setOutTradeNo(updateReq.getOutTradeNo());
        }
        if(!CheckParam.isNull(updateReq.getOrderAmount())){
            entity.setOrderAmount(updateReq.getOrderAmount());
        }
        if(!CheckParam.isNull(updateReq.getPayType())){
            entity.setPayType(updateReq.getPayType());
        }
        if(!CheckParam.isNull(updateReq.getExtraData())){
            entity.setExtraData(updateReq.getExtraData());
        }
        if(!CheckParam.isNull(updateReq.getOrderStatus())){
            entity.setOrderStatus(updateReq.getOrderStatus());
        }
    }
}
