package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 收货地址信息实体
 * @title: TakeAddress.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
@TableName("take_address")
public class TakeAddress extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -796419110300285561L;

    /**
     * 业务主键ID
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
    @TableField(jdbcType = JdbcType.VARCHAR)
    private String addressDetailed;

    /**
     * 是否默认 1:是 2:否
     */
    private Integer defaultStatus;

}
