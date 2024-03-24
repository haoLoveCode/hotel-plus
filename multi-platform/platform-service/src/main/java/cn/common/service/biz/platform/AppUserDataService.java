package cn.common.service.biz.platform;


import cn.common.req.biz.openBiz.AppUserDataAddReq;
import cn.common.req.biz.openBiz.AppUserDataReq;
import cn.common.req.biz.openBiz.AppUserDataUpdateReq;
import cn.common.resp.biz.platform.AppUserDataResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: APP用户其他信息相关服务
 * @date 2024-03-06
 */
public interface AppUserDataService {

    /**
     * 导出APP用户其他信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
     */
    void exportData(AppUserDataReq pageReq);

    /**
     * 新增APP用户其他信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(AppUserDataAddReq addReq);

    /**
     * 批量删除APP用户其他信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的APP用户其他信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询所有APP用户其他信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<AppUserDataResp> queryAllAppUserData();

    /**
     * 分页查询APP用户其他信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询APP用户其他信息Req
     * @return Pagination
     */
    Pagination<AppUserDataResp> queryByPage(
        AppUserDataReq pageReq);

    /**
     * 更新APP用户其他信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新APP用户其他信息请求参数
     */
    void updateItem(AppUserDataUpdateReq updateReq);

}
