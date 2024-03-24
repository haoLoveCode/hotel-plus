package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.TakeAddressAddReq;
import cn.common.req.biz.TakeAddressReq;
import cn.common.req.biz.TakeAddressUpdateReq;
import cn.common.resp.biz.TakeAddressResp;
import cn.common.service.biz.app.AppTakeAddressService;
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
 * @Description: 收货地址信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/takeAddress")
@Slf4j
public class AppTakeAddressController extends BaseApiController {

    @Resource
    private AppTakeAddressService appTakeAddressService;

    /**
     * 新增收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增收货地址信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增收货地址信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid TakeAddressAddReq addReq){
        appTakeAddressService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的收货地址信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除收货地址信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        appTakeAddressService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新收货地址信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新收货地址信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid TakeAddressUpdateReq updateReq){
        appTakeAddressService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryTakeAddress")
    @ApiLog(value = "查询收货地址信息")
    @NeedLogin()
    public ApiResponse<List<TakeAddressResp>> queryTakeAddress(
        @RequestBody @Valid TakeAddressReq req){
        return apiResponse(appTakeAddressService.queryTakeAddress(req));
    }

    /**
     * 查询单个收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneTakeAddress")
    @ApiLog(value = "查询收货地址信息")
    @NeedLogin()
    public ApiResponse<TakeAddressResp> queryOneTakeAddress(
        @RequestBody @Valid TakeAddressReq req){
        return apiResponse(appTakeAddressService.queryOneTakeAddress(req));
    }

    /**
     * 分页查询收货地址信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询收货地址信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询收货地址信息")
    @NeedLogin()
    public ApiResponse<Pagination<TakeAddressResp>> queryByPage(
        @RequestBody @Valid TakeAddressReq pageReq){
        return apiResponse(appTakeAddressService.queryByPage(pageReq));
    }
}
