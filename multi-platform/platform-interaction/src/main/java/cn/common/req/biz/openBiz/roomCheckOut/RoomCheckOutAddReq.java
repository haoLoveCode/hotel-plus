package cn.common.req.biz.openBiz.roomCheckOut;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 客房退房信息新增Req
 * @title: RoomCheckOutAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomCheckOutAddReq implements Serializable {

    private static final long serialVersionUID = 742675139950396368L;

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
     * 房间ID
     */
    @NotEmpty(message = "房间ID->不可为空")
    private String roomDataId;

    /**
     * 房间编号
     */
    @NotEmpty(message = "房间编号->不可为空")
    private String roomNo;

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