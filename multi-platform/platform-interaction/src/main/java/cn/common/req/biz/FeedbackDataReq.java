package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 投诉建议信息分页查询请求封装类
 * @title: FeedbackDataReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class FeedbackDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 870026873249475265L;

    /**
     * 业务主键ID->"feedbackDataId"
     */
    private String feedbackDataId;

    /**
     * 内容标题
     */
    private String dataTitle;

    /**
     * 内容
     */
    private String dataValue;

    /**
     * 提交人ID
     */
    private String submitterId;

    /**
     * 处理状态
     */
    private Integer handleStatus;

    /**
     * 备注
     */
    private String remarkData;

}
