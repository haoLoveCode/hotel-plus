package cn.common.req.biz;

import lombok.Data;
import pro.skywalking.req.base.BasePageReq;
import java.io.Serializable;

/**
 * 房间类型信息分页查询请求封装类
 * @title: RoomTypeReq.java
 * @author Singer
 * @date 2024-02-29 11:13
 */
@Data
public class RoomTypeReq extends BasePageReq implements Serializable {

    private static final long serialVersionUID = -158536351714116835L;

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
