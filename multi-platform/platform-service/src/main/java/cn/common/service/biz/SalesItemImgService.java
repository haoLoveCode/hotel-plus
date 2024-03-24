package cn.common.service.biz;


import cn.common.req.biz.openBiz.SalesItemImgAddReq;
import cn.common.req.biz.openBiz.SalesItemImgReq;
import cn.common.req.biz.openBiz.SalesItemImgUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemImgResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品图片信息相关服务
 * @date 2024-03-06
 */
public interface SalesItemImgService {

    /**
     * 导出商品图片信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    void exportData(SalesItemImgReq pageReq);

    /**
     * 新增商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(SalesItemImgAddReq addReq);

    /**
     * 批量删除商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的商品图片信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<SalesItemImgResp> querySalesItemImg(SalesItemImgReq req);

    /**
     * 查询单个商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    SalesItemImgResp queryOneSalesItemImg(SalesItemImgReq req);

    /**
     * 分页查询商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询商品图片信息Req
     * @return Pagination
     */
    Pagination<SalesItemImgResp> queryByPage(
        SalesItemImgReq pageReq);

    /**
     * 更新商品图片信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新商品图片信息请求参数
     */
    void updateItem(SalesItemImgUpdateReq updateReq);

}
