package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.platform.SetTradeOrderReq;
import cn.common.req.biz.platform.TradeOrderReq;
import cn.common.resp.biz.openBiz.TradeOrderResp;
import cn.common.service.biz.app.AppTradeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 交易订单信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/tradeOrder")
@Slf4j
public class AppTradeOrderController extends BaseApiController {
    @Resource
    private AppTradeOrderService appTradeOrderService;

    /**
     *
     * @description: 查询单个交易订单数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  outTradeNo 外部交易订单号
     * @return cn.common.resp.biz.openBiz.TradeOrderResp
     */
    @GetMapping(value = "/queryByOrderNo")
    @ApiLog(value = "查询单个交易订单数据")
    @NeedLogin()
    public ApiResponse<TradeOrderResp> queryByOrderNo(@RequestParam(name = "outTradeNo")
                                                          String outTradeNo){
        return apiResponse(appTradeOrderService.queryByOrderNo(outTradeNo));
    }

    /**
     * 更新订单状态
     * @author: Singer
     * @date 2024-03-06
     * @param req 更新请求参数
     */
    @PutMapping(value = "/setOrderStatus")
    @ApiLog(value = "更新订单状态")
    @NeedLogin()
    public ApiResponse setOrderStatus(@RequestBody @Valid SetTradeOrderReq req){
        appTradeOrderService.setOrderStatus(req);
        return apiResponse();
    }

    /**
     *
     * @description: 查询当前用户的所有订单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return java.util.List
     */
    @PostMapping(value = "/queryOrderList")
    @ApiLog(value = "查询当前用户的所有订单信息")
    @NeedLogin()
    public ApiResponse<List<TradeOrderResp>> queryOrderList(){
        return apiResponse(appTradeOrderService.queryOrderList());
    }

    /**
     * 分页查询用户订单信息
     * @author: Singer
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询用户订单信息")
    @NeedLogin()
    public ApiResponse<Pagination<TradeOrderResp>> queryByPage(
            @RequestBody @Valid TradeOrderReq pageReq){
        return apiResponse(appTradeOrderService.queryByPage(pageReq));
    }

}
