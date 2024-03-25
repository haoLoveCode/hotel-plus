package cn.common.service.impl.biz;

import cn.common.constants.BizConstants;
import cn.common.repository.entity.biz.PayData;
import cn.common.repository.entity.biz.TradeOrder;
import cn.common.repository.entity.biz.TradeRefund;
import cn.common.repository.repository.biz.PayDataRepository;
import cn.common.repository.repository.biz.TradeOrderRepository;
import cn.common.repository.repository.biz.TradeRefundRepository;
import cn.common.req.biz.app.callback.WxPayCallPack;
import cn.common.req.biz.app.callback.WxPayCallPackResource;
import cn.common.req.biz.app.wxPay.WxOrderReq;
import cn.common.req.platform.wxPay.SubmitTransferItemReq;
import cn.common.req.platform.wxPay.SubmitTransferReq;
import cn.common.resp.biz.app.auth.AuthAppLoginResp;
import cn.common.resp.biz.app.wxpay.WxMiniPayResp;
import cn.common.resp.biz.app.wxpay.callback.WxPayCallBackData;
import cn.common.resp.biz.app.wxpay.callback.WxRefundCallBackData;
import cn.common.service.biz.AsyncService;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.WxPayService;
import cn.common.wx.WxBizConstants;
import cn.common.wxPay.WxAesUtil;
import cn.common.wxPay.WxPayV3PayConstants;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.transferbatch.TransferBatchService;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferResponse;
import com.wechat.pay.java.service.transferbatch.model.TransferDetailInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.configuration.redis.RedisRepository;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.enums.FefundStatusEnum;
import pro.skywalking.enums.OrderStatusEnum;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.stream.CollectorsUtil;
import pro.skywalking.utils.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 微信支付相关服务方法实现
 * @date 2024-03-25
 */
@Service("wxPayService")
@ConditionalOnProperty(
        prefix = "baseConstant.wechatPay",
        name = "appId"
)
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private WxPayV3PayConstants wxPayV3PayConstants;

    @Resource
    private WxBizConstants wxBizConstants;

    @Resource
    private TradeOrderRepository tradeOrderRepository;

    @Resource
    private JsapiService jsapiService;

    @Resource
    private TransferBatchService transferBatchService;

    @Resource
    private RefundService refundService;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthAppUserService authAppUserService;

    @Resource
    private PayDataRepository payDataRepository;

    @Resource
    private TradeRefundRepository tradeRefundRepository;

    @Resource
    @Lazy
    private AsyncService asyncService;

    /**
     * 微信支付成功状态
     */
    private static final String WX_PAY_SUCCESS = "SUCCESS";

    /**
     * 微信退款处理成功状态
     */
    private static final String WX_REFUND_SUCCESS = "SUCCESS";

    /**
     * RFC3339时间格式
     */
    private static final DateTimeFormatter RFC3339_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss+08:00")
            .withZone(ZoneId.of("Asia/Shanghai"));

    /**
     *
     * @description: 存储支付快照信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param outTradeNo 外部交易订单号
     * @param payData 支付数据
     * @return
     */
    public void setPayData(String outTradeNo,String payData){

        PayData entity = new PayData();

        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setPayDataId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(StrUtil.EMPTY);
            entity.setPayData(payData);
            entity.setOutTradeNo(outTradeNo);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        payDataRepository.insert(entity);
    }

    /**
     * 微信支付回调
     * @description:
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req
     * @return
     */
    public void wxV3PayCallBack(WxPayCallPack req){
        String callBack = JSON.toJSONString(req);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信支付回调参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                callBack);
        WxPayCallPackResource resource = req.getResource();
        //附加数据
        String associatedData = resource.getAssociatedData();
        //随机串
        String nonce = resource.getNonce();
        //加密信息
        String ciphertext = resource.getCiphertext();
        //回调摘要
        //String summary = req.getSummary();
        //拿到微信私钥
        //String wxPrivate = wxPrivate();
        WxAesUtil wxAesUtil = new WxAesUtil(wxPayV3PayConstants.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String decrypt = null;
        try {
            decrypt = wxAesUtil.decrypt(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "微信回调解密失败");
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信下单回调解密参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                decrypt);
        WxPayCallBackData wxPayCallBackData = JSON.parseObject(decrypt, WxPayCallBackData.class);
        //使用外部订单号关联
        String outTradeNo = wxPayCallBackData.getOutTradeNo();
        //查询订单
        TradeOrder tradeOrder = tradeOrderRepository.selectOne(
                new LambdaQueryWrapper<TradeOrder>()
                        .eq(TradeOrder::getOutTradeNo, outTradeNo));
        if(CheckParam.isNull(tradeOrder)){
            return;
        }
        //微信支付状态
        String tradeState = wxPayCallBackData.getTradeState();
        if(StrUtil.equalsIgnoreCase(WX_PAY_SUCCESS,tradeState)){
            tradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        }
        //存储解密快照和回调原始信息
        setPayData(outTradeNo,callBack);
        setPayData(outTradeNo,decrypt);
        tradeOrderRepository.updateById(tradeOrder);
        //异步处理业务
        //asyncService.setAsyncData(tradeOrder);
    }

    /**
     *
     * @description: 微信退款回调
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req 退款回调参数
     * @return
     */
    public void wxV3RefundCallBack(WxPayCallPack req){
        String callBack = JSON.toJSONString(req);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信退款回调参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                callBack);
        WxPayCallPackResource resource = req.getResource();
        //附加数据
        String associatedData = resource.getAssociatedData();
        //随机串
        String nonce = resource.getNonce();
        //加密信息
        String ciphertext = resource.getCiphertext();
        //回调摘要
        //String summary = req.getSummary();
        //拿到微信私钥
        //String wxPrivate = wxPrivate();
        WxAesUtil wxAesUtil = new WxAesUtil(wxPayV3PayConstants.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String decrypt = null;
        try {
            decrypt = wxAesUtil.decrypt(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "微信退款回调解密失败");
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信退款回调解密参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                decrypt);

        WxRefundCallBackData wxRefundCallBackData = JSON.parseObject(decrypt, WxRefundCallBackData.class);
        //使用外部订单号关联
        String outTradeNo = wxRefundCallBackData.getOutTradeNo();
        //查询订单
        TradeOrder tradeOrder = tradeOrderRepository.selectOne(
                new LambdaQueryWrapper<TradeOrder>()
                        .eq(TradeOrder::getOutTradeNo, outTradeNo));
        if(CheckParam.isNull(tradeOrder)){
            return;
        }
        //微信退款状态
        String refundStatus = wxRefundCallBackData.getRefundStatus();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信退款结果状态 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                refundStatus);
        if(StrUtil.equalsIgnoreCase(WX_REFUND_SUCCESS,refundStatus)){
            tradeOrder.setOrderStatus(OrderStatusEnum.REFUND.getCode());
        }else{
            tradeOrder.setOrderStatus(OrderStatusEnum.REFUND_FAIL.getCode());
        }
        //存储解密快照和回调原始信息
        setPayData(outTradeNo,callBack);
        setPayData(outTradeNo,decrypt);
        tradeOrderRepository.updateById(tradeOrder);

        //异步处理数据
        //asyncService.setAsyncBiz(tradeOrder);
    }

    /**
     *
     * @description: 商家批量转账
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024/1/8
     * @param req 转账Req
     * @return
     */
    public InitiateBatchTransferResponse handleTransfer(SubmitTransferReq req){
        List<SubmitTransferItemReq> itemList = req.getItemList();
        if(CollectionUtils.isEmpty(itemList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "转账明细信息不可为空");
        }
        //转账总额
        BigDecimal totalAmount = BigDecimalUtils.multiplyOneHundred(req.getTotalAmount());

        //明细总额
        BigDecimal itemTotalAmount = itemList.stream().collect(CollectorsUtil.summingBigDecimal(SubmitTransferItemReq::getTransferAmount));
        itemTotalAmount = BigDecimalUtils.multiplyOneHundred(itemTotalAmount);
        if(totalAmount.compareTo(itemTotalAmount) != 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "转账总额和转账明细总额信息不一致");
        }

        InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
        request.setAppid(wxBizConstants.getAppId());
        request.setOutBatchNo(req.getOutBatchNo());
        request.setBatchName(req.getBatchName());
        request.setTotalAmount(totalAmount.longValue());
        request.setBatchRemark(req.getRemarkData());
        request.setTotalNum(itemList.size());

        List<TransferDetailInput> transferDetailList = Lists.newArrayList();
        itemList.stream().forEach(item -> {
            TransferDetailInput transferDetailInput = new TransferDetailInput();
            transferDetailInput.setOpenid(item.getOuterUserId());
            transferDetailInput.setUserName(StrUtil.EMPTY);
            transferDetailInput.setOutDetailNo(item.getTransferItemNo());
            transferDetailInput.setTransferRemark(item.getRemarkData());
            Long transferAmount = BigDecimalUtils.multiplyOneHundred(item.getTransferAmount()).longValue();
            transferDetailInput.setTransferAmount(transferAmount);

            transferDetailList.add(transferDetailInput);
        });
        request.setTransferDetailList(transferDetailList);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信商家转账到零钱请求参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                JSON.toJSONString(request));
        try{
            InitiateBatchTransferResponse transferResponse = transferBatchService.initiateBatchTransfer(request);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信商家转账到零钱返回参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                    JSON.toJSONString(transferResponse));
            return transferResponse;
        }catch (Exception e){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    StrUtil.format("商家转账到零钱出现异常:{},{}",e.getMessage(),e));
        }
    }

    /**
     *
     * @description: 处理退款
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param  tradeOrder 订单数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Refund handleRefund(TradeOrder tradeOrder){
        String outTradeNo = tradeOrder.getOutTradeNo();

        String outRefundNo = SnowflakeIdWorker.uniqueMainId();
        CreateRequest refundRequest = new CreateRequest();
        refundRequest.setOutTradeNo(outTradeNo);
        refundRequest.setOutRefundNo(outRefundNo);
        refundRequest.setReason("平台退款");
        refundRequest.setNotifyUrl(wxPayV3PayConstants.getNotifyRefundUrl());

        BigDecimal orderAmount = BigDecimalUtils.multiplyOneHundred(tradeOrder.getOrderAmount());

        AmountReq refundAmount = new AmountReq();
        refundAmount.setRefund(orderAmount.longValue());
        refundAmount.setTotal(orderAmount.longValue());
        refundAmount.setCurrency("CNY");

        refundRequest.setAmount(refundAmount);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信退款请求参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                JSON.toJSONString(refundRequest));
        Refund refundResp = refundService.create(refundRequest);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信退款返回参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                JSON.toJSONString(refundResp));
        //设置退款下相关数据
        insertTradeRefund(tradeOrder,refundResp);
        return refundResp;
    }

    /**
     *
     * @description: 设置退款下相关数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param tradeOrder 交易订单信息
     * @param refundResp 退款结果
     * @return
     */
    private void insertTradeRefund(TradeOrder tradeOrder, Refund refundResp){
        TradeRefund tradeRefund = new TradeRefund();
        tradeRefund.setTradeRefundId(SnowflakeIdWorker.uniqueMainId());
        tradeRefund.setOutRefundNo(refundResp.getOutRefundNo());
        tradeRefund.setAuthAppUserId(tradeOrder.getAuthAppUserId());
        tradeRefund.setExtraData(JSON.toJSONString(tradeRefund));
        tradeRefund.setRefundRemark(StrUtil.EMPTY);
        tradeRefund.setOutTradeNo(refundResp.getOutTradeNo());
        tradeRefund.setRefundRemark(tradeOrder.getOrderRemark());
        //此处根据微信返回的退款状态设置数据库的退款表里面的退款状态
        tradeRefund.setRefundStatus(FefundStatusEnum.getCodeByWxCode(refundResp.getStatus().toString()));

        com.wechat.pay.java.service.refund.model.Amount amount = refundResp.getAmount();
        Long refundAmountResp = amount.getRefund();
        tradeRefund.setRefundAmount(BigDecimal.valueOf(refundAmountResp));
        tradeRefund.setTradeOrderId(tradeOrder.getTradeOrderId());
        try {
            BaseUtil.setFieldValueNotNull(tradeRefund);
        } catch (Exception e) {
            log.error("新增退款信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }

        tradeRefundRepository.insert(tradeRefund);
    }

    /**
     *
     * @description: 微信下单
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param req
     * @return cn.common.resp.biz.app.wxpay.WxMiniPayResp
     */
    public WxMiniPayResp wxOrder(WxOrderReq req){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>微信下单请求参数 : <<<<<<<<<<<<<<<<<<<<<<<<<<< {} ",
                JSON.toJSONString(req));

        String outTradeNo = SnowflakeIdWorker.uniqueMainId();

        AuthAppLoginResp meta = authAppUserService.queryLoginUserMeta();
        String openId = meta.getOpenId();
        PrepayResponse payResponse = handleWxOrder(req, outTradeNo, openId);
        //微信支付的外部订单号
        String prePayId = payResponse.getPrepayId();

        WxMiniPayResp resp = new WxMiniPayResp();

        String appId = wxPayV3PayConstants.getAppId();
        Long currentTime = DateTimeUtil.currenLongTime();
        String uniqueStr = SnowflakeIdWorker.uniqueMainId();
        String payPackage = "prepay_id="+prePayId;

        resp.setNonceStr(uniqueStr);
        resp.setPayPackage(payPackage);
        resp.setSignType("RSA");
        resp.setTimeStamp(currentTime);
        resp.setOutTradeNo(outTradeNo);
        //返回到前端的签名信息
        StringBuffer signBuffer = new StringBuffer();
        signBuffer.append(appId).append("\n").append(currentTime).append("\n").append(uniqueStr).append("\n").append(payPackage).append("\n");
        String needSignStr = signBuffer.toString();
        log.info(">>>>>>>>>>>>>>>>>微信签名参数 : {} <<<<<<<<<<<<<<",needSignStr);
        String sha256WithRsaSign = signByPcks8Sha256WithRsa(needSignStr, "utf-8");
        resp.setPaySign(sha256WithRsaSign);
        String respData = JSON.toJSONString(resp);
        log.info(">>>>>>>>>>>>>微信下单接口返回参数 : <<<<<<<<<<< {} ",respData);
        return resp;
    }

    /**
     *
     * @description: 返回微信私钥信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param
     * @return java.lang.String
     */
    public String wxPrivate(){
        String privateKeyRedisKey = BizConstants.WX_PAY_SIGN_PRIVATE_KEY_CACHE_PREFIX +
                wxPayV3PayConstants.getAppId();
        String privateKey = redisRepository.get(privateKeyRedisKey);
        if(CheckParam.isNull(privateKey)){
            ClassPathResource apiClientPrivateKeyResource = new ClassPathResource("wxV3PayCert/apiclient_key.pem");
            try {
                privateKey = BaseUtil.readFileContent(apiClientPrivateKeyResource.getFile()).replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+", "");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            redisRepository.set(privateKeyRedisKey,
                    privateKey,10L);
        }
        return privateKey;
    }

    /**
     * SHA256withRSA签名
     * 使用商户私钥对待签名串进行SHA256 with RSA签名，
     * 并对签名结果进行Base64编码得到签名值
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param content 内容
     * @param charset 编码
     * @return java.lang.String
     */
    public String signByPcks8Sha256WithRsa(String content, String charset){
        String privateKey = wxPrivate();
        try {
            PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(priPkcs8);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            redisRepository.set(privateKey,
                    privateKey,10L);
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>SHA256withRSA签名失败 : {} , {} <<<<<<<<<<<<<<<<<<",e.getMessage(),e);
            //签名失败
            return null;
        }
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
        LocalDateTime plusMinutes = localDateTime.plusMinutes(BizConstants.WX_ORDER_EXPIRED);

        System.out.println(plusMinutes.format(RFC3339_FORMAT));
    }

    /**
     * 设置微信下单请求参数并且返回请求后的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param wxOrderReq 微信下单请求参数
     * @param outTradeNo 外部订单号
     * @param openId 支付用户的OpenId
     * @return cn.common.resp.biz.app.wxpay.WxV3PayResult
     */
    public PrepayResponse handleWxOrder(WxOrderReq wxOrderReq,
                                        String outTradeNo,
                                        String openId){
        //当前时间
        LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.now(), Clock.systemDefaultZone().getZone());
        //订单失效时间
        LocalDateTime timeExpire = currentTime.plusMinutes(BizConstants.WX_ORDER_EXPIRED);

        String description = wxOrderReq.getDescription();
        BigDecimal totalAmount = BigDecimalUtils.multiplyOneHundred(wxOrderReq.getTotalAmount());

        //组装微信下单请求
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(totalAmount.intValue());
        request.setAmount(amount);
        request.setAppid(wxBizConstants.getAppId());
        request.setMchid(wxPayV3PayConstants.getMchId());
        request.setDescription(description);
        request.setNotifyUrl(wxPayV3PayConstants.getNotifyUrl());
        request.setOutTradeNo(outTradeNo);
        request.setTimeExpire(timeExpire.format(RFC3339_FORMAT));
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        PrepayResponse v3PayResp;
        log.info(">>>>>>>>>>>>>>>>>微信jsApiService下单请求Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(request));
        try{
            v3PayResp = jsapiService.prepay(request);
            log.info(">>>>>>>>>>>>>>>>>微信jsApiService下单请求Resp {} <<<<<<<<<<<<<<<<", JSON.toJSONString(v3PayResp));
            return v3PayResp;
        }catch (Exception e){
            log.error(">>>>>>>>>>>>>>>>>>>>>微信jsApiService下单失败:{},{}<<<<<<<<<<<<<<<<<<<<<<",
                    e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "下单失败");
        }
    }

    /**
     *
     * @description: 根据外部支付订单号查询微信订单状态
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     * @param outTradeNo 外部支付订单号
     * @return
     */
    public Transaction.TradeStateEnum queryOrder(String outTradeNo){
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wxPayV3PayConstants.getMchId());
        request.setOutTradeNo(outTradeNo);
        log.info(">>>>>>>>>>>>>>>>>微信jsApiService查询订单请求Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(request));
        Transaction transaction = jsapiService.queryOrderByOutTradeNo(request);
        log.info(">>>>>>>>>>>>>>>>>微信jsApiService查询订单请求Resp {} <<<<<<<<<<<<<<<<", JSON.toJSONString(transaction));
        if(CheckParam.isNull(transaction)){
            return Transaction.TradeStateEnum.CLOSED;
        }
        return transaction.getTradeState();
    }
}
