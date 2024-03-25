package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 投诉建议信息返回数据封装类
 * @title: FeedbackDataResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class FeedbackDataResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = -800101981631564038L;

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

    //--------------用户信息-start-------------------------

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 真实姓名
     */
    private String realName;


    //--------------用户信息-end-------------------------

}
