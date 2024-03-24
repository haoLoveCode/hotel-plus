package cn.common.service.platform;

import org.springframework.web.multipart.MultipartFile;
import pro.skywalking.configuration.oss.meta.MultipartFileUrlMeta;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 平台文件上传Service
 * @date 2024-03-06
 */
public interface PlatformFileService {

    /**
     * 上传文件
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param file
     * @return pro.skywalking.req.platform.upload.MultipartFileUrlResp
     */
    MultipartFileUrlMeta uploadFile(MultipartFile file);

    /**
     * 上传文件
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param file
     * @return pro.skywalking.req.platform.upload.MultipartFileUrlResp
     */
    MultipartFileUrlMeta uploadFileMixed(MultipartFile file);
}
