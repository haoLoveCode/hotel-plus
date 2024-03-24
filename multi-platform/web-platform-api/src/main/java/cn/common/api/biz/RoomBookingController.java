package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomBookingAddReq;
import cn.common.req.biz.RoomBookingReq;
import cn.common.req.biz.RoomBookingUpdateReq;
import cn.common.resp.biz.RoomBookingResp;
import cn.common.service.biz.platform.RoomBookingService;
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
 * @Description: 房间预订信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomBooking")
@Slf4j
public class RoomBookingController extends BaseApiController {

    @Resource
    private RoomBookingService roomBookingService;

    /**
     * 新增房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增房间预订信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间预订信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomBookingAddReq addReq){
        roomBookingService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出房间预订信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "房间预订信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid RoomBookingReq pageReq){
        roomBookingService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间预订信息信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间预订信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        roomBookingService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间预订信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间预订信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomBookingUpdateReq updateReq){
        roomBookingService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间预订信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryRoomBooking")
    @ApiLog(value = "查询房间预订信息信息")
    @NeedLogin()
    public ApiResponse<List<RoomBookingResp>> queryRoomBooking(
        @RequestBody @Valid RoomBookingReq req){
        return apiResponse(roomBookingService.queryRoomBooking(req));
    }

    /**
     * 查询单个房间预订信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneRoomBooking")
    @ApiLog(value = "查询房间预订信息信息")
    @NeedLogin()
    public ApiResponse<RoomBookingResp> queryOneRoomBooking(
        @RequestBody @Valid RoomBookingReq req){
        return apiResponse(roomBookingService.queryOneRoomBooking(req));
    }

    /**
     * 分页查询房间预订信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间预订信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间预订信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomBookingResp>> queryByPage(
        @RequestBody @Valid RoomBookingReq pageReq){
        return apiResponse(roomBookingService.queryByPage(pageReq));
    }
}
