package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.AuthRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.ApiPerm;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.role.AuthRoleAddReq;
import pro.skywalking.req.platform.auth.role.AuthRoleReq;
import pro.skywalking.req.platform.auth.role.AuthRoleUpdateReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.api.controller
* @Description: 前端控制器
* @date 2024-03-06
*/
@RestController
@RequestMapping(value = "api/v1/authRole")
@Slf4j
public class AuthRoleController extends BaseApiController {

    @Resource
    private AuthRoleService authRoleService;

    /**
     * 新增角色
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增角色Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增角色信息")
    public ApiResponse addItem(@RequestBody @Valid AuthRoleAddReq addReq){
        authRoleService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量角色
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 需要被删除的角色信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除角色")
    @ApiPerm(accessStatus = true)
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        authRoleService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新角色
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新角色请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新角色信息")
    @ApiPerm(accessStatus = true)
    public ApiResponse updateItem(@RequestBody @Valid AuthRoleUpdateReq updateReq){
        authRoleService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询所有角色信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-067
     * @return cn.common.resp.base.ApiResponse
     */
    @GetMapping(value = "/queryAllAuthRole")
    @ApiLog(value = "查询所有角色信息")
    @NeedLogin(value = false)
    public ApiResponse<List<AuthRoleResp>> queryAllAuthRole(){
        return apiResponse(authRoleService.queryAllAuthRole());
    }

    /**
     * 查询角色具备的权限信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param authRoleId 角色ID
     * @return cn.common.resp.base.ApiResponse
     */
    @GetMapping(value = "/queryRolePermission")
    @ApiLog(value = "查询角色具备的权限信息")
    public ApiResponse<List<AuthPermissionResp>> queryRolePermission(@RequestParam(name = "authRoleId")
                                                                                 String authRoleId){
        return apiResponse(authRoleService.queryRolePermission(authRoleId));
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询角色信息")
    public ApiResponse<Pagination<AuthRoleResp>> queryByPage(
        @RequestBody @Valid AuthRoleReq pageReq){
        return apiResponse(authRoleService.queryByPage(pageReq));
    }
}
