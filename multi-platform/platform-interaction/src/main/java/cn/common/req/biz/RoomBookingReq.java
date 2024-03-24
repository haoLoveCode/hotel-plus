package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 房间预订信息分页查询请求封装类
 * @title: RoomBookingReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomBookingReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 422835643661827730L;

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
