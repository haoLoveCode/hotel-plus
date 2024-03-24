package cn.common.service.biz.app;


import cn.common.repository.entity.biz.RoomData;
import cn.common.req.biz.openBiz.roomData.RoomDataAddReq;
import cn.common.req.biz.openBiz.roomData.RoomDataReq;
import cn.common.req.biz.openBiz.roomData.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间信息相关服务
 * @date 2024/3/24
 */
public interface AppRoomDataService {

    /**
     *
     * @description: 根据房间预定ID查询房间信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/3/24
     * @param roomBookingId 房间预定ID
     * @return cn.common.repository.entity.biz.RoomData
     */
    RoomData queryRoomByBookingUd(String roomBookingId);

    /**
     * 新增房间信息
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024/3/24
     */
    void addItem(RoomDataAddReq addReq);

    /**
     * 批量删除房间信息
     *
     * @param req 需要被删除的房间信息
     * @author: Singer
     * @date 2024/3/24
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    List<RoomDataResp> queryRoomData(RoomDataReq req);

    /**
     * 查询单个房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    RoomDataResp queryOneRoomData(RoomDataReq req);

    /**
     * 分页查询房间信息
     *
     * @param pageReq 分页查询房间信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    Pagination<RoomDataResp> queryByPage(
            RoomDataReq pageReq);

    /**
     * 更新房间信息
     *
     * @param updateReq 更新房间信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    void updateItem(RoomDataUpdateReq updateReq);

}
