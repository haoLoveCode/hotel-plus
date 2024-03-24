package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 公告信息新增Req
 * @title: NoticeDataAddReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class NoticeDataAddReq implements Serializable {

    private static final long serialVersionUID = -2282881020420968678L;

    /**
     * 标题
     */
    @NotEmpty(message = "标题->不可为空")
    private String dataTitle;

    /**
     * 内容
     */
    @NotEmpty(message = "内容->不可为空")
    private String dataValue;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remarkData;

    /**
     * 是否上架
     */
    @NotEmpty(message = "是否上架->不可为空")
    private Integer dataStatus;

}
