package cn.common.api.platform;

import cn.common.api.BaseApiController;
import cn.common.service.platform.PlatformService;
import cn.common.utils.TcMapUtils;
import cn.common.utils.interaction.TcPlaceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.skywalking.anon.ApiLog;
import pro.skywalking.resp.base.ApiResponse;

import javax.annotation.Resource;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.ant.fun.api.controller
 * @Description: 平台前端控制器
 * @date 2024-03-06
 */
@RestController
@RequestMapping(value = "api/v1/platform")
@Slf4j
public class PlatformController extends BaseApiController {

    @Resource
    private PlatformService platformService;

    /**
     *
     * @description: 搜索地点
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2023/11/1
     * @param  keyword 关键词
     * @return pro.skywalking.resp.base.ApiResponse
     */
    @GetMapping(value = "/queryPlace")
    @ApiLog(value = "搜索地点")
    public ApiResponse<TcPlaceData[]> queryPlace(@RequestParam(name = "keyword") String keyword){
        TcPlaceData[] placeData = TcMapUtils.queryPlace(keyword);
        return apiResponse(placeData);
    }

}
