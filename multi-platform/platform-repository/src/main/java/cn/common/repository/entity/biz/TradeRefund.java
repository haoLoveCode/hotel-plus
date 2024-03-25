package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 退款交易信息实体
 * @title: TradeRefund.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("trade_refund")
public class TradeRefund extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键ID
     */
    private String tradeRefundId;

    /**
     * 交易订单ID
     */
    private String tradeOrderId;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 订单备注
     */
    private String refundRemark;

    /**
     * 系统内部的支付订单号
     */
    private String outTradeNo;

    /**
     * 系统内部的退款订单号
     */
    private String outRefundNo;

    /**
     * 订单价格
     */
    private BigDecimal refundAmount;

    /**
     * 订单额外数据
     */
    private String extraData;

    /**
     * 订单状态
     */
    private Integer refundStatus;

}
