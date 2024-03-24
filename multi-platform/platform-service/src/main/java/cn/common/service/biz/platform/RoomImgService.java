package cn.common.service.biz.platform;


import cn.common.req.biz.RoomImgAddReq;
import cn.common.req.biz.RoomImgReq;
import cn.common.req.biz.RoomImgUpdateReq;
import cn.common.resp.biz.RoomImgResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 房间图片信息相关服务
 * @date 2024/3/24
 */
public interface RoomImgService {

    /**
     * 导出房间图片信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    void exportData(RoomImgReq pageReq);

    /**
     * 新增房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增Req
     */
    void addItem(RoomImgAddReq addReq);

    /**
     * 批量删除房间图片信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间图片信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询房间图片信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    List<RoomImgResp> queryRoomImg(RoomImgReq req);

    /**
     * 查询单个房间图片信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    RoomImgResp queryOneRoomImg(RoomImgReq req);

    /**
     * 分页查询房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间图片信息Req
     * @return Pagination
     */
    Pagination<RoomImgResp> queryByPage(
        RoomImgReq pageReq);

    /**
     * 更新房间图片信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间图片信息请求参数
     */
    void updateItem(RoomImgUpdateReq updateReq);

}
