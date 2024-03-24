package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易订单信息实体
 * @title: TradeOrderUpdateReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TradeOrderUpdateReq implements Serializable {

    private static final long serialVersionUID = -742589538569448373L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"tradeOrderId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String tradeOrderId;


    /**
     * 关联的物品ID
     */
     @NotEmpty(message = "关联的物品ID->不可为空")
     private String itemId;

    /**
     * APP用户ID
     */
     @NotEmpty(message = "APP用户ID->不可为空")
     private String authAppUserId;

    /**
     * 订单类型 系统自定义
     */
     @NotEmpty(message = "订单类型 系统自定义->不可为空")
     private String orderType;

    /**
     * 订单备注
     */
     @NotEmpty(message = "订单备注->不可为空")
     private String orderRemark;

    /**
     * 系统内部的支付订单号
     */
     @NotEmpty(message = "系统内部的支付订单号->不可为空")
     private String outTradeNo;

    /**
     * 订单价格
     */
     @NotEmpty(message = "订单价格->不可为空")
     private BigDecimal orderAmount;

    /**
     * 支付方式 aliPay/支付宝  weChatPay/微信 free/免费
     */
     @NotEmpty(message = "支付方式 aliPay/支付宝  weChatPay/微信 free/免费->不可为空")
     private String payType;

    /**
     * 订单简略信息(通过JSON格式存储)
     */
     @NotEmpty(message = "订单简略信息(通过JSON格式存储)->不可为空")
     private String extraData;

    /**
     * 订单状态
     */
     @NotEmpty(message = "订单状态->不可为空")
     private String orderStatus;

    /**
     * 收货地址ID
     */
    @NotEmpty(message = "收货地址ID-不可为空")
    private String takeAddressId;

}
