package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * APP用户其他信息实体
 * @title: AppUserData.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("app_user_data")
public class AppUserData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3330563318909516538L;

    /**
     * 业务主键ID
     */
    private String appUserDataId;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String identityCard;

    /**
     * 性别
     */
    private Integer gender;

}
