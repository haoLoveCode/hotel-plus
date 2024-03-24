package cn.common.service.platform;


import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.role.AuthRoleAddReq;
import pro.skywalking.req.platform.auth.role.AuthRoleReq;
import pro.skywalking.req.platform.auth.role.AuthRoleUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;

import java.util.List;


/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.service
* @Description: 系统角色相关服务
* @date 2024-03-06
*/
public interface AuthRoleService {

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(AuthRoleAddReq addReq);

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询所有用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-067
     * @return java.util.List
     */
    List<AuthRoleResp> queryAllAuthRole();

    /**
     * 查询角色具备的权限信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param authRoleId 角色ID
     * @return java.util.List
     */
    List<AuthPermissionResp> queryRolePermission(String authRoleId);

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<AuthRoleResp> queryByPage(
        AuthRoleReq pageReq);

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新请求参数
     */
    void updateItem(AuthRoleUpdateReq updateReq);

}
