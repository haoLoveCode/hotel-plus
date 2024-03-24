package ${basePackageName}.req.biz;

import pro.skywalking.validation.NotEmpty;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * ${entityDesc}数据导入请求封装类
 * @title: ${className}ImportReq.java
 * @author Singer
 * @date
 */
@Data
public class ${className}ImportReq implements Serializable {

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
