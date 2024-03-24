package cn.common.resp.biz.app.wxpay.callback;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 微信退款回调数据
 * @date 2024-03-06
 */
@Data
public class WxRefundCallBackData implements Serializable {

    private static final long serialVersionUID = 5398228549252582124L;

    /**
     * 商户号
     */
    @JSONField(name = "mchid")
    private String mchId;

    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 退款订单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;

    /**
     * 退款状态
     */
    @JSONField(name = "refund_status")
    private String refundStatus;

    /**
     * 成功时间
     */
    @JSONField(name = "success_time")
    private String successTime;

    /**
     * 金额
     */
    @JSONField(name = "amount")
    private WxRefundCallBackAmount amount;

    /**
     * 取当前退款单的退款入账方
     */
    @JSONField(name = "user_received_account")
    private String userReceivedAccount;


    @Data
    class WxRefundCallBackAmount{

        /**
         * 订单总金额，单位为分，只能为整数
         */
        @JSONField(name = "total")
        private Integer total;

        /**
         * 退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额，如果有使用券，后台会按比例退。
         */
        @JSONField(name = "refund")
        private Integer refund;

        /**
         * 用户实际支付金额，单位为分，只能为整数
         */
        @JSONField(name = "payer_total")
        private Integer payer_total;

        /**
         * 退款给用户的金额，不包含所有优惠券金额
         */
        @JSONField(name = "payer_refund")
        private Integer payerRefund;
    }

}
