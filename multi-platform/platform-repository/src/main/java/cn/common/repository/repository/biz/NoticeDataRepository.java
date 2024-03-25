package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.NoticeData;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 公告信息->Repository
 * @date 2024-03-25
 */
@Mapper
public interface NoticeDataRepository extends MPJBaseMapper<NoticeData> {


    /**
     * 根据业务主键批量删除公告信息
     * @title: NoticeDataRepository.java
     * @author Singer
     * @date 2024-03-25 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM notice_data WHERE 1=1 AND  " +
        " notice_data_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
