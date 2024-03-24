package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 收货地址信息分页查询请求封装类
 * @title: TakeAddressReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class TakeAddressReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -192696769625417843L;

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
