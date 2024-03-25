package cn.common.service.biz;


import cn.common.req.biz.platform.OrderDataAddReq;
import cn.common.req.biz.platform.OrderDataReq;
import cn.common.req.biz.platform.OrderDataUpdateReq;
import cn.common.resp.biz.openBiz.OrderDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 订单信息相关服务
 * @date 2024-03-25
 */
public interface OrderDataService {

    /**
     * 导出数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
     */
    void exportData(OrderDataReq pageReq);

    /**
     * 新增
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    void addItem(OrderDataAddReq addReq);

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询单个的订单数据
     * @author: Singer
     * @date 2024-03-25
     * @param outTradeNo 交易订单号
     * @return java.util.List
     */
    OrderDataResp queryByTradeNo(String outTradeNo);

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    List<OrderDataResp> queryAllOrderData();

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<OrderDataResp> queryByPage(
        OrderDataReq pageReq);

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    void updateItem(OrderDataUpdateReq updateReq);

}
