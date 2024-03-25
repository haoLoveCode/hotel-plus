package cn.common.req.biz.openBiz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品信息分页查询请求封装类
 * @title: SalesItemReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class SalesItemReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 448143466632579602L;

    /**
     * 业务主键ID->"salesItemId"
     */
    private String salesItemId;

    /**
     * 商品类型ID
     */
    private String typeItemId;

    /**
     * 发布人ID
     */
    private String publisherId;

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

    /**
     * 商品状态
     */
    private Integer itemStatus;

    /**
     * 备注
     */
    private String remarkData;

}
