package cn.common.repository.repository.biz;

import cn.common.repository.entity.biz.MainSwiper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pro.skywalking.repository.BaseRepository;

import java.util.List;

/**
 * @author Singer
 * @packageName cn.singer.repository.entity
 * @Description: 首页轮播图->Repository
 * @date 2024-03-25
 */
@Mapper
public interface MainSwiperRepository extends BaseRepository<MainSwiper> {

    /**
     * 根据业务主键批量删除
     * @title: MainSwiperRepository.java
     * @author Singer
     * @date 2024-03-25 11:05
     * @param mainIdList 业务主键ID集合
     * @return List
     */
    @Delete(
        "<script>" +
        " DELETE FROM main_swiper WHERE 1=1 AND  " +
        " main_swiper_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "   #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);

}
