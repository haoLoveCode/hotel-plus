package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品信息实体
 * @title: SalesItem.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("sales_item")
public class SalesItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 189528233191588874L;

    /**
     * 业务主键ID
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

}
