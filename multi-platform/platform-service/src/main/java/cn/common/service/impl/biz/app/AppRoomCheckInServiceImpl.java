package cn.common.service.impl.biz.app;

import cn.common.enums.BookingStatusEnum;
import cn.common.enums.RoomStatusEnum;
import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.entity.biz.RoomCheckIn;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.entity.biz.TradeOrder;
import cn.common.repository.repository.biz.RoomBookingRepository;
import cn.common.repository.repository.biz.RoomCheckInRepository;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.req.biz.RoomCheckInAddReq;
import cn.common.req.biz.RoomCheckInReq;
import cn.common.req.biz.RoomCheckInUpdateReq;
import cn.common.resp.biz.RoomCheckInResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.app.AppRoomCheckInService;
import cn.common.service.biz.app.AppRoomDataService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 客房入住信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("appRoomCheckInService")
@Slf4j
public class AppRoomCheckInServiceImpl implements AppRoomCheckInService {

    @Resource
    private RoomCheckInRepository roomCheckInRepository;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private AppRoomDataService appRoomDataService;

    @Resource
    private RoomBookingRepository roomBookingRepository;

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
     * 新增客房入住信息
     *
     * @param addReq 新增客房入住信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addItem(RoomCheckInAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>新增客房入住信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String authAppUserId = authAppUserService.authAppUserId();
        String roomBookingId = addReq.getRoomBookingId();
        RoomData roomData = appRoomDataService.queryRoomByBookingUd(roomBookingId);
        if(CheckParam.isNull(roomData)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "房间信息不存在");
        }
        if(roomData.getRoomStatus().compareTo(RoomStatusEnum.BOOKING.getCode()) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "该房间没有被预定，不可入住");
        }
        RoomBooking roomBooking = roomBookingRepository.selectOne(new LambdaQueryWrapper<RoomBooking>()
                .eq(RoomBooking::getRoomBookingId,roomBookingId));
        if(CheckParam.isNull(roomBooking)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "预定订单不存在");
        }
        //非预定成功状态不可入住
        if(roomBooking.getBookingStatus().compareTo(BookingStatusEnum.BOOKING_SUCCESS.getCode()) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "非预定成功状态不可入住");
        }
        //此处为交易订单号
        String bookingNo = roomBooking.getBookingNo();
        TradeOrder tradeOrder = tradeOrderRepository.selectOne(new MPJLambdaWrapper<TradeOrder>()
                .eq(TradeOrder::getOutTradeNo, bookingNo)
                .eq(TradeOrder::getAuthAppUserId, authAppUserId));
        if(CheckParam.isNull(tradeOrder)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "交易订单不存在");
        }
        String mainId = SnowflakeIdWorker.uniqueMainId();
        RoomCheckIn entity = mapperFacade.map(addReq, RoomCheckIn.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomCheckInId(mainId);
            entity.setOperatorId(authAppUserId);
        } catch (Exception e) {
            log.error("新增客房入住信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        roomCheckInRepository.insert(entity);

        //更新房间状态信息为已入住
        roomData.setRoomStatus(RoomStatusEnum.CHECK_IN.getCode());
        roomDataRepository.updateById(roomData);

        //更新为已入住
        roomBooking.setBookingStatus(BookingStatusEnum.CHECKED_IN.getCode());
        roomBookingRepository.updateById(roomBooking);

        //更新订单为完结
        tradeOrder.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        tradeOrderRepository.updateById(tradeOrder);
    }

    /**
     * 批量删除客房入住信息
     *
     * @param req 需要被删除的客房入住信息
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomCheckIn> entityList = roomCheckInRepository.selectList(
                new LambdaQueryWrapper<RoomCheckIn>().in(RoomCheckIn::getRoomCheckInId, mainIdList));
        entityList.stream().forEach(item -> {
            roomCheckInRepository.deleteById(item);
        });
    }

    /**
     * 查询客房入住信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public List<RoomCheckInResp> queryRoomCheckIn(RoomCheckInReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询客房入住信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckIn> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper, req);
        wrapper.orderBy(true, false, RoomCheckIn::getCreateTime);
        List<RoomCheckIn> entityList = roomCheckInRepository.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList, RoomCheckInResp.class);
    }

    /**
     * 查询单个客房入住信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public RoomCheckInResp queryOneRoomCheckIn(RoomCheckInReq req) {
        log.info(">>>>>>>>>>>>>>>>>查询单个客房入住信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckIn> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper, req);
        wrapper.orderBy(true, false, RoomCheckIn::getCreateTime);
        RoomCheckIn entity = roomCheckInRepository.selectOne(wrapper);
        if (CheckParam.isNull(entity)) {
            return new RoomCheckInResp();
        }
        return mapperFacade.map(entity, RoomCheckInResp.class);
    }

    /**
     * 设置查询条件
     *
     * @param wrapper 查询条件
     * @param req     查询参数
     * @return
     * @author: Singer
     * @date 2024/3/24
     */
    private void setCriteria(LambdaQueryWrapper<RoomCheckIn> wrapper,
                             RoomCheckInReq req) {

        if (!CheckParam.isNull(req.getRoomBookingId())) {
            wrapper.like(RoomCheckIn::getRoomBookingId, req.getRoomBookingId());
        }

        if (!CheckParam.isNull(req.getGuestIdentifyId())) {
            wrapper.like(RoomCheckIn::getGuestIdentifyId, req.getGuestIdentifyId());
        }

        if (!CheckParam.isNull(req.getRemark())) {
            wrapper.like(RoomCheckIn::getRemark, req.getRemark());
        }

        if (!CheckParam.isNull(req.getCheckInTime())) {
            wrapper.like(RoomCheckIn::getCheckInTime, req.getCheckInTime());
        }
    }

    /**
     * 分页查询客房入住信息
     *
     * @param pageReq 分页查询客房入住信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    public Pagination<RoomCheckInResp> queryByPage(
            RoomCheckInReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询客房入住信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckIn> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, RoomCheckIn::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomCheckIn> pageList = roomCheckInRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        List<RoomCheckInResp> respList =
                mapperFacade.mapAsList(pageList, RoomCheckInResp.class);
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
     * @date 2024/3/24
     */
    private void setPageCriteria(LambdaQueryWrapper<RoomCheckIn> pageWrapper,
                                 RoomCheckInReq pageReq) {

        if (!CheckParam.isNull(pageReq.getRoomBookingId())) {
            pageWrapper.eq(RoomCheckIn::getRoomBookingId, pageReq.getRoomBookingId());
        }

        if (!CheckParam.isNull(pageReq.getGuestIdentifyId())) {
            pageWrapper.eq(RoomCheckIn::getGuestIdentifyId, pageReq.getGuestIdentifyId());
        }

        if (!CheckParam.isNull(pageReq.getRemark())) {
            pageWrapper.like(RoomCheckIn::getRemark, pageReq.getRemark());
        }

        if (!CheckParam.isNull(pageReq.getCheckInTime())) {
            pageWrapper.ge(RoomCheckIn::getCheckInTime, pageReq.getCheckInTime());
        }
    }

    /**
     * 更新客房入住信息
     *
     * @param updateReq 更新客房入住信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateItem(RoomCheckInUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>>>>>更新客房入住信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomCheckInId();
        RoomCheckIn result = roomCheckInRepository.selectOne(new LambdaQueryWrapper<RoomCheckIn>()
                .eq(RoomCheckIn::getRoomCheckInId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result, updateReq);
        roomCheckInRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     *
     * @param updateReq 更新参数
     * @param entity    实体
     * @author: Singer
     * @date 2024/3/24
     */
    private void setNeedUpdateItem(RoomCheckIn entity,
                                   RoomCheckInUpdateReq updateReq) {
        if (!CheckParam.isNull(updateReq.getRoomBookingId())) {
            entity.setRoomBookingId(updateReq.getRoomBookingId());
        }
        if (!CheckParam.isNull(updateReq.getGuestIdentifyId())) {
            entity.setGuestIdentifyId(updateReq.getGuestIdentifyId());
        }
        if (!CheckParam.isNull(updateReq.getRemark())) {
            entity.setRemark(updateReq.getRemark());
        }
        if (!CheckParam.isNull(updateReq.getCheckInTime())) {
            entity.setCheckInTime(updateReq.getCheckInTime());
        }
    }
}
