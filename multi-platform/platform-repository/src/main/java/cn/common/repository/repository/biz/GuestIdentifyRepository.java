package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.GuestIdentify;
import org.apache.ibatis.annotations.Mapper;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 客户身份信息->Repository
 * @date 2024-02-29
 */
@Mapper
public interface GuestIdentifyRepository extends MPJBaseMapper<GuestIdentify> {


    /**
     * 根据业务主键批量删除客户身份信息
     * @title: GuestIdentifyRepository.java
     * @author Singer
     * @date 2024-02-29 11:05
     * @param mainIdList 业务主键ID集合
     * @return List

    @Delete(
        "<script>" +
        " DELETE FROM guest_identify WHERE 1=1 AND  " +
        " guest_identify_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);*/

}
