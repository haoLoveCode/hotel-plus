package cn.common.service.impl.biz;

import cn.common.constants.BizConstants;
import cn.common.internal.wx.WxOpenIdResp;
import cn.common.repository.entity.biz.AppUserData;
import cn.common.repository.entity.biz.AuthAppUser;
import cn.common.repository.repository.biz.AppUserDataRepository;
import cn.common.repository.repository.biz.AuthAppUserRepository;
import cn.common.req.biz.app.auth.*;
import cn.common.req.biz.app.wx.WxBaseReq;
import cn.common.req.biz.platform.AuthAppUserAddReq;
import cn.common.req.biz.platform.AuthAppUserReq;
import cn.common.req.biz.platform.AuthAppUserUpdateReq;
import cn.common.resp.biz.app.auth.AuthAppLoginResp;
import cn.common.resp.biz.app.wx.WxPhoneData;
import cn.common.resp.biz.app.wx.WxPhoneResp;
import cn.common.resp.biz.platform.AuthAppUserExportResp;
import cn.common.resp.biz.openBiz.AuthAppUserResp;
import cn.common.service.biz.AuthAppUserService;
import cn.common.service.biz.WxService;
import cn.common.service.platform.AuthUserService;
import cn.common.wx.WxApiConstants;
import cn.common.wx.WxBizConstants;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.configuration.redis.RedisRepository;
import pro.skywalking.constants.AuthConstants;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.enums.UserStatusEnum;
import pro.skywalking.excel.ExportExcelHandler;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.resp.app.token.TokenVerifyResp;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: APP认证用户相关服务方法实现
 * @date 2024-03-25
 */
@Service("authAppUserService")
@Slf4j
public class AuthAppUserServiceImpl implements AuthAppUserService {

    @Resource
    private AuthAppUserRepository authAppUserRepository;

    @Resource
    private RedisRepository redisRepository;

    @Resource
    private AppUserDataRepository appUserDataRepository;

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
    private WxService wxService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    /**
     * 导出数据
     *
     * @param pageReq
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    public void exportData(AuthAppUserReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>数据导出Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<AuthAppUser> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, AuthAppUser::getCreateTime);
        List<AuthAppUser> pageList = authAppUserRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return;
        }
        List<AuthAppUserExportResp> respList = mapperFacade.mapAsList(pageList, AuthAppUserExportResp.class);
        try {
            String fileName = "导出信息.xlsx";
            ExportExcelHandler.setExportResponse(response, fileName);
            ExcelWriter excelWriter = null;
            try {
                // 这里 需要指定写用哪个class去写
                excelWriter = EasyExcel.write(response.getOutputStream(), AuthAppUserExportResp.class).build();
                // 这里注意 如果同一个sheet只要创建一次
                WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
                excelWriter.write(respList, writeSheet);
            } finally {
                // 关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>导出数据异常:{},{}<<<<<<<<<<<<<<<<<<<<<<", e.getMessage(), e);
            ExportExcelHandler.setExportErrorResponse(response);
            Map<String, String> map = Maps.newHashMap();
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                log.error(">>>>>>>>>>>>>>>>>导出数据发生异常:{},{}<<<<<<<<<<<<<<<<<", e.getMessage(), e);
                ioException.printStackTrace();
            }
        }
    }


    /**
     * 验证Token
     *
     * @return java.lang.Boolean
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public TokenVerifyResp verifyToken() {
        String token = currentToken();
        String result = redisRepository.get(AuthConstants.APP_AUTH_TOKEN_PREFIX + token);

        TokenVerifyResp resp = new TokenVerifyResp();
        if (CheckParam.isNull(result)) {
            resp.setVerifyStatus(Boolean.FALSE);
        } else {
            resp.setVerifyStatus(Boolean.TRUE);
        }
        return resp;
    }

    /**
     * 拿到APP登陆的当前用户ID
     *
     * @return java.lang.String
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public String authAppUserId() {
        AuthAppLoginResp meta = queryLoginUserMeta();
        return meta.getAuthAppUserId();
    }

    /**
     * 拿到登录后的用户信息
     *
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public AuthAppLoginResp queryLoginUserMeta() {
        String token = currentToken();
        String result = redisRepository.get(AuthConstants.APP_AUTH_USER_SKETCH_PREFIX + token);
        if (CheckParam.isNull(result)) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(), ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        AuthAppLoginResp resp = JSON.parseObject(result, AuthAppLoginResp.class);
        if (CheckParam.isNull(resp)) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(), ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        return resp;
    }

    /**
     * 拿到登录后的用户信息 不抛出异常
     *
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public AuthAppLoginResp queryLoginUserMetaNoThrow() {
        String token = currentToken();
        String result = redisRepository.get(AuthConstants.APP_AUTH_USER_SKETCH_PREFIX + token);
        if (CheckParam.isNull(result)) {
            return new AuthAppLoginResp();
        }
        AuthAppLoginResp resp = JSON.parseObject(result, AuthAppLoginResp.class);
        return resp;
    }

    /**
     * 系统退出登录
     *
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public void logOut() {
        String token = request.getHeader("token");
        if (CheckParam.isNull(token)) {
            return;
        }
        redisRepository.delete(AuthConstants.APP_AUTH_TOKEN_PREFIX + token);
        redisRepository.delete(AuthConstants.APP_AUTH_USER_SKETCH_PREFIX + token);
    }


    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信手机号
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    public AuthAppLoginResp bindWxPhone(WxMiniPhoneReq req) {
        log.info(">>>>>>>>>>>>>>>>>绑定用户手机号:Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        AuthAppLoginResp meta = queryLoginUserMeta();

        String getPhoneCode = req.getGetPhoneCode();
        //用户的微信OpenId
        String openId = meta.getOpenId();
        //微信的accessToken
        String weChatAccessToken = wxService.weChatAccessToken(new WxBaseReq());

        //调用接口，拿到用户的微信手机号信息
        WxPhoneResp wxPhoneResp = wxService.wxChatPhone(getPhoneCode, weChatAccessToken);
        if (CheckParam.isNull(wxPhoneResp)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "绑定手机号失败");
        }
        WxPhoneData wxPhoneData = wxPhoneResp.getWxPhoneData();
        if (CheckParam.isNull(wxPhoneData)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "绑定手机号失败");
        }
        //手机号 明文
        String phoneNumber = wxPhoneData.getPhoneNumber();
        AuthAppUser authAppUser = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getOpenId, openId));
        if (!CheckParam.isNull(authAppUser)) {
            if(phoneNumber.length()>4){
                authAppUser.setNickName("用户-"+StrUtil.subWithLength(phoneNumber,phoneNumber.length() -4 ,4));
            }
            authAppUser.setPhoneNumber(phoneNumber);
            authAppUserRepository.updateById(authAppUser);
        } else {
            //之前已经执行过微信的静默注册了
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(), ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        //先执行登出，删除之前的数据
        logOut();
        return handleLoginSuccess(authAppUser);
    }

    /**
     * 用户注册
     *
     * @param req 用户名密码注册请求参数
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    public AuthAppLoginResp userReg(AuthAppUserRegReq req) {
        log.info(">>>>>>>>>>>>>>>>>用户注册:Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String password = req.getPassword();
        String uniqueNo = req.getUniqueNo();
        String openId = req.getOpenId();
        String code = req.getCode();

        //如果jsCode存在说明可以拿到用户的openID
        if (!CheckParam.isNull(code)) {
            WxOpenIdResp wxOpenIdResp = initAuthCode2SessionParamMap(code);
            if (CheckParam.isNull(wxOpenIdResp)) {
                throw new BusinessException(ErrorCode.ERROR.getCode(), "注册失败");
            }
            openId = wxOpenIdResp.getOpenId();
            if (CheckParam.isNull(openId)) {
                throw new BusinessException(ErrorCode.ERROR.getCode(), "注册失败");
            }
            log.info(">>>>>>>>>>>>>>>>>用户的OpenId {} <<<<<<<<<<<<<<<<", JSON.toJSONString(wxOpenIdResp));
        }
        AuthAppUser authAppUser = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, uniqueNo));
        if (!CheckParam.isNull(authAppUser)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "用户已经存在");
        }
        //如果已经存在了该openId的用户了则执行更新用户名密码步骤
        if(!CheckParam.isNull(openId)){
            authAppUser = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getOpenId, openId));
        }
        if (!CheckParam.isNull(authAppUser)) {
            authAppUser.setUserName(uniqueNo);
            authAppUser.setPassword(MD5.md5(password));
            authAppUser.setDecryptionPassword(password);
            authAppUserRepository.updateById(authAppUser);
            return handleLoginSuccess(authAppUser);
        } else {
            //如果用户不存在则直接初始化用户信息
            authAppUser = new AuthAppUser();
            if (!CheckParam.isNull(openId)) {
                authAppUser.setOpenId(openId);
            }
            authAppUser.setUserName(uniqueNo);
            authAppUser.setPassword(MD5.md5(password));
            authAppUser.setDecryptionPassword(password);
            authAppUser.setAuthStatus(UserStatusEnum.EFFECTIVE.getCode());
            authAppUser.setUserNumber(SnowflakeIdWorker.uniqueMainId());
            try {
                String authUserId = SnowflakeIdWorker.uniqueMainId();
                BaseUtil.setFieldValueNotNull(authAppUser);
                authAppUser.setAuthAppUserId(authUserId);
                authAppUser.setOperatorId(authUserId);
            } catch (Exception e) {
                log.error("新增->设置为空的属性失败 {} , {} ", e.getMessage(), e);
                throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
            }
            authAppUserRepository.insert(authAppUser);
            return handleLoginSuccess(authAppUser);
        }
    }

    /**
     * 用户名密码登陆
     *
     * @param req 用户名密码登陆请求参数
     * @return
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    public AuthAppLoginResp userLogin(AuthAppUserLoginReq req) {
        log.info(">>>>>>>>>>>>>>>>>用户名密码登陆:Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String password = req.getPassword();
        String uniqueNo = req.getUniqueNo();

        AuthAppUser authAppUser = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, uniqueNo).eq(AuthAppUser::getPassword, MD5.md5(password)));
        if (!CheckParam.isNull(authAppUser)) {
            return handleLoginSuccess(authAppUser);
        } else {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "认证错误");
        }
    }

    /**
     * @param req
     * @return cn.common.resp.biz.app.auth.AuthAppLoginResp
     * @description: 绑定用户的微信openId
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    public AuthAppLoginResp bindWxOpenId(WxMiniOpenIdReq req) {
        log.info(">>>>>>>>>>>>>>>>>绑定微信OpenId:Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        WxOpenIdResp wxOpenIdResp = initAuthCode2SessionParamMap(req.getCode());
        if (CheckParam.isNull(wxOpenIdResp)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "绑定失败");
        }
        String openId = wxOpenIdResp.getOpenId();
        if (CheckParam.isNull(openId)) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "绑定失败");
        }
        log.info(">>>>>>>>>>>>>>>>>用户的OpenId {} <<<<<<<<<<<<<<<<", JSON.toJSONString(wxOpenIdResp));

        AuthAppUser authAppUser = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getOpenId, openId));
        if (!CheckParam.isNull(authAppUser)) {
            return handleLoginSuccess(authAppUser);
        }
        //如果用户不存在则直接初始化用户信息
        String userNumber = SnowflakeIdWorker.uniqueMainId();
        authAppUser = new AuthAppUser();
        authAppUser.setAuthStatus(UserStatusEnum.EFFECTIVE.getCode());
        authAppUser.setOpenId(openId);
        authAppUser.setUserName(BizConstants.APP_DEFAULT_USER_NAME + userNumber);
        authAppUser.setDecryptionPassword(BizConstants.APP_DEFAULT_USER_PASSWORD);
        authAppUser.setPassword(MD5.md5(BizConstants.APP_DEFAULT_USER_PASSWORD));
        authAppUser.setUserNumber(userNumber);
        try {
            String authUserId = SnowflakeIdWorker.uniqueMainId();
            BaseUtil.setFieldValueNotNull(authAppUser);
            authAppUser.setAuthAppUserId(authUserId);
            authAppUser.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        authAppUserRepository.insert(authAppUser);
        return handleLoginSuccess(authAppUser);
    }

    /**
     * 拿到当前登录用户的Token
     *
     * @return java.lang.Long
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    @Override
    public String currentToken() {
        String token = request.getHeader(AuthConstants.TOKEN_KEY);
        if (CheckParam.isNull(token)) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(), ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        return token;
    }

    /**
     * @param authAppUser 用户信息
     * @return
     * @description: 设置返回的用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    private AuthAppLoginResp handleLoginSuccess(AuthAppUser authAppUser) {
        AuthAppLoginResp resp = mapperFacade.map(authAppUser, AuthAppLoginResp.class);

        String authAppUserId = authAppUser.getAuthAppUserId();
        AppUserData appUserData = appUserDataRepository.selectOne(new LambdaQueryWrapper<AppUserData>()
                .eq(AppUserData::getAuthAppUserId, authAppUserId));
        if(!CheckParam.isNull(appUserData)){
            /*resp.setSignature(appUserData.getSignature());
            resp.setIdentityCard(appUserData.getIdentityCard());
            resp.setAge(appUserData.getAge());*/
        }
        String token = SnowflakeIdWorker.uniqueMainId();
        resp.setToken(token);
        redisRepository.set(AuthConstants.APP_AUTH_TOKEN_PREFIX + token, token, authConstants.getTokenValidTime(), TimeUnit.DAYS);
        redisRepository.set(AuthConstants.APP_AUTH_USER_SKETCH_PREFIX + token, JSON.toJSONString(resp), authConstants.getTokenValidTime(), TimeUnit.DAYS);
        return resp;
    }

    /**
     * 初始化拿到openId的参数和拿到结果
     *
     * @param jsCode
     * @return WxCodeToSessionResult
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    private WxOpenIdResp initAuthCode2SessionParamMap(String jsCode) {
        Map<String, Object> paramHashMap = new HashMap(8);
        paramHashMap.put("appid", wxBizConstants.getAppId());
        paramHashMap.put("secret", wxBizConstants.getAppSecret());
        paramHashMap.put("js_code", jsCode);
        paramHashMap.put("grant_type", "authorization_code");
        String result = HttpTookit.getRequest(wxApiConstants.getWxAuthCode2SessionApi(),
                paramHashMap, "UTF-8");
        log.info(">>>>>>>>>>>>>调用微信auth.code2Session接口返回数据:{}<<<<<<<<<<<",
                result);
        WxOpenIdResp wxPhoneResp = JSON.parseObject(result, WxOpenIdResp.class);
        log.info(">>>>>>>>>>>>>调用微信auth.code2Session接口返回:{}<<<<<<<<<<<",
                JSON.toJSONString(wxPhoneResp));
        return wxPhoneResp;
    }

    /**
     * 新增
     *
     * @param addReq 新增Req
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addItem(AuthAppUserAddReq addReq) {
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String userName = addReq.getUserName();
        Long count = authAppUserRepository.selectCount(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, userName));
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "用户名重复");
        }
        AuthAppUser entity = mapperFacade.map(addReq, AuthAppUser.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setAuthAppUserId(SnowflakeIdWorker.uniqueMainId());
            entity.setPassword(MD5.md5(addReq.getDecryptionPassword()));
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        authAppUserRepository.insert(entity);
    }

    /**
     * Web用户注册
     *
     * @param regReq Web用户注册请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AuthAppLoginResp webUserReg(AuthWebUserRegReq regReq) {
        log.info(">>>>>>>>>>>>>>>>>Web用户注册Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(regReq));
        String userName = regReq.getUserName();
        Long count = authAppUserRepository.selectCount(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, userName));
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "用户名重复");
        }
        AuthAppUser entity = mapperFacade.map(regReq, AuthAppUser.class);
        try {
            BaseUtil.setFieldValueNotNull(entity);
            entity.setAuthAppUserId(SnowflakeIdWorker.uniqueMainId());
            entity.setPassword(MD5.md5(regReq.getDecryptionPassword()));
            entity.setAuthStatus(UserStatusEnum.EFFECTIVE.getCode());
            entity.setOperatorId(StrUtil.EMPTY);
        } catch (Exception e) {
            log.error("Web用户注册->设置为空的属性失败 {} , {} ", e.getMessage(), e);
            throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
        }
        authAppUserRepository.insert(entity);
        return handleLoginSuccess(entity);
    }

    /**
     * 批量删除信息
     *
     * @param req 需要被删除的信息
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req) {
        List<String> mainIdList = req.getMainIdList();
        if (CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        List<AuthAppUser> entityList = authAppUserRepository.selectList(new LambdaQueryWrapper<AuthAppUser>().in(AuthAppUser::getAuthAppUserId, mainIdList));
        entityList.stream().forEach(item -> {
            authAppUserRepository.deleteById(item);
        });
    }

    /**
     * 查询所有信息
     *
     * @param
     * @return java.util.List
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    public List<AuthAppUserResp> queryAuthAppUser() {
        List<AuthAppUser> entityList = authAppUserRepository.selectList(new LambdaQueryWrapper<>());
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList, AuthAppUserResp.class);
    }

    /**
     * @param
     * @return cn.common.resp.biz.openBiz.AuthAppUserResp
     * @description: 查询当前APP用户的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    public AuthAppLoginResp currentUserMeta() {
        return queryLoginUserMeta();
    }

    /**
     * 分页查询
     *
     * @param pageReq 分页查询Req
     * @return Pagination
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    public Pagination<AuthAppUserResp> queryByPage(AuthAppUserReq pageReq) {
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<AuthAppUser> pageWrapper = new LambdaQueryWrapper<>();
        setPageCriteria(pageWrapper, pageReq);
        pageWrapper.orderBy(true, false, AuthAppUser::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<AuthAppUser> pageList = authAppUserRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page, new ArrayList<>());
        }
        List<AuthAppUserResp> respList = mapperFacade.mapAsList(pageList, AuthAppUserResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page, respList);
    }

    /**
     * 设置分页条件
     *
     * @param pageWrapper 查询条件
     * @param pageReq     查询参数
     * @return
     * @author: Singer
     * @date 2024-03-25
     */
    private void setPageCriteria(LambdaQueryWrapper<AuthAppUser> pageWrapper, AuthAppUserReq pageReq) {

        if (!CheckParam.isNull(pageReq.getUserName())) {
            pageWrapper.like(AuthAppUser::getUserName, pageReq.getUserName());
        }

        if (!CheckParam.isNull(pageReq.getUserNumber())) {
            pageWrapper.like(AuthAppUser::getUserNumber, pageReq.getUserNumber());
        }

        if (!CheckParam.isNull(pageReq.getPhoneNumber())) {
            pageWrapper.like(AuthAppUser::getPhoneNumber, pageReq.getPhoneNumber());
        }

        if (!CheckParam.isNull(pageReq.getNickName())) {
            pageWrapper.like(AuthAppUser::getNickName, pageReq.getNickName());
        }

        if (!CheckParam.isNull(pageReq.getAvatarUrl())) {
            pageWrapper.eq(AuthAppUser::getAvatarUrl, pageReq.getAvatarUrl());
        }

        if (!CheckParam.isNull(pageReq.getRealName())) {
            pageWrapper.like(AuthAppUser::getRealName, pageReq.getRealName());
        }

        if (!CheckParam.isNull(pageReq.getGender())) {
            pageWrapper.eq(AuthAppUser::getGender, pageReq.getGender());
        }

        if (!CheckParam.isNull(pageReq.getOpenId())) {
            pageWrapper.eq(AuthAppUser::getOpenId, pageReq.getOpenId());
        }

        if (!CheckParam.isNull(pageReq.getDecryptionPassword())) {
            pageWrapper.eq(AuthAppUser::getDecryptionPassword, pageReq.getDecryptionPassword());
        }

        if (!CheckParam.isNull(pageReq.getPassword())) {
            pageWrapper.eq(AuthAppUser::getPassword, pageReq.getPassword());
        }

        if (!CheckParam.isNull(pageReq.getAuthStatus())) {
            pageWrapper.eq(AuthAppUser::getAuthStatus, pageReq.getAuthStatus());
        }
    }

    /**
     * 更新APP认证用户
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateAppAuthUser(AuthAppUserUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>>>>>更新APP认证用户请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = authAppUserId();
        String userName = updateReq.getUserName();
        Long count = authAppUserRepository.selectCount(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, userName).ne(AuthAppUser::getAuthAppUserId, mainId));
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "用户名重复");
        }
        AuthAppUser result = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getAuthAppUserId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result, updateReq);
        authAppUserRepository.updateById(result);
        updateUserOtherData(updateReq, result);
    }

    /**
     * 更新
     *
     * @param updateReq 更新请求参数
     * @author: Singer
     * @date 2024-03-25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateItem(AuthAppUserUpdateReq updateReq) {
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getAuthAppUserId();
        String userName = updateReq.getUserName();
        Long count = authAppUserRepository.selectCount(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getUserName, userName).ne(AuthAppUser::getAuthAppUserId, mainId));
        if (count > 0) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "用户名重复");
        }
        AuthAppUser result = authAppUserRepository.selectOne(new LambdaQueryWrapper<AuthAppUser>().eq(AuthAppUser::getAuthAppUserId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result, updateReq);
        authAppUserRepository.updateById(result);
        updateUserOtherData(updateReq, result);
    }

    /**
     * @param updateReq 需要更新的数据
     * @param entity    APP用户实体
     * @return
     * @description: 更新用户其他的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-25
     */
    private void updateUserOtherData(AuthAppUserUpdateReq updateReq, AuthAppUser entity) {
        AppUserData appUserData = appUserDataRepository.selectOne(new LambdaQueryWrapper<AppUserData>().eq(AppUserData::getAuthAppUserId, entity.getAuthAppUserId()));
        Boolean entityInsert = Boolean.FALSE;
        if (CheckParam.isNull(appUserData)) {
            appUserData = new AppUserData();
            try {
                appUserData.setAuthAppUserId(entity.getAuthAppUserId());
                appUserData.setAppUserDataId(SnowflakeIdWorker.uniqueMainId());
                BaseUtil.setFieldValueNotNull(appUserData);
            } catch (Exception e) {
                log.error("新增用户信息->设置为空的属性失败 {} , {} ", e.getMessage(), e);
                throw new BusinessException(ErrorCode.ERROR.getCode(), ErrorCode.ERROR.getMessage() + StrUtil.COLON + e.getMessage() + StrUtil.COLON + e);
            }
            entityInsert = Boolean.TRUE;
        }
        if (!CheckParam.isNull(updateReq.getIdentityCard())) {
            appUserData.setIdentityCard(updateReq.getIdentityCard());
        }
        if (!CheckParam.isNull(updateReq.getRealName())) {
            appUserData.setRealName(updateReq.getRealName());
        }
        if (!CheckParam.isNull(updateReq.getGender())) {
            appUserData.setGender(updateReq.getGender());
        }
        if (entityInsert) {
            appUserDataRepository.insert(appUserData);
        } else {
            appUserDataRepository.updateById(appUserData);
        }
    }

    /**
     * 设置需要更新的字段
     *
     * @param updateReq 更新参数
     * @param entity    产业
     * @author: Singer
     * @date 2024-03-25
     */
    private void setNeedUpdateItem(AuthAppUser entity, AuthAppUserUpdateReq updateReq) {
        if (!CheckParam.isNull(updateReq.getUserName())) {
            entity.setUserName(updateReq.getUserName());
        }
        if (!CheckParam.isNull(updateReq.getUserNumber())) {
            entity.setUserNumber(updateReq.getUserNumber());
        }
        if (!CheckParam.isNull(updateReq.getPhoneNumber())) {
            entity.setPhoneNumber(updateReq.getPhoneNumber());
        }
        if (!CheckParam.isNull(updateReq.getNickName())) {
            entity.setNickName(updateReq.getNickName());
        }
        if (!CheckParam.isNull(updateReq.getAvatarUrl())) {
            entity.setAvatarUrl(updateReq.getAvatarUrl());
        }
        if (!CheckParam.isNull(updateReq.getRealName())) {
            entity.setRealName(updateReq.getRealName());
        }
        if (!CheckParam.isNull(updateReq.getGender())) {
            entity.setGender(updateReq.getGender());
        }
        if (!CheckParam.isNull(updateReq.getOpenId())) {
            entity.setOpenId(updateReq.getOpenId());
        }
        if (!CheckParam.isNull(updateReq.getDecryptionPassword())) {
            entity.setPassword(MD5.md5(updateReq.getDecryptionPassword()));
            entity.setDecryptionPassword(updateReq.getDecryptionPassword());
        }
        if (!CheckParam.isNull(updateReq.getAuthStatus())) {
            entity.setAuthStatus(updateReq.getAuthStatus());
        }
    }
}
