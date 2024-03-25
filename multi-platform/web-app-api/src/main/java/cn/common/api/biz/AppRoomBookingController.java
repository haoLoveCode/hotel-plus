package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.BookingRoomReq;
import cn.common.req.biz.RoomBookingAddReq;
import cn.common.req.biz.RoomBookingReq;
import cn.common.req.biz.RoomBookingUpdateReq;
import cn.common.resp.biz.RoomBookingResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.app.AppRoomBookingService;
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
 * @Description: 房间预订信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomBooking")
@Slf4j
public class AppRoomBookingController extends BaseApiController {

    @Resource
    private AppRoomBookingService appRoomBookingService;

    /**
     * 新增房间预订信息
     *
     * @param req 新增房间预订信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间预订信息")
    @NeedLogin()
    public ApiResponse<TradeOrderResp> addItem(@RequestBody @Valid BookingRoomReq req) {
        return apiResponse(appRoomBookingService.addItem(req));
    }

    /**
     * 主键ID集合批量删除房间预订信息
     *
     * @param req 需要被删除的房间预订信息
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间预订信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appRoomBookingService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间预订信息
     *
     * @param updateReq 更新房间预订信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间预订信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomBookingUpdateReq updateReq) {
        appRoomBookingService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间预订信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryRoomBooking")
    @ApiLog(value = "查询房间预订信息")
    @NeedLogin()
    public ApiResponse<List<RoomBookingResp>> queryRoomBooking(
            @RequestBody @Valid RoomBookingReq req) {
        return apiResponse(appRoomBookingService.queryRoomBooking(req));
    }

    /**
     * 查询单个房间预订信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryOneRoomBooking")
    @ApiLog(value = "查询房间预订信息")
    @NeedLogin()
    public ApiResponse<RoomBookingResp> queryOneRoomBooking(
            @RequestBody @Valid RoomBookingReq req) {
        return apiResponse(appRoomBookingService.queryOneRoomBooking(req));
    }

    /**
     * 分页查询房间预订信息
     *
     * @param pageReq 分页查询房间预订信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间预订信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomBookingResp>> queryByPage(
            @RequestBody @Valid RoomBookingReq pageReq) {
        return apiResponse(appRoomBookingService.queryByPage(pageReq));
    }
}
