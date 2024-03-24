package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 投诉建议信息实体
 * @title: FeedbackData.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
@TableName("feedback_data")
public class FeedbackData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 271792562808862637L;

    /**
     * 业务主键ID
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
