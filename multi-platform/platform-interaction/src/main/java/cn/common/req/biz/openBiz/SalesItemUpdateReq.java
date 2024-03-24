package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品信息请求数据封装类
 * @title: SalesItemUpdateReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class SalesItemUpdateReq implements Serializable {

    private static final long serialVersionUID = -115091722842922884L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"salesItemId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String salesItemId;

    /**
     * 发布人ID
     */
    @NotEmpty(message = "发布人ID->不可为空")
    private String publisherId;

    /**
     * 商品类型ID
     */
    @NotEmpty(message = "商品类型ID->不可为空")
    private String typeItemId;

    /**
     * 商品名称
     */
    @NotEmpty(message = "商品名称->不可为空")
    private String itemName;

    /**
     * 商品简介
     */
    @NotEmpty(message = "商品简介->不可为空")
    private String itemSummary;

    /**
     * 商品标题
     */
    @NotEmpty(message = "商品标题->不可为空")
    private String itemTitle;

    /**
     * 销售价格
     */
    @NotEmpty(message = "销售价格->不可为空")
    private BigDecimal salePrice;

    /**
     * 原价
     */
    @NotEmpty(message = "原价->不可为空")
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    @NotEmpty(message = "商品图片->不可为空")
    private String mainImg;

    /**
     * 商品状态
     */
    @NotEmpty(message = "商品状态->不可为空")
    private Integer itemStatus;

    /**
     * 备注
     */
    @NotEmpty(message = "备注->不可为空")
    private String remarkData;

    /**
     * 图片信息
     */
    @NotEmpty(message = "图片信息->不可为空")
    private List<String> imgList;

}
