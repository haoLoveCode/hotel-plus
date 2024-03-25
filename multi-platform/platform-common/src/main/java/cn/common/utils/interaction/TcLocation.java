package cn.common.utils.interaction;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName animal-plus
 * @Description: 位置
 * @date 2024-03-25
 */
@Data
public class TcLocation implements Serializable {
    private static final long serialVersionUID = -6764017922763125770L;

    private String lat;

    private String lng;
}
