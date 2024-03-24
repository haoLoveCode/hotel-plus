package cn.common.resp.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;
import pro.skywalking.constants.PlatformConstant;
import pro.skywalking.validation.NotEmpty;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间预订信息数据导出请求封装类
 * @title: RoomBookingExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class RoomBookingExportResp implements Serializable {

    private static final long serialVersionUID = 250790028757424577L;

    /**
     * 预定人
     */
    @NotEmpty(message = "预定人->不可为空")
    @ExcelProperty(value = "预定人")
    private String subscriberId;

    /**
     * 预订单号
     */
    @NotEmpty(message = "预订单号->不可为空")
    @ExcelProperty(value = "预订单号")
    private String bookingNo;

    /**
     * 房间ID
     */
    @NotEmpty(message = "房间ID->不可为空")
    @ExcelProperty(value = "房间ID")
    private String roomDataId;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    @ExcelProperty(value = "备注信息")
    private String remark;

    /**
     * 预订时间
     */
    @NotEmpty(message = "预订时间->不可为空")
    @ExcelProperty(value = "预订时间")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime bookingTime;

    /**
     * 入住开始时间
     */
    @NotEmpty(message = "入住开始时间->不可为空")
    @ExcelProperty(value = "入住开始时间")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInBegin;

    /**
     * 入住结束时间
     */
    @NotEmpty(message = "入住结束时间->不可为空")
    @ExcelProperty(value = "入住结束时间")
    @JSONField(format = PlatformConstant.TIME_SECONDS_FORMAT)
    @DateTimeFormat(pattern = PlatformConstant.TIME_SECONDS_FORMAT)
    private LocalDateTime checkInEnd;

    /**
     * 预定状态
     */
    @NotEmpty(message = "预定状态->不可为空")
    @ExcelProperty(value = "预定状态")
    private Integer bookingStatus;

}
