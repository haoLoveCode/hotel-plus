package cn.common.req.biz.openBiz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 公告信息请求数据封装类
 * @title: NoticeDataUpdateReq.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
public class NoticeDataUpdateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"noticeDataId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String noticeDataId;

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
