package cn.common.utils.interaction;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 腾讯IP位置定位数据
 * @date 2024-03-25
 */
@Data
public class IpPlaceData implements Serializable {

    private static final long serialVersionUID = 4434607260197736724L;

    /**
     * IP地址数据信息
     */
    @JSONField(name = "ad_info")
    TcIpAddressData addressData;

    /**
     * 位置信息
     */
    @JSONField(name = "location")
    TcLocation location;

}
