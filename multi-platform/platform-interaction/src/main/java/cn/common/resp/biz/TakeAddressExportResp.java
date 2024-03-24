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
 * 收货地址信息数据导出请求封装类
 * @title: TakeAddressExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class TakeAddressExportResp implements Serializable {

    private static final long serialVersionUID = -609607458701200902L;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID->不可为空")
    @ExcelProperty(value = "用户ID")
    private String authAppUserId;

    /**
     * 收货人姓名
     */
    @NotEmpty(message = "收货人姓名->不可为空")
    @ExcelProperty(value = "收货人姓名")
    private String takerName;

    /**
     * 收货人手机号
     */
    @NotEmpty(message = "收货人手机号->不可为空")
    @ExcelProperty(value = "收货人手机号")
    private String takerPhone;

    /**
     * 省份名称
     */
    @NotEmpty(message = "省份名称->不可为空")
    @ExcelProperty(value = "省份名称")
    private String provinceName;

    /**
     * 城市名称
     */
    @NotEmpty(message = "城市名称->不可为空")
    @ExcelProperty(value = "城市名称")
    private String cityName;

    /**
     * 区名称
     */
    @NotEmpty(message = "区名称->不可为空")
    @ExcelProperty(value = "区名称")
    private String districtName;

    /**
     * 邮编
     */
    @NotEmpty(message = "邮编->不可为空")
    @ExcelProperty(value = "邮编")
    private String postCode;

    /**
     * 地点经度
     */
    @NotEmpty(message = "地点经度->不可为空")
    @ExcelProperty(value = "地点经度")
    private String placeLongitude;

    /**
     * 地点纬度
     */
    @NotEmpty(message = "地点纬度->不可为空")
    @ExcelProperty(value = "地点纬度")
    private String placeLatitude;

    /**
     * 详细地址
     */
    @NotEmpty(message = "详细地址->不可为空")
    @ExcelProperty(value = "详细地址")
    private String addressDetailed;

    /**
     * 是否默认 1:是 2:否
     */
    @NotEmpty(message = "是否默认 1:是 2:否->不可为空")
    @ExcelProperty(value = "是否默认 1:是 2:否")
    private Integer defaultStatus;

}
