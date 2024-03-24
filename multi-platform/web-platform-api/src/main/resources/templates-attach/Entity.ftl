package ${basePackageName}.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * ${entityDesc}实体
 * @title: ${className}.java
 * @author Singer
 * @date 2024-03-04 11:13
 */
@Data
@TableName("${originalTableName}")
public class ${className} extends BaseEntity implements Serializable {

    private static final long serialVersionUID = ${serialVersionUID};

    /**
     * 业务主键ID
     */
    private String ${mainId};
    <#list columnList as column>

    /**
     * ${column.columnNameDesc}
     */
    private ${column.dataType} ${column.camelCaseName};
    </#list>

}
