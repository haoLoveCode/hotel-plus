package cn.common.req.biz.app.auth;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;

/**
 * 系统用户注册
 * @title: Place.java
 * @author create by singer - Singer email:singer-coder@qq.com
 * @date 2024-03-25 11:13
 */
@Data
public class AuthWebUserRegReq implements Serializable {

	private static final long serialVersionUID = 3083153893363450633L;

	/**
	 * 用户名
	 */
	@NotEmpty(message = "用户名->不可为空")
	private String userName;

	/**
	 * 用户编号
	 */
	@NotEmpty(message = "用户编号->不可为空")
	private String userNumber;

	/**
	 * 手机号码
	 */
	@NotEmpty(message = "手机号码->不可为空")
	private String phoneNumber;

	/**
	 * 昵称
	 */
	@NotEmpty(message = "昵称->不可为空")
	private String nickName;

	/**
	 * 头像
	 */
	//@NotEmpty(message = "头像->不可为空")
	private String avatarUrl;

	/**
	 * 真实姓名
	 */
	@NotEmpty(message = "真实姓名->不可为空")
	private String realName;

	/**
	 * 性别
	 */
	@NotEmpty(message = "性别->不可为空")
	private Integer gender;

	/**
	 * 明文密码
	 */
	@NotEmpty(message = "明文密码->不可为空")
	private String decryptionPassword;

	/**
	 * 密码(MD5)

	 @NotEmpty(message = "密码(MD5)->不可为空")*/
	private String password;

}
