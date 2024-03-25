package cn.common.service.impl.biz;

import cn.common.service.biz.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.skywalking.configuration.redis.RedisRepository;

import javax.annotation.Resource;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: 异步处理Service
 * @date 2024-03-25
 */
@Service("asyncService")
@Slf4j
public class AsyncServiceImpl implements AsyncService {

    @Resource
    private RedisRepository redisRepository;


}




