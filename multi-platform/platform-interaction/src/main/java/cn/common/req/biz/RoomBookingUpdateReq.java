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
 * 房间预订信息请求数据封装类
 * @title: RoomBookingUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomBookingUpdateReq implements Serializable {

    private static final long serialVersionUID = -214111905236886723L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"roomBookingId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String roomBookingId;

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
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    @NotEmpty(message = "入住开始时间->不可为空")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @NotEmpty(message = "入住结束时间->不可为空")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    @NotEmpty(message = "预定状态->不可为空")
    private Integer bookingStatus;

}
