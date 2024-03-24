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
 * 订单-收货地址关联信息数据导出请求封装类
 * @title: OrderAddressExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class OrderAddressExportResp implements Serializable {

    private static final long serialVersionUID = -458210925976888379L;

    /**
     * 关联的订单ID
     */
    @NotEmpty(message = "关联的订单ID->不可为空")
    @ExcelProperty(value = "关联的订单ID")
    private String orderId;

    /**
     * APP用户ID
     */
    @NotEmpty(message = "APP用户ID->不可为空")
    @ExcelProperty(value = "APP用户ID")
    private String authAppUserId;

    /**
     * 收货地址ID
     */
    @NotEmpty(message = "收货地址ID->不可为空")
    @ExcelProperty(value = "收货地址ID")
    private String takeAddressId;

}
