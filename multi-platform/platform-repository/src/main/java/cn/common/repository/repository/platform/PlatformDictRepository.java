package cn.common.repository.repository.platform;

import cn.common.repository.entity.platform.PlatformDict;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pro.skywalking.repository.BaseRepository;

import java.util.List;

/**
* @author Singer create by singer email:singer-coder@qq.com
* @packageName cn.singer.repository.entity
* @Description: 字典信息->Repository
* @date 2024-03-25
*/
@Mapper
public interface PlatformDictRepository extends BaseRepository<PlatformDict> {



        /**
         * 根据业务主键批量删除
         * @title: PlatformDictRepository.java
         * @author create by Singer - Singer email:singer-coder@qq.com
         * @date 2024-03-25 11:05
         * @param mainIdList 业务主键ID集合
         * @return List
         */
        @Delete(
                " DELETE FROM platform_dict WHERE 1=1 AND  " +
                " platform_dict_id IN " +
                " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
                "          #{item} " +
                " </foreach>"
        )
        void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);

}
