package cn.common.repository.repository.platform;

import cn.common.repository.entity.platform.PlatformApiLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pro.skywalking.repository.BaseRepository;

import java.util.List;

/**
 * @author Singer create by singer email:singer-coder@qq.com
 * @packageName cn.singer.repository.entity
 * @Description: 系统日志->Repository
 * @date 2024-03-06
 */
@Mapper
public interface PlatformApiLogRepository extends BaseRepository<PlatformApiLog> {


    /**
     * 清空日志信息表
     *
     * @return List
     * @title: AuthRoleRepository.java
     * @author create by Singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06 11:05
     */
    @Delete(
            "<script>" +
            " truncate platform_api_log " +
            "</script>"
    )
    void clearApiLog();

    /**
     * 根据业务主键批量删除
     *
     * @param mainIdList 业务主键ID集合
     * @return List
     * @title: AuthRoleRepository.java
     * @author create by Singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06 11:05
     */
    @Delete(
            "<script>" +
            " DELETE FROM platform_api_log WHERE 1=1 AND  " +
            " platform_api_log_id IN " +
            " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
            "          #{item} " +
            " </foreach>" +
            "</script>"
    )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);

}
