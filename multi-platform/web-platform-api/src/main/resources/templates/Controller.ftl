package ${basePackageName}.api.biz;

import ${basePackageName}.api.BaseApiController;
import ${basePackageName}.req.biz.${className}AddReq;
import ${basePackageName}.req.biz.${className}Req;
import ${basePackageName}.req.biz.${className}UpdateReq;
import ${basePackageName}.resp.biz.${className}Resp;
import ${basePackageName}.service.biz.platform.${className}Service;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author Singer
 * @packageName ${basePackageName}.api.controller
 * @Description: ${entityDesc}前端控制器
 * @date 2024/1/25
 */
@RestController
@RequestMapping(value = "api/v1/${camelCaseEntityName}")
@Slf4j
public class ${className}Controller extends BaseApiController {

    @Resource
    private ${className}Service ${camelCaseEntityName}Service;

    /**
     * 新增${entityDesc}
     * @author: Singer
     * @date 2024/1/25
     * @param addReq 新增${entityDesc}Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增${entityDesc}信息")
    @NeedLogin()
    public ApiResponse addItem(@RequestBody @Valid ${className}AddReq addReq){
        ${camelCaseEntityName}Service.addItem(addReq);
        return apiResponse();
    }

    /**
     * 导出${entityDesc}数据
     * @author: Singer
     * @date 2024/1/25
     * @param pageReq
     * @return java.util.List
    */
    @PostMapping(value = "/exportData")
    @ApiLog(value = "${entityDesc}数据导出")
    @NeedLogin()
    public void exportData(@RequestBody(required = false) @Valid ${className}Req pageReq){
        ${camelCaseEntityName}Service.exportData(pageReq);
    }

    /**
     * 主键ID集合批量删除${entityDesc}
     * @author: Singer
     * @date 2024/1/25
     * @param req 需要被删除的${entityDesc}信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除${entityDesc}")
    @NeedLogin()
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        ${camelCaseEntityName}Service.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新${entityDesc}
     * @author: Singer
     * @date 2024/1/25
     * @param updateReq 更新${entityDesc}请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新${entityDesc}信息")
    @NeedLogin()
    public ApiResponse updateItem(@RequestBody @Valid ${className}UpdateReq updateReq){
        ${camelCaseEntityName}Service.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 查询${entityDesc}信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/query${className}")
    @ApiLog(value = "查询${entityDesc}信息")
    @NeedLogin()
    public ApiResponse<List<${className}Resp>> query${className}(
        @RequestBody @Valid ${className}Req req){
        return apiResponse(${camelCaseEntityName}Service.query${className}(req));
    }

    /**
     * 查询单个${entityDesc}信息
     * @author: Singer
     * @date 2024/1/25
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOne${className}")
    @ApiLog(value = "查询${entityDesc}信息")
    @NeedLogin()
    public ApiResponse<${className}Resp> queryOne${className}(
        @RequestBody @Valid ${className}Req req){
        return apiResponse(${camelCaseEntityName}Service.queryOne${className}(req));
    }

    /**
     * 分页查询${entityDesc}
     * @author: Singer
     * @date 2024/1/25
     * @param  pageReq 分页查询${entityDesc}Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询${entityDesc}信息")
    @NeedLogin()
    public ApiResponse<#noparse><Pagination<</#noparse>${className}<#noparse>Resp>></#noparse> queryByPage(
        @RequestBody @Valid ${className}Req pageReq){
        return apiResponse(${camelCaseEntityName}Service.queryByPage(pageReq));
    }
}
