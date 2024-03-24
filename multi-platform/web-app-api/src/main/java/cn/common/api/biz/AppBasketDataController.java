package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.BasketDataAddReq;
import cn.common.req.biz.BasketDataReq;
import cn.common.req.biz.BasketDataUpdateReq;
import cn.common.req.biz.app.ItemOrderingOneReq;
import cn.common.req.biz.app.OrderingByBasketItemReq;
import cn.common.resp.biz.BasketDataResp;
import cn.common.resp.biz.app.AppBasketItemResp;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.app.AppBasketDataService;
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
 * @Description: 购物车信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/basketData")
@Slf4j
public class AppBasketDataController extends BaseApiController {

    @Resource
    private AppBasketDataService appBasketDataService;

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/queryBasketItem")
    @ApiLog(value = "查询当前用户购物车的商品信息")
    @NeedLogin()
    public ApiResponse<List<AppBasketItemResp>> queryBasketItem() {
        return apiResponse(appBasketDataService.queryBasketItem());
    }

    /**
     *
     * @description: 商品单个信息下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req  商品单个信息下单Req
     * @return
     */
    @PostMapping(value = "/orderingOne")
    @ApiLog(value = "商品单个信息下单")
    @NeedLogin()
    public ApiResponse<TradeOrderResp> orderingOne(@RequestBody @Valid ItemOrderingOneReq req){
        return apiResponse(appBasketDataService.orderingOne(req));
    }

    /**
     *
     * @description: 购物车商品信息下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 购物车商品信息下单参数
     * @return
     */
    @PostMapping(value = "/basketItemOrdering")
    @ApiLog(value = "购物车商品信息下单")
    @NeedLogin()
    public ApiResponse<String> basketItemOrdering(@RequestBody @Valid OrderingByBasketItemReq req){
        return apiResponse(appBasketDataService.basketItemOrdering(req));
    }

    /**
     * @param
     * @return java.util.List
     * @description: 查询当前用户购物车的单个商品信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @PostMapping(value = "/queryOneBasketItem")
    @ApiLog(value = "查询当前用户购物车的单个商品信息")
    @NeedLogin()
    public ApiResponse<AppBasketItemResp> queryOneBasketItem(@RequestBody @Valid BasketDataReq req) {
        return apiResponse(appBasketDataService.queryOneBasketItem(req));
    }

    /**
     * 购物车业务处理
     *
     * @param addReq 购物车业务处理Req
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/handleBasketBiz")
    @ApiLog(value = "购物车业务处理")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid BasketDataAddReq addReq) {
        appBasketDataService.handleBasketBiz(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量删除购物车信息
     *
     * @param req 需要被删除的购物车信息
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除购物车信息")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req) {
        appBasketDataService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新购物车信息
     *
     * @param updateReq 更新购物车信息请求参数
     * @author: Singer
     * @date 2024-03-06
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新购物车信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid BasketDataUpdateReq updateReq) {
        appBasketDataService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/queryBasketData")
    @ApiLog(value = "查询购物车信息")
    @NeedLogin()
    public ApiResponse<List<BasketDataResp>> queryBasketData(
            @RequestBody @Valid BasketDataReq req) {
        return apiResponse(appBasketDataService.queryBasketData(req));
    }

    /**
     * 查询单个购物车信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/queryOneBasketData")
    @ApiLog(value = "查询购物车信息")
    @NeedLogin()
    public ApiResponse<BasketDataResp> queryOneBasketData(
            @RequestBody @Valid BasketDataReq req) {
        return apiResponse(appBasketDataService.queryOneBasketData(req));
    }

    /**
     * 分页查询购物车信息
     *
     * @param pageReq 分页查询购物车信息Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-06
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询购物车信息")
    @NeedLogin()
    public ApiResponse<Pagination<BasketDataResp>> queryByPage(
            @RequestBody @Valid BasketDataReq pageReq) {
        return apiResponse(appBasketDataService.queryByPage(pageReq));
    }
}
