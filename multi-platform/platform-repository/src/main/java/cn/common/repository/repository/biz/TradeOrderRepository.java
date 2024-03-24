package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.TradeOrder;
import org.apache.ibatis.annotations.Mapper;
import pro.skywalking.repository.BaseRepository;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 交易订单信息->Repository
 * @date 2024-03-06
 */
@Mapper
public interface TradeOrderRepository extends BaseRepository<TradeOrder> {


    /**
     * 根据业务主键批量删除
     * @title: TradeOrderRepository.java
     * @author Singer
     * @date 2024-03-06 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM trade_order WHERE 1=1 AND  " +
        " trade_order_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
