package cn.common.service.biz.platform;


import cn.common.req.biz.openBiz.SalesItemTypeAddReq;
import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.req.biz.openBiz.SalesItemTypeUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品类型信息相关服务
 * @date 2024-03-06
 */
public interface SalesItemTypeService {

    /**
     * 导出商品类型信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    void exportData(SalesItemTypeReq pageReq);

    /**
     * 新增商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(SalesItemTypeAddReq addReq);

    /**
     * 批量删除商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品类型信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<SalesItemTypeResp> querySalesItemType(SalesItemTypeReq req);

    /**
     * 查询单个商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    SalesItemTypeResp queryOneSalesItemType(SalesItemTypeReq req);

    /**
     * 分页查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品类型信息Req
     * @return Pagination
     */
    Pagination<SalesItemTypeResp> queryByPage(
        SalesItemTypeReq pageReq);

    /**
     * 更新商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品类型信息请求参数
     */
    void updateItem(SalesItemTypeUpdateReq updateReq);

}
