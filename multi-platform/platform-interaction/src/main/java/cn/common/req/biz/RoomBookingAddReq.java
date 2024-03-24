package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间预订信息新增Req
 * @title: RoomBookingAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomBookingAddReq implements Serializable {

    private static final long serialVersionUID = 194646303874639134L;

    /**
     * 预定人
     */
    @NotEmpty(message = "预定人->不可为空")
    private String subscriberId;

    /**
     * 预订单号
     */
    @NotEmpty(message = "预订单号->不可为空")
    private String bookingNo;

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
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remark;

    /**
     * 预订时间
     */
    @NotEmpty(message = "预订时间->不可为空")
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    @NotEmpty(message = "入住开始时间->不可为空")
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @NotEmpty(message = "入住结束时间->不可为空")
    @JSONField(format = PlatformConstant.TIME_HOUR_TO_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_HOUR_TO_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    @NotEmpty(message = "预定状态->不可为空")
    private Integer bookingStatus;

}
