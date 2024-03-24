package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.TakeAddress;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 收货地址信息->Repository
 * @date 2024-03-06
 */
@Mapper
public interface TakeAddressRepository extends MPJBaseMapper<TakeAddress> {


    /**
     * 根据业务主键批量删除收货地址信息
     * @title: TakeAddressRepository.java
     * @author Singer
     * @date 2024-03-06 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM take_address WHERE 1=1 AND  " +
        " take_address_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
