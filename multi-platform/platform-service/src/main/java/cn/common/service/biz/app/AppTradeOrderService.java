package cn.common.service.biz.app;


import cn.common.req.biz.platform.SetTradeOrderReq;
import cn.common.req.biz.platform.TradeOrderAddReq;
import cn.common.req.biz.platform.TradeOrderReq;
import cn.common.req.biz.platform.TradeOrderUpdateReq;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 交易订单信息相关服务
 * @date 2024-03-25
 */
public interface AppTradeOrderService {

    /**
     * 新增
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    void addItem(TradeOrderAddReq addReq);

    /**
     * 批量删除信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     *
     * @description: 查询单个交易订单数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  outTradeNo 外部交易订单号
     * @return cn.common.resp.biz.openBiz.TradeOrderResp
     */
    TradeOrderResp queryByOrderNo(String outTradeNo);

    /**
     *
     * @description: 查询当前用户的所有订单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return java.util.List
     */
    List<TradeOrderResp> queryOrderList();

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    List<TradeOrderResp> queryAllTradeOrder();

    /**
     * 分页查询
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<TradeOrderResp> queryByPage(
        TradeOrderReq pageReq);

    /**
     * 更新订单状态
     * @author: Singer
     * @date 2024-03-25
     * @param req 更新请求参数
     */
    void setOrderStatus(SetTradeOrderReq req);

    /**
     * 更新
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    void updateItem(TradeOrderUpdateReq updateReq);

}
