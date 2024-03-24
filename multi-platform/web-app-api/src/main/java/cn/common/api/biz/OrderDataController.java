package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.resp.biz.openBiz.OrderDataResp;
import cn.common.service.biz.OrderDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 订单信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/orderData")
@Slf4j
public class OrderDataController extends BaseApiController {

    @Resource
    private OrderDataService orderDataService;

    /**
     * 查询单个的订单数据
     * @author: Singer
     * @date 2024-03-06
     * @param outTradeNo 交易订单号
     * @return java.util.List
     */
    @GetMapping(value = "/queryByTradeNo")
    @ApiLog(value = "查询单个的订单数据")
    @NeedLogin()
    public ApiResponse<OrderDataResp> queryByTradeNo(@RequestParam(name = "outTradeNo")
                                                     String outTradeNo){
        return apiResponse(orderDataService.queryByTradeNo(outTradeNo));
    }

}
