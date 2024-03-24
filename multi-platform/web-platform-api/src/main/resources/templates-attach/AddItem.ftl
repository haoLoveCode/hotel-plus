package ${basePackageName}.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * ${entityDesc}新增Req
 * @title: ${className}AddReq.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class ${className}AddReq implements Serializable {

    private static final long serialVersionUID = ${serialVersionUID};
    <#list columnList as column>

    /**
     * ${column.columnNameDesc}
     */
    @NotEmpty(message = "${column.columnNameDesc}->不可为空")
    private ${column.dataType} ${column.camelCaseName};
    </#list>

}
