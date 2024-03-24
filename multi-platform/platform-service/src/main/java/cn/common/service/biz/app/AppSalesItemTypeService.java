package cn.common.service.biz.app;


import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品类型信息相关服务
 * @date 2024-03-06
 */
public interface AppSalesItemTypeService {

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


}
