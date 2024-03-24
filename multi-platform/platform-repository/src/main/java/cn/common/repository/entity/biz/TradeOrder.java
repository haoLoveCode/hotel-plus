package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 交易订单信息实体
 * @title: TradeOrder.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("trade_order")
public class TradeOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 9107350332056240581L;

    /**
     * 业务主键ID
     */
    private String tradeOrderId;

    /**
     * 关联的物品ID
     */
    private String itemId;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 订单类型 系统自定义
     */
    private String orderType;

    /**
     * 订单备注
     */
    private String orderRemark;

    /**
     * 系统内部的支付订单号
     */
    private String outTradeNo;

    /**
     * 订单价格
     */
    private BigDecimal orderAmount;

    /**
     * 支付方式 aliPay/支付宝  weChatPay/微信 free/免费
     */
    private String payType;

    /**
     * 订单简略信息(通过JSON格式存储)
     */
    private String extraData;

    /**
     * 订单状态
     */
    private String orderStatus;

}
