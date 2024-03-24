package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;
import cn.common.service.biz.app.AppSalesItemTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 商品类型信息前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/salesItemType")
@Slf4j
public class AppSalesItemTypeController extends BaseApiController {

    @Resource
    private AppSalesItemTypeService appSalesItemTypeService;

    /**
     * 查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/querySalesItemType")
    @ApiLog(value = "查询商品类型信息")
    @NeedLogin()
    public ApiResponse<List<SalesItemTypeResp>> querySalesItemType(
        @RequestBody @Valid SalesItemTypeReq req){
        return apiResponse(appSalesItemTypeService.querySalesItemType(req));
    }

    /**
     * 查询单个商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @PostMapping(value = "/queryOneSalesItemType")
    @ApiLog(value = "查询商品类型信息")
    @NeedLogin()
    public ApiResponse<SalesItemTypeResp> queryOneSalesItemType(
        @RequestBody @Valid SalesItemTypeReq req){
        return apiResponse(appSalesItemTypeService.queryOneSalesItemType(req));
    }

}
