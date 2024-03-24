package ${basePackageName}.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * ${entityDesc}分页查询请求封装类
 * @title: ${className}Req.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
public class ${className}Req extends BasePageReq implements Serializable {

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
