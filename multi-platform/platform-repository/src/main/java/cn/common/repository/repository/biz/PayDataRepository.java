package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.PayData;
import org.apache.ibatis.annotations.Mapper;
import pro.skywalking.repository.BaseRepository;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 支付数据信息->Repository
 * @date 2024-03-25
 */
@Mapper
public interface PayDataRepository extends BaseRepository<PayData> {


    /**
     * 根据业务主键批量删除
     * @title: PayDataRepository.java
     * @author Singer
     * @date 2024-03-25 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM pay_data WHERE 1=1 AND  " +
        " pay_data_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
