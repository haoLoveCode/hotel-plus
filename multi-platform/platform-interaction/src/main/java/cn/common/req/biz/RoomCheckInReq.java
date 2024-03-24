package cn.common.req.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客房入住信息分页查询请求封装类
 * @title: RoomCheckInReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomCheckInReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 853318025354090954L;

    /**
     * 业务主键ID->"roomCheckInId"
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
     * 入住时间
     */
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInTime;

}
