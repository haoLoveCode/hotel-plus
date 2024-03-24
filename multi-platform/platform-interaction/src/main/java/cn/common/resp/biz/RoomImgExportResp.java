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
 * 房间图片信息数据导出请求封装类
 * @title: RoomImgExportResp.java
 * @author Singer
 * @date
*/
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class RoomImgExportResp implements Serializable {

    private static final long serialVersionUID = 914099224524618912L;

    /**
     * 房间信息ID
     */
    @NotEmpty(message = "房间信息ID->不可为空")
    @ExcelProperty(value = "房间信息ID")
    private String roomDataId;

    /**
     * 访问地址
     */
    @NotEmpty(message = "访问地址->不可为空")
    @ExcelProperty(value = "访问地址")
    private String imgUrl;

}
