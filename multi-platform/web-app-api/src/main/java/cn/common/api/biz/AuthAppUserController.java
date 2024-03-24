package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.app.auth.*;
import cn.common.req.biz.platform.AuthAppUserUpdateReq;
import cn.common.resp.biz.openBiz.AuthAppUserResp;
import cn.common.resp.biz.app.auth.AuthAppLoginResp;
import cn.common.service.biz.AuthAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.app.token.TokenVerifyResp;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: APP认证用户前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/authAppUser")
@Slf4j
public class AuthAppUserController extends BaseApiController {

    @Resource
    private AuthAppUserService authAppUserService;

    /**
     * 退出登录
     *
     * @return pro.skywalking.resp.base.ApiResponse
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping("/logOut")
    @ApiLog(value = "退出登录")
    @NeedLogin
    public ApiResponse logOut() {
        authAppUserService.logOut();
        return apiResponse();
    }

    /**
     * 用户名密码登陆
     *
     * @param req 用户名密码登陆请求参数
     * @return
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/userLogin")
    @ApiLog(value = "用户名密码登陆")
    @NeedLogin(value = false)
    public ApiResponse<AuthAppLoginResp> userLogin(@RequestBody @Valid
                                                   AuthAppUserLoginReq req) {
        return apiResponse(authAppUserService.userLogin(req));
    }

    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信手机号
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/bindWxPhone")
    @ApiLog(value = "绑定用户的微信手机号")
    @NeedLogin()
    public ApiResponse<AuthAppLoginResp> bindWxPhone(@RequestBody @Valid
                                                     WxMiniPhoneReq req) {
        return apiResponse(authAppUserService.bindWxPhone(req));
    }

    /**
     * 用户注册
     *
     * @param req 用户名密码注册请求参数
     * @return
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/userReg")
    @ApiLog(value = "用户注册")
    @NeedLogin(value = false)
    public ApiResponse<AuthAppLoginResp> userReg(@RequestBody @Valid
                                                 AuthAppUserRegReq req) {
        return apiResponse(authAppUserService.userReg(req));
    }

    /**
     * Web用户注册
     *
     * @param regReq Web用户注册请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/webUserReg")
    @ApiLog(value = "Web用户注册")
    @NeedLogin(value = false)
    public ApiResponse<AuthAppLoginResp> webUserReg(@RequestBody @Valid AuthWebUserRegReq regReq){
        return apiResponse(authAppUserService.webUserReg(regReq));
    }

    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信openId
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/bindWxOpenId")
    @ApiLog(value = "绑定用户的微信openId")
    @NeedLogin(value = false)
    public ApiResponse<AuthAppLoginResp> bindWxOpenId(@RequestBody @Valid
                                                      WxMiniOpenIdReq req) {
        return apiResponse(authAppUserService.bindWxOpenId(req));
    }

    /**
     * 验证Token
     *
     * @return java.lang.Boolean
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @GetMapping("/verifyToken")
    @ApiLog(value = "验证Token")
    @NeedLogin(value = false)
    public ApiResponse<TokenVerifyResp> verifyToken() {
        return apiResponse(authAppUserService.verifyToken());
    }

    /**
     * 更新APP认证用户
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @PutMapping(value = "/updateAppAuthUser")
    @ApiLog(value = "更新APP认证用户")
    @NeedLogin()
    public ApiResponse updateAppAuthUser(@RequestBody @Valid AuthAppUserUpdateReq updateReq) {
        authAppUserService.updateAppAuthUser(updateReq);
        return apiResponse();
    }

    /**
     * @param
     * @return cn.common.resp.biz.openBiz.AuthAppUserResp
     * @description: 查询当前APP用户的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @GetMapping(value = "/currentUserMeta")
    @ApiLog(value = "查询当前APP用户的数据")
    @NeedLogin()
    public ApiResponse<AuthAppLoginResp> currentUserMeta() {
        return apiResponse(authAppUserService.currentUserMeta());
    }

    /**
     * 查询所有APP认证用户信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @GetMapping(value = "/queryAuthAppUser")
    @ApiLog(value = "查询所有信息")
    @NeedLogin()
    public ApiResponse<List<AuthAppUserResp>> queryAuthAppUser(){
        return apiResponse(authAppUserService.queryAuthAppUser());
    }
}
