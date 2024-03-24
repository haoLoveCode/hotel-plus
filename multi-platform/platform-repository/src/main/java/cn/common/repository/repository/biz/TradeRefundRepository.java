package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.TradeRefund;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 退款交易信息->Repository
 * @date 2024-03-06
 */
@Mapper
public interface TradeRefundRepository extends MPJBaseMapper<TradeRefund> {


    /**
     * 根据业务主键批量删除退款交易信息
     * @title: TradeRefundRepository.java
     * @author Singer
     * @date 2024-03-06 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM trade_refund WHERE 1=1 AND  " +
        " trade_refund_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
