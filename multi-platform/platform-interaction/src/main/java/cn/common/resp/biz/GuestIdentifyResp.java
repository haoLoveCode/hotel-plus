package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 客户身份信息返回数据封装类
 * @title: GuestIdentifyResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class GuestIdentifyResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = -950070530454801425L;

    /**
     * 业务主键ID->"guestIdentifyId"
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
