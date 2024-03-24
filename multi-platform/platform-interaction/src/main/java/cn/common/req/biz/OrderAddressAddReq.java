package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 订单-收货地址关联信息新增Req
 * @title: OrderAddressAddReq.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class OrderAddressAddReq implements Serializable {

    private static final long serialVersionUID = -419606848385891740L;

    /**
     * 关联的订单ID
     */
    @NotEmpty(message = "关联的订单ID->不可为空")
    private String orderId;

    /**
     * APP用户ID
     */
    @NotEmpty(message = "APP用户ID->不可为空")
    private String authAppUserId;

    /**
     * 收货地址ID
     */
    @NotEmpty(message = "收货地址ID->不可为空")
    private String takeAddressId;

}
