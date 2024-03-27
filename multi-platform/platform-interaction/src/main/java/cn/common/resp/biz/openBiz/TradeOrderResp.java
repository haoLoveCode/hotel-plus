package cn.common.resp.biz.openBiz;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
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
 * @date 2024-03-25 11:13
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

    //-------------房间信息---start--------------

    /**
     * 房间ID
     */
    private String roomDataId;

    /**
     * 房间类型ID
     */
    private String roomTypeId;

    /**
     * 房间展示标题
     */
    private String roomTitle;

    /**
     * 房间简介
     */
    private String briefData;

    /**
     * 房间编号
     */
    private String roomNo;

    /**
     * 房间图片
     */
    private String mainImg;

    /**
     * 房间状态
     */
    private Integer roomStatus;

    /**
     * 房间楼层
     */
    private Integer roomFloor;

    /**
     * 价格
     */
    private BigDecimal unitPrice;

    /**
     * 房间面积
     */
    private BigDecimal roomArea;

    /**
     * 床位数量
     */
    private Integer bedNum;


    //-------------房间信息---end--------------

    /**
     * 业务主键ID
     */
    private String roomBookingId;

    /**
     * 预定人
     */
    private String subscriberId;

    /**
     * 预订单号
     */
    private String bookingNo;

    /**
     * 备注信息
     */
    private String remark;

    //-------------房间预定信息---start--------------


    /**
     * 预订时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    private Integer bookingStatus;

    //-------------房间预定信息---end--------------

}
