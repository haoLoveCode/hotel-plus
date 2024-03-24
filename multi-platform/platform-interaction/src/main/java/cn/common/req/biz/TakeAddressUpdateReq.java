package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收货地址信息请求数据封装类
 * @title: TakeAddressUpdateReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TakeAddressUpdateReq implements Serializable {

    private static final long serialVersionUID = -203362507432228683L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"takeAddressId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String takeAddressId;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID->不可为空")
    private String authAppUserId;

    /**
     * 收货人姓名
     */
    @NotEmpty(message = "收货人姓名->不可为空")
    private String takerName;

    /**
     * 收货人手机号
     */
    @NotEmpty(message = "收货人手机号->不可为空")
    private String takerPhone;

    /**
     * 省份名称
     */
    @NotEmpty(message = "省份名称->不可为空")
    private String provinceName;

    /**
     * 城市名称
     */
    @NotEmpty(message = "城市名称->不可为空")
    private String cityName;

    /**
     * 区名称
     */
    @NotEmpty(message = "区名称->不可为空")
    private String districtName;

    /**
     * 邮编
     */
    @NotEmpty(message = "邮编->不可为空")
    private String postCode;

    /**
     * 地点经度
     */
    @NotEmpty(message = "地点经度->不可为空")
    private String placeLongitude;

    /**
     * 地点纬度
     */
    @NotEmpty(message = "地点纬度->不可为空")
    private String placeLatitude;

    /**
     * 详细地址
     */
    @NotEmpty(message = "详细地址->不可为空")
    private String addressDetailed;

    /**
     * 是否默认 1:是 2:否
     */
    @NotEmpty(message = "是否默认 1:是 2:否->不可为空")
    private Integer defaultStatus;

}
