package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投诉建议信息请求数据封装类
 * @title: FeedbackDataUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class FeedbackDataUpdateReq implements Serializable {

    private static final long serialVersionUID = -562439535332655138L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"feedbackDataId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String feedbackDataId;

    /**
     * 内容标题
     */
    @NotEmpty(message = "内容标题->不可为空")
    private String dataTitle;

    /**
     * 内容
     */
    @NotEmpty(message = "内容->不可为空")
    private String dataValue;

    /**
     * 提交人ID
     */
    @NotEmpty(message = "提交人ID->不可为空")
    private String submitterId;

    /**
     * 处理状态
     */
    @NotEmpty(message = "处理状态->不可为空")
    private Integer handleStatus;

    /**
     * 备注
     */
    @NotEmpty(message = "备注->不可为空")
    private String remarkData;

}
