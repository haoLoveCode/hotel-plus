package cn.common.service.impl.biz.app;

import cn.common.constants.BizConstants;
import cn.common.req.biz.app.wx.WxBaseReq;
import cn.common.req.biz.app.wx.WxPageQrCode;
import cn.common.resp.biz.app.auth.WxAccessToken;
import cn.common.resp.biz.app.wx.WxPhoneResp;
import cn.common.service.biz.WxService;
import cn.common.wx.WxApiConstants;
import cn.common.wx.WxBizConstants;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.url.UrlBuilder;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.skywalking.configuration.oss.ObjectStorage;
import pro.skywalking.configuration.redis.RedisRepository;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.HttpTookit;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.zmi.common.utils.wx
 * @Description: 微信相关工具
 * @date 2024-03-25
 */
@Component
@Slf4j
public class WxServiceImpl implements WxService {

    @Resource
    private WxBizConstants wxBizConstants;

    @Resource
    private WxApiConstants wxApiConstants;


    @Resource
    private RedisRepository redisRepository;


     /**
       * 拿到微信用户的手机号
       * @description:
       * @author: create by singer - Singer email:singer-coder@qq.com
       * @date 2024-03-25
       * @param getPhoneCode getPhoneNumber返回的code
       * @param weChatAccessToken  weChatAccessToken
       * @return
       */
    @Override
    public WxPhoneResp wxChatPhone(String getPhoneCode, String weChatAccessToken){
        Map<String, Object> paramHashMap = new HashMap(8);
        paramHashMap.put("code", getPhoneCode);
        Map<String, Object> urlParamMap = new HashMap<>(8);
        urlParamMap.put("access_token", weChatAccessToken);
        String result = null;
        try {
            result = HttpTookit.postBodyRequest(
                    wxApiConstants.getUserPhoneApi(),
                    urlParamMap,
                    paramHashMap,
                    new HashMap<>());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "拿到用户手机号失败");
        }
        if(CheckParam.isNull(result)){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "拿到用户手机号失败");
        }
        WxPhoneResp wxPhoneResp = null;
        try{
            wxPhoneResp = JSON.parseObject(result, WxPhoneResp.class);
            return wxPhoneResp;
        }catch (Exception e){
            log.error("拿到用户手机号失败+"+e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(), "拿到用户手机号失败");
        }
    }

    /**
      * 生成微信页面二维码
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-25
      * @param wxPageQrCode 微信页面二维码请求参数
      * @return
      */
    @Override
    public String wxPageQrCodeBase64(WxPageQrCode wxPageQrCode){

        log.info(">>>>>>>>>>>>微信生成页面二维码请求参数 : {} <<<<<<<<<<<", JSON.toJSON(wxPageQrCode));
        String itemId = wxPageQrCode.getItemId();
        String qrCodebase64 = redisRepository.get(BizConstants.WX_QR_CODE_CACHE_PREFIX+itemId);

        if(!CheckParam.isNull(qrCodebase64)){
            return qrCodebase64;
        }
        String buildUrl = UrlBuilder.ofHttp(wxApiConstants.getPageQrCodeUnLimit(), Charset.forName("utf-8"))
                .addQuery("access_token",weChatAccessToken(null))
                .build();
        //HttpPost httpPost = new HttpPost(buildUrl);
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, String> headersMap = Maps.newHashMap();

        headersMap.put("Content-Type","application/json");
        headersMap.put("Accept","*/*");
        headersMap.put("Accept-Encoding","gzip, deflate, br");
        headersMap.put("Connection","keep-alive");

        paramMap.put("scene",wxPageQrCode.getScene());
        paramMap.put("page",wxPageQrCode.getPage());

        try {
            byte[] bytes = HttpTookit.postBodyRequestBuffer(buildUrl, Maps.newHashMap(), paramMap, headersMap);

            StringBuffer resultBuffer = new StringBuffer();
            String result = Base64.encode(bytes);
            resultBuffer.append("data:image/jpeg;base64,").append(result);

            qrCodebase64 = resultBuffer.toString();

            redisRepository.set(BizConstants.WX_QR_CODE_CACHE_PREFIX+itemId,qrCodebase64);

            return resultBuffer.toString();
        } catch (IOException e) {
            log.error(">>>>>>>>>>微信生成页面二维码请求参数请求出错: {} , {}  <<<<<<<<<",e.getMessage(),e);
        }

        return "";
    }



    /**
     * 拿到微信AccessToken
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-255
     * @param wxBaseReq 拿到微信AccessToken的请求参数 可以传入空 主要是设置 client_credential
     * @return String
     */
    @Override
    public String weChatAccessToken(WxBaseReq wxBaseReq){
        log.info(">>>>>>>>>>>微信拿到access-token请求参数: {} <<<<<<<<<<<",JSON.toJSONString(wxBaseReq));
        String accessToken = redisRepository.get(BizConstants.WX_ACCESS_TOKEN_CACHE_PREFIX);
        if(!CheckParam.isNull(accessToken)){
            return accessToken;
        }
        Map<String, Object> paramHashMap = new HashMap(8);
        paramHashMap.put("appid", wxBizConstants.getAppId());
        paramHashMap.put("secret", wxBizConstants.getAppSecret());
        if(!CheckParam.isNull(wxBaseReq)){
            if(!CheckParam.isNull(wxBaseReq.getGrantType())){
                paramHashMap.put("grant_type", wxBaseReq.getGrantType());
            }else{
                paramHashMap.put("grant_type", "client_credential");
            }
        }else{
            paramHashMap.put("grant_type", "client_credential");
        }
        String result = HttpTookit.getRequest(wxApiConstants.getAuthAccessTokenApi(), paramHashMap, "UTF-8");
        log.info(">>>>>>>>>>>>>调用微信auth.code2Session接口直接返回 : {}<<<<<<<<<<<",result);
        if(CheckParam.isNull(result)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "拿到微信AccessToken出现异常");
        }
        WxAccessToken wxAccessToken = JSON.parseObject(result, WxAccessToken.class);
        log.info(">>>>>>>>>>>>>调用微信auth.code2Session接口返回 : {}<<<<<<<<<<<",JSON.toJSONString(wxAccessToken));
        accessToken = wxAccessToken.getAccessToken();
        redisRepository.set(BizConstants.WX_ACCESS_TOKEN_CACHE_PREFIX,accessToken,7200L);
        return accessToken;
    }


}
