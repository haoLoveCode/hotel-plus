package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.app.callback.WxPayCallPack;
import cn.common.service.biz.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: APP认证用户前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/wxPayApi")
@Slf4j
//存在appId配置才进行注入
@ConditionalOnProperty(
        prefix = "baseConstant.wechatPay",
        name = "appId"
)
public class WxPayController extends BaseApiController {

    @Resource
    private WxPayService wxPayService;

    /**
     * 微信支付回调
     * @description:
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req
     * @return
     */
    @PostMapping("/wxV3PayCallBack")
    @ApiLog(value = "微信支付回调")
    @NeedLogin(value = false)
    public ApiResponse wxV3PayCallBack(@RequestBody @Valid WxPayCallPack req){
        wxPayService.wxV3PayCallBack(req);
        return apiResponse();
    }

}
