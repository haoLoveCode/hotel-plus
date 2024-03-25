package cn.common.api.biz;

import cn.common.api.BaseApiController;
import cn.common.resp.platform.MainSwiperResp;
import cn.common.service.biz.app.AppMainSwiperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.interceptor.NeedLogin;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.api.controller
 * @Description: 首页轮播图前端控制器
 * @date 2024-03-25
 */
@RestController
@RequestMapping(value = "api/v1/mainSwiper")
@Slf4j
public class MainSwiperController extends BaseApiController {

    @Resource
    private AppMainSwiperService appMainSwiperService;

    /**
     * 查询所有首页轮播图信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @GetMapping(value = "/queryAllMainSwiper")
    @ApiLog(value = "查询所有首页轮播图信息")
    @NeedLogin(value = false)
    public ApiResponse<List<MainSwiperResp>> queryAllMainSwiper(){
        return apiResponse(appMainSwiperService.queryAllMainSwiper());
    }

}
