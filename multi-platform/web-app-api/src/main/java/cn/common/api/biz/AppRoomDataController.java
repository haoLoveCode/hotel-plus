package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.roomData.RoomDataAddReq;
import cn.common.req.biz.openBiz.roomData.RoomDataReq;
import cn.common.req.biz.openBiz.roomData.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataResp;
import cn.common.service.biz.app.AppRoomDataService;
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
 * @Description: 房间信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomData")
@Slf4j
public class AppRoomDataController extends BaseApiController {

    @Resource
    private AppRoomDataService appRoomDataService;

    /**
     * 新增房间信息
     *
     * @param addReq 新增房间信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomDataAddReq addReq) {
        appRoomDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除房间信息
     *
     * @param req 需要被删除的房间信息
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appRoomDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间信息
     *
     * @param updateReq 更新房间信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomDataUpdateReq updateReq) {
        appRoomDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryRoomData")
    @ApiLog(value = "查询房间信息")
    @NeedLogin(value = false)
    public ApiResponse<List<RoomDataResp>> queryRoomData(
            @RequestBody @Valid RoomDataReq req) {
        return apiResponse(appRoomDataService.queryRoomData(req));
    }

    /**
     * 查询单个房间信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryOneRoomData")
    @ApiLog(value = "查询房间信息")
    @NeedLogin(value = false)
    public ApiResponse<RoomDataResp> queryOneRoomData(
            @RequestBody @Valid RoomDataReq req) {
        return apiResponse(appRoomDataService.queryOneRoomData(req));
    }

    /**
     * 分页查询房间信息
     *
     * @param pageReq 分页查询房间信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间信息")
    @NeedLogin(value = false)
    public ApiResponse<Pagination<RoomDataResp>> queryByPage(
            @RequestBody @Valid RoomDataReq pageReq) {
        return apiResponse(appRoomDataService.queryByPage(pageReq));
    }
}
