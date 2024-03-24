package cn.common.resp.biz.openBiz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 交易订单信息返回数据封装类
 * @title: TradeOrderResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TradeOrderResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 1876447753952191721L;

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
     * 收货地址ID
     */
    private String takeAddressId;

    //-------------商品信息---start--------------


    /**
     * 业务主键ID
     */
    private String salesItemId;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 商品类型ID
     */
    private String typeItemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品简介
     */
    private String itemSummary;

    /**
     * 商品标题
     */
    private String itemTitle;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    private String mainImg;

    /**
     * 商品状态
     */
    private Integer itemStatus;

    /**
     * 备注
     */
    private String remarkData;


    //-------------商品信息---end--------------

}
