package cn.common.service.biz.app;


import cn.common.req.biz.RoomTypeAddReq;
import cn.common.req.biz.RoomTypeReq;
import cn.common.req.biz.RoomTypeUpdateReq;
import cn.common.resp.biz.RoomTypeResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间类型信息相关服务
 * @date 2024/3/24
 */
public interface AppRoomTypeService {

    /**
     * 新增房间类型信息
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024/3/24
     */
    void addItem(RoomTypeAddReq addReq);

    /**
     * 批量删除房间类型信息
     *
     * @param req 需要被删除的房间类型信息
     * @author: Singer
     * @date 2024/3/24
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间类型信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    List<RoomTypeResp> queryRoomType(RoomTypeReq req);

    /**
     * 查询单个房间类型信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    RoomTypeResp queryOneRoomType(RoomTypeReq req);

    /**
     * 分页查询房间类型信息
     *
     * @param pageReq 分页查询房间类型信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    Pagination<RoomTypeResp> queryByPage(
            RoomTypeReq pageReq);

    /**
     * 更新房间类型信息
     *
     * @param updateReq 更新房间类型信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    void updateItem(RoomTypeUpdateReq updateReq);

}
