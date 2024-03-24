package cn.common.service.biz.platform;


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
 * @date 2024/1/25
 */
public interface RoomDataService {

    /**
     * 导出房间信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
     */
    void exportData(RoomDataReq pageReq);

    /**
     * 新增房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增Req
     */
    void addItem(RoomDataAddReq addReq);

    /**
     * 批量删除房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的房间信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    List<RoomDataResp> queryRoomData(RoomDataReq req);

    /**
     * 查询单个房间信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    RoomDataResp queryOneRoomData(RoomDataReq req);

    /**
     * 分页查询房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询房间信息Req
     * @return Pagination
     */
    Pagination<RoomDataResp> queryByPage(
        RoomDataReq pageReq);

    /**
     * 更新房间信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新房间信息请求参数
     */
    void updateItem(RoomDataUpdateReq updateReq);

}
