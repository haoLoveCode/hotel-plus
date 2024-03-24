package cn.common.req.platform.wxPay;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提交转账信息
 * @title: TransferDataAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SubmitTransferReq implements Serializable {

    private static final long serialVersionUID = 7253858092402914032L;

    /**
     * 转账业务ID
     */
    @NotEmpty(message = "转账业务ID->不可为空")
    private String transferDataId;

    /**
     * 转账总金额
     */
    @NotEmpty(message = "转账总金额->不可为空")
    private BigDecimal totalAmount;

    /**
     * 转账单号
     */
    @NotEmpty(message = "转账单号->不可为空")
    private String outBatchNo;

    /**
     * 批量转账名称
     */
    @NotEmpty(message = "批量转账名称->不可为空")
    private String batchName;

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

    /**
     * 转账状态
     */
    @NotEmpty(message = "转账状态->不可为空")
    private Integer transferStatus;

    /**
     * 明细信息
     */
    @NotEmpty(message = "明细信息->不可为空")
    List<SubmitTransferItemReq> itemList;

}
