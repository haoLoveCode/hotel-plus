package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.SalesItem;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 商品信息->Repository
 * @date 2024-03-06
 */
@Mapper
public interface SalesItemRepository extends MPJBaseMapper<SalesItem> {


    /**
     * 根据业务主键批量删除商品信息
     * @title: SalesItemRepository.java
     * @author Singer
     * @date 2024-03-06 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM sales_item WHERE 1=1 AND  " +
        " sales_item_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
