package cn.common.repository.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;

import java.io.Serializable;


/**
 * 公告信息实体
 * @title: NoticeData.java
 * @author Singer
 * @date 2024-03-25 11:13
 */
@Data
@TableName("notice_data")
public class NoticeData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1743102480388755601L;

    /**
     * 业务主键ID
     */
    private String noticeDataId;

    /**
     * 标题
     */
    private String dataTitle;

    /**
     * 内容
     */
    private String dataValue;

    /**
     * 备注信息
     */
    private String remarkData;

    /**
     * 是否上架
     */
    private Integer dataStatus;

}
