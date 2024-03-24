package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.platform.other.AuthRoleCountResp;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.api
 * @Description: 系统统计相关前端控制器
 * @date 2024-03-04
 */
@RestController
@RequestMapping(value = "api/v1/statistics")
@Slf4j
public class StatisticsController extends BaseApiController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 查询系统角色对应的用户数量
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryAuthRoleCount")
    @ApiLog(value = "查询系统角色对应的用户数量")
    public ApiResponse<List<AuthRoleCountResp>> queryAuthRoleCount(){
        return apiResponse(statisticsService.queryAuthRoleCount());
    }
}
