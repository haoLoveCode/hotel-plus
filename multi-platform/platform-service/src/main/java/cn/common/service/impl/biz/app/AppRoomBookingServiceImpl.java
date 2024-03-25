package cn.common.service.impl.biz.app;

import cn.common.enums.BizErrorCode;
import cn.common.enums.OrderTypeEnum;
import cn.common.enums.RoomStatusEnum;
import cn.common.repository.entity.biz.*;
import cn.common.repository.repository.biz.OrderDataRepository;
import cn.common.repository.repository.biz.RoomBookingRepository;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.req.biz.BookingRoomReq;
import cn.common.req.biz.RoomBookingAddReq;
import cn.common.req.biz.RoomBookingReq;
import cn.common.req.biz.RoomBookingUpdateReq;
import cn.common.resp.biz.RoomBookingExportResp;
import cn.common.resp.biz.RoomBookingResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.app.AppRoomBookingService;
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
import pro.skywalking.enums.OrderStatusEnum;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间预订信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("appRoomBookingService")
@Slf4j
public class AppRoomBookingServiceImpl implements AppRoomBookingService {

    @Resource
    private RoomBookingRepository roomBookingRepository;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private OrderDataRepository orderDataRepository;

    @Resource
    private RoomDataRepository roomDataRepository;

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
     * 新增房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 新增房间预订信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public TradeOrderResp addItem(BookingRoomReq req){
        log.info(">>>>>>>>>>>>>>>>>新增房间预订信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String roomDataId = req.getRoomDataId();
        RoomData roomData = roomDataRepository.selectOne(new LambdaQueryWrapper<RoomData>()
                .eq(RoomData::getRoomDataId,roomDataId));
        if(CheckParam.isNull(roomData)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "房间不存在");
        }
        if(roomData.getRoomStatus().compareTo(RoomStatusEnum.UNUSED.getCode()) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "当前房间非闲置，不可预定");
        }
        String authAppUserId = authAppUserService.authAppUserId();
        //内部支付交易订单号
        String outTradeNo = SnowflakeIdWorker.uniqueMainId();

        //设置交易订单信息
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setItemId(roomDataId);
        tradeOrder.setOrderRemark(StrUtil.EMPTY);
        tradeOrder.setAuthAppUserId(authAppUserId);
        tradeOrder.setOrderAmount(req.getTotalAmount());
        tradeOrder.setOutTradeNo(outTradeNo);
        tradeOrder.setOrderType(OrderTypeEnum.SALES_ITEM.getCode());
        //订单状态
        tradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        //支付方式 aliPay/支付宝  weChatPay/微信
        tradeOrder.setPayType("weChatPay");
        tradeOrder.setTradeOrderId(SnowflakeIdWorker.uniqueMainId());
        //存入交易数据
        setOrderData(tradeOrder,roomData);

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
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        String mainId = SnowflakeIdWorker.uniqueMainId();
        RoomBooking entity = mapperFacade.map(req, RoomBooking.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomBookingId(mainId);
            entity.setSubscriberId(authAppUserId);
            entity.setOperatorId(authAppUserId);
        } catch (Exception e) {
            log.error("新增房间预订信息->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        roomBookingRepository.insert(entity);

        //更新房间状态
        roomData.setRoomStatus(RoomStatusEnum.CHECKED_IN.getCode());
        roomDataRepository.updateById(roomData);

        TradeOrderResp resp = mapperFacade.map(tradeOrder, TradeOrderResp.class);
        return resp;
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
                             RoomData itemData){
        String roomDataId = itemData.getRoomDataId();
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
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
    }

    /**
     * 批量删除房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间预订信息
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
     * 查询房间预订信息
     * @author: Singer
     * @date 2024/3/24
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
     * 查询单个房间预订信息
     * @author: Singer
     * @date 2024/3/24
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
     * @date 2024/3/24
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
     * @date 2024/3/24
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
     * @date 2024/3/24
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
     * @date 2024/3/24
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
        String roomDataId = updateReq.getRoomDataId();
        RoomData roomData = roomDataRepository.selectOne(new LambdaQueryWrapper<RoomData>()
                .eq(RoomData::getRoomDataId,roomDataId));
        if(CheckParam.isNull(roomData)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "房间不存在");
        }
        //是否可以预定
        Boolean canBeBooking = roomData.getRoomStatus().compareTo(RoomStatusEnum.UNUSED.getCode()) == 0 ?
         Boolean.TRUE : Boolean.FALSE;
        if(!canBeBooking && !StrUtil.equalsAnyIgnoreCase(roomDataId,result.getRoomDataId())){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "当前房间非闲置，不可预定");
        }
        setNeedUpdateItem(result,updateReq);
        roomBookingRepository.updateById(result);

        RoomData roomUpdateData = new RoomData();
        roomUpdateData.setRoomDataId(result.getRoomDataId());
        roomUpdateData.setRoomStatus(RoomStatusEnum.CHECKED_IN.getCode());
        roomDataRepository.update(roomUpdateData,new LambdaQueryWrapper<RoomData>().eq(RoomData::getRoomDataId,roomDataId));

        roomData.setRoomStatus(RoomStatusEnum.UNUSED.getCode());
        roomDataRepository.updateById(roomData);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
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
