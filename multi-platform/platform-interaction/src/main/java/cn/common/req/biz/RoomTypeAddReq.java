package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间类型信息新增Req
 * @title: RoomTypeAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomTypeAddReq implements Serializable {

    private static final long serialVersionUID = 450429122829580108L;

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称->不可为空")
    private String typeName;

    /**
     * 备注信息
     */
    @NotEmpty(message = "备注信息->不可为空")
    private String remarkData;

}
