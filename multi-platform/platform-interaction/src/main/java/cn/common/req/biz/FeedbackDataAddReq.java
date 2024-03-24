package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.validation.NotEmpty;

import java.io.Serializable;

/**
 * 投诉建议信息新增Req
 * @title: FeedbackDataAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class FeedbackDataAddReq implements Serializable {

    private static final long serialVersionUID = -807009305190907522L;

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
