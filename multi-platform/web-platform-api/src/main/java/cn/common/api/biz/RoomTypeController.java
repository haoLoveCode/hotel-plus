package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomTypeAddReq;
import cn.common.req.biz.RoomTypeReq;
import cn.common.req.biz.RoomTypeUpdateReq;
import cn.common.resp.biz.RoomTypeResp;
import cn.common.service.biz.platform.RoomTypeService;
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
 * @Description: 房间类型信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomType")
@Slf4j
public class RoomTypeController extends BaseApiController {

    @Resource
    private RoomTypeService roomTypeService;

    /**
     * 新增房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增房间类型信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增房间类型信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomTypeAddReq addReq){
        roomTypeService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出房间类型信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "房间类型信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid RoomTypeReq pageReq){
        roomTypeService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的房间类型信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除房间类型信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        roomTypeService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新房间类型信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新房间类型信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomTypeUpdateReq updateReq){
        roomTypeService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryRoomType")
    @ApiLog(value = "查询房间类型信息")
    @NeedLogin()
    public ApiResponse<List<RoomTypeResp>> queryRoomType(
        @RequestBody @Valid RoomTypeReq req){
        return apiResponse(roomTypeService.queryRoomType(req));
    }

    /**
     * 查询单个房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneRoomType")
    @ApiLog(value = "查询房间类型信息")
    @NeedLogin()
    public ApiResponse<RoomTypeResp> queryOneRoomType(
        @RequestBody @Valid RoomTypeReq req){
        return apiResponse(roomTypeService.queryOneRoomType(req));
    }

    /**
     * 分页查询房间类型信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询房间类型信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询房间类型信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomTypeResp>> queryByPage(
        @RequestBody @Valid RoomTypeReq pageReq){
        return apiResponse(roomTypeService.queryByPage(pageReq));
    }
}
