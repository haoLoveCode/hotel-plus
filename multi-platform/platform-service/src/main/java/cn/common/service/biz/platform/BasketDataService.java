package cn.common.service.biz.platform;


import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.resp.biz.BasketDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 购物车信息相关服务
 * @date 2024-03-06
 */
public interface BasketDataService {

    /**
     * 导出购物车信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    void exportData(BasketDataReq pageReq);

    /**
     * 新增购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(BasketDataAddReq addReq);

    /**
     * 批量删除购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的购物车信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<BasketDataResp> queryBasketData(BasketDataReq req);

    /**
     * 查询单个购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    BasketDataResp queryOneBasketData(BasketDataReq req);

    /**
     * 分页查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询购物车信息Req
     * @return Pagination
     */
    Pagination<BasketDataResp> queryByPage(
        BasketDataReq pageReq);

    /**
     * 更新购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新购物车信息请求参数
     */
    void updateItem(BasketDataUpdateReq updateReq);

}
