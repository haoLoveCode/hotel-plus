package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.AppUserDataAddReq;
import cn.common.req.biz.openBiz.AppUserDataReq;
import cn.common.req.biz.openBiz.AppUserDataUpdateReq;
import cn.common.resp.biz.platform.AppUserDataResp;
import cn.common.service.biz.platform.AppUserDataService;
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
 * @Description: APP用户其他信息前端控制器
 * @date 2024-03-25
 */
@RestController
@RequestMapping(value = "api/v1/appUserData")
@Slf4j
public class AppUserDataController extends BaseApiController {

    @Resource
    private AppUserDataService appUserDataService;

    /**
     * 新增APP用户其他信息
     * @author: Singer
     * @date 2024-03-25
     * @param addReq 新增APP用户其他信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增APP用户其他信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid AppUserDataAddReq addReq){
        appUserDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出APP用户其他信息数据
     * @author: Singer
     * @date 2024-03-25
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "APP用户其他信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid AppUserDataReq pageReq){
        appUserDataService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除APP用户其他信息
     * @author: Singer
     * @date 2024-03-25
     * @param req 需要被删除的APP用户其他信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除APP用户其他信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        appUserDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新APP用户其他信息
     * @author: Singer
     * @date 2024-03-25
     * @param updateReq 更新APP用户其他信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新APP用户其他信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid AppUserDataUpdateReq updateReq){
        appUserDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询所有APP用户其他信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @GetMapping(value = "/queryAllAppUserData")
    @ApiLog(value = "查询所有APP用户其他信息")
    @NeedLogin()
    public ApiResponse<List<AppUserDataResp>> queryAllAppUserData(){
        return apiResponse(appUserDataService.queryAllAppUserData());
    }

    /**
     * 分页查询APP用户其他信息
     * @author: Singer
     * @date 2024-03-25
     * @param  pageReq 分页查询APP用户其他信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询APP用户其他信息")
    @NeedLogin()
    public ApiResponse<Pagination<AppUserDataResp>> queryByPage(
        @RequestBody @Valid AppUserDataReq pageReq){
        return apiResponse(appUserDataService.queryByPage(pageReq));
    }
}
