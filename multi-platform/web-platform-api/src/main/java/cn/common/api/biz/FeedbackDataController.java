package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.FeedbackDataAddReq;
import cn.common.req.biz.FeedbackDataReq;
import cn.common.req.biz.FeedbackDataUpdateReq;
import cn.common.resp.biz.FeedbackDataResp;
import cn.common.service.biz.platform.FeedbackDataService;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 投诉建议信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/feedbackData")
@Slf4j
public class FeedbackDataController extends BaseApiController {

    @Resource
    private FeedbackDataService feedbackDataService;

    /**
     * 新增投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增投诉建议信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增投诉建议信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid FeedbackDataAddReq addReq){
        feedbackDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出投诉建议信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "投诉建议信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid FeedbackDataReq pageReq){
        feedbackDataService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的投诉建议信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除投诉建议信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        feedbackDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新投诉建议信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新投诉建议信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid FeedbackDataUpdateReq updateReq){
        feedbackDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryFeedbackData")
    @ApiLog(value = "查询投诉建议信息")
    @NeedLogin()
    public ApiResponse<List<FeedbackDataResp>> queryFeedbackData(
        @RequestBody @Valid FeedbackDataReq req){
        return apiResponse(feedbackDataService.queryFeedbackData(req));
    }

    /**
     * 查询单个投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneFeedbackData")
    @ApiLog(value = "查询投诉建议信息")
    @NeedLogin()
    public ApiResponse<FeedbackDataResp> queryOneFeedbackData(
        @RequestBody @Valid FeedbackDataReq req){
        return apiResponse(feedbackDataService.queryOneFeedbackData(req));
    }

    /**
     * 分页查询投诉建议信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询投诉建议信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询投诉建议信息")
    @NeedLogin()
    public ApiResponse<Pagination<FeedbackDataResp>> queryByPage(
        @RequestBody @Valid FeedbackDataReq pageReq){
        return apiResponse(feedbackDataService.queryByPage(pageReq));
    }
}
