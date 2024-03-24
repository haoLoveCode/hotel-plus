package cn.common.service.biz.app;


import cn.common.req.biz.openBiz.SalesItemAddReq;
import cn.common.req.biz.openBiz.SalesItemReq;
import cn.common.req.biz.openBiz.SalesItemUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品信息相关服务
 * @date 2024-03-06
 */
public interface AppSalesItemService {

    /**
     * 新增商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(SalesItemAddReq addReq);

    /**
     * 批量删除商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<SalesItemResp> querySalesItem(SalesItemReq req);

    /**
     * 查询单个商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    SalesItemResp queryOneSalesItem(SalesItemReq req);

    /**
     * 分页查询商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品信息Req
     * @return Pagination
     */
    Pagination<SalesItemResp> queryByPage(
        SalesItemReq pageReq);

    /**
     * 更新商品信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品信息请求参数
     */
    void updateItem(SalesItemUpdateReq updateReq);

}
