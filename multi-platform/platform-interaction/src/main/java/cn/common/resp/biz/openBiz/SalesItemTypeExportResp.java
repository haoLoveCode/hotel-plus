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
 * 商品类型信息数据导出请求封装类
 * @title: SalesItemTypeExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SalesItemTypeExportResp implements Serializable {

    private static final long serialVersionUID = 126740038357973994L;

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称->不可为空")
    @ExcelProperty(value = "类型名称")
    private String typeName;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    @ExcelProperty(value = "备注信息")
    private String remarkData;

    /**
     * 类型状态
     */
    @NotEmpty(message = "类型状态->不可为空")
    @ExcelProperty(value = "类型状态")
    private Integer dataStatus;

}
