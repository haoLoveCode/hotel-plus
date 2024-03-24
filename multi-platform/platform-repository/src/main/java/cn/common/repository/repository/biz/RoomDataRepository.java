package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.RoomData;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 房间信息->Repository
 * @date 2024-02-29
 */
@Mapper
public interface RoomDataRepository extends MPJBaseMapper<RoomData> {

    /**
     * 根据业务主键批量删除房间信息
     * @title: RoomDataRepository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM room_data WHERE 1=1 AND  " +
        " room_data_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
