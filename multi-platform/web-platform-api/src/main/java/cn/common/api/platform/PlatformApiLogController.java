package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.PlatformApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.ApiPerm;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.log.PlatformApiLogReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.log.ApiLogStatisticsResp;
import pro.skywalking.resp.platform.log.PlatformApiLogResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.api.controller
 * @Description: 前端控制器
 * @date 2024-03-25
 */
@RestController
@RequestMapping(value = "api/v1/platformApiLog")
@Slf4j
public class PlatformApiLogController extends BaseApiController {

    @Resource
    private PlatformApiLogService platformApiLogService;


    /**
     * 主键ID集合批量系统日志
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 需要被删除的系统日志信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除系统日志")
    @ApiPerm(accessStatus = true)
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        platformApiLogService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 分页查询系统日志
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  pageReq 分页查询系统日志Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询系统日志信息")
    public ApiResponse<Pagination<PlatformApiLogResp>> queryByPage(
            @RequestBody @Valid PlatformApiLogReq pageReq){
        return apiResponse(platformApiLogService.queryByPage(pageReq));
    }

    /**
     * 根据参数统计访问量
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 查询参数
     * @return
     */
    @PostMapping("/statisticsVisitCount")
    @ApiLog(value = "分页统计查询系统日志")
    public ApiResponse<List<ApiLogStatisticsResp>> statisticsVisitCount(@RequestBody @Valid PlatformApiLogReq req){
        return apiResponse(platformApiLogService.statisticsVisitCount(req));
    }
}
