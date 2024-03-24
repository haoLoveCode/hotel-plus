package cn.common.service.biz.platform;


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
 * @date 2024/1/25
 */
public interface RoomTypeService {

    /**
     * 导出房间类型信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
     */
    void exportData(RoomTypeReq pageReq);

    /**
     * 新增房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增Req
     */
    void addItem(RoomTypeAddReq addReq);

    /**
     * 批量删除房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的房间类型信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    List<RoomTypeResp> queryRoomType(RoomTypeReq req);

    /**
     * 查询单个房间类型信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    RoomTypeResp queryOneRoomType(RoomTypeReq req);

    /**
     * 分页查询房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询房间类型信息Req
     * @return Pagination
     */
    Pagination<RoomTypeResp> queryByPage(
        RoomTypeReq pageReq);

    /**
     * 更新房间类型信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新房间类型信息请求参数
     */
    void updateItem(RoomTypeUpdateReq updateReq);

}
