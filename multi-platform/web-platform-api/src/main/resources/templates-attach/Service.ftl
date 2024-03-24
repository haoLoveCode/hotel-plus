package ${basePackageName}.service.biz.platform;


import ${basePackageName}.req.biz.${className}AddReq;
import ${basePackageName}.req.biz.${className}Req;
import ${basePackageName}.req.biz.${className}UpdateReq;
import ${basePackageName}.resp.biz.${className}Resp;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.page.Pagination;
import java.util.List;

/**
 * @author Singer
 * @packageName ${basePackageName}.service
 * @Description: ${entityDesc}相关服务
 * @date 2024-03-04
 */
public interface ${className}Service {

    /**
     * 导入${entityDesc}数据
     * @author: Singer
     * @date 2024-03-04
     * @param file 文件
     * @return
     */
    void importData(MultipartFile file);

    /**
     * 导出${entityDesc}数据
     * @author: Singer
     * @date 2024-03-04
     * @param pageReq
     * @return java.util.List
     */
    void exportData(${className}Req pageReq);

    /**
     * 新增${entityDesc}
     * @author: Singer
     * @date 2024-03-04
     * @param addReq 新增Req
     */
    void addItem(${className}AddReq addReq);

    /**
     * 批量删除${entityDesc}信息
     * @author: Singer
     * @date 2024-03-04
     * @param req 需要被删除的${entityDesc}信息
     */
    void batchDeleteItem(BaseDeleteReq req);

    /**
     * 查询${entityDesc}信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    List<${className}Resp> query${className}(${className}Req req);

    /**
     * 查询单个${entityDesc}信息
     * @author: Singer
     * @date 2024-03-04
     * @param
     * @return java.util.List
     */
    ${className}Resp queryOne${className}(${className}Req req);

    /**
     * 分页查询${entityDesc}
     * @author: Singer
     * @date 2024-03-04
     * @param  pageReq 分页查询${entityDesc}Req
     * @return Pagination
     */
    Pagination<${className}Resp> queryByPage(
        ${className}Req pageReq);

    /**
     * 更新${entityDesc}
     * @author: Singer
     * @date 2024-03-04
     * @param updateReq 更新${entityDesc}请求参数
     */
    void updateItem(${className}UpdateReq updateReq);

}
