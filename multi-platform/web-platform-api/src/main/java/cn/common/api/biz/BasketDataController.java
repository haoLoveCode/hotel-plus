package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.resp.biz.BasketDataResp;
import cn.common.service.biz.platform.BasketDataService;
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
 * @Description: 购物车信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/basketData")
@Slf4j
public class BasketDataController extends BaseApiController {

    @Resource
    private BasketDataService basketDataService;

    /**
     * 新增购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param addReq 新增购物车信息Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增购物车信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid BasketDataAddReq addReq){
        basketDataService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出购物车信息数据
     * @author: Singer
     * @date 2024-03-06
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "购物车信息数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid BasketDataReq pageReq){
        basketDataService.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param req 需要被删除的购物车信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除购物车信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        basketDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新购物车信息请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新购物车信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid BasketDataUpdateReq updateReq){
        basketDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryBasketData")
    @ApiLog(value = "查询购物车信息")
    @NeedLogin()
    public ApiResponse<List<BasketDataResp>> queryBasketData(
        @RequestBody @Valid BasketDataReq req){
        return apiResponse(basketDataService.queryBasketData(req));
    }

    /**
     * 查询单个购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneBasketData")
    @ApiLog(value = "查询购物车信息")
    @NeedLogin()
    public ApiResponse<BasketDataResp> queryOneBasketData(
        @RequestBody @Valid BasketDataReq req){
        return apiResponse(basketDataService.queryOneBasketData(req));
    }

    /**
     * 分页查询购物车信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询购物车信息Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询购物车信息")
    @NeedLogin()
    public ApiResponse<Pagination<BasketDataResp>> queryByPage(
        @RequestBody @Valid BasketDataReq pageReq){
        return apiResponse(basketDataService.queryByPage(pageReq));
    }
}
