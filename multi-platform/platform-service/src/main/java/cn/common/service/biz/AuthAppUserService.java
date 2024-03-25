package cn.common.service.biz;


import cn.common.req.biz.app.auth.*;
import cn.common.req.biz.platform.AuthAppUserAddReq;
import cn.common.req.biz.platform.AuthAppUserReq;
import cn.common.req.biz.platform.AuthAppUserUpdateReq;
import cn.common.resp.biz.app.auth.AuthAppLoginResp;
import cn.common.resp.biz.openBiz.AuthAppUserResp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.app.token.TokenVerifyResp;
import pro.skywalking.resp.page.Pagination;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: APP认证用户相关服务
 * @date 2024-03-25
 */
public interface AuthAppUserService {

    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信手机号
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp bindWxPhone(WxMiniPhoneReq req);

    /**
     * 导出数据
     *
     * @param pageReq
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-25
     */
    void exportData(AuthAppUserReq pageReq);

    /**
     * 系统退出登录
     *
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    void logOut();

    /**
     * 用户名密码登陆
     *
     * @param req 用户名密码登陆请求参数
     * @return
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp userLogin(AuthAppUserLoginReq req);

    /**
     * 用户注册
     *
     * @param req 用户名密码注册请求参数
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp userReg(AuthAppUserRegReq req);

    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信openId
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp bindWxOpenId(WxMiniOpenIdReq req);

    /**
     * 拿到当前登录用户的Token
     *
     * @return java.lang.Long
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    String currentToken();

    /**
     * 验证Token
     *
     * @return java.lang.Boolean
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    TokenVerifyResp verifyToken();

    /**
     * 拿到APP登陆的当前用户ID
     *
     * @return java.lang.String
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    String authAppUserId();

    /**
     * 拿到登录后的用户信息 不抛出异常
     *
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp queryLoginUserMetaNoThrow();

    /**
     * 拿到登录后的用户信息
     *
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp queryLoginUserMeta();

    /**
     * 新增
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024-03-25
     */
    void addItem(AuthAppUserAddReq addReq);

    /**
     * Web用户注册
     *
     * @param regReq Web用户注册请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    AuthAppLoginResp webUserReg(AuthWebUserRegReq regReq);

    /**
     * 批量删除信息
     *
     * @param req 需要被删除的信息
     * @author: Singer
     * @date 2024-03-25
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询所有信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-25
     */
    List<AuthAppUserResp> queryAuthAppUser();

    /**
     * @param
     * @return cn.common.resp.biz.openBiz.AuthAppUserResp
     * @description: 查询当前APP用户的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    AuthAppLoginResp currentUserMeta();

    /**
     * 分页查询
     *
     * @param pageReq 分页查询Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-25
     */
    Pagination<AuthAppUserResp> queryByPage(
            AuthAppUserReq pageReq);

    /**
     * 更新
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    void updateItem(AuthAppUserUpdateReq updateReq);

    /**
     * 更新APP认证用户
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    void updateAppAuthUser(AuthAppUserUpdateReq updateReq);

}
