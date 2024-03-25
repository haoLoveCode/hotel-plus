package cn.common.repository.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;
import pro.skywalking.utils.SnowflakeIdWorker;

import java.io.Serializable;


/**
* 系统日志实体
* @title: PlatformApiLog.java
* @author create by Singer - Singer email:singer-coder@qq.com
* @date 2024-03-25 11:13
*/
@Data
@TableName("platform_api_log")
public class PlatformApiLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4006243363645794849L;

    /**
     * 业务主键ID
     */
    private String platformApiLogId = SnowflakeIdWorker.uniqueMainId();

    /**
     * 操作者用户名
     */
    private String userName;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 耗费时间
     */
    private String operationTime;

    /**
     * 操作方法
     */
    private String method;

    /**
     * 方法参数
     */
    private String params;

    /**
     * 操作地点
     */
    private String location;

    /**
     * IP地址
     */
    private String requestIp;

}
