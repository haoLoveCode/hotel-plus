package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.GuestIdentifyAddReq;
import cn.common.req.biz.GuestIdentifyReq;
import cn.common.req.biz.GuestIdentifyUpdateReq;
import cn.common.resp.biz.GuestIdentifyResp;
import cn.common.service.biz.platform.GuestIdentifyService;
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
 * @Description: 客户身份信息前端控制器
 * @date 2024/3/24
 */
@RestController
@RequestMapping(value = "api/v1/guestIdentify")
@Slf4j
public class GuestIdentifyController extends BaseApiController {

    @Resource
    private GuestIdentifyService guestIdentifyService;

    /**
     * 新增客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param addReq 新增客户身份信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增客户身份信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid GuestIdentifyAddReq addReq){
        guestIdentifyService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出客户身份信息数据
     * @author: Singer
     * @date 2024/3/24
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "客户身份信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid GuestIdentifyReq pageReq){
        guestIdentifyService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param req 需要被删除的客户身份信息信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除客户身份信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        guestIdentifyService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param updateReq 更新客户身份信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新客户身份信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid GuestIdentifyUpdateReq updateReq){
        guestIdentifyService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询客户身份信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryGuestIdentify")
    @ApiLog(value = "查询客户身份信息信息")
    @NeedLogin()
    public ApiResponse<List<GuestIdentifyResp>> queryGuestIdentify(
        @RequestBody @Valid GuestIdentifyReq req){
        return apiResponse(guestIdentifyService.queryGuestIdentify(req));
    }

    /**
     * 查询单个客户身份信息信息
     * @author: Singer
     * @date 2024/3/24
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneGuestIdentify")
    @ApiLog(value = "查询客户身份信息信息")
    @NeedLogin()
    public ApiResponse<GuestIdentifyResp> queryOneGuestIdentify(
        @RequestBody @Valid GuestIdentifyReq req){
        return apiResponse(guestIdentifyService.queryOneGuestIdentify(req));
    }

    /**
     * 分页查询客户身份信息
     * @author: Singer
     * @date 2024/3/24
     * @param  pageReq 分页查询客户身份信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询客户身份信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<GuestIdentifyResp>> queryByPage(
        @RequestBody @Valid GuestIdentifyReq pageReq){
        return apiResponse(guestIdentifyService.queryByPage(pageReq));
    }
}
