package cn.common.req.platform.wxPay;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提交转-账明细信息Req
 * @title: TransferItemAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SubmitTransferItemReq implements Serializable {

    private static final long serialVersionUID = 3745762130617435430L;

    /**
     * 业务主键ID
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String transferItemId;

    /**
     * 转账业务ID
     */
    @NotEmpty(message = "转账业务ID->不可为空")
    private String transferDataId;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID->不可为空")
    private String authAppUserId;

    /**
     * 外部平台的用户ID
     */
    @NotEmpty(message = "外部平台的用户ID->不可为空")
    private String outerUserId;

    /**
     * 关联的业务ID
     */
    @NotEmpty(message = "关联的业务ID->不可为空")
    private String bizId;

    /**
     * 转账金额
     */
    @NotEmpty(message = "转账金额->不可为空")
    private BigDecimal transferAmount;

    /**
     * 明细单号
     */
    @NotEmpty(message = "明细单号->不可为空")
    private String transferItemNo;

    /**
     * 备注
     */
    @NotEmpty(message = "备注->不可为空")
    private String remarkData;

    /**
     * 审核状态
     */
    @NotEmpty(message = "审核状态->不可为空")
    private Integer auditStatus;

}
