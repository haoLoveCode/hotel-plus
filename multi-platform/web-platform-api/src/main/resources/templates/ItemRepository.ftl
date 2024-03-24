package ${basePackageName}.repository.repository.biz;

import ${basePackageName}.repository.entity.biz.${className};
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: ${entityDesc}->Repository
 * @date 2024-02-29
 */
@Mapper
public interface ${className}Repository extends MPJBaseMapper<${className}> {


    /**
     * 根据业务主键批量删除${entityDesc}
     * @title: ${className}Repository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM ${tableName} WHERE 1=1 AND  " +
        " ${tableName}_id IN " +
        " <#noparse><foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' ></#noparse>" +
        "   <#noparse>#{item}</#noparse> " +
        " <#noparse></foreach></#noparse>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
