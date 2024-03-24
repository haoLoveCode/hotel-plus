package cn.common.service.impl.platform;

import cn.common.service.platform.PlatformService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.configuration.redis.RedisRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Singer create by singer email:singer-coder@qq.com
 * @projectName byte-mini-api
 * @Description: 平台相关服务方法实现
 * @date 2024-03-06
 */
@Service("platformService")
@Slf4j
public class PlatformServiceImpl implements PlatformService {

    /**
      * 注册当前的ServletRequest
      */
    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private RedisRepository redisRepository;



}
