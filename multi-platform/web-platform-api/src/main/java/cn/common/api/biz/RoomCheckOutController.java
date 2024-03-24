package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomCheckOutAddReq;
import cn.common.req.biz.RoomCheckOutReq;
import cn.common.req.biz.RoomCheckOutUpdateReq;
import cn.common.resp.biz.RoomCheckOutResp;
import cn.common.service.biz.platform.RoomCheckOutService;
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
 * @Description: 客房退房信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/roomCheckOut")
@Slf4j
public class RoomCheckOutController extends BaseApiController {

    @Resource
    private RoomCheckOutService roomCheckOutService;

    /**
     * 新增客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增客房退房信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增客房退房信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomCheckOutAddReq addReq){
        roomCheckOutService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出客房退房信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "客房退房信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid RoomCheckOutReq pageReq){
        roomCheckOutService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客房退房信息信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除客房退房信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        roomCheckOutService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客房退房信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新客房退房信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomCheckOutUpdateReq updateReq){
        roomCheckOutService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询客房退房信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryRoomCheckOut")
    @ApiLog(value = "查询客房退房信息信息")
    @NeedLogin()
    public ApiResponse<List<RoomCheckOutResp>> queryRoomCheckOut(
        @RequestBody @Valid RoomCheckOutReq req){
        return apiResponse(roomCheckOutService.queryRoomCheckOut(req));
    }

    /**
     * 查询单个客房退房信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneRoomCheckOut")
    @ApiLog(value = "查询客房退房信息信息")
    @NeedLogin()
    public ApiResponse<RoomCheckOutResp> queryOneRoomCheckOut(
        @RequestBody @Valid RoomCheckOutReq req){
        return apiResponse(roomCheckOutService.queryOneRoomCheckOut(req));
    }

    /**
     * 分页查询客房退房信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客房退房信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询客房退房信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomCheckOutResp>> queryByPage(
        @RequestBody @Valid RoomCheckOutReq pageReq){
        return apiResponse(roomCheckOutService.queryByPage(pageReq));
    }
}
