package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.RoomCheckOut;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 客房退房信息->Repository
 * @date 2024-02-29
 */
@Mapper
public interface RoomCheckOutRepository extends MPJBaseMapper<RoomCheckOut> {


    /**
     * 根据业务主键批量删除客房退房信息
     * @title: RoomCheckOutRepository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM room_check_out WHERE 1=1 AND  " +
        " room_check_out_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
