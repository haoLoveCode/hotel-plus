package cn.common.resp.biz.openBiz;

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
 * 公告信息数据导出请求封装类
 * @title: NoticeDataExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class NoticeDataExportResp implements Serializable {

    private static final long serialVersionUID = 280938813047208410L;

    /**
     * 标题
     */
    @NotEmpty(message = "标题->不可为空")
    @ExcelProperty(value = "标题")
    private String dataTitle;

    /**
     * 内容
     */
    @NotEmpty(message = "内容->不可为空")
    @ExcelProperty(value = "内容")
    private String dataValue;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    @ExcelProperty(value = "备注信息")
    private String remarkData;

    /**
     * 是否上架
     */
    @NotEmpty(message = "是否上架->不可为空")
    @ExcelProperty(value = "是否上架")
    private Integer dataStatus;

}
