package cn.common.req.biz.app;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 根据购物车商品信息下单
 * @title: SalesItemReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class OrderingByBasketItemReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -1752381897379208781L;

    /**
     * 购物车商品ID集合
     */
    private List<String> basketDataIdList;

    /**
     * 收货地址ID
     */
    @NotEmpty(message = "收货地址ID-不可为空")
    private String takeAddressId;

}
