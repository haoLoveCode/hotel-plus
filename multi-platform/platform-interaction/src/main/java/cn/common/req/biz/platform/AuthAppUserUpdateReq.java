package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;

/**
 * APP认证用户实体
 *
 * @author Singer
 * @title: AuthAppUserUpdateReq.java
 * @date 2024-03-06 11:13
 */
@Data
public class AuthAppUserUpdateReq implements Serializable {

    private static final long serialVersionUID = 8931822656712622980L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"authAppUserId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
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
    //@NotEmpty(message = "密码(MD5)->不可为空")
    private String password;

    /**
     * 用户状态 跟随枚举
     */
    private Integer authStatus;

    //-------------其他用户数据 start-----------------
    /**
     * 身份证号
     */
    private String identityCard;

    //-------------其他用户数据 end-----------------

}
