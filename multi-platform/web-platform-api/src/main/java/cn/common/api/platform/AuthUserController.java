package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.internal.AuthUserLoginReq;
import cn.common.service.platform.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.ApiPerm;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.captcha.CaptchaReq;
import pro.skywalking.req.platform.auth.user.AuthUserAddReq;
import pro.skywalking.req.platform.auth.user.AuthUserReq;
import pro.skywalking.req.platform.auth.user.AuthUserUpdateReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.captcha.CaptchaResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;
import pro.skywalking.resp.platform.auth.user.AuthUserResp;
import pro.skywalking.resp.platform.auth.user.PlatformLoginResp;
import pro.skywalking.resp.platform.other.AuthUserSketchResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.api.controller
* @Description: 前端控制器
* @date 2024-03-25
*/
@RestController
@RequestMapping(value = "api/v1/authUser")
@Slf4j
public class AuthUserController extends BaseApiController {

    @Resource
    private AuthUserService authUserService;

    /**
     * @description: 生成图形验证码
     * @auther: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param captchaReq 生成图形验证码请求参数
     * @return pro.skywalking.resp.platform.auth.captcha.CaptchaResp
     */
    @PostMapping("/base64captchaCode")
    @ApiLog(value = "生成Base64图形验证码")
    @NeedLogin(value = false)
    public ApiResponse<CaptchaResp> base64captchaCode(@RequestBody CaptchaReq captchaReq){
        return apiResponse(authUserService.base64captchaCode(captchaReq));
    }

    /**
     * 拿到登录后的用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return cn.common.resp.base.ApiResponse
     */
    @GetMapping("/queryLoginUserMeta")
    @ApiLog(value = "拿到登录后的用户信息")
    public ApiResponse<PlatformLoginResp> queryLoginUserMeta(){
        return apiResponse(authUserService.queryLoginUserMeta());
    }

    /**
     * 系统登录
     * 登录后返回该用户角色
     * @param req
     * @return cn.common.resp.base.ApiResponse
     */
    @PostMapping("/authUserLogin")
    @ApiLog(value = "系统登录")
    @NeedLogin(value = false)
    public ApiResponse<PlatformLoginResp> authUserLogin(@RequestBody AuthUserLoginReq req){
        return apiResponse(authUserService.authUserLogin(req));
    }

    /**
     * 拿到登录后返回的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param
     * @return cn.common.resp.platform.auth.user.PlatformLoginResp
     */
    @GetMapping("/queryLoginMeta")
    @ApiLog(value = "拿到登录后返回的数据")
    @NeedLogin()
    public ApiResponse<PlatformLoginResp> queryLoginMeta(){
        return apiResponse(authUserService.queryLoginMeta());
    }

    /**
      * 退出登录
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-25
      * @return cn.common.resp.base.ApiResponse
      */
    @PostMapping("/logOut")
    @ApiLog(value = "退出登录")
    public ApiResponse logOut(){
        authUserService.logOut();
        return apiResponse();
    }

    /**
     * 查询所有用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-257
     * @return java.util.List
     */
    @GetMapping("/queryAllAuthUser")
    @ApiLog(value = "查询所有用户信息")
    public ApiResponse<List<AuthUserSketchResp>> queryAllAuthUser(){
        return apiResponse(authUserService.queryAllAuthUser());
    }

    /**
     * 新增用户
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增新增用户信息")
    public ApiResponse addItem(@RequestBody @Valid AuthUserAddReq addReq){
        authUserService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量用户
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除用户")
    @ApiPerm(accessStatus = true)
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        authUserService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新用户
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新用户信息")
    @ApiPerm(accessStatus = true)
    public ApiResponse updateItem(@RequestBody @Valid AuthUserUpdateReq updateReq){
        authUserService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 分页查询用户
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询用户信息")
    public ApiResponse<Pagination<AuthUserResp>> queryByPage(
        @RequestBody @Valid AuthUserReq pageReq){
        return apiResponse(authUserService.queryByPage(pageReq));
    }

    /**
     * 查询用户角色信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param authUserId 用户ID
     * @return java.util.List
     */
    @GetMapping(value = "/queryRoleByAuthUserId")
    @ApiLog(value = "查询用户角色信息")
    public ApiResponse<List<AuthRoleResp>> queryRoleByAuthUserId(@RequestParam(name = "authUserId") String authUserId){
        return apiResponse(authUserService.queryRoleByAuthUserId(authUserId));
    }
}
