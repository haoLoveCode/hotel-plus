package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.roomData.RoomDataAddReq;
import cn.common.req.biz.openBiz.roomData.RoomDataReq;
import cn.common.req.biz.openBiz.roomData.RoomDataUpdateReq;
import cn.common.resp.biz.RoomDataResp;
import cn.common.service.biz.platform.RoomDataService;
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
 * @Description: 房间信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomData")
@Slf4j
public class RoomDataController extends BaseApiController {

    @Resource
    private RoomDataService roomDataService;

    /**
     * 新增房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增房间信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomDataAddReq addReq){
        roomDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出房间信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "房间信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid RoomDataReq pageReq){
        roomDataService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        roomDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomDataUpdateReq updateReq){
        roomDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryRoomData")
    @ApiLog(value = "查询房间信息")
    @NeedLogin()
    public ApiResponse<List<RoomDataResp>> queryRoomData(
        @RequestBody @Valid RoomDataReq req){
        return apiResponse(roomDataService.queryRoomData(req));
    }

    /**
     * 查询单个房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneRoomData")
    @ApiLog(value = "查询房间信息")
    @NeedLogin()
    public ApiResponse<RoomDataResp> queryOneRoomData(
        @RequestBody @Valid RoomDataReq req){
        return apiResponse(roomDataService.queryOneRoomData(req));
    }

    /**
     * 分页查询房间信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomDataResp>> queryByPage(
        @RequestBody @Valid RoomDataReq pageReq){
        return apiResponse(roomDataService.queryByPage(pageReq));
    }
}
