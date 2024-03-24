package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间图片信息新增Req
 * @title: RoomImgAddReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomImgAddReq implements Serializable {

    private static final long serialVersionUID = 646896933124617154L;

    /**
     * 房间信息ID
     */
    @NotEmpty(message = "房间信息ID->不可为空")
    private String roomDataId;

    /**
     * 访问地址
     */
    @NotEmpty(message = "访问地址->不可为空")
    private String imgUrl;

}
