package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客房退房信息请求数据封装类
 * @title: RoomCheckOutUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomCheckOutUpdateReq implements Serializable {

    private static final long serialVersionUID = 696719846420005421L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"roomCheckOutId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String roomCheckOutId;

    /**
     * 预定订单ID
     */
    @NotEmpty(message = "预定订单ID->不可为空")
    private String roomBookingId;

    /**
     * 客户身份信息ID
     */
    @NotEmpty(message = "客户身份信息ID->不可为空")
    private String guestIdentifyId;

    /**
     * 退房时间
     */
    @NotEmpty(message = "退房时间->不可为空")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkOutTime;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remark;

}
