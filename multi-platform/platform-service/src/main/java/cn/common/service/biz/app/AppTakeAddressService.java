package cn.common.service.biz.app;


import cn.common.req.biz.TakeAddressAddReq;
import cn.common.req.biz.TakeAddressReq;
import cn.common.req.biz.TakeAddressUpdateReq;
import cn.common.resp.biz.TakeAddressResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 收货地址信息相关服务
 * @date 2024-03-06
 */
public interface AppTakeAddressService {

    /**
     * 新增收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(TakeAddressAddReq addReq);

    /**
     * 批量删除收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的收货地址信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<TakeAddressResp> queryTakeAddress(TakeAddressReq req);

    /**
     * 查询单个收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    TakeAddressResp queryOneTakeAddress(TakeAddressReq req);

    /**
     * 分页查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询收货地址信息Req
     * @return Pagination
     */
    Pagination<TakeAddressResp> queryByPage(
        TakeAddressReq pageReq);

    /**
     * 更新收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新收货地址信息请求参数
     */
    void updateItem(TakeAddressUpdateReq updateReq);

}
