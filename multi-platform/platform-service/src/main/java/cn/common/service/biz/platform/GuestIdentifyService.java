package cn.common.service.biz.platform;


import cn.common.req.biz.GuestIdentifyAddReq;
import cn.common.req.biz.GuestIdentifyReq;
import cn.common.req.biz.GuestIdentifyUpdateReq;
import cn.common.resp.biz.GuestIdentifyResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 客户身份信息相关服务
 * @date 2024/3/24
 */
public interface GuestIdentifyService {

    /**
     * 导出客户身份信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
     */
    void exportData(GuestIdentifyReq pageReq);

    /**
     * 新增客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增Req
     */
    void addItem(GuestIdentifyAddReq addReq);

    /**
     * 批量删除客户身份信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客户身份信息信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询客户身份信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    List<GuestIdentifyResp> queryGuestIdentify(GuestIdentifyReq req);

    /**
     * 查询单个客户身份信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    GuestIdentifyResp queryOneGuestIdentify(GuestIdentifyReq req);

    /**
     * 分页查询客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客户身份信息Req
     * @return Pagination
     */
    Pagination<GuestIdentifyResp> queryByPage(
        GuestIdentifyReq pageReq);

    /**
     * 更新客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客户身份信息请求参数
     */
    void updateItem(GuestIdentifyUpdateReq updateReq);

}
