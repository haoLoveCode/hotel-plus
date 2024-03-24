package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.OrderAddress;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 订单-收货地址关联信息->Repository
 * @date 2024-03-04
 */
@Mapper
public interface OrderAddressRepository extends MPJBaseMapper<OrderAddress> {


    /**
     * 根据业务主键批量删除订单-收货地址关联信息
     * @title: OrderAddressRepository.java
     * @author Singer
     * @date 2024-03-04 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM order_address WHERE 1=1 AND  " +
        " order_address_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
