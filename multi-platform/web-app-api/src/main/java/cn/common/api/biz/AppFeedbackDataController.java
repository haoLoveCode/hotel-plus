package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.FeedbackDataAddReq;
import cn.common.req.biz.FeedbackDataReq;
import cn.common.req.biz.FeedbackDataUpdateReq;
import cn.common.resp.biz.FeedbackDataResp;
import cn.common.service.biz.app.AppFeedbackDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 投诉建议信息前端控制器
 * @date 2024/1/25
 */
@RestController
@RequestMapping(value = "api/v1/feedbackData")
@Slf4j
public class AppFeedbackDataController extends BaseApiController {

    @Resource
    private AppFeedbackDataService appFeedbackDataService;

    /**
     * 新增投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增投诉建议信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增投诉建议信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid FeedbackDataAddReq addReq){
        appFeedbackDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的投诉建议信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除投诉建议信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        appFeedbackDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新投诉建议信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新投诉建议信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid FeedbackDataUpdateReq updateReq){
        appFeedbackDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryFeedbackData")
    @ApiLog(value = "查询投诉建议信息")
    @NeedLogin(value = false)
    public ApiResponse<List<FeedbackDataResp>> queryFeedbackData(
        @RequestBody @Valid FeedbackDataReq req){
        return apiResponse(appFeedbackDataService.queryFeedbackData(req));
    }

    /**
     * 查询单个投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneFeedbackData")
    @ApiLog(value = "查询投诉建议信息")
    @NeedLogin(value = false)
    public ApiResponse<FeedbackDataResp> queryOneFeedbackData(
        @RequestBody @Valid FeedbackDataReq req){
        return apiResponse(appFeedbackDataService.queryOneFeedbackData(req));
    }

    /**
     * 分页查询投诉建议信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询投诉建议信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询投诉建议信息")
    @NeedLogin(value = false)
    public ApiResponse<Pagination<FeedbackDataResp>> queryByPage(
        @RequestBody @Valid FeedbackDataReq pageReq){
        return apiResponse(appFeedbackDataService.queryByPage(pageReq));
    }
}
