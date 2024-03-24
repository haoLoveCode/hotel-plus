package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户身份信息请求数据封装类
 * @title: GuestIdentifyUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class GuestIdentifyUpdateReq implements Serializable {

    private static final long serialVersionUID = 185098560901177330L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"guestIdentifyId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String guestIdentifyId;

    /**
     * 姓名
     */
    @NotEmpty(message = "姓名->不可为空")
    private String realName;

    /**
     * 身份证号
     */
    @NotEmpty(message = "身份证号->不可为空")
    private String identifyNo;

    /**
     * 性别
     */
    @NotEmpty(message = "性别->不可为空")
    private Integer gender;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remark;

}
