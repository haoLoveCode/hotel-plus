package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.PlatformDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.ApiPerm;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.dict.PlatformDictAddReq;
import pro.skywalking.req.platform.dict.PlatformDictReq;
import pro.skywalking.req.platform.dict.PlatformDictUpdateReq;
import pro.skywalking.resp.base.ApiResponse;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.dict.PlatformDictResp;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.api.controller
 * @Description: 前端控制器
 * @date 2024-03-04
 */
@RestController
@RequestMapping(value = "api/v1/platformDict")
@Slf4j
public class PlatformDictController extends BaseApiController {

    @Resource
    private PlatformDictService platformDictService;

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param addReq 新增Req
     */
    @PostMapping(value = "/addItem")
    @ApiLog(value = "新增信息")
    public ApiResponse addItem(@RequestBody @Valid PlatformDictAddReq addReq){
        platformDictService.addItem(addReq);
        return apiResponse();
    }

    /**
     * 主键ID集合批量
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param req 需要被删除的信息
     */
    @PostMapping(value = "/batchDeleteItem")
    @ApiLog(value = "根据主键ID集合批量删除")
    @ApiPerm(accessStatus = true)
    public ApiResponse batchDeleteItem(@RequestBody BaseDeleteReq req){
        platformDictService.batchDeleteItem(req);
        return apiResponse();
    }

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param updateReq 更新请求参数
     */
    @PutMapping(value = "/updateItem")
    @ApiLog(value = "更新信息")
    @ApiPerm(accessStatus = true)
    public ApiResponse updateItem(@RequestBody @Valid PlatformDictUpdateReq updateReq){
        platformDictService.updateItem(updateReq);
        return apiResponse();
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @PostMapping(value = "/queryByPage")
    @ApiLog(value = "分页查询信息")
    public ApiResponse<Pagination<PlatformDictResp>> queryByPage(
            @RequestBody @Valid PlatformDictReq pageReq){
        return apiResponse(platformDictService.queryByPage(pageReq));
    }

    /**
     * 根据字典类型查询字典数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-04
     * @param dictType 字典类型
     * @return java.util.List
     */
    @GetMapping(value = "/queryDictByType")
    @ApiLog(value = "根据字典类型查询字典数据")
    public ApiResponse<List<PlatformDictResp>> queryByType(@RequestParam(name = "dictType") String dictType){
        return apiResponse(platformDictService.queryByType(dictType));
    }
}
