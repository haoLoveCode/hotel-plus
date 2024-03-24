package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomCheckInAddReq;
import cn.common.req.biz.RoomCheckInReq;
import cn.common.req.biz.RoomCheckInUpdateReq;
import cn.common.resp.biz.RoomCheckInResp;
import cn.common.service.biz.app.AppRoomCheckInService;
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
 * @Description: 客房入住信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomCheckIn")
@Slf4j
public class AppRoomCheckInController extends BaseApiController {

    @Resource
    private AppRoomCheckInService appRoomCheckInService;

    /**
     * 新增客房入住信息
     *
     * @param addReq 新增客房入住信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增客房入住信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomCheckInAddReq addReq) {
        appRoomCheckInService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除客房入住信息
     *
     * @param req 需要被删除的客房入住信息信息
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除客房入住信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appRoomCheckInService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新客房入住信息
     *
     * @param updateReq 更新客房入住信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新客房入住信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomCheckInUpdateReq updateReq) {
        appRoomCheckInService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询客房入住信息信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryRoomCheckIn")
    @ApiLog(value = "查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<List<RoomCheckInResp>> queryRoomCheckIn(
            @RequestBody @Valid RoomCheckInReq req) {
        return apiResponse(appRoomCheckInService.queryRoomCheckIn(req));
    }

    /**
     * 查询单个客房入住信息信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryOneRoomCheckIn")
    @ApiLog(value = "查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<RoomCheckInResp> queryOneRoomCheckIn(
            @RequestBody @Valid RoomCheckInReq req) {
        return apiResponse(appRoomCheckInService.queryOneRoomCheckIn(req));
    }

    /**
     * 分页查询客房入住信息
     *
     * @param pageReq 分页查询客房入住信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomCheckInResp>> queryByPage(
            @RequestBody @Valid RoomCheckInReq pageReq) {
        return apiResponse(appRoomCheckInService.queryByPage(pageReq));
    }
}
