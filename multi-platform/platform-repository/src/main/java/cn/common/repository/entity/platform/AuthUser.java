package cn.common.repository.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;
import pro.skywalking.enums.AuthStatusEnum;
import pro.skywalking.utils.SnowflakeIdWorker;

import java.io.Serializable;


/**
* 系统用户实体
* @title: AuthUser.java
* @author create by Singer - Singer email:singer-coder@qq.com
* @date 2024-03-06 11:13
*/
@Data
@TableName("auth_user")
public class AuthUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4161211307007064956L;

    /**
     * 业务主键ID
     */
    private String authUserId = SnowflakeIdWorker.uniqueMainId();

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
     * 明文密码
     */
    private String decryptionPassword;

    /**
     * 密码(MD5)
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户状态
     */
    private Integer authStatus = AuthStatusEnum.DISABLED.getCode();

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickName;

}
