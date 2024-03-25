package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易订单信息实体
 * @title: TradeOrderUpdateReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class SetTradeOrderReq implements Serializable {

    private static final long serialVersionUID = -7079373272850435536L;

    /**
     * 业务主键ID->"tradeOrderId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String tradeOrderId;

    /**
     * 订单状态
     */
     @NotEmpty(message = "订单状态->不可为空")
     private String orderStatus;

}
