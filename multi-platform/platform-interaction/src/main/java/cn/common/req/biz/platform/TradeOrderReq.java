package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易订单信息分页查询请求封装类
 * @title: TradeOrderReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TradeOrderReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 4640970463891955886L;

    /**
     * 业务主键ID->"tradeOrderId"
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

    /**
     * 是否查询我卖出的
     */
    private Boolean queryMineSales = Boolean.FALSE;

}
