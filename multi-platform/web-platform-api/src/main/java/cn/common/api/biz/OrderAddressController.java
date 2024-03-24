package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.OrderAddressAddReq;
import cn.common.req.biz.OrderAddressReq;
import cn.common.req.biz.OrderAddressUpdateReq;
import cn.common.resp.biz.OrderAddressResp;
import cn.common.service.biz.platform.OrderAddressService;
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
 * @Description: 订单-收货地址关联信息前端控制器
 * @date 2024-03-04
 */
@RestController
@RequestMapping(value = "api/v1/orderAddress")
@Slf4j
public class OrderAddressController extends BaseApiController {

    @Resource
    private OrderAddressService orderAddressService;

    /**
     * 新增订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param addReq 新增订单-收货地址关联信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增订单-收货地址关联信息信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid OrderAddressAddReq addReq){
        orderAddressService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出订单-收货地址关联信息数据
     * @author: Singer
     * @date 2024-03-04
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "订单-收货地址关联信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid OrderAddressReq pageReq){
        orderAddressService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param req 需要被删除的订单-收货地址关联信息信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除订单-收货地址关联信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        orderAddressService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param updateReq 更新订单-收货地址关联信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新订单-收货地址关联信息信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid OrderAddressUpdateReq updateReq){
        orderAddressService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOrderAddress")
    @ApiLog(value = "查询订单-收货地址关联信息信息")
    @NeedLogin()
    public ApiResponse<List<OrderAddressResp>> queryOrderAddress(
        @RequestBody @Valid OrderAddressReq req){
        return apiResponse(orderAddressService.queryOrderAddress(req));
    }

    /**
     * 查询单个订单-收货地址关联信息信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneOrderAddress")
    @ApiLog(value = "查询订单-收货地址关联信息信息")
    @NeedLogin()
    public ApiResponse<OrderAddressResp> queryOneOrderAddress(
        @RequestBody @Valid OrderAddressReq req){
        return apiResponse(orderAddressService.queryOneOrderAddress(req));
    }

    /**
     * 分页查询订单-收货地址关联信息
     * @author: Singer
     * @date 2024-03-04
     * @param  pageReq 分页查询订单-收货地址关联信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询订单-收货地址关联信息信息")
    @NeedLogin()
    public ApiResponse<Pagination<OrderAddressResp>> queryByPage(
        @RequestBody @Valid OrderAddressReq pageReq){
        return apiResponse(orderAddressService.queryByPage(pageReq));
    }
}
