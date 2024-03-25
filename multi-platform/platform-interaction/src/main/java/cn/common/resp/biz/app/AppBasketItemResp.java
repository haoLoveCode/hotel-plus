package cn.common.resp.biz.app;

import lombok.Data;
import pro.skywalking.req.platform.base.BaseResp;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车和商品信息返回数据封装类
 * @title: AppBasketItemResp.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class AppBasketItemResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 8143310076276729133L;

    /**
     * 业务主键ID->"basketDataId"
     */
    private String basketDataId;

    /**
     * 用户ID
     */
    private String authAppUserId;

    /**
     * 商品ID
     */
    private String salesItemId;

    /**
     * 备注信息
     */
    private String remarkData;

    /**
     * 购买数量
     */
    private Integer itemNum;

    //-------------商品信息-start-----------------------

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

    //-------------商品信息-end-----------------------

}
