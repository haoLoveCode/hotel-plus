package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.AppUserData;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: APP用户其他信息->Repository
 * @date 2024-03-25
 */
@Mapper
public interface AppUserDataRepository extends MPJBaseMapper<AppUserData> {


    /**
     * 根据业务主键批量删除APP用户其他信息
     * @title: AppUserDataRepository.java
     * @author Singer
     * @date 2024-03-25 11:05
     * @param mainIdList 业务主键ID集合
     * @return List
     */
    @Delete(
        "<script>" +
        " DELETE FROM app_user_data WHERE 1=1 AND  " +
        " app_user_data_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);

}
