package cn.common.service.biz.platform;


import cn.common.req.biz.FeedbackDataAddReq;
import cn.common.req.biz.FeedbackDataReq;
import cn.common.req.biz.FeedbackDataUpdateReq;
import cn.common.resp.biz.FeedbackDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 投诉建议信息相关服务
 * @date 2024/3/24
 */
public interface FeedbackDataService {

    /**
     * 导出投诉建议信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    void exportData(FeedbackDataReq pageReq);

    /**
     * 新增投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增Req
     */
    void addItem(FeedbackDataAddReq addReq);

    /**
     * 批量删除投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的投诉建议信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    List<FeedbackDataResp> queryFeedbackData(FeedbackDataReq req);

    /**
     * 查询单个投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    FeedbackDataResp queryOneFeedbackData(FeedbackDataReq req);

    /**
     * 分页查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询投诉建议信息Req
     * @return Pagination
     */
    Pagination<FeedbackDataResp> queryByPage(
        FeedbackDataReq pageReq);

    /**
     * 更新投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新投诉建议信息请求参数
     */
    void updateItem(FeedbackDataUpdateReq updateReq);

}
