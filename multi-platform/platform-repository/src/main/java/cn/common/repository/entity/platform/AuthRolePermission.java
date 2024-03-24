package cn.common.repository.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import pro.skywalking.entity.BaseEntity;
import pro.skywalking.utils.SnowflakeIdWorker;

import java.io.Serializable;


/**
* 角色权限实体
* @title: AuthRolePermission.java
* @author create by Singer - Singer email:singer-coder@qq.com
* @date 2024-03-06 11:13
*/
@Data
@TableName("auth_role_permission")
public class AuthRolePermission extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5867103270447284159L;

    /**
     * 业务主键ID
     */
    private String authRolePermissionId = SnowflakeIdWorker.uniqueMainId();

    /**
     * 角色ID
     */
    private String authRoleId;

    /**
     * 权限ID
     */
    private String authPermissionId;

}
