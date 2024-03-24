package cn.common.req.biz.app;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 下单单个商品
 * @title: SalesItemAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class ItemOrderingOneReq implements Serializable {

    private static final long serialVersionUID = -605735387520517932L;

    /**
     * 商品ID
     */
    @NotEmpty(message = "商品ID->不可为空")
    private String itemId;

    /**
     * 收货地址ID
     */
    @NotEmpty(message = "收货地址ID-不可为空")
    private String takeAddressId;

}
