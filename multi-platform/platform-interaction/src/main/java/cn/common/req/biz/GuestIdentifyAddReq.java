package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 客户身份信息新增Req
 * @title: GuestIdentifyAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class GuestIdentifyAddReq implements Serializable {

    private static final long serialVersionUID = -944428724117120552L;

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
