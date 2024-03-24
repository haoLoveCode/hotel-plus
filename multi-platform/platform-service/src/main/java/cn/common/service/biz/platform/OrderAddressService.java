package cn.common.service.biz.platform;


import cn.common.req.biz.OrderAddressAddReq;
import cn.common.req.biz.OrderAddressReq;
import cn.common.req.biz.OrderAddressUpdateReq;
import cn.common.resp.biz.OrderAddressResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 订单-收货地址关联信息相关服务
 * @date 2024-03-04
 */
public interface OrderAddressService {

    /**
     * 导出订单-收货地址关联信息数据
     * @author: Singer
     * @date 2024-03-04
     * @param pageReq
     * @return java.util.List
     */
    void exportData(OrderAddressReq pageReq);

    /**
     * 新增订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param addReq 新增Req
     */
    void addItem(OrderAddressAddReq addReq);

    /**
     * 批量删除订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param req 需要被删除的订单-收货地址关联信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    List<OrderAddressResp> queryOrderAddress(OrderAddressReq req);

    /**
     * 查询单个订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    OrderAddressResp queryOneOrderAddress(OrderAddressReq req);

    /**
     * 分页查询订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param  pageReq 分页查询订单-收货地址关联信息Req
     * @return Pagination
     */
    Pagination<OrderAddressResp> queryByPage(
        OrderAddressReq pageReq);

    /**
     * 更新订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param updateReq 更新订单-收货地址关联信息请求参数
     */
    void updateItem(OrderAddressUpdateReq updateReq);

}
