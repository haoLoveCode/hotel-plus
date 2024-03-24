package cn.common.req.biz;

import pro.skywalking.validation.NotEmpty;
import lombok.Data;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间图片信息请求数据封装类
 * @title: RoomImgUpdateReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomImgUpdateReq implements Serializable {

    private static final long serialVersionUID = -392399764284130916L;

    /**
     * 主键ID->ID
     */
    private Long id;

    /**
     * 业务主键ID->"roomImgId"
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String roomImgId;

    /**
     * 业务主键ID
     */
    @NotEmpty(message = "业务主键ID->不可为空")
    private String salesItemImgId;

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
