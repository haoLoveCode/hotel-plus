package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间类型信息返回数据封装类
 * @title: RoomTypeResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomTypeResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = -567592202761600311L;

    /**
     * 业务主键ID->"roomTypeId"
     */
    private String roomTypeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 备注信息
     */
    private String remarkData;

}
