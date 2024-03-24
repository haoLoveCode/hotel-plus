package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单-收货地址关联信息请求数据封装类
 * @title: OrderAddressUpdateReq.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class OrderAddressUpdateReq implements Serializable {

    private static final long serialVersionUID = -178993747743446873L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"orderAddressId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String orderAddressId;

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
