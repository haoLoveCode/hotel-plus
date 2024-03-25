package cn.common.service.impl.biz.platform;

import cn.common.enums.BookingStatusEnum;
import cn.common.enums.RoomStatusEnum;
import cn.common.repository.entity.biz.RoomBooking;
import cn.common.repository.entity.biz.RoomData;
import cn.common.repository.entity.biz.TradeOrder;
import cn.common.repository.repository.biz.RoomBookingRepository;
import cn.common.repository.repository.biz.RoomDataRepository;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.req.biz.RoomCheckOutAddReq;
import cn.common.req.biz.RoomCheckOutReq;
import cn.common.req.biz.RoomCheckOutUpdateReq;
import cn.common.resp.biz.RoomCheckOutResp;
import cn.common.resp.biz.RoomCheckOutExportResp;
import cn.common.service.biz.app.AppRoomDataService;
import cn.common.service.biz.platform.RoomCheckOutService;
import cn.common.repository.entity.biz.RoomCheckOut;
import cn.common.repository.repository.biz.RoomCheckOutRepository;
import cn.common.service.biz.platform.RoomDataService;
import cn.common.service.platform.AuthUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
 * @Description: 客房退房信息相关服务方法实现
 * @date 2024/3/24
 */
@Service("roomCheckOutService")
@Slf4j
public class RoomCheckOutServiceImpl implements RoomCheckOutService {

    @Resource
    private RoomCheckOutRepository roomCheckOutRepository;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private RoomDataService roomDataService;

    @Resource
    private RoomBookingRepository roomBookingRepository;

    @Resource
    private AppRoomDataService appRoomDataService;

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
     * 导出客房退房信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    @Override
    public void exportData(RoomCheckOutReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>客房退房信息数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckOut> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomCheckOut::getCreateTime);
        List<RoomCheckOut> pageList = roomCheckOutRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<RoomCheckOutExportResp> respList =
            mapperFacade.mapAsList(pageList, RoomCheckOutExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response,fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), RoomCheckOutExportResp.class).build();
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
     * 新增客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增客房退房信息Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(RoomCheckOutAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增客房退房信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String authUserId = authUserService.currentAuthUserId();
        String roomBookingId = addReq.getRoomBookingId();
        RoomData roomData = appRoomDataService.queryRoomByBookingUd(roomBookingId);
        if(CheckParam.isNull(roomData)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "房间信息不存在");
        }
        if(roomData.getRoomStatus().compareTo(RoomStatusEnum.CHECK_IN.getCode()) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "该房间没有被入住，不可退住");
        }
        RoomBooking roomBooking = roomBookingRepository.selectOne(new LambdaQueryWrapper<RoomBooking>()
                .eq(RoomBooking::getRoomBookingId,roomBookingId));
        if(CheckParam.isNull(roomBooking)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "预定订单不存在");
        }
        //预定人
        String subscriberId = roomBooking.getSubscriberId();
        //非预定入住状态不可退住
        if(roomBooking.getBookingStatus().compareTo(BookingStatusEnum.CHECKED_IN.getCode()) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "非预定入住状态不可退住");
        }
        //此处为交易订单号
        String bookingNo = roomBooking.getBookingNo();
        TradeOrder tradeOrder = tradeOrderRepository.selectOne(new LambdaQueryWrapper<TradeOrder>()
                .eq(TradeOrder::getOutTradeNo, bookingNo)
                .eq(TradeOrder::getAuthAppUserId, subscriberId));
        if(CheckParam.isNull(tradeOrder)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "交易订单不存在");
        }
        String mainId = SnowflakeIdWorker.uniqueMainId();
        RoomCheckOut entity = mapperFacade.map(addReq, RoomCheckOut.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setRoomCheckOutId(mainId);
            entity.setOperatorId(subscriberId);
        } catch (Exception e) {
            log.error("新增客房退房信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        roomCheckOutRepository.insert(entity);

        //更新房间状态信息为维护中
        roomData.setRoomStatus(RoomStatusEnum.MAINTAINED.getCode());
        roomDataRepository.updateById(roomData);

        //更新为已退住
        roomBooking.setBookingStatus(BookingStatusEnum.CHECK_OUT.getCode());
        roomBookingRepository.updateById(roomBooking);

        //更新订单为完结
        tradeOrder.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        tradeOrderRepository.updateById(tradeOrder);
    }

    /**
     * 批量删除客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客房退房信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<RoomCheckOut> entityList = roomCheckOutRepository.selectList(
            new LambdaQueryWrapper<RoomCheckOut>().in(RoomCheckOut::getRoomCheckOutId,mainIdList));
        entityList.stream().forEach(item -> {
            roomCheckOutRepository.deleteById(item);
        });
    }

    /**
     * 查询客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public List<RoomCheckOutResp> queryRoomCheckOut(RoomCheckOutReq req){
        log.info(">>>>>>>>>>>>>>>>>查询客房退房信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckOut> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomCheckOut::getCreateTime);
        List<RoomCheckOut> entityList = roomCheckOutRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,RoomCheckOutResp.class);
    }

    /**
     * 查询单个客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @Override
    public RoomCheckOutResp queryOneRoomCheckOut(RoomCheckOutReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个客房退房信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckOut> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,RoomCheckOut::getCreateTime);
        RoomCheckOut entity = roomCheckOutRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new RoomCheckOutResp();
        }
        return mapperFacade.map(entity,RoomCheckOutResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024/3/24
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<RoomCheckOut> wrapper,
                        RoomCheckOutReq req){

        if(!CheckParam.isNull(req.getRoomBookingId())){
            wrapper.like(RoomCheckOut::getRoomBookingId,req.getRoomBookingId());
        }

        if(!CheckParam.isNull(req.getGuestIdentifyId())){
            wrapper.like(RoomCheckOut::getGuestIdentifyId,req.getGuestIdentifyId());
        }

        if(!CheckParam.isNull(req.getCheckOutTime())){
            wrapper.like(RoomCheckOut::getCheckOutTime,req.getCheckOutTime());
        }

        if(!CheckParam.isNull(req.getRemark())){
            wrapper.like(RoomCheckOut::getRemark,req.getRemark());
        }
    }

    /**
     * 分页查询客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客房退房信息Req
     * @return Pagination
     */
    @Override
    public Pagination<RoomCheckOutResp> queryByPage(
        RoomCheckOutReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询客房退房信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<RoomCheckOut> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,RoomCheckOut::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<RoomCheckOut> pageList = roomCheckOutRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<RoomCheckOutResp> respList =
            mapperFacade.mapAsList(pageList, RoomCheckOutResp.class);
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
    private void setPageCriteria(LambdaQueryWrapper<RoomCheckOut> pageWrapper,
                        RoomCheckOutReq pageReq){

        if(!CheckParam.isNull(pageReq.getRoomBookingId())){
            pageWrapper.like(RoomCheckOut::getRoomBookingId,pageReq.getRoomBookingId());
        }

        if(!CheckParam.isNull(pageReq.getGuestIdentifyId())){
            pageWrapper.like(RoomCheckOut::getGuestIdentifyId,pageReq.getGuestIdentifyId());
        }

        if(!CheckParam.isNull(pageReq.getCheckOutTime())){
            pageWrapper.like(RoomCheckOut::getCheckOutTime,pageReq.getCheckOutTime());
        }

        if(!CheckParam.isNull(pageReq.getRemark())){
            pageWrapper.like(RoomCheckOut::getRemark,pageReq.getRemark());
        }
    }

    /**
     * 更新客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客房退房信息请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(RoomCheckOutUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新客房退房信息请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getRoomCheckOutId();
        RoomCheckOut result = roomCheckOutRepository.selectOne(new LambdaQueryWrapper<RoomCheckOut>()
                    .eq(RoomCheckOut::getRoomCheckOutId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        roomCheckOutRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(RoomCheckOut entity,
        RoomCheckOutUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRoomBookingId())){
            entity.setRoomBookingId(updateReq.getRoomBookingId());
        }
        if(!CheckParam.isNull(updateReq.getGuestIdentifyId())){
            entity.setGuestIdentifyId(updateReq.getGuestIdentifyId());
        }
        if(!CheckParam.isNull(updateReq.getCheckOutTime())){
            entity.setCheckOutTime(updateReq.getCheckOutTime());
        }
        if(!CheckParam.isNull(updateReq.getRemark())){
            entity.setRemark(updateReq.getRemark());
        }
    }
}
