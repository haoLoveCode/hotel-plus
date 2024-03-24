package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.RoomBooking;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 房间预订信息->Repository
 * @date 2024-02-29
 */
@Mapper
public interface RoomBookingRepository extends MPJBaseMapper<RoomBooking> {


    /**
     * 根据业务主键批量删除房间预订信息
     * @title: RoomBookingRepository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM room_booking WHERE 1=1 AND  " +
        " room_booking_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
