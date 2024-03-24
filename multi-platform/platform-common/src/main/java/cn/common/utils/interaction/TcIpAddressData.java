package cn.common.utils.interaction;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 腾讯IP详细地址数据
 * @date 2024-03-06
 */
@Data
public class TcIpAddressData implements Serializable {

    private static final long serialVersionUID = 1322498942296808715L;


    private String nation;

    private String province;

    private String city;

    private String district;

    private long adcode;

    @JSONField(name = "nation_code")
    private int nationCode;

}
