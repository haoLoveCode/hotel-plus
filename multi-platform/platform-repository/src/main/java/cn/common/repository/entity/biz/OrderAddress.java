package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 订单-收货地址关联信息实体
 * @title: OrderAddress.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
@TableName("order_address")
public class OrderAddress extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -799206623104137274L;

    /**
     * 业务主键ID
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
