package cn.common.repository.repository.platform;

import cn.common.repository.entity.platform.AuthRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pro.skywalking.repository.BaseRepository;
import pro.skywalking.repository.result.AuthRoleCount;

import java.util.List;

/**
* @author Singer create by singer email:singer-coder@qq.com
* @packageName cn.singer.repository.entity
* @Description: 系统角色->Repository
* @date 2024-03-06
*/
@Mapper
public interface AuthRoleRepository extends BaseRepository<AuthRole> {

    /**
     * 统计每个角色有多少用户
     * @title: AuthRoleRepository.java
     * @author create by Singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06 11:05
     * @return List
     */
    @Select(
        "<script>" +
        " SELECT " +
        "  count( 1 ) AS countNum, " +
        "  userRole.auth_role_id AS auth_role_id  " +
        "FROM " +
        "  auth_user_role userRole " +
        "  LEFT JOIN auth_user authUser ON authUser.auth_user_id = userRole.auth_user_id  " +
        "GROUP BY " +
        "  userRole.auth_role_id;" +
        "</script>"
    )
    List<AuthRoleCount> countUserRole();

    /**
    * 根据业务主键批量删除
    * @title: AuthRoleRepository.java
    * @author create by Singer - Singer email:singer-coder@qq.com
    * @date 2024-03-06 11:05
    * @param mainIdList 业务主键ID集合
    * @return List
     */
    @Delete(
        "<script>" +
        " DELETE FROM auth_role WHERE 1=1 AND  " +
        " auth_role_id IN " +
        " <foreach collection='mainIdList' item= 'item' index= 'index' open='(' separator=',' close=')' >" +
        "          #{item} " +
        " </foreach>" +
        "</script>"
        )
    void batchDeleteItem(@Param("mainIdList") List<String> mainIdList);
}
