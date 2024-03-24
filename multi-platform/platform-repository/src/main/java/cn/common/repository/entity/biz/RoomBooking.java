package cn.common.repository.entity.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 房间预订信息实体
 * @title: RoomBooking.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("room_booking")
public class RoomBooking extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 138224710581969146L;

    /**
     * 业务主键ID
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
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @JSONField(format = PlatformConstant.TIME_HOUR_TO_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_HOUR_TO_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    private Integer bookingStatus;

}
