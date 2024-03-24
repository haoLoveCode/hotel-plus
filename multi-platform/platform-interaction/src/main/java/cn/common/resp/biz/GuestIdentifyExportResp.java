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
 * 客户身份信息数据导出请求封装类
 * @title: GuestIdentifyExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class GuestIdentifyExportResp implements Serializable {

    private static final long serialVersionUID = 990316681223617135L;

    /**
     * 姓名
     */
    @NotEmpty(message = "姓名->不可为空")
    @ExcelProperty(value = "姓名")
    private String realName;

    /**
     * 身份证号
     */
    @NotEmpty(message = "身份证号->不可为空")
    @ExcelProperty(value = "身份证号")
    private String identifyNo;

    /**
     * 性别
     */
    @NotEmpty(message = "性别->不可为空")
    @ExcelProperty(value = "性别")
    private Integer gender;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    @ExcelProperty(value = "备注信息")
    private String remark;

}
