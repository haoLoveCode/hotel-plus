package ${basePackageName}.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ${entityDesc}请求数据封装类
 * @title: ${className}UpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class ${className}UpdateReq implements Serializable {

    private static final long serialVersionUID = ${serialVersionUID};

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"${mainId}"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String ${mainId};
    <#list columnList as column>

    /**
     * ${column.columnNameDesc}
     */
    @NotEmpty(message = "${column.columnNameDesc}->不可为空")
    private ${column.dataType} ${column.camelCaseName};
    </#list>

}
