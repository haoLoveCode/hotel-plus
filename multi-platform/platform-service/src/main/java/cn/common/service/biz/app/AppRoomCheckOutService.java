package cn.common.service.biz.app;


import cn.common.req.biz.RoomCheckOutAddReq;
import cn.common.req.biz.RoomCheckOutReq;
import cn.common.req.biz.RoomCheckOutUpdateReq;
import cn.common.resp.biz.RoomCheckOutResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 客房退房信息相关服务
 * @date 2024/3/24
 */
public interface AppRoomCheckOutService {

    /**
     * 新增客房退房信息
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024/3/24
     */
    void addItem(RoomCheckOutAddReq addReq);

    /**
     * 批量删除客房退房信息信息
     *
     * @param req 需要被删除的客房退房信息信息
     * @author: Singer
     * @date 2024/3/24
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询客房退房信息信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    List<RoomCheckOutResp> queryRoomCheckOut(RoomCheckOutReq req);

    /**
     * 查询单个客房退房信息信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    RoomCheckOutResp queryOneRoomCheckOut(RoomCheckOutReq req);

    /**
     * 分页查询客房退房信息
     *
     * @param pageReq 分页查询客房退房信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    Pagination<RoomCheckOutResp> queryByPage(
            RoomCheckOutReq pageReq);

    /**
     * 更新客房退房信息
     *
     * @param updateReq 更新客房退房信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    void updateItem(RoomCheckOutUpdateReq updateReq);

}
