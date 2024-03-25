package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomTypeAddReq;
import cn.common.req.biz.RoomTypeReq;
import cn.common.req.biz.RoomTypeUpdateReq;
import cn.common.resp.biz.RoomTypeResp;
import cn.common.service.biz.app.AppRoomTypeService;
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
 * @Description: 房间类型信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomType")
@Slf4j
public class AppRoomTypeController extends BaseApiController {

    @Resource
    private AppRoomTypeService appRoomTypeService;

    /**
     * 新增房间类型信息
     *
     * @param addReq 新增房间类型信息Req
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间类型信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomTypeAddReq addReq) {
        appRoomTypeService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除房间类型信息
     *
     * @param req 需要被删除的房间类型信息
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间类型信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appRoomTypeService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间类型信息
     *
     * @param updateReq 更新房间类型信息请求参数
     * @author: Singer
     * @date 2024/3/24
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间类型信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomTypeUpdateReq updateReq) {
        appRoomTypeService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间类型信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryRoomType")
    @ApiLog(value = "查询房间类型信息")
    @NeedLogin()
    public ApiResponse<List<RoomTypeResp>> queryRoomType(
            @RequestBody @Valid RoomTypeReq req) {
        return apiResponse(appRoomTypeService.queryRoomType(req));
    }

    /**
     * 查询单个房间类型信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryOneRoomType")
    @ApiLog(value = "查询房间类型信息")
    @NeedLogin()
    public ApiResponse<RoomTypeResp> queryOneRoomType(
            @RequestBody @Valid RoomTypeReq req) {
        return apiResponse(appRoomTypeService.queryOneRoomType(req));
    }

    /**
     * 分页查询房间类型信息
     *
     * @param pageReq 分页查询房间类型信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024/3/24
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间类型信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomTypeResp>> queryByPage(
            @RequestBody @Valid RoomTypeReq pageReq) {
        return apiResponse(appRoomTypeService.queryByPage(pageReq));
    }
}
