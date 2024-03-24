package cn.common.service.biz.platform;


import cn.common.req.biz.RoomBookingAddReq;
import cn.common.req.biz.RoomBookingReq;
import cn.common.req.biz.RoomBookingUpdateReq;
import cn.common.resp.biz.RoomBookingResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间预订信息相关服务
 * @date 2024/3/24
 */
public interface RoomBookingService {

    /**
     * 导出房间预订信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    void exportData(RoomBookingReq pageReq);

    /**
     * 新增房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增Req
     */
    void addItem(RoomBookingAddReq addReq);

    /**
     * 批量删除房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间预订信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    List<RoomBookingResp> queryRoomBooking(RoomBookingReq req);

    /**
     * 查询单个房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    RoomBookingResp queryOneRoomBooking(RoomBookingReq req);

    /**
     * 分页查询房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间预订信息Req
     * @return Pagination
     */
    Pagination<RoomBookingResp> queryByPage(
        RoomBookingReq pageReq);

    /**
     * 更新房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间预订信息请求参数
     */
    void updateItem(RoomBookingUpdateReq updateReq);

}
