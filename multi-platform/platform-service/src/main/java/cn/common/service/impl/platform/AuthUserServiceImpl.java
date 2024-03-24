package cn.common.service.impl.platform;

import cn.common.internal.AuthUserLoginReq;
import cn.common.repository.entity.platform.AuthUser;
import cn.common.repository.entity.platform.AuthUserRole;
import cn.common.repository.repository.platform.AuthPermissionRepository;
import cn.common.repository.repository.platform.AuthUserRepository;
import cn.common.repository.repository.platform.AuthUserRoleRepository;
import cn.common.service.data.ItemCriteriaBuilder;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.configuration.redis.RedisRepository;
import pro.skywalking.constants.AuthConstants;
import pro.skywalking.enums.AuthStatusEnum;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.enums.UserStatusEnum;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.repository.result.AuthPermissionResult;
import pro.skywalking.repository.result.AuthUserRoleResult;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.captcha.CaptchaReq;
import pro.skywalking.req.platform.auth.user.AuthUserAddReq;
import pro.skywalking.req.platform.auth.user.AuthUserReq;
import pro.skywalking.req.platform.auth.user.AuthUserUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.captcha.CaptchaResp;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;
import pro.skywalking.resp.platform.auth.user.AuthUserResp;
import pro.skywalking.resp.platform.auth.user.PlatformLoginResp;
import pro.skywalking.resp.platform.other.AuthUserSketchResp;
import pro.skywalking.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 系统用户信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("authUserService")
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {

    @Resource
    private AuthUserRepository authUserRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthConstants authConstants;

    @Resource
    private AuthUserRoleRepository authUserRoleRepository;

    @Resource
    private ItemCriteriaBuilder itemCriteriaBuilder;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private RedisRepository redisRepository;

    /**
     * @description: 生成图形验证码
     * @auther: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param captchaReq 生成图形验证码请求参数
     * @return pro.skywalking.resp.platform.auth.captcha.CaptchaResp
     */
    @Override
    public CaptchaResp base64captchaCode(CaptchaReq captchaReq){
        log.info("生成验证码请求参数:{}",JSON.toJSONString(captchaReq));
        String userName = captchaReq.getUserName();
        // png类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(captchaReq.getWidth(),
                captchaReq.getHeight());
        String text = captcha.text();// 获取验证码的字符
        String base64 = captcha.toBase64();
        log.info("验证码base64："+base64);
        //缓存验证码
        String cachePrefix = AuthConstants.CAPTCHA_CACHE_PREFIX+userName;
        redisRepository.set(cachePrefix,text,3L,TimeUnit.MINUTES);
        log.info("验证码："+text);
        CaptchaResp resp = new CaptchaResp();
        resp.setBase64Value(base64);
        resp.setHeight(captchaReq.getHeight());
        resp.setWidth(captchaReq.getWidth());
        resp.setUserName(captchaReq.getUserName());

        return resp;
    }

    /**
     * 拿到当前登录用户的Token
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return java.lang.Long
     */
    @Override
    public String currentToken(){
        String token = request.getHeader(AuthConstants.TOKEN_KEY);
        if(CheckParam.isNull(token)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        return token;
    }

    /**
     * 拿到用户ID
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return java.lang.String
     */
    @Override
    public String currentAuthUserId(){
        String token = request.getHeader(AuthConstants.TOKEN_KEY);
        String result = redisRepository.get(AuthConstants.PLATFORM_AUTH_USER_SKETCH_PREFIX + token);
        if(CheckParam.isNull(result)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        PlatformLoginResp platformLoginResp = JSON.parseObject(result, PlatformLoginResp.class);
        if(CheckParam.isNull(platformLoginResp)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        AuthUserResp userData = platformLoginResp.getUserData();
        if(CheckParam.isNull(userData)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        return userData.getAuthUserId();
    }

    /**
     * 查询所有用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-067
     * @return java.util.List
     */
    @Override
    public List<AuthUserSketchResp> queryAllAuthUser(){
        List<AuthUser> authUserList =
                authUserRepository.selectList(new LambdaQueryWrapper<>());
        if(CollectionUtils.isEmpty(authUserList)){
            return Lists.newArrayList();
        }
        List<AuthUserSketchResp> respList = mapperFacade.mapAsList(authUserList, AuthUserSketchResp.class);
        return respList;
    }

    /**
     * 拿到登录后的用户信息 不抛出异常
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    @Override
    public PlatformLoginResp queryLoginUserMetaNoThrow(){
        String token = request.getHeader(AuthConstants.TOKEN_KEY);
        String result = redisRepository.get(AuthConstants.PLATFORM_AUTH_USER_SKETCH_PREFIX + token);
        if(CheckParam.isNull(result)){
            return new PlatformLoginResp();
        }
        PlatformLoginResp platformLoginResp = JSON.parseObject(result, PlatformLoginResp.class);
        return platformLoginResp;
    }

    /**
     * 拿到登录后的用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    @Override
    public PlatformLoginResp queryLoginUserMeta(){
        String token = request.getHeader(AuthConstants.TOKEN_KEY);
        String result = redisRepository.get(AuthConstants.PLATFORM_AUTH_USER_SKETCH_PREFIX + token);
        if(CheckParam.isNull(result)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        PlatformLoginResp platformLoginResp = JSON.parseObject(result, PlatformLoginResp.class);
        if(CheckParam.isNull(platformLoginResp)){
            throw new BusinessException(ErrorCode.LOGIN_ERROR_CODE.getCode(),
                    ErrorCode.LOGIN_ERROR_CODE.getMessage());
        }
        return platformLoginResp;
    }

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(AuthUserAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        AuthUser existedAuthUser = authUserRepository.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getUserName,addReq.getUserName()));
        if(!CheckParam.isNull(existedAuthUser)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "账号已经存在");
        }
        String currentAuthUserId = currentAuthUserId();
        String authUserId = SnowflakeIdWorker.uniqueMainId();
        addReq.setDecryptionPassword(addReq.getPassword());
        addReq.setPassword(MD5.md5(addReq.getPassword()));
        AuthUser authUser = mapperFacade.map(addReq, AuthUser.class);
        try {
            BaseUtil.setFieldValueNotNull(authUser);
            authUser.setAuthStatus(UserStatusEnum.DISABLED.getCode());
            authUser.setAuthUserId(authUserId);
            authUser.setOperatorId(currentAuthUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        authUserRepository.insert(authUser);
        List<String> authRoleIdList = addReq.getAuthRoleIdList();
        if(CollectionUtils.isEmpty(authRoleIdList)){
            return;
        }
        authUserRoleRepository.deleteByAuthUserId(authUserId);
        authRoleIdList.stream().forEach(item -> {
            AuthUserRole authUserRole = new AuthUserRole();
            authUserRole.setAuthRoleId(item);
            authUserRole.setAuthUserId(authUserId);
            authUserRoleRepository.insert(authUserRole);
        });
    }

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        authUserRepository.batchDeleteItem(mainIdList);
        authUserRoleRepository.batchDeleteByAuthUserId(mainIdList);
    }

    /**
     * 查询用户角色信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param authUserId 用户ID
     * @return java.util.List
     */
    @Override
    public List<AuthRoleResp> queryRoleByAuthUserId(String authUserId){
        List<AuthUserRoleResult> authRoleList = authUserRoleRepository.queryUserRoleList(authUserId);
        if(CollectionUtils.isEmpty(authRoleList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(authRoleList,AuthRoleResp.class);
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return pro.skywalking.resp.page.Pagination
     */
    @Override
    public Pagination<AuthUserResp> queryByPage(
            AuthUserReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //当前登录用户
        LambdaQueryWrapper<AuthUser> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper,true);
        pageWrapper.orderBy(true,false, AuthUser::getCreateTime);
        setPageCriteria(pageWrapper,pageReq);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        //
        List<AuthUser> pageList = authUserRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<AuthUserResp> respList = mapperFacade.mapAsList(pageList, AuthUserResp.class);
        List<String> authUserIdList =
                respList.stream().map(item -> item.getAuthUserId()).distinct().collect(Collectors.toList());
        List<AuthUserRoleResult> authUserRoleList = authUserRoleRepository.queryRoleListByUserIdList(authUserIdList);
        if(CollectionUtils.isEmpty(authUserRoleList)){
            return PageBuilder.buildPageResult(page,respList);
        }
        Map<String, List<AuthUserRoleResult>> authUserRoleHashMap = authUserRoleList.stream()
                .collect(Collectors.groupingBy(AuthUserRoleResult::getAuthUserId));
        respList.forEach(item -> {
            String authUserId = item.getAuthUserId();
            List<AuthUserRoleResult> roleDataList = authUserRoleHashMap.get(authUserId);
            if(CollectionUtils.isEmpty(roleDataList)){
                return;
            }
            String roleName = roleDataList.stream().map(roleData -> roleData.getRoleName()).distinct().collect(Collectors.joining(","));
            item.setRoleName(roleName);
            List<String> authRoleIdList =
                    roleDataList.stream().map(roleData -> roleData.getAuthRoleId()).distinct().collect(Collectors.toList());
            item.setAuthRoleIdList(authRoleIdList);
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 设置分页条件
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param pageWrapper 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<AuthUser> pageWrapper, AuthUserReq pageReq){

        if(!CheckParam.isNull(pageReq.getUserName())){
            pageWrapper.like(AuthUser::getUserName,pageReq.getUserName());
        }

        if(!CheckParam.isNull(pageReq.getUserNumber())){
            pageWrapper.like(AuthUser::getUserNumber,pageReq.getUserNumber());
        }

        if(!CheckParam.isNull(pageReq.getPhoneNumber())){
            pageWrapper.like(AuthUser::getPhoneNumber,pageReq.getPhoneNumber());
        }

        if(!CheckParam.isNull(pageReq.getPassword())){
            pageWrapper.like(AuthUser::getPassword,pageReq.getPassword());
        }

        if(!CheckParam.isNull(pageReq.getRealName())){
            pageWrapper.like(AuthUser::getRealName,pageReq.getRealName());
        }

        if(!CheckParam.isNull(pageReq.getGender())){
            pageWrapper.eq(AuthUser::getGender,pageReq.getGender());
        }

        if(!CheckParam.isNull(pageReq.getAuthStatus())){
            pageWrapper.eq(AuthUser::getAuthStatus,pageReq.getAuthStatus());
        }

        if(!CheckParam.isNull(pageReq.getAvatarUrl())){
            pageWrapper.like(AuthUser::getAvatarUrl,pageReq.getAvatarUrl());
        }

        if(!CheckParam.isNull(pageReq.getNickName())){
            pageWrapper.like(AuthUser::getNickName,pageReq.getNickName());
        }
    }

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(AuthUserUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getAuthUserId();
        AuthUser result = authUserRepository.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getAuthUserId, mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        authUserRepository.updateById(result);
        List<String> authRoleIdList = updateReq.getAuthRoleIdList();
        if(CollectionUtils.isEmpty(authRoleIdList)){
            return;
        }
        authUserRoleRepository.deleteByAuthUserId(mainId);
        authRoleIdList.stream().forEach(item -> {
            AuthUserRole authUserRole = new AuthUserRole();
            authUserRole.setAuthRoleId(item);
            authUserRole.setAuthUserId(mainId);
            authUserRoleRepository.insert(authUserRole);
        });
    }

    /**
     * 设置需要更新的字段
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(AuthUser entity,
                                   AuthUserUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getUserName())){
            entity.setUserName(updateReq.getUserName());
        }
        if(!CheckParam.isNull(updateReq.getUserNumber())){
            entity.setUserNumber(updateReq.getUserNumber());
        }
        if(!CheckParam.isNull(updateReq.getPhoneNumber())){
            entity.setPhoneNumber(updateReq.getPhoneNumber());
        }
        if(!CheckParam.isNull(updateReq.getDecryptionPassword())){
            entity.setDecryptionPassword(updateReq.getDecryptionPassword());
        }
        if(!CheckParam.isNull(updateReq.getPassword())){
            entity.setPassword(MD5.md5(updateReq.getPassword()));
        }
        if(!CheckParam.isNull(updateReq.getRealName())){
            entity.setRealName(updateReq.getRealName());
        }
        if(!CheckParam.isNull(updateReq.getGender())){
            entity.setGender(updateReq.getGender());
        }
        if(!CheckParam.isNull(updateReq.getAuthStatus())){
            entity.setAuthStatus(updateReq.getAuthStatus());
        }
        if(!CheckParam.isNull(updateReq.getAvatarUrl())){
            entity.setAvatarUrl(updateReq.getAvatarUrl());
        }
        if(!CheckParam.isNull(updateReq.getNickName())){
            entity.setNickName(updateReq.getNickName());
        }
    }

    /**
     * 系统退出登录
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    @Override
    public void logOut(){
        String token = request.getHeader("token");
        if(CheckParam.isNull(token)){
            return;
        }
        redisRepository.delete(AuthConstants.PLATFORM_AUTH_TOKEN_PREFIX+token);
        redisRepository.delete(AuthConstants.PLATFORM_AUTH_USER_SKETCH_PREFIX+token);
        redisRepository.delete(AuthConstants.PLATFORM_AUTH_USER_ROLE_PREFIX+token);
        redisRepository.delete(AuthConstants.PLATFORM_AUTH_USER_PERMISSION_PREFIX+token);
    }

    /**
     * 系统登录
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 用户登录请求参数
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    @Override
    public PlatformLoginResp authUserLogin(AuthUserLoginReq req){
        log.info(">>>>>>>>>>>登录请求参数:<<<<<<<<<<<",JSON.toJSONString(req));
        if(StringUtils.isEmpty(req.getPassword())){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "密码不能为空");
        }
        String userName = req.getUserName();
        String cachePrefix = AuthConstants.CAPTCHA_CACHE_PREFIX+userName;
        String verifyCode = redisRepository.get(cachePrefix);
        if(CheckParam.isNull(verifyCode)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "验证码错误");
        }
        if(!StrUtil.equals(verifyCode,req.getVerifyCode())){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "验证码错误");
        }
        redisRepository.delete(verifyCode);
        String loginPassWord = MD5.md5(req.getPassword());
        req.setPassword(loginPassWord);
        AuthUser authUser = authUserRepository.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getUserName,req.getUserName())
                .eq(AuthUser::getPassword,req.getPassword()));
        if(CheckParam.isNull(authUser)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "用户名密码错误");
        }
        Integer authStatus = authUser.getAuthStatus();
        //如果是禁用状态则无法登录
        if(AuthStatusEnum.DISABLED.getCode().compareTo(authStatus) == 0){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "用户被禁用，无法登录");
        }
        return handleLoginSuccess(authUser);
    }

    /**
     * 拿到登录后返回的数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    @Override
    public PlatformLoginResp queryLoginMeta(){
        String authUserId = currentAuthUserId();
        AuthUser authUser = authUserRepository
                .selectOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getAuthUserId, authUserId));
        PlatformLoginResp platformLoginResp = new PlatformLoginResp();
        List<AuthPermissionResult> permList = authPermissionRepository.queryAuthUserPermission(authUserId);
        List<AuthPermissionResp> permissionList = Lists.newArrayList();
        List<PermissionNode> treePermissionList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(permList)){
            handlePermissionList(permList);
            permissionList.addAll(mapperFacade.mapAsList(permList, AuthPermissionResp.class));
            List<PermissionNode> treeList = handlePermissionTree(permList);
            treePermissionList.addAll(treeList);
            platformLoginResp.setPermissionList(permissionList);
            platformLoginResp.setTreePermissionList(treePermissionList);
        }
        if(!CheckParam.isNull(authUser)){
            AuthUserResp authUserResp = mapperFacade.map(authUser, AuthUserResp.class);
            platformLoginResp.setUserData(authUserResp);
        }
        List<AuthUserRoleResult> roleList = authUserRoleRepository.queryUserRoleList(authUserId);
        List<AuthRoleResp> authRoleList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(roleList)){
            authRoleList.addAll(mapperFacade.mapAsList(roleList, AuthRoleResp.class));
            platformLoginResp.setRoleList(authRoleList);
        }
        platformLoginResp.setToken(currentToken());
        return platformLoginResp;
    }

    /**
     * 登录成功的逻辑处理
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param authUser 系统用户
     * @return pro.skywalking.resp.platform.auth.user.PlatformLoginResp
     */
    public PlatformLoginResp handleLoginSuccess(AuthUser authUser){
        String authUserId = authUser.getAuthUserId();
        PlatformLoginResp platformLoginResp = new PlatformLoginResp();
        List<AuthPermissionResult> permList = authPermissionRepository.queryAuthUserPermission(authUserId);
        List<AuthPermissionResp> permissionList = Lists.newArrayList();
        List<PermissionNode> treePermissionList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(permList)){
            handlePermissionList(permList);
            permissionList.addAll(mapperFacade.mapAsList(permList, AuthPermissionResp.class));
            List<PermissionNode> treeList = handlePermissionTree(permList);
            treePermissionList.addAll(treeList);
            platformLoginResp.setPermissionList(permissionList);
            platformLoginResp.setTreePermissionList(treePermissionList);
        }
        if(!CheckParam.isNull(authUser)){
            AuthUserResp authUserResp = mapperFacade.map(authUser, AuthUserResp.class);
            //拿到当前用户的IP地址
            String ipAddress = BaseUtil.getIpAddress(request);
            authUserResp.setCurrentIp(ipAddress);
            platformLoginResp.setUserData(authUserResp);
        }
        List<AuthUserRoleResult> roleList = authUserRoleRepository.queryUserRoleList(authUserId);
        List<AuthRoleResp> authRoleList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(roleList)){
            authRoleList.addAll(mapperFacade.mapAsList(roleList, AuthRoleResp.class));
            platformLoginResp.setRoleList(authRoleList);
        }
        String token = SnowflakeIdWorker.uniqueMainId();
        try {
            platformLoginResp.setToken(token);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "登录失败");
        }
        redisRepository.set(AuthConstants.PLATFORM_AUTH_TOKEN_PREFIX+token,token,authConstants.getTokenValidTime(),TimeUnit.HOURS);
        redisRepository.set(AuthConstants.PLATFORM_AUTH_USER_SKETCH_PREFIX+token,JSON.toJSONString(platformLoginResp),authConstants.getTokenValidTime(),TimeUnit.HOURS);
        redisRepository.set(AuthConstants.PLATFORM_AUTH_USER_ROLE_PREFIX+token,JSON.toJSONString(authRoleList),authConstants.getTokenValidTime(),TimeUnit.HOURS);
        redisRepository.set(AuthConstants.PLATFORM_AUTH_USER_PERMISSION_PREFIX+token,JSON.toJSONString(platformLoginResp.getPermissionList()),authConstants.getTokenValidTime(),TimeUnit.HOURS);
        return platformLoginResp;
    }

    /**
     * 处理树节点数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param dataList 数据
     * @return java.util.List
     */
    public static List<PermissionNode> handlePermissionTree(List<AuthPermissionResult> dataList) {
        List<PermissionNode> resultList = new ArrayList<>();
        List<PermissionNode> permissionNodeList = new ArrayList<>();
        dataList.forEach(item -> {
            PermissionNode node = new PermissionNode();
            node.setId(item.getAuthPermissionId());
            node.setParentId(item.getParentId());
            node.setPath(item.getPermissionPath());
            node.setPermissionIcon(item.getPermissionIcon());
            node.setParentId(item.getParentId());
            node.setName(item.getPermissionName());
            node.setSortIndex(item.getSortIndex());
            node.setPermissionType(item.getPermissionType());
            permissionNodeList.add(node);
        });
        // 遍历节点列表
        permissionNodeList.stream().forEach(item -> {
            if (StrUtil.equalsIgnoreCase(AuthConstants.TOP_NODE_ID, item.getParentId())) {
                // parentId为0 (根节点）作为入口
                List<PermissionNode> childrenNodeList = handleChildrenNode(item.getId(), permissionNodeList);
                item.setChildren(childrenNodeList);
                if(CollectionUtils.isEmpty(childrenNodeList)){
                    item.setHasChildren(Boolean.TRUE);
                }
                resultList.add(item);
            }
        });
        List<PermissionNode> nodeList = resultList.stream().sorted(Comparator.comparing(PermissionNode::getSortIndex))
                .collect(Collectors.toList());
        return nodeList;
    }

    /**
     * 拿到子节点的递归方法
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param nodeId 数据
     * @param permissionNodeList 节点数据
     * @return java.util.List
     */
    public static List<PermissionNode> handleChildrenNode(String nodeId,
                                                          List<PermissionNode> permissionNodeList) {
        List<PermissionNode> resultList = new ArrayList<>();
        //选出子节点 排除顶级节点
        List<PermissionNode> childrenNodeList =
                permissionNodeList.stream().filter(item -> StrUtil.equalsIgnoreCase(item.getParentId(), nodeId))
                        .filter(item -> !StrUtil.equalsIgnoreCase(AuthConstants.TOP_NODE_ID, item.getParentId()))
                        .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childrenNodeList)){
            return resultList;
        }
        childrenNodeList.stream().forEach(item -> {
            resultList.add(item);
            String parentId = item.getParentId();
            //如果是顶级节点，直接绕过
            if (StrUtil.equalsIgnoreCase(parentId,nodeId)) {
                List<PermissionNode> childrenNode = handleChildrenNode(item.getId(), permissionNodeList);
                log.info(">>>>>>>>>>>>递归查询出来的子节点:<<<<<<<<<<<<<",JSON.toJSONString(childrenNode));
                // 递归获取子节点
                item.setHasChildren(Boolean.TRUE);
                item.setChildren(childrenNode);

            }
        });
        List<PermissionNode> nodeList = resultList.stream().sorted(Comparator.comparing(PermissionNode::getSortIndex))
                .collect(Collectors.toList());
        return nodeList;
    }

    /**
     * 设置权限数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param permList 权限集合
     */
    private void handlePermissionList(List<AuthPermissionResult> permList){
        permList = permList.stream().filter(item -> !CheckParam.isNull(item)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(permList)){
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    "该用户对应角色无权限信息，请联系管理员");
        }
        permList.stream().forEach(item -> {
            item.setId(String.valueOf(item.getAuthPermissionId()));
        });
    }
}
