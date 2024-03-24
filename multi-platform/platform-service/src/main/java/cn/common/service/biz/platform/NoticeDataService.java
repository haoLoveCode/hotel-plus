package cn.common.service.biz.platform;


import cn.common.req.biz.openBiz.NoticeDataAddReq;
import cn.common.req.biz.openBiz.NoticeDataReq;
import cn.common.req.biz.openBiz.NoticeDataUpdateReq;
import cn.common.resp.biz.openBiz.NoticeDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 公告信息相关服务
 * @date 2024-03-06
 */
public interface NoticeDataService {

    /**
     * 导出公告信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    void exportData(NoticeDataReq pageReq);

    /**
     * 新增公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(NoticeDataAddReq addReq);

    /**
     * 批量删除公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的公告信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<NoticeDataResp> queryNoticeData(NoticeDataReq req);

    /**
     * 查询单个公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    NoticeDataResp queryOneNoticeData(NoticeDataReq req);

    /**
     * 分页查询公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询公告信息Req
     * @return Pagination
     */
    Pagination<NoticeDataResp> queryByPage(
        NoticeDataReq pageReq);

    /**
     * 更新公告信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新公告信息请求参数
     */
    void updateItem(NoticeDataUpdateReq updateReq);

}
