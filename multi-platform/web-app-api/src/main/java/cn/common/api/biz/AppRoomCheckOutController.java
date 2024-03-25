package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomCheckOutAddReq;
import cn.common.req.biz.RoomCheckOutReq;
import cn.common.req.biz.RoomCheckOutUpdateReq;
import cn.common.resp.biz.RoomCheckOutResp;
import cn.common.service.biz.app.AppRoomCheckOutService;
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
 * @Description: 客房退房信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomCheckOut")
@Slf4j
public class AppRoomCheckOutController extends BaseApiController {

    @Resource
    private AppRoomCheckOutService appRoomCheckOutService;

    /**
     * 新增客房退房信息
     *
     * @param addReq 新增客房退房信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增客房退房信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomCheckOutAddReq addReq) {
        appRoomCheckOutService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除客房退房信息
     *
     * @param req 需要被删除的客房退房信息
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除客房退房信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appRoomCheckOutService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新客房退房信息
     *
     * @param updateReq 更新客房退房信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新客房退房信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomCheckOutUpdateReq updateReq) {
        appRoomCheckOutService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询客房退房信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryRoomCheckOut")
    @ApiLog(value = "查询客房退房信息")
    @NeedLogin()
    public ApiResponse<List<RoomCheckOutResp>> queryRoomCheckOut(
            @RequestBody @Valid RoomCheckOutReq req) {
        return apiResponse(appRoomCheckOutService.queryRoomCheckOut(req));
    }

    /**
     * 查询单个客房退房信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryOneRoomCheckOut")
    @ApiLog(value = "查询客房退房信息")
    @NeedLogin()
    public ApiResponse<RoomCheckOutResp> queryOneRoomCheckOut(
            @RequestBody @Valid RoomCheckOutReq req) {
        return apiResponse(appRoomCheckOutService.queryOneRoomCheckOut(req));
    }

    /**
     * 分页查询客房退房信息
     *
     * @param pageReq 分页查询客房退房信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询客房退房信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomCheckOutResp>> queryByPage(
            @RequestBody @Valid RoomCheckOutReq pageReq) {
        return apiResponse(appRoomCheckOutService.queryByPage(pageReq));
    }
}
