package cn.common.service.platform;


import cn.common.repository.entity.platform.AuthPermission;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionAddReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;

import java.util.List;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.service
* @Description: 系统权限相关服务
* @date 2024-03-06
*/
public interface AuthPermissionService {

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    void addItem(AuthPermissionAddReq addReq);

    /**
     *
     * @description: 根据业务主键ID删除
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param mainIdList 业务主键ID
     * @return
     */
    void deletePermById(List<String> mainIdList);

    /**
     *
     * @description: 根据菜单名称删除权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param pathList 业务主键ID
     * @return
     */
    void deletePermByPath(List<String> pathList);

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    Pagination<AuthPermissionResp> queryByPage(
        AuthPermissionReq pageReq);

    /**
     * 查询权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询权限集合Req
     * @return java.util.List
     */
    List<AuthPermissionResp> queryPermissionNodeList(
            AuthPermissionReq pageReq);

    /**
     * 查询权限树集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询集合Req
     * @return Pagination
     */
    List<AuthPermissionResp> queryPermissionTreeList(
            AuthPermissionReq pageReq);

    /**
     * 根据主键ID查询菜单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param mainId 菜单主键ID
     * @return cn.common.resp.platform.auth.perm.AuthPermissionResp
     */
    AuthPermissionResp queryPermissionByMainId(String mainId);

    /**
     * 查询所有权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询所有权限集合Req
     * @return java.util.List
     */
    List<AuthPermissionResp> queryAllPermissionNodeList(
            AuthPermissionReq pageReq);

    /**
     * 处理查询所有子节点
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param placeList 场所集合
     * @param authPermissionIdList 节点ID
     * @return
     */
    void queryChildrenPerm(List<AuthPermission> placeList, List<String> authPermissionIdList);

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 更新请求参数
     */
    void updateItem(AuthPermissionUpdateReq req);

}
