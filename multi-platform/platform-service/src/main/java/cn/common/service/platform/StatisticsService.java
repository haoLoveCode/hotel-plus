package cn.common.service.platform;

import pro.skywalking.resp.platform.other.AuthRoleCountResp;

import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 统计相关服务
 * @date 2024-03-06
 */
public interface StatisticsService {

    /**
     * 查询系统角色对应的用户数量
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    List<AuthRoleCountResp> queryAuthRoleCount();

}
