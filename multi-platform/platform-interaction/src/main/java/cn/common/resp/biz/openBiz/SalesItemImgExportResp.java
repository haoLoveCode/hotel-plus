package cn.common.resp.biz.openBiz;

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
 * 商品图片信息数据导出请求封装类
 * @title: SalesItemImgExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SalesItemImgExportResp implements Serializable {

    private static final long serialVersionUID = 996462349070831128L;

    /**
     * 商品信息ID
     */
    @NotEmpty(message = "商品信息ID->不可为空")
    @ExcelProperty(value = "商品信息ID")
    private String salesItemId;

    /**
     * 淡纹地址
     */
    @NotEmpty(message = "淡纹地址->不可为空")
    @ExcelProperty(value = "淡纹地址")
    private String imgUrl;

}
