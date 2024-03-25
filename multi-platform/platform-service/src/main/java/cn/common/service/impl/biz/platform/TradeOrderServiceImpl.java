package cn.common.service.impl.biz.platform;

import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.entity.biz.TradeOrder;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.req.biz.platform.SetTradeOrderReq;
import cn.common.req.biz.platform.TradeOrderAddReq;
import cn.common.req.biz.platform.TradeOrderReq;
import cn.common.req.biz.platform.TradeOrderUpdateReq;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.app.AppTradeOrderService;
import cn.common.service.platform.AuthUserService;
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
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 交易订单信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("tradeOrderService")
@Slf4j
public class TradeOrderServiceImpl implements AppTradeOrderService {

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;


    /**
     * 新增
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addItem(TradeOrderAddReq addReq) {
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
            log.error("新增->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        tradeOrderRepository.insert(entity);
        //插入收货地址信息
        //insertOrderAddress(entity,takeAddressId);
    }

    /**
     * 批量删除信息
     *
     * @param req 需要被删除的信息
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<TradeOrder> entityList = tradeOrderRepository.selectList(
                new MPJLambdaWrapper<TradeOrder>().in(TradeOrder::getTradeOrderId, mainIdList));
        entityList.stream().forEach(item -> {
            tradeOrderRepository.deleteById(item);
        });
    }

    /**
     * @param outTradeNo 外部交易订单号
     * @return cn.common.resp.biz.openBiz.TradeOrderResp
     * @description: 查询单个交易订单数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public TradeOrderResp queryByOrderNo(String outTradeNo) {
        TradeOrder entity = tradeOrderRepository.selectOne(new LambdaQueryWrapper<TradeOrder>()
                .eq(TradeOrder::getOutTradeNo, outTradeNo));
        if (CheckParam.isNull(entity)) {
            return new TradeOrderResp();
        }
        return mapperFacade.map(entity, TradeOrderResp.class);
    }

    /**
     * @return java.util.List
     * @description: 查询当前用户的所有订单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public List<TradeOrderResp> queryOrderList() {
        //构建查询条件
        MPJLambdaWrapper<TradeOrder> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.leftJoin(RoomData.class, RoomData::getRoomDataId, TradeOrder::getItemId);
        //是否查询当前用户卖出的
        /*Boolean queryMineSales = pageReq.getQueryMineSales();
        if(queryMineSales){
            pageWrapper.eq(RoomData::getPublisherId,authUserId);
        }*/
        queryWrapper.selectAll(TradeOrder.class);
        queryWrapper.selectAs(RoomData::getRoomDataId, TradeOrderResp::getRoomDataId);
        queryWrapper.selectAs(RoomData::getBriefData, TradeOrderResp::getBriefData);
        queryWrapper.selectAs(RoomData::getRoomNo, TradeOrderResp::getRoomNo);
        queryWrapper.selectAs(RoomData::getRoomArea, TradeOrderResp::getRoomArea);
        queryWrapper.selectAs(RoomData::getRoomStatus, TradeOrderResp::getRoomStatus);
        queryWrapper.selectAs(RoomData::getRoomFloor, TradeOrderResp::getRoomFloor);
        queryWrapper.selectAs(RoomData::getRoomTitle, TradeOrderResp::getRoomTitle);
        queryWrapper.selectAs(RoomData::getRoomTypeId, TradeOrderResp::getRoomTypeId);
        queryWrapper.selectAs(RoomData::getBedNum, TradeOrderResp::getBedNum);
        queryWrapper.selectAs(RoomData::getMainImg, TradeOrderResp::getMainImg);
        queryWrapper.selectAs(RoomData::getUnitPrice, TradeOrderResp::getUnitPrice);

        queryWrapper.selectAs(RoomBooking::getRoomBookingId, TradeOrderResp::getRoomBookingId);
        queryWrapper.selectAs(RoomBooking::getBookingNo, TradeOrderResp::getBookingNo);
        queryWrapper.selectAs(RoomBooking::getBookingStatus, TradeOrderResp::getBookingStatus);
        queryWrapper.selectAs(RoomBooking::getBookingTime, TradeOrderResp::getBookingTime);
        queryWrapper.selectAs(RoomBooking::getRemark, TradeOrderResp::getRemark);
        queryWrapper.selectAs(RoomBooking::getCheckInEnd, TradeOrderResp::getCheckInEnd);
        queryWrapper.selectAs(RoomBooking::getCheckInBegin, TradeOrderResp::getCheckInBegin);
        queryWrapper.selectAs(RoomBooking::getSubscriberId, TradeOrderResp::getSubscriberId);

        List<TradeOrderResp> respList = tradeOrderRepository.selectJoinList(TradeOrderResp.class, queryWrapper);
        if (CollectionUtils.isEmpty(respList)) {
            return Lists.newArrayList();
        }
        return respList;
    }

    /**
     * 查询所有信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public List<TradeOrderResp> queryAllTradeOrder() {
        List<TradeOrder> entityList = tradeOrderRepository.selectList(new MPJLambdaWrapper<>());
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList, TradeOrderResp.class);
    }

    /**
     * 分页查询
     *
     * @param pageReq 分页查询Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    public Pagination<TradeOrderResp> queryByPage(
            TradeOrderReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        MPJLambdaWrapper<TradeOrder> pageWrapper = new MPJLambdaWrapper<>();
        pageWrapper.leftJoin(RoomData.class, RoomData::getRoomDataId, TradeOrder::getItemId);
        pageWrapper.leftJoin(RoomBooking.class, RoomBooking::getRoomDataId, RoomData::getRoomDataId);
        setQueryCriteria(pageWrapper,pageReq);
        //是否查询当前用户卖出的
        /*Boolean queryMineSales = pageReq.getQueryMineSales();
        if(queryMineSales){
            pageWrapper.eq(RoomData::getPublisherId,authUserId);
        }*/
        pageWrapper.selectAll(TradeOrder.class);
        pageWrapper.selectAs(RoomData::getRoomDataId, TradeOrderResp::getRoomDataId);
        pageWrapper.selectAs(RoomData::getBriefData, TradeOrderResp::getBriefData);
        pageWrapper.selectAs(RoomData::getRoomNo, TradeOrderResp::getRoomNo);
        pageWrapper.selectAs(RoomData::getRoomArea, TradeOrderResp::getRoomArea);
        pageWrapper.selectAs(RoomData::getRoomStatus, TradeOrderResp::getRoomStatus);
        pageWrapper.selectAs(RoomData::getRoomFloor, TradeOrderResp::getRoomFloor);
        pageWrapper.selectAs(RoomData::getRoomTitle, TradeOrderResp::getRoomTitle);
        pageWrapper.selectAs(RoomData::getRoomTypeId, TradeOrderResp::getRoomTypeId);
        pageWrapper.selectAs(RoomData::getBedNum, TradeOrderResp::getBedNum);
        pageWrapper.selectAs(RoomData::getMainImg, TradeOrderResp::getMainImg);
        pageWrapper.selectAs(RoomData::getUnitPrice, TradeOrderResp::getUnitPrice);

        pageWrapper.selectAs(RoomBooking::getRoomBookingId, TradeOrderResp::getRoomBookingId);
        pageWrapper.selectAs(RoomBooking::getBookingNo, TradeOrderResp::getBookingNo);
        pageWrapper.selectAs(RoomBooking::getBookingStatus, TradeOrderResp::getBookingStatus);
        pageWrapper.selectAs(RoomBooking::getBookingTime, TradeOrderResp::getBookingTime);
        pageWrapper.selectAs(RoomBooking::getRemark, TradeOrderResp::getRemark);
        pageWrapper.selectAs(RoomBooking::getCheckInEnd, TradeOrderResp::getCheckInEnd);
        pageWrapper.selectAs(RoomBooking::getCheckInBegin, TradeOrderResp::getCheckInBegin);
        pageWrapper.selectAs(RoomBooking::getSubscriberId, TradeOrderResp::getSubscriberId);

        setQueryCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, TradeOrder::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<TradeOrderResp> respList = tradeOrderRepository.selectJoinList(TradeOrderResp.class, pageWrapper);
        if (CollectionUtils.isEmpty(respList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        //此处需要去重
        respList = respList.stream().filter(BaseUtil.distinctByKey(TradeOrderResp::getRoomBookingId)).collect(Collectors.toList());
        return PageBuilder.buildPageResult(page, respList);
    }

    /**
     * 设置分页条件
     *
     * @param pageWrapper 查询条件
     * @param pageReq     分页插件
     * @return
     * @author: Singer
     * @date 2024-03-06
     */
    private void setQueryCriteria(MPJLambdaWrapper<TradeOrder> pageWrapper, TradeOrderReq pageReq) {

        if (!CheckParam.isNull(pageReq.getItemId())) {
            pageWrapper.eq(TradeOrder::getItemId, pageReq.getItemId());
        }

        if (!CheckParam.isNull(pageReq.getAuthAppUserId())) {
            pageWrapper.eq(TradeOrder::getAuthAppUserId, pageReq.getAuthAppUserId());
        }

        if (!CheckParam.isNull(pageReq.getOrderType())) {
            pageWrapper.eq(TradeOrder::getOrderType, pageReq.getOrderType());
        }

        if (!CheckParam.isNull(pageReq.getOrderRemark())) {
            pageWrapper.like(TradeOrder::getOrderRemark, pageReq.getOrderRemark());
        }

        if (!CheckParam.isNull(pageReq.getOutTradeNo())) {
            pageWrapper.like(TradeOrder::getOutTradeNo, pageReq.getOutTradeNo());
        }

        if (!CheckParam.isNull(pageReq.getOrderAmount())) {
            pageWrapper.eq(TradeOrder::getOrderAmount, pageReq.getOrderAmount());
        }

        if (!CheckParam.isNull(pageReq.getPayType())) {
            pageWrapper.eq(TradeOrder::getPayType, pageReq.getPayType());
        }

        if (!CheckParam.isNull(pageReq.getExtraData())) {
            pageWrapper.like(TradeOrder::getExtraData, pageReq.getExtraData());
        }

        if (!CheckParam.isNull(pageReq.getOrderStatus())) {
            pageWrapper.eq(TradeOrder::getOrderStatus, pageReq.getOrderStatus());
        }
    }

    /**
     * 更新订单状态
     *
     * @param req 更新请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setOrderStatus(SetTradeOrderReq req) {
        log.info(">>>>>>>>>>>>>>>>>更新订单状态 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String mainId = req.getTradeOrderId();
        MPJLambdaWrapper<TradeOrder> lambdaWrapper = new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getTradeOrderId, mainId);
        lambdaWrapper.selectAll(TradeOrder.class);
        TradeOrder result = tradeOrderRepository.selectOne(lambdaWrapper);
        if (CheckParam.isNull(result)) {
            return;
        }
        if (!CheckParam.isNull(req.getOrderStatus())) {
            result.setOrderStatus(req.getOrderStatus());
        }
        tradeOrderRepository.updateById(result);
    }

    /**
     * 更新
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateItem(TradeOrderUpdateReq updateReq) {
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
        setNeedUpdateItem(result, updateReq);
        tradeOrderRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     *
     * @param updateReq 更新参数
     * @param entity    产业
     * @author: Singer
     * @date 2024-03-06
     */
    private void setNeedUpdateItem(TradeOrder entity,
                                   TradeOrderUpdateReq updateReq) {
        if (!CheckParam.isNull(updateReq.getItemId())) {
            entity.setItemId(updateReq.getItemId());
        }
        if (!CheckParam.isNull(updateReq.getAuthAppUserId())) {
            entity.setAuthAppUserId(updateReq.getAuthAppUserId());
        }
        if (!CheckParam.isNull(updateReq.getOrderType())) {
            entity.setOrderType(updateReq.getOrderType());
        }
        if (!CheckParam.isNull(updateReq.getOrderRemark())) {
            entity.setOrderRemark(updateReq.getOrderRemark());
        }
        if (!CheckParam.isNull(updateReq.getOutTradeNo())) {
            entity.setOutTradeNo(updateReq.getOutTradeNo());
        }
        if (!CheckParam.isNull(updateReq.getOrderAmount())) {
            entity.setOrderAmount(updateReq.getOrderAmount());
        }
        if (!CheckParam.isNull(updateReq.getPayType())) {
            entity.setPayType(updateReq.getPayType());
        }
        if (!CheckParam.isNull(updateReq.getExtraData())) {
            entity.setExtraData(updateReq.getExtraData());
        }
        if (!CheckParam.isNull(updateReq.getOrderStatus())) {
            entity.setOrderStatus(updateReq.getOrderStatus());
        }
    }
}
