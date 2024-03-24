package cn.common.resp.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间预订信息返回数据封装类
 * @title: RoomBookingResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomBookingResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 841455077763730932L;

    /**
     * 业务主键ID->"roomBookingId"
     */
    private String roomBookingId;

    /**
     * 预定人
     */
    private String subscriberId;

    /**
     * 预订单号
     */
    private String bookingNo;

    /**
     * 房间ID
     */
    private String roomDataId;

    /**
     * 房间编号
     */
    private String roomNo;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 预订时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    private Integer bookingStatus;

}
