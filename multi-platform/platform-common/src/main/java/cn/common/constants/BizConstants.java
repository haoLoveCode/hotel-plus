package cn.common.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 业务常量
 * @date 2024-03-25
 */
@Data
@Component
public class BizConstants {

    /**
     * 微信支付私钥缓存前缀
     */
    public static final String WX_PAY_SIGN_PRIVATE_KEY_CACHE_PREFIX = "wx_pay_sign_private_key_cache_prefix:";

    /**
     * 微信AccessToken信息缓存前缀
     */
    public static final String WX_ACCESS_TOKEN_CACHE_PREFIX = "wx_access_token_cache_prefix:";

    /**
     * 微信二维码信息缓存前缀
     */
    public static final String WX_QR_CODE_CACHE_PREFIX = "WX_QR_CODE_CACHE_PREFIX:";

    /**
     * 时间和分钟格式
     */
    public static final String DAY_MINUTE_FORMAT = "MM-dd HH:mm";

    /**
     * APP默认用户名
     */
    public static final String APP_DEFAULT_USER_NAME = "用户-";

    /**
     * APP默认密码
     */
    public static final String APP_DEFAULT_USER_PASSWORD = "123456";

    /**
     * 平台订单缓存前缀
     */
    public static final String PLATFORM_ORDER_PREFIX = "platform_order_prefix:";

    /**
     * 微信订单失效时间(分钟)
     */
    public static final Integer WX_ORDER_EXPIRED = 30;

    /**
     * 绑定微信OpenId的锁
     */
    public static final String BIND_OPEN_ID_LOCK = "BIND_OPEN_ID_LOCK";

    /**
     * 系统业务配置缓存前缀
     */
    public static final String BIZ_CONSTANTS_CACHE_PREFIX = "biz_constants_cache_prefix";

}
