package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 收货地址信息返回数据封装类
 * @title: TakeAddressResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TakeAddressResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 281807598877817737L;

    /**
     * 业务主键ID->"takeAddressId"
     */
    private String takeAddressId;

    /**
     * 用户ID
     */
    private String authAppUserId;

    /**
     * 收货人姓名
     */
    private String takerName;

    /**
     * 收货人手机号
     */
    private String takerPhone;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区名称
     */
    private String districtName;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 地点经度
     */
    private String placeLongitude;

    /**
     * 地点纬度
     */
    private String placeLatitude;

    /**
     * 详细地址
     */
    private String addressDetailed;

    /**
     * 是否默认 1:是 2:否
     */
    private Integer defaultStatus;

}
