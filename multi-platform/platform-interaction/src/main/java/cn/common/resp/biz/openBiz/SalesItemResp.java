package cn.common.resp.biz.openBiz;

import lombok.Data;
import pro.skywalking.req.platform.base.BaseResp;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息返回数据封装类
 * @title: SalesItemResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 909862197574122922L;

    /**
     * 业务主键ID->"salesItemId"
     */
    private String salesItemId;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 商品类型ID
     */
    private String typeItemId;

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

    /**
     * 图片信息
     */
    private List<String> imgList;

}
