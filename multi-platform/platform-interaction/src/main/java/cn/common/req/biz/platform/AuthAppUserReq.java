package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;

import java.io.Serializable;

/**
 * APP认证用户分页查询请求封装类
 * @title: AuthAppUserReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class AuthAppUserReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 654148487177843117L;

    /**
     * 业务主键ID->"authAppUserId"
     */
    private String authAppUserId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户编号
     */
    private String userNumber;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 绑定三方平台的OpenID
     */
    private String openId;

    /**
     * 明文密码
     */
    private String decryptionPassword;

    /**
     * 密码(MD5)
     */
    private String password;

    /**
     * 用户状态 跟随枚举
     */
    private Integer authStatus;

}
