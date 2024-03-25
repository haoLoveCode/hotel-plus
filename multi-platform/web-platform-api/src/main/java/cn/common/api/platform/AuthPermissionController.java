package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.AuthPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.ApiPerm;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionAddReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionUpdateReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.api.controller
* @Description: 权限前端控制器
* @date 2024-03-25
*/
@RestController
@RequestMapping(value = "api/v1/authPermission")
@Slf4j
public class AuthPermissionController extends BaseApiController {

    @Resource
    private AuthPermissionService authPermissionService;

    /**
     * 新增权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增权限信息")
    public ApiResponse addItem(@RequestBody @Valid AuthPermissionAddReq addReq){
        authPermissionService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 需要被删除的权限信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除权限")
    //@ApiPerm(accessStatus = true)
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        authPermissionService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 更新权限请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新权限信息")
    @ApiPerm(accessStatus = true)
    public ApiResponse updateItem(@RequestBody @Valid AuthPermissionUpdateReq req){
        authPermissionService.updateItem(req);
        return apiResponse();
    }

    /**
     * 分页查询权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询权限信息")
    public ApiResponse<Pagination<AuthPermissionResp>> queryByPage(
        @RequestBody @Valid AuthPermissionReq pageReq){
        return apiResponse(authPermissionService.queryByPage(pageReq));
    }

    /**
     * 查询权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 查询集合Req
     * @return Pagination
     */
    @PostMapping(value = "/queryPermissionNodeList")
    @ApiLog(value = "查询权限集合")
    public ApiResponse<List<AuthPermissionResp>> queryPermissionNodeList(
            @RequestBody @Valid AuthPermissionReq pageReq){
        return apiResponse(authPermissionService.queryPermissionNodeList(pageReq));
    }

    /**
     * 查询权限树集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 查询集合Req
     * @return Pagination
     */
    @PostMapping(value = "/queryPermissionTreeList")
    @ApiLog(value = "查询权限树集合")
    public ApiResponse<List<AuthPermissionResp>> queryPermissionTreeList(
            AuthPermissionReq pageReq){
        return apiResponse(authPermissionService.queryPermissionTreeList(pageReq));
    }

    /**
     * 根据主键ID查询菜单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param mainId 菜单主键ID
     * @return cn.common.resp.platform.auth.perm.AuthPermissionResp
     */
    @GetMapping(value = "/queryPermissionByMainId")
    @ApiLog(value = "根据主键ID查询菜单信息")
    @NeedLogin
    public ApiResponse<AuthPermissionResp> queryPermissionByMainId(@RequestParam(name = "mainId") String mainId){
        return apiResponse(authPermissionService.queryPermissionByMainId(mainId));
    }

    /**
     * 查询所有权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 查询所有权限集合Req
     * @return java.util.List
     */
    @PostMapping(value = "/queryAllPermissionNodeList")
    @ApiLog(value = "查询所有权限集合")
    public ApiResponse<List<AuthPermissionResp>> queryAllPermissionNodeList(
            @RequestBody @Valid AuthPermissionReq pageReq){
        return apiResponse(authPermissionService.queryPermissionNodeList(pageReq));
    }
}
