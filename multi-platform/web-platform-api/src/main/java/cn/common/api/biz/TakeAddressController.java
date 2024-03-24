package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.TakeAddressAddReq;
import cn.common.req.biz.TakeAddressReq;
import cn.common.req.biz.TakeAddressUpdateReq;
import cn.common.resp.biz.TakeAddressResp;
import cn.common.service.biz.platform.TakeAddressService;
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
 * @Description: 收货地址信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/takeAddress")
@Slf4j
public class TakeAddressController extends BaseApiController {

    @Resource
    private TakeAddressService takeAddressService;

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
        takeAddressService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出收货地址信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "收货地址信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid TakeAddressReq pageReq){
        takeAddressService.exportData(pageReq);
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
        takeAddressService.batchDeleteItem(req);
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
        takeAddressService.updateItem(updateReq);
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
        return apiResponse(takeAddressService.queryTakeAddress(req));
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
        return apiResponse(takeAddressService.queryOneTakeAddress(req));
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
        return apiResponse(takeAddressService.queryByPage(pageReq));
    }
}
