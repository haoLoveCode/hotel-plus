package cn.common.service.biz.app;


import cn.common.req.biz.*;
import cn.common.resp.biz.RoomBookingResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间预订信息相关服务
 * @date 2024/3/24
 */
public interface AppRoomBookingService {

    /**
     *
     * @description: 取消预定
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/3/25
     * @param req
     * @return
     */
    void cancelBooking(BookingRoomCancelReq req);

    /**
     * 新增房间预订信息
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024/3/24
     */
    TradeOrderResp addItem(BookingRoomReq addReq);

    /**
     * 批量删除房间预订信息
     *
     * @param req 需要被删除的房间预订信息
     * @author: Singer
     * @date 2024/3/24
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间预订信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    List<RoomBookingResp> queryRoomBooking(RoomBookingReq req);

    /**
     * 查询单个房间预订信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    RoomBookingResp queryOneRoomBooking(RoomBookingReq req);

    /**
     * 分页查询房间预订信息
     *
     * @param pageReq 分页查询房间预订信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    Pagination<RoomBookingResp> queryByPage(
            RoomBookingReq pageReq);

    /**
     * 更新房间预订信息
     *
     * @param updateReq 更新房间预订信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    void updateItem(RoomBookingUpdateReq updateReq);

}
