package cn.common.req.biz.openBiz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 公告信息分页查询请求封装类
 * @title: NoticeDataReq.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class NoticeDataReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务主键ID->"noticeDataId"
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
