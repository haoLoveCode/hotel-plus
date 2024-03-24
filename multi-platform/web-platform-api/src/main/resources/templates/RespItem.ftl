package ${basePackageName}.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * ${entityDesc}返回数据封装类
 * @title: ${className}Resp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class ${className}Resp extends BaseResp implements Serializable {

    private static final long serialVersionUID = ${serialVersionUID};

    /**
     * 业务主键ID->"${mainId}"
     */
    private String ${mainId};
    <#list columnList as column>

    /**
     * ${column.columnNameDesc}
     */
    private ${column.dataType} ${column.camelCaseName};
    </#list>

}
