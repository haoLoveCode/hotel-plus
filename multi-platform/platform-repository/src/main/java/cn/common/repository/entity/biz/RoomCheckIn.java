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
 * 客房入住信息实体
 * @title: RoomCheckIn.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("room_check_in")
public class RoomCheckIn extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 850535287469773109L;

    /**
     * 业务主键ID
     */
    private String roomCheckInId;

    /**
     * 预定订单ID
     */
    private String roomBookingId;

    /**
     * 客户身份信息ID
     */
    private String guestIdentifyId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 入住时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInTime;

}
