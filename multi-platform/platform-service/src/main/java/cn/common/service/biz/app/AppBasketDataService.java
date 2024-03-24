package cn.common.service.biz.app;


import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.req.biz.app.ItemOrderingOneReq;
import cn.common.req.biz.app.OrderingByBasketItemReq;
import cn.common.resp.biz.BasketDataResp;
import cn.common.resp.biz.app.AppBasketItemResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 购物车信息相关服务
 * @date 2024-03-06
 */
public interface AppBasketDataService {

    /**
     *
     * @description: 购物车商品信息下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 购物车商品信息下单参数
     * @return
     */
    String basketItemOrdering(OrderingByBasketItemReq req);

    /**
     *
     * @description: 商品单个信息下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req  商品单个信息下单Req
     * @return
     */
    TradeOrderResp orderingOne(ItemOrderingOneReq req);

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    List<AppBasketItemResp> queryBasketItem();

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的单个商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    AppBasketItemResp queryOneBasketItem(BasketDataReq req);

    /**
     * 购物车业务处理
     *
     * @param addReq 购物车业务处理Req
     * @author: Singer
     * @date 2024-03-06
     */
    void handleBasketBiz(BasketDataAddReq addReq);

    /**
     * 批量删除购物车信息
     *
     * @param req 需要被删除的购物车信息
     * @author: Singer
     * @date 2024-03-06
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    List<BasketDataResp> queryBasketData(BasketDataReq req);

    /**
     * 查询单个购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    BasketDataResp queryOneBasketData(BasketDataReq req);

    /**
     * 分页查询购物车信息
     *
     * @param pageReq 分页查询购物车信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-06
     */
    Pagination<BasketDataResp> queryByPage(
            BasketDataReq pageReq);

    /**
     * 更新购物车信息
     *
     * @param updateReq 更新购物车信息请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    void updateItem(BasketDataUpdateReq updateReq);

}
