package cn.common.service.platform;


import cn.common.internal.AuthUserLoginReq;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.captcha.CaptchaReq;
import pro.skywalking.req.platform.auth.user.AuthUserAddReq;
import pro.skywalking.req.platform.auth.user.AuthUserReq;
import pro.skywalking.req.platform.auth.user.AuthUserUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.captcha.CaptchaResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;
import pro.skywalking.resp.platform.auth.user.AuthUserResp;
import pro.skywalking.resp.platform.auth.user.PlatformLoginResp;
import pro.skywalking.resp.platform.other.AuthUserSketchResp;

import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 系统用户相关服务
 * @date 2024-03-25
 */
public interface AuthUserService {

    /**
     * @description: 生成图形验证码
     * @auther: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param captchaReq 生成图形验证码请求参数
     * @return pro.skywalking.resp.platform.auth.captcha.CaptchaResp
     */
    CaptchaResp base64captchaCode(CaptchaReq captchaReq);

    /**
     * 拿到当前登录用户的Token
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return java.lang.Long
     */
    String currentToken();

    /**
     * 拿到用户ID
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return java.lang.String
     */
    String currentAuthUserId();

    /**
     * 拿到登录后的用户信息 不抛出异常
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    PlatformLoginResp queryLoginUserMetaNoThrow();

    /**
     * 拿到登录后的用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    PlatformLoginResp queryLoginUserMeta();

    /**
     * 查询所有用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-257
     * @return java.util.List
     */
    List<AuthUserSketchResp> queryAllAuthUser();

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param addReq 新增Req
     */
    void addItem(AuthUserAddReq addReq);

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 需要被删除的信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询用户角色信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param authUserId 用户ID
     * @return java.util.List
     */
    List<AuthRoleResp> queryRoleByAuthUserId(String authUserId);

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 分页查询Req
     * @return pro.skywalking.resp.page.Pagination
     */
    Pagination<AuthUserResp> queryByPage(
            AuthUserReq pageReq);

    /**
     * 系统退出登录
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    void logOut();

    /**
     * 拿到登录后返回的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    PlatformLoginResp queryLoginMeta();

    /**
     * 系统登录
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 用户登录请求参数
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    PlatformLoginResp authUserLogin(AuthUserLoginReq req);

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param updateReq 更新请求参数
     */
    void updateItem(AuthUserUpdateReq updateReq);

}
