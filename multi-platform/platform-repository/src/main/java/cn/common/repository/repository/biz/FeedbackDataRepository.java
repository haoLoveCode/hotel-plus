package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.FeedbackData;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 投诉建议信息->Repository
 * @date 2024-02-29
 */
@Mapper
public interface FeedbackDataRepository extends MPJBaseMapper<FeedbackData> {


    /**
     * 根据业务主键批量删除投诉建议信息
     * @title: FeedbackDataRepository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM feedback_data WHERE 1=1 AND  " +
        " feedback_data_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
