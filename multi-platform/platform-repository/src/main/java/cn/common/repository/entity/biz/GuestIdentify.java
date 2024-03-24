package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 客户身份信息实体
 * @title: GuestIdentify.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("guest_identify")
public class GuestIdentify extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -226491193633220178L;

    /**
     * 业务主键ID
     */
    private String guestIdentifyId;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String identifyNo;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 备注信息
     */
    private String remark;

}
