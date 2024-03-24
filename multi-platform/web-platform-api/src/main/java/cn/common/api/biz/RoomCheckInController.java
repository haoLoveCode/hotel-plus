package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.RoomCheckInAddReq;
import cn.common.req.biz.RoomCheckInReq;
import cn.common.req.biz.RoomCheckInUpdateReq;
import cn.common.resp.biz.RoomCheckInResp;
import cn.common.service.biz.platform.RoomCheckInService;
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
 * @Description: 客房入住信息前端控制器
 * @date 2024/1/25
 */
@RestController
@RequestMapping(value = "api/v1/roomCheckIn")
@Slf4j
public class RoomCheckInController extends BaseApiController {

    @Resource
    private RoomCheckInService roomCheckInService;

    /**
     * 新增客房入住信息
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增客房入住信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增客房入住信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid RoomCheckInAddReq addReq){
        roomCheckInService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出客房入住信息数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "客房入住信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid RoomCheckInReq pageReq){
        roomCheckInService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除客房入住信息
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的客房入住信息信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除客房入住信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        roomCheckInService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新客房入住信息
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新客房入住信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新客房入住信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid RoomCheckInUpdateReq updateReq){
        roomCheckInService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询客房入住信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryRoomCheckIn")
    @ApiLog(value = "查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<List<RoomCheckInResp>> queryRoomCheckIn(
        @RequestBody @Valid RoomCheckInReq req){
        return apiResponse(roomCheckInService.queryRoomCheckIn(req));
    }

    /**
     * 查询单个客房入住信息信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneRoomCheckIn")
    @ApiLog(value = "查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<RoomCheckInResp> queryOneRoomCheckIn(
        @RequestBody @Valid RoomCheckInReq req){
        return apiResponse(roomCheckInService.queryOneRoomCheckIn(req));
    }

    /**
     * 分页查询客房入住信息
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询客房入住信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询客房入住信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<RoomCheckInResp>> queryByPage(
        @RequestBody @Valid RoomCheckInReq pageReq){
        return apiResponse(roomCheckInService.queryByPage(pageReq));
    }
}
