package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.NoticeDataAddReq;
import cn.common.req.biz.openBiz.NoticeDataReq;
import cn.common.req.biz.openBiz.NoticeDataUpdateReq;
import cn.common.resp.biz.openBiz.NoticeDataResp;
import cn.common.service.biz.platform.NoticeDataService;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 公告信息前端控制器
 * @date 2024-03-25
 */
@RestController
@RequestMapping(value = "api/v1/noticeData")
@Slf4j
public class NoticeDataController extends BaseApiController {

    @Resource
    private NoticeDataService noticeDataService;

    /**
     * 新增公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增公告信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增公告信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid NoticeDataAddReq addReq){
        noticeDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出公告信息数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "公告信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid NoticeDataReq pageReq){
        noticeDataService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的公告信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除公告信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        noticeDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新公告信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新公告信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid NoticeDataUpdateReq updateReq){
        noticeDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryNoticeData")
    @ApiLog(value = "查询公告信息")
    @NeedLogin()
    public ApiResponse<List<NoticeDataResp>> queryNoticeData(
        @RequestBody @Valid NoticeDataReq req){
        return apiResponse(noticeDataService.queryNoticeData(req));
    }

    /**
     * 查询单个公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneNoticeData")
    @ApiLog(value = "查询公告信息")
    @NeedLogin()
    public ApiResponse<NoticeDataResp> queryOneNoticeData(
        @RequestBody @Valid NoticeDataReq req){
        return apiResponse(noticeDataService.queryOneNoticeData(req));
    }

    /**
     * 分页查询公告信息
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询公告信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询公告信息")
    @NeedLogin()
    public ApiResponse<Pagination<NoticeDataResp>> queryByPage(
        @RequestBody @Valid NoticeDataReq pageReq){
        return apiResponse(noticeDataService.queryByPage(pageReq));
    }
}
