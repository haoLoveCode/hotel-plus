package cn.common.resp.biz;

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
 * 购物车信息数据导出请求封装类
 * @title: BasketDataExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class BasketDataExportResp implements Serializable {

    private static final long serialVersionUID = -333878932251561300L;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID->不可为空")
    @ExcelProperty(value = "用户ID")
    private String authAppUserId;

    /**
     * 商品ID
     */
    @NotEmpty(message = "商品ID->不可为空")
    @ExcelProperty(value = "商品ID")
    private String salesItemId;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    @ExcelProperty(value = "备注信息")
    private String remarkData;

    /**
     * 购买数量
     */
    @NotEmpty(message = "购买数量->不可为空")
    @ExcelProperty(value = "购买数量")
    private Integer itemNum;

}
