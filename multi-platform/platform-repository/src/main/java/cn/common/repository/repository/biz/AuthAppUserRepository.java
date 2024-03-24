package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.AuthAppUser;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pro.skywalking.repository.BaseRepository;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: APP认证用户->Repository
 * @date 2024-03-06
 */
@Mapper
public interface AuthAppUserRepository extends MPJBaseMapper<AuthAppUser> {


    /**
     * 根据业务主键批量删除
     * @title: AuthAppUserRepository.java
     * @author Singer
     * @date 2024-03-06 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM auth_app_user WHERE 1=1 AND  " +
        " auth_app_user_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
