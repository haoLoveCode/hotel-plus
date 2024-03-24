package cn.common.resp.biz;

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
 * 房间信息数据导出请求封装类
 * @title: RoomDataExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class RoomDataExportResp implements Serializable {

    private static final long serialVersionUID = 181777771262158895L;

    /**
     * 房间展示标题
     */
    @NotEmpty(message = "房间展示标题->不可为空")
    @ExcelProperty(value = "房间展示标题")
    private String roomTitle;

    /**
     * 房间简介
     */
    @NotEmpty(message = "房间简介->不可为空")
    @ExcelProperty(value = "房间简介")
    private String briefData;

    /**
     * 房间编号
     */
    @NotEmpty(message = "房间编号->不可为空")
    @ExcelProperty(value = "房间编号")
    private String roomNo;

    /**
     * 房间图片
     */
    @NotEmpty(message = "房间图片->不可为空")
    @ExcelProperty(value = "房间图片")
    private String roomImg;

    /**
     * 房间状态
     */
    @NotEmpty(message = "房间状态->不可为空")
    @ExcelProperty(value = "房间状态")
    private Integer roomStatus;

    /**
     * 房间楼层
     */
    @NotEmpty(message = "房间楼层->不可为空")
    @ExcelProperty(value = "房间楼层")
    private Integer roomFloor;

    /**
     * 房间类型
     */
    @NotEmpty(message = "房间类型->不可为空")
    @ExcelProperty(value = "房间类型")
    private String roomType;

    /**
     * 价格
     */
    @NotEmpty(message = "价格->不可为空")
    @ExcelProperty(value = "价格")
    private BigDecimal unitPrice;

    /**
     * 房间面积
     */
    @NotEmpty(message = "房间面积->不可为空")
    @ExcelProperty(value = "房间面积")
    private BigDecimal roomArea;

    /**
     * 床位数量
     */
    @NotEmpty(message = "床位数量->不可为空")
    @ExcelProperty(value = "床位数量")
    private Integer bedNum;

}
