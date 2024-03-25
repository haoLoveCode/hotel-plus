package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间预订信息新增Req
 * @title: BookingRoomReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class BookingRoomReq implements Serializable {

    private static final long serialVersionUID = -7616027920767734097L;

    /**
     * 预定人
     */
    @NotEmpty(message = "预定人->不可为空")
    private String subscriberId;

    /**
     * 入住天数
     */
    @NotEmpty(message = "入住天数->不可为空")
    private Integer itemNum;

    /**
     * 总金额
     */
    @NotEmpty(message = "总金额->不可为空")
    private BigDecimal totalAmount;

    /**
     * 房间ID
     */
    @NotEmpty(message = "房间ID->不可为空")
    private String roomDataId;

    /**
     * 备注信息
     */
    //@NotEmpty(message = "备注信息->不可为空")
    private String remark;

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



}
