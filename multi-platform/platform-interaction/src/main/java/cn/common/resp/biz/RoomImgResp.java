package cn.common.resp.biz;

import pro.skywalking.req.platform.base.BaseResp;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.*;

/**
 * 房间图片信息返回数据封装类
 * @title: RoomImgResp.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomImgResp extends BaseResp implements Serializable {

    private static final long serialVersionUID = 475993637102820150L;

    /**
     * 业务主键ID->"roomImgId"
     */
    private String roomImgId;

    /**
     * 房间信息ID
     */
    private String roomDataId;

    /**
     * 访问地址
     */
    private String imgUrl;

}
