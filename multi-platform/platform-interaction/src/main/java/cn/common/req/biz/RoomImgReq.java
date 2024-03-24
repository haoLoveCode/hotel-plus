package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 房间图片信息分页查询请求封装类
 * @title: RoomImgReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomImgReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = 504116201272580679L;

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
