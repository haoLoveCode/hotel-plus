package ${basePackageName}.resp.biz;

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
 * ${entityDesc}数据导出请求封装类
 * @title: ${className}ExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class ${className}ExportResp implements Serializable {

    private static final long serialVersionUID = ${serialVersionUID};
    <#list columnList as column>

    /**
     * ${column.columnNameDesc}
     */
    @NotEmpty(message = "${column.columnNameDesc}->不可为空")
    @ExcelProperty(value = "${column.columnNameDesc}")
    private ${column.dataType} ${column.camelCaseName};
    </#list>

}
