package cn.common.service.biz;

import cn.common.repository.entity.biz.TradeOrder;
import cn.common.req.biz.app.wxPay.WxOrderReq;
import cn.common.req.biz.app.callback.WxPayCallPack;
import cn.common.req.platform.wxPay.SubmitTransferReq;
import cn.common.resp.biz.app.wxpay.WxMiniPayResp;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferResponse;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 微信支付相关服务方法
 * @date 2024-03-25
 */
public interface WxPayService {

    /**
     *
     * @description: 商家批量转账
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/1/8
     * @param req 转账Req
     * @return
     */
    InitiateBatchTransferResponse handleTransfer(SubmitTransferReq req);

    /**
     *
     * @description: 处理退款
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  tradeOrder 订单数据
     * @return
     */
    Refund handleRefund(TradeOrder tradeOrder);

    /**
     * 微信支付回调
     * @description:
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req
     * @return
     */
    void wxV3PayCallBack(WxPayCallPack req);

    /**
     *
     * @description: 微信退款回调
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 退款回调参数
     * @return
     */
    void wxV3RefundCallBack(WxPayCallPack req);

    /**
     *
     * @description: 微信下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req
     * @return cn.common.resp.biz.app.wxpay.WxMiniPayResp
     */
    WxMiniPayResp wxOrder(WxOrderReq req);

    /**
     * SHA256withRSA签名
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param content 内容
     * @param charset 编码
     * @return String
     */
    String signByPcks8Sha256WithRsa(String content, String charset);

    /**
     * 设置微信下单请求参数并且返回请求后的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param wxOrderReq 微信下单请求参数
     * @param outTradeNo 外部订单号
     * @param openId 支付用户的OpenId
     * @return cn.common.resp.biz.app.wxpay.WxV3PayResult
     */
    PrepayResponse handleWxOrder(WxOrderReq wxOrderReq,
                                 String outTradeNo,
                                 String openId);


    /**
     *
     * @description: 根据外部支付订单号查询微信订单状态
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param outTradeNo 外部支付订单号
     * @return
     */
    Transaction.TradeStateEnum queryOrder(String outTradeNo);

}
