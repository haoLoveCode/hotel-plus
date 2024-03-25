package cn.common.service.biz;


import cn.common.req.biz.platform.PayDataAddReq;
import cn.common.req.biz.platform.PayDataReq;
import cn.common.req.biz.platform.PayDataUpdateReq;
import cn.common.resp.biz.openBiz.PayDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 支付数据信息相关服务
 * @date 2024-03-25
 */
public interface PayDataService {

    /**
     * 导出数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
     */
    void exportData(PayDataReq pageReq);

    /**
     * 新增
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    void addItem(PayDataAddReq addReq);

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    List<PayDataResp> queryAllPayData();

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<PayDataResp> queryByPage(
        PayDataReq pageReq);

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    void updateItem(PayDataUpdateReq updateReq);

}
