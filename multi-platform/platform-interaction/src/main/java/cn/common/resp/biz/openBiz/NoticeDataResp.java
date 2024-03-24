package cn.common.resp.biz.openBiz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 公告信息返回数据封装类
 * @title: NoticeDataResp.java
 * @author Singer
 * @date 2024-03-06 11:13
 */
@Data
public class NoticeDataResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = -9001776897869529762L;

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
