package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 购物车信息新增Req
 * @title: BasketDataAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class BasketDataAddReq implements Serializable {

    private static final long serialVersionUID = -702796447727603591L;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID->不可为空")
    private String authAppUserId;

    /**
     * 商品ID
     */
    @NotEmpty(message = "商品ID->不可为空")
    private String salesItemId;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remarkData;

    /**
     * 购买数量
     */
    @NotEmpty(message = "购买数量->不可为空")
    private Integer itemNum;

    /**
     * 动作类型 1:增加 2:减少
     */
    @NotEmpty(message = "动作类型 1:增加 2:减少->不可为空")
    private Integer bizType;

}
