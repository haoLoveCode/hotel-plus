package cn.common.service.impl.biz.app;

import cn.common.constants.BizConstants;
import cn.common.enums.BasketHandleTypeEnum;
import cn.common.enums.BizErrorCode;
import cn.common.enums.OrderTypeEnum;
import cn.common.repository.entity.biz.*;
import cn.common.repository.repository.biz.*;
import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.req.biz.app.ItemOrderingOneReq;
import cn.common.req.biz.app.OrderingByBasketItemReq;
import cn.common.resp.biz.BasketDataResp;
import cn.common.resp.biz.app.AppBasketItemResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.app.AppBasketDataService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.enums.OrderStatusEnum;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 购物车信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("appBasketDataService")
@Slf4j
public class AppBasketDataServiceImpl implements AppBasketDataService {

    @Resource
    private BasketDataRepository basketDataRepository;

    @Resource
    private SalesItemRepository salesItemRepository;

    @Resource
    private OrderAddressRepository orderAddressRepository;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private OrderDataRepository orderDataRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthAppUserService authAppUserService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;


    /**
      *
      * @description: 商品单个信息下单
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-06
      * @param req  商品单个信息下单Req
      * @return
      */
    public TradeOrderResp orderingOne(ItemOrderingOneReq req){
        log.info(">>>>>>>>>>>>>>>>>商品单个信息下单Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        SalesItem salesItem = salesItemRepository.selectOne(new LambdaQueryWrapper<SalesItem>()
                .eq(SalesItem::getSalesItemId, req.getItemId()));
        if(CheckParam.isNull(salesItem)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "商品信息不可为空");
        }
        //收货地址ID
        String takeAddressId = req.getTakeAddressId();
        String authAppUserId = authAppUserService.authAppUserId();
        //内部支付交易订单号
        String outTradeNo = SnowflakeIdWorker.uniqueMainId();

        //设置交易订单信息
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setItemId(salesItem.getSalesItemId());
        tradeOrder.setOrderRemark(StrUtil.EMPTY);
        tradeOrder.setAuthAppUserId(authAppUserId);
        tradeOrder.setOrderAmount(salesItem.getSalePrice().multiply(BigDecimal.ONE));
        tradeOrder.setOutTradeNo(outTradeNo);
        tradeOrder.setOrderType(OrderTypeEnum.SALES_ITEM.getCode());
        //订单状态
        tradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        //支付方式 aliPay/支付宝  weChatPay/微信
        tradeOrder.setPayType("weChatPay");
        tradeOrder.setTradeOrderId(SnowflakeIdWorker.uniqueMainId());

        //存入交易数据
        setOrderData(tradeOrder,salesItem);
        //插入收货地址信息
        insertOrderAddress(tradeOrder,takeAddressId);
        try{
            //防止字段为空
            BaseUtil.setFieldValueNotNull(tradeOrder);
            tradeOrderRepository.insert(tradeOrder);

            //缓存支付信息
                /*redisRepository.set(cachePrefix,
                        JSON.toJSONString(wxMiniPayResp),
                        Long.valueOf(BizConstants.WX_ORDER_EXPIRED),
                        TimeUnit.MINUTES);
                return wxMiniPayResp;*/
        }catch (Exception e){
            log.error(">>>>>>>>>>>下单出现异常 : {} , {} <<<<<<<<<",e.getMessage(),e);
            throw new BusinessException(BizErrorCode.WX_PAY_SIGN_ERROR.getCode(),
                    BizErrorCode.WX_PAY_SIGN_ERROR.getMessage());
        }
        TradeOrderResp resp = mapperFacade.map(tradeOrder, TradeOrderResp.class);
        return resp;
    }

    /**
      *
      * @description: 购物车商品信息下单
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-06
      * @param req 购物车商品信息下单参数
      * @return
      */
    public String basketItemOrdering(OrderingByBasketItemReq req){
        log.info(">>>>>>>>>>>>>>>>>购物车商品信息下单Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));

        List<String> basketDataIdList = req.getBasketDataIdList();
        if(CollectionUtils.isEmpty(basketDataIdList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "购物车商品信息ID不可为空");
        }
        //收货地址ID
        String takeAddressId = req.getTakeAddressId();
        String authAppUserId = authAppUserService.authAppUserId();
        MPJLambdaWrapper<BasketData> backetDataWrapper = new MPJLambdaWrapper<BasketData>()
                .eq(BasketData::getAuthAppUserId, authAppUserId)
                .in(BasketData::getBasketDataId, basketDataIdList);
        backetDataWrapper.leftJoin(SalesItem.class, SalesItem::getSalesItemId, BasketData::getSalesItemId);
        backetDataWrapper.selectAll(BasketData.class);
        backetDataWrapper.selectAs(SalesItem::getItemName, AppBasketItemResp::getItemName);
        backetDataWrapper.selectAs(SalesItem::getItemSummary, AppBasketItemResp::getItemSummary);
        backetDataWrapper.selectAs(SalesItem::getItemTitle, AppBasketItemResp::getItemTitle);
        backetDataWrapper.selectAs(SalesItem::getSalePrice, AppBasketItemResp::getSalePrice);
        backetDataWrapper.selectAs(SalesItem::getOriginalPrice, AppBasketItemResp::getOriginalPrice);
        backetDataWrapper.selectAs(SalesItem::getMainImg, AppBasketItemResp::getMainImg);
        List<AppBasketItemResp> basketDataList = basketDataRepository.selectJoinList(AppBasketItemResp.class,backetDataWrapper);
        if(CollectionUtils.isEmpty(basketDataList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "购物车商品信息不存在");
        }
        //内部支付交易订单号
        String outTradeNo = SnowflakeIdWorker.uniqueMainId();
        basketDataList.stream().forEach(item -> {
            //设置交易订单信息
            TradeOrder tradeOrder = new TradeOrder();
            tradeOrder.setItemId(item.getSalesItemId());
            tradeOrder.setOrderRemark(StrUtil.EMPTY);
            tradeOrder.setAuthAppUserId(authAppUserId);
            tradeOrder.setOrderAmount(item.getSalePrice().multiply(BigDecimal.valueOf(item.getItemNum())));
            tradeOrder.setOutTradeNo(outTradeNo);
            tradeOrder.setOrderType(OrderTypeEnum.SALES_ITEM.getCode());
            //订单状态
            tradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
            //支付方式 aliPay/支付宝  weChatPay/微信
            tradeOrder.setPayType("weChatPay");
            tradeOrder.setTradeOrderId(SnowflakeIdWorker.uniqueMainId());

            //插入收货地址信息
            insertOrderAddress(tradeOrder,takeAddressId);

            //存入交易数据
            setOrderData(tradeOrder,item);
            try{
                //防止字段为空
                BaseUtil.setFieldValueNotNull(tradeOrder);
                tradeOrderRepository.insert(tradeOrder);

                //缓存支付信息
                /*redisRepository.set(cachePrefix,
                        JSON.toJSONString(wxMiniPayResp),
                        Long.valueOf(BizConstants.WX_ORDER_EXPIRED),
                        TimeUnit.MINUTES);
                return wxMiniPayResp;*/
            }catch (Exception e){
                log.error(">>>>>>>>>>>下单出现异常 : {} , {} <<<<<<<<<",e.getMessage(),e);
                throw new BusinessException(BizErrorCode.WX_PAY_SIGN_ERROR.getCode(),
                        BizErrorCode.WX_PAY_SIGN_ERROR.getMessage());
            }
            //删除购物车信息
            basketDataRepository.delete(new LambdaQueryWrapper<BasketData>()
                    .eq(BasketData::getBasketDataId,item.getBasketDataId()));
        });
        return outTradeNo;
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
     *
     * @description: 设置交易数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/2/24
     * @param tradeOrder 交易订单信息
     * @param tradeOrder 创建订单的信息
     * @param itemData 购买的物品信息
     * @return
     */
    public void setOrderData(TradeOrder tradeOrder,
                             AppBasketItemResp itemData){
        String salesItemId = itemData.getSalesItemId();
        String tradeOrderId = tradeOrder.getTradeOrderId();
        String authAppUserId = tradeOrder.getAuthAppUserId();
        OrderData entity = new OrderData();

        entity.setItemId(tradeOrder.getItemId());
        entity.setTradeOrderId(tradeOrder.getTradeOrderId());
        entity.setOutTradeNo(tradeOrder.getOutTradeNo());
        entity.setItemNum(BigInteger.ONE.intValue());
        entity.setAuthAppUserId(authAppUserId);

        entity.setItemData(JSON.toJSONString(itemData));
        try{
            //防止字段为空
            BaseUtil.setFieldValueNotNull(entity);
            entity.setOrderDataId(SnowflakeIdWorker.uniqueMainId());
            orderDataRepository.insert(entity);
        }catch (Exception e){
            log.error(">>>>>>>>>>>下单出现异常 : {} , {} <<<<<<<<<",e.getMessage(),e);
            throw new BusinessException(BizErrorCode.WX_PAY_SIGN_ERROR.getCode(),
                    BizErrorCode.WX_PAY_SIGN_ERROR.getMessage());
        }
    }

    /**
     *
     * @description: 设置交易数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/2/24
     * @param tradeOrder 交易订单信息
     * @param tradeOrder 创建订单的信息
     * @param itemData 购买的物品信息
     * @return
     */
    public void setOrderData(TradeOrder tradeOrder,
                             SalesItem itemData){
        String salesItemId = itemData.getSalesItemId();
        String tradeOrderId = tradeOrder.getTradeOrderId();
        String authAppUserId = tradeOrder.getAuthAppUserId();
        OrderData entity = new OrderData();

        entity.setItemId(tradeOrder.getItemId());
        entity.setTradeOrderId(tradeOrder.getTradeOrderId());
        entity.setOutTradeNo(tradeOrder.getOutTradeNo());
        entity.setItemNum(BigInteger.ONE.intValue());
        entity.setAuthAppUserId(authAppUserId);

        entity.setItemData(JSON.toJSONString(itemData));
        try{
            //防止字段为空
            BaseUtil.setFieldValueNotNull(entity);
            entity.setOrderDataId(SnowflakeIdWorker.uniqueMainId());
            orderDataRepository.insert(entity);
        }catch (Exception e){
            log.error(">>>>>>>>>>>下单出现异常 : {} , {} <<<<<<<<<",e.getMessage(),e);
            throw new BusinessException(BizErrorCode.WX_PAY_SIGN_ERROR.getCode(),
                    BizErrorCode.WX_PAY_SIGN_ERROR.getMessage());
        }
    }

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    public List<AppBasketItemResp> queryBasketItem() {
        String authAppUserId = authAppUserService.authAppUserId();
        MPJLambdaWrapper<BasketData> queryWrapper = new MPJLambdaWrapper<BasketData>()
                .eq(BasketData::getAuthAppUserId, authAppUserId);
        queryWrapper.leftJoin(SalesItem.class, SalesItem::getSalesItemId, BasketData::getSalesItemId);
        queryWrapper.selectAll(BasketData.class);
        queryWrapper.selectAs(SalesItem::getItemName, AppBasketItemResp::getItemName);
        queryWrapper.selectAs(SalesItem::getItemSummary, AppBasketItemResp::getItemSummary);
        queryWrapper.selectAs(SalesItem::getItemTitle, AppBasketItemResp::getItemTitle);
        queryWrapper.selectAs(SalesItem::getSalePrice, AppBasketItemResp::getSalePrice);
        queryWrapper.selectAs(SalesItem::getOriginalPrice, AppBasketItemResp::getOriginalPrice);
        queryWrapper.selectAs(SalesItem::getMainImg, AppBasketItemResp::getMainImg);
        queryWrapper.eq(SalesItem::getDeleteStatus,baseConstant.getUnDeleteStatus());
        queryWrapper.eq(BasketData::getDeleteStatus,baseConstant.getUnDeleteStatus());

        List<AppBasketItemResp> respList = basketDataRepository.selectJoinList(AppBasketItemResp.class, queryWrapper);
        return respList;
    }

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的单个商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    public AppBasketItemResp queryOneBasketItem(BasketDataReq req) {
        String authAppUserId = authAppUserService.authAppUserId();
        MPJLambdaWrapper<BasketData> queryWrapper = new MPJLambdaWrapper<BasketData>()
                .eq(BasketData::getAuthAppUserId, authAppUserId)
                .eq(BasketData::getSalesItemId, req.getSalesItemId());
        queryWrapper.leftJoin(SalesItem.class, SalesItem::getSalesItemId, BasketData::getSalesItemId);
        queryWrapper.selectAll(BasketData.class);
        queryWrapper.selectAs(SalesItem::getItemName, AppBasketItemResp::getItemName);
        queryWrapper.selectAs(SalesItem::getItemSummary, AppBasketItemResp::getItemSummary);
        queryWrapper.selectAs(SalesItem::getItemTitle, AppBasketItemResp::getItemTitle);
        queryWrapper.selectAs(SalesItem::getSalePrice, AppBasketItemResp::getSalePrice);
        queryWrapper.selectAs(SalesItem::getOriginalPrice, AppBasketItemResp::getOriginalPrice);
        queryWrapper.selectAs(SalesItem::getMainImg, AppBasketItemResp::getMainImg);

        AppBasketItemResp resp = basketDataRepository.selectJoinOne(AppBasketItemResp.class, queryWrapper);
        if(CheckParam.isNull(resp)){
            return new AppBasketItemResp();
        }
        return resp;
    }


    /**
     * 购物车业务处理
     *
     * @param addReq 购物车业务处理Req
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void handleBasketBiz(BasketDataAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>购物车业务处理 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String mainId = SnowflakeIdWorker.uniqueMainId();
        String authUserId = authAppUserService.authAppUserId();
        String salesItemId = addReq.getSalesItemId();
        String authAppUserId = addReq.getAuthAppUserId();
        Integer itemNum = addReq.getItemNum();
        Integer bizType = addReq.getBizType();
        BasketData entity = basketDataRepository.selectOne(new LambdaQueryWrapper<BasketData>()
                .eq(BasketData::getSalesItemId, salesItemId)
                .eq(BasketData::getAuthAppUserId, authAppUserId));
        if (CheckParam.isNull(entity)) {
            entity = mapperFacade.map(addReq, BasketData.class);
            try {
                BaseUtil.setFieldValueNotNull(entity);
                entity.setAuthAppUserId(authUserId);
                entity.setBasketDataId(mainId);
                entity.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增购物车信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
                throw new BusinessException(ErrorCode.ERROR.getCode(),
                        ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
            }
            basketDataRepository.insert(entity);
            return;
        }
        //增加
        if (bizType.compareTo(BasketHandleTypeEnum.ADD.getCode()) == 0) {
            entity.setItemNum(entity.getItemNum() + itemNum);
        }
        //减少
        if (bizType.compareTo(BasketHandleTypeEnum.MINUS.getCode()) == 0) {
            if (itemNum >= entity.getItemNum()) {
                entity.setItemNum(0);
            } else {
                entity.setItemNum(entity.getItemNum() - itemNum);
            }
        }
        basketDataRepository.updateById(entity);
        //如果此时购物车中没有了商品则删除
        if(entity.getItemNum().compareTo(BigInteger.ZERO.intValue()) == 0){
            basketDataRepository.deleteById(entity);
        }
    }

    /**
     * 批量删除购物车信息
     *
     * @param req 需要被删除的购物车信息
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<BasketData> entityList = basketDataRepository.selectList(
                new LambdaQueryWrapper<BasketData>().in(BasketData::getBasketDataId, mainIdList));
        entityList.stream().forEach(item -> {
            basketDataRepository.deleteById(item);
        });
    }

    /**
     * 查询购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public List<BasketDataResp> queryBasketData(BasketDataReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<BasketData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper, req);
        wrapper.orderBy(true, false, BasketData::getCreateTime);
        List<BasketData> entityList = basketDataRepository.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList, BasketDataResp.class);
    }

    /**
     * 查询单个购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public BasketDataResp queryOneBasketData(BasketDataReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询单个购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<BasketData> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper, req);
        wrapper.orderBy(true, false, BasketData::getCreateTime);
        BasketData entity = basketDataRepository.selectOne(wrapper);
        if (CheckParam.isNull(entity)) {
            return new BasketDataResp();
        }
        return mapperFacade.map(entity, BasketDataResp.class);
    }

    /**
     * 设置查询条件
     *
     * @param wrapper 查询条件
     * @param req     查询参数
     * @return
     * @author: Singer
     * @date 2024-03-06
     */
    private void setCriteria(LambdaQueryWrapper<BasketData> wrapper,
                             BasketDataReq req) {

        if (!CheckParam.isNull(req.getAuthAppUserId())) {
            wrapper.like(BasketData::getAuthAppUserId, req.getAuthAppUserId());
        }

        if (!CheckParam.isNull(req.getSalesItemId())) {
            wrapper.like(BasketData::getSalesItemId, req.getSalesItemId());
        }

        if (!CheckParam.isNull(req.getRemarkData())) {
            wrapper.like(BasketData::getRemarkData, req.getRemarkData());
        }

        if (!CheckParam.isNull(req.getItemNum())) {
            wrapper.like(BasketData::getItemNum, req.getItemNum());
        }
    }

    /**
     * 分页查询购物车信息
     *
     * @param pageReq 分页查询购物车信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public Pagination<BasketDataResp> queryByPage(
            BasketDataReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询购物车信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<BasketData> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, BasketData::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<BasketData> pageList = basketDataRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        List<BasketDataResp> respList =
                mapperFacade.mapAsList(pageList, BasketDataResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page, respList);
    }

    /**
     * 设置分页条件
     *
     * @param pageWrapper 查询条件
     * @param pageReq     查询参数
     * @return
     * @author: Singer
     * @date 2024-03-06
     */
    private void setPageCriteria(LambdaQueryWrapper<BasketData> pageWrapper,
                                 BasketDataReq pageReq) {

        if (!CheckParam.isNull(pageReq.getAuthAppUserId())) {
            pageWrapper.like(BasketData::getAuthAppUserId, pageReq.getAuthAppUserId());
        }

        if (!CheckParam.isNull(pageReq.getSalesItemId())) {
            pageWrapper.like(BasketData::getSalesItemId, pageReq.getSalesItemId());
        }

        if (!CheckParam.isNull(pageReq.getRemarkData())) {
            pageWrapper.like(BasketData::getRemarkData, pageReq.getRemarkData());
        }

        if (!CheckParam.isNull(pageReq.getItemNum())) {
            pageWrapper.like(BasketData::getItemNum, pageReq.getItemNum());
        }
    }

    /**
     * 更新购物车信息
     *
     * @param updateReq 更新购物车信息请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateItem(BasketDataUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>>>>>更新购物车信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getBasketDataId();
        //String salesItemId = updateReq.getSalesItemId();
        //String authAppUserId = updateReq.getAuthAppUserId();
        Integer itemNum = updateReq.getItemNum();
        Integer bizType = updateReq.getBizType();
        BasketData result = basketDataRepository.selectOne(new LambdaQueryWrapper<BasketData>()
                .eq(BasketData::getBasketDataId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result, updateReq);
        //增加
        if (bizType.compareTo(BasketHandleTypeEnum.ADD.getCode()) == 0) {
            result.setItemNum(result.getItemNum() + itemNum);
        }
        //减少
        if (bizType.compareTo(BasketHandleTypeEnum.MINUS.getCode()) == 0) {
            if (itemNum >= result.getItemNum()) {
                result.setItemNum(0);
            } else {
                result.setItemNum(result.getItemNum() - itemNum);
            }
        }
        basketDataRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     *
     * @param updateReq 更新参数
     * @param entity    实体
     * @author: Singer
     * @date 2024-03-06
     */
    private void setNeedUpdateItem(BasketData entity,
                                   BasketDataUpdateReq updateReq) {
        if (!CheckParam.isNull(updateReq.getAuthAppUserId())) {
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if (!CheckParam.isNull(updateReq.getSalesItemId())) {
            entity.setSalesItemId(updateReq.getSalesItemId());
        }
        if (!CheckParam.isNull(updateReq.getRemarkData())) {
            entity.setRemarkData(updateReq.getRemarkData());
        }
        if (!CheckParam.isNull(updateReq.getItemNum())) {
            entity.setItemNum(updateReq.getItemNum());
        }
    }
}
