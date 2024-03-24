package cn.common.resp.biz.platform;

import pro.skywalking.validation.NotEmpty;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 商品信息数据导出请求封装类
 * @title: SalesItemExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SalesItemExportResp implements Serializable {

    private static final long serialVersionUID = -497615862280049585L;

    /**
     * 商品类型ID
     */
    @NotEmpty(message = "商品类型ID->不可为空")
    @ExcelProperty(value = "商品类型ID")
    private String typeItemId;

    /**
     * 发布人ID
     */
    @NotEmpty(message = "发布人ID->不可为空")
    @ExcelProperty(value = "发布人ID")
    private String publisherId;

    /**
     * 商品名称
     */
    @NotEmpty(message = "商品名称->不可为空")
    @ExcelProperty(value = "商品名称")
    private String itemName;

    /**
     * 商品简介
     */
    @NotEmpty(message = "商品简介->不可为空")
    @ExcelProperty(value = "商品简介")
    private String itemSummary;

    /**
     * 商品标题
     */
    @NotEmpty(message = "商品标题->不可为空")
    @ExcelProperty(value = "商品标题")
    private String itemTitle;

    /**
     * 销售价格
     */
    @NotEmpty(message = "销售价格->不可为空")
    @ExcelProperty(value = "销售价格")
    private BigDecimal salePrice;

    /**
     * 原价
     */
    @NotEmpty(message = "原价->不可为空")
    @ExcelProperty(value = "原价")
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    @NotEmpty(message = "商品图片->不可为空")
    @ExcelProperty(value = "商品图片")
    private String mainImg;

    /**
     * 商品状态
     */
    @NotEmpty(message = "商品状态->不可为空")
    @ExcelProperty(value = "商品状态")
    private Integer itemStatus;

    /**
     * 备注
     */
    @NotEmpty(message = "备注->不可为空")
    @ExcelProperty(value = "备注")
    private String remarkData;

}
