package cn.common.service.impl.biz.app;

import cn.common.repository.repository.biz.AuthAppUserRepository;
import cn.common.service.biz.app.AppMainService;
import cn.common.service.platform.AuthUserService;
import cn.common.wx.WxApiConstants;
import cn.common.wx.WxBizConstants;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.configuration.redis.RedisRepository;
import pro.skywalking.constants.AuthConstants;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName multi-platform
 * @Description: APP主要业务
 * @date 2024-03-06
 */
@Service("appMainService")
@Slf4j
public class AppMainServiceImpl implements AppMainService {

    @Resource
    private AuthAppUserRepository authAppUserRepository;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AuthConstants authConstants;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private WxApiConstants wxApiConstants;

    @Resource
    private WxBizConstants wxBizConstants;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;





}
