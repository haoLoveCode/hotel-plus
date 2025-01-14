package cn.common.req.biz.platform;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;

import java.io.Serializable;

/**
 * 订单信息分页查询请求封装类
 * @title: OrderDataReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class OrderDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 1009694266808915986L;

    /**
     * 业务主键ID->"orderDataId"
     */
    private String orderDataId;

    /**
     * 订单交易订单ID
     */
    private String tradeOrderId;

    /**
     * 订单的支付订单号
     */
    private String outTradeNo;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 商品快照数据
     */
    private String itemData;

    /**
     * 购买数量
     */
    private Integer itemNum;

    /**
     * 订单备注
     */
    private String orderRemark;

}
