package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 订单-收货地址关联信息返回数据封装类
 * @title: OrderAddressResp.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class OrderAddressResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 784606212793869600L;

    /**
     * 业务主键ID->"orderAddressId"
     */
    private String orderAddressId;

    /**
     * 关联的订单ID
     */
    private String orderId;

    /**
     * APP用户ID
     */
    private String authAppUserId;

    /**
     * 收货地址ID
     */
    private String takeAddressId;

}
