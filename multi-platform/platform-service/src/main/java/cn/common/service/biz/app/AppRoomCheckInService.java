package cn.common.service.biz.app;


import cn.common.req.biz.RoomCheckInAddReq;
import cn.common.req.biz.RoomCheckInReq;
import cn.common.req.biz.RoomCheckInUpdateReq;
import cn.common.resp.biz.RoomCheckInResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 客房入住信息相关服务
 * @date 2024/3/24
 */
public interface AppRoomCheckInService {

    /**
     * 新增客房入住信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增Req
     */
    void addItem(RoomCheckInAddReq addReq);

    /**
     * 批量删除客房入住信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客房入住信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询客房入住信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    List<RoomCheckInResp> queryRoomCheckIn(RoomCheckInReq req);

    /**
     * 查询单个客房入住信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    RoomCheckInResp queryOneRoomCheckIn(RoomCheckInReq req);

    /**
     * 分页查询客房入住信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客房入住信息Req
     * @return Pagination
     */
    Pagination<RoomCheckInResp> queryByPage(
        RoomCheckInReq pageReq);

    /**
     * 更新客房入住信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客房入住信息请求参数
     */
    void updateItem(RoomCheckInUpdateReq updateReq);

}
