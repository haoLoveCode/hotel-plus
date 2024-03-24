package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 客户身份信息分页查询请求封装类
 * @title: GuestIdentifyReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class GuestIdentifyReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -524445192289730494L;

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
