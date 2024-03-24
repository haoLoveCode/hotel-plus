package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;

/**
 * 订单信息新增Req
 * @title: OrderDataAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class OrderDataAddReq implements Serializable {

    private static final long serialVersionUID = 898258451314221787L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"orderDataId"
     */
    private String orderDataId;

    /**
     * 订单交易订单ID
     */
    @NotEmpty(message = "订单交易订单ID->不可为空")
    private String tradeOrderId;

    /**
     * 订单的支付订单号
     */
    @NotEmpty(message = "订单的支付订单号->不可为空")
    private String outTradeNo;

    /**
     * APP用户ID
     */
    @NotEmpty(message = "APP用户ID->不可为空")
    private String authAppUserId;

    /**
     * 商品ID
     */
    @NotEmpty(message = "商品ID->不可为空")
    private String itemId;

    /**
     * 商品快照数据
     */
    @NotEmpty(message = "商品快照数据->不可为空")
    private String itemData;

    /**
     * 购买数量
     */
    @NotEmpty(message = "购买数量->不可为空")
    private Integer itemNum;

    /**
     * 订单备注
     */
    @NotEmpty(message = "订单备注->不可为空")
    private String orderRemark;

}
