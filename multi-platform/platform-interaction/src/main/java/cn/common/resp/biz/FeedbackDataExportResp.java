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
 * 投诉建议信息数据导出请求封装类
 * @title: FeedbackDataExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FeedbackDataExportResp implements Serializable {

    private static final long serialVersionUID = 880464154156524596L;

    /**
     * 内容标题
     */
    @NotEmpty(message = "内容标题->不可为空")
    @ExcelProperty(value = "内容标题")
    private String dataTitle;

    /**
     * 内容
     */
    @NotEmpty(message = "内容->不可为空")
    @ExcelProperty(value = "内容")
    private String dataValue;

    /**
     * 提交人ID
     */
    @NotEmpty(message = "提交人ID->不可为空")
    @ExcelProperty(value = "提交人ID")
    private String submitterId;

    /**
     * 处理状态
     */
    @NotEmpty(message = "处理状态->不可为空")
    @ExcelProperty(value = "处理状态")
    private Integer handleStatus;

    /**
     * 备注
     */
    @NotEmpty(message = "备注->不可为空")
    @ExcelProperty(value = "备注")
    private String remarkData;

}
