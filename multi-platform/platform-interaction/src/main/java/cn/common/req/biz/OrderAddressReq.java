package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 订单-收货地址关联信息分页查询请求封装类
 * @title: OrderAddressReq.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class OrderAddressReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 768663746278523416L;

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
