package cn.common.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 已经上传了的文件信息
 * @date 2024-03-25
 */
@Data
public class AppUploadFile implements Serializable {

    private static final long serialVersionUID = -8650062849194126557L;

    private String status;

    private String message;

    private String url;

    private String fileName;

}
