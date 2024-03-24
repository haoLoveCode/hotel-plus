package cn.common.service.platform;


import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.log.PlatformApiLogReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.log.ApiLogStatisticsResp;
import pro.skywalking.resp.platform.log.PlatformApiLogResp;

import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 系统日志相关服务
 * @date 2024-03-06
 */
public interface PlatformApiLogService {

    /**
     * @param
     * @return
     * @description: 清空日志表
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    void clearApiLog();

    /**
     * 批量删除信息
     *
     * @param req 需要被删除的信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 根据参数统计访问量
     *
     * @param req 查询参数
     * @return java.util.List
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    List<ApiLogStatisticsResp> statisticsVisitCount(PlatformApiLogReq req);

    /**
     * 分页查询
     *
     * @param pageReq 分页查询Req
     * @return pro.skywalking.resp.page.Pagination
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    Pagination<PlatformApiLogResp> queryByPage(
            PlatformApiLogReq pageReq);
}
