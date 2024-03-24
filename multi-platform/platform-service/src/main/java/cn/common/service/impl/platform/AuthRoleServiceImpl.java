package cn.common.service.impl.platform;

import cn.common.repository.entity.platform.AuthRole;
import cn.common.repository.entity.platform.AuthRolePermission;
import cn.common.repository.repository.platform.AuthPermissionRepository;
import cn.common.repository.repository.platform.AuthRolePermissionRepository;
import cn.common.repository.repository.platform.AuthRoleRepository;
import cn.common.service.data.ItemCriteriaBuilder;
import cn.common.service.platform.AuthRoleService;
import cn.common.service.platform.AuthUserService;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.repository.result.AuthPermissionResult;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.role.AuthRoleAddReq;
import pro.skywalking.req.platform.auth.role.AuthRoleReq;
import pro.skywalking.req.platform.auth.role.AuthRoleUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;
import pro.skywalking.resp.platform.auth.role.AuthRoleResp;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author Singer create by Singer email:singer-coder@qq.com
* @packageName cn.common.service
* @Description: 首页分类相关服务方法实现
* @date 2024-03-06
*/
@Service("authRoleService")
@Slf4j
public class AuthRoleServiceImpl implements AuthRoleService {

    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private AuthRolePermissionRepository authRolePermissionRepository;

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private ItemCriteriaBuilder itemCriteriaBuilder;

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(AuthRoleAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String roleName = addReq.getRoleName();
        String roleCode = addReq.getRoleCode();
        Long nameCount =
                authRoleRepository.selectCount(new LambdaQueryWrapper<AuthRole>().eq(AuthRole::getRoleName, roleName));
        if(nameCount != 0L){
            throw new BusinessException(ErrorCode.ERROR.getCode(), "角色名称已存在");
        }
        Long roleCount = authRoleRepository
                .selectCount(new LambdaQueryWrapper<AuthRole>().eq(AuthRole::getRoleCode,roleCode));
        if(roleCount != 0L) {
            throw new BusinessException(ErrorCode.ERROR.getCode(), "角色编码已存在");
        }
        AuthRole entity = mapperFacade.map(addReq, AuthRole.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setAuthRoleId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+ StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        authRoleRepository.insert(entity);
        List<String> permissionList = addReq.getAuthPermissionList();
        if(CollectionUtils.isEmpty(permissionList)){
            return;
        }
        String authRoleId = entity.getAuthRoleId();
        permissionList.stream().forEach(item -> {
            AuthRolePermission rolePermission = new AuthRolePermission();
            rolePermission.setAuthPermissionId(item);
            rolePermission.setAuthRoleId(authRoleId);
            authRolePermissionRepository.insert(rolePermission);
        });
    }

    /**
     * 批量删除信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 需要被删除的信息
     */
    @Override
    public void batchDeleteItem(BaseDeleteReq req){
        List<String> mainIdList = req.getMainIdList();
        if(CollectionUtils.isEmpty(mainIdList)) {
            return;
        }
        authRoleRepository.batchDeleteItem(mainIdList);
        authRolePermissionRepository.batchDeleteByRoleIdList(mainIdList);
    }

    /**
      * 查询角色具备的权限信息
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-06
      * @param authRoleId 角色ID
      * @return java.util.List
      */
    @Override
    public List<AuthPermissionResp> queryRolePermission(String authRoleId){
        List<AuthPermissionResult> permissionList = authPermissionRepository.queryAuthRolePermission(authRoleId);
        if(CollectionUtils.isEmpty(permissionList)){
            return Lists.newArrayList();
        }
        List<AuthPermissionResp> respList = mapperFacade
                .mapAsList(permissionList, AuthPermissionResp.class);
        return respList;
    }

    /**
     * 查询所有用户信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-067
     * @return java.util.List
     */
    @Override
    public List<AuthRoleResp> queryAllAuthRole(){
        List<AuthRole> authUserList =
                authRoleRepository.selectList(new LambdaQueryWrapper<>());
        if(CollectionUtils.isEmpty(authUserList)){
            return Lists.newArrayList();
        }
        List<AuthRoleResp> respList = mapperFacade.mapAsList(authUserList, AuthRoleResp.class);
        return respList;
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<AuthRoleResp> queryByPage(
        AuthRoleReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<AuthRole> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper,true);
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,AuthRole::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<AuthRole> pageList = authRoleRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<AuthRoleResp> respList =
            mapperFacade.mapAsList(pageList, AuthRoleResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
            respList.stream().forEach(item -> {
                item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
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
    private void setPageCriteria(LambdaQueryWrapper<AuthRole> pageWrapper, AuthRoleReq pageReq){

         if(!CheckParam.isNull(pageReq.getRoleName())){
             pageWrapper.like(AuthRole::getRoleName,pageReq.getRoleName());
         }

         if(!CheckParam.isNull(pageReq.getRoleCode())){
             pageWrapper.like(AuthRole::getRoleCode,pageReq.getRoleCode());
         }

         if(!CheckParam.isNull(pageReq.getRoleRemark())){
             pageWrapper.like(AuthRole::getRoleRemark,pageReq.getRoleRemark());
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
    public void updateItem(AuthRoleUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getAuthRoleId();
        AuthRole result = authRoleRepository.selectOne(new LambdaQueryWrapper<AuthRole>()
                .eq(AuthRole::getAuthRoleId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        authRoleRepository.updateById(result);
        List<String> permissionList = updateReq.getAuthPermissionList();
        if(CollectionUtils.isEmpty(permissionList)){
            return;
        }
        authRolePermissionRepository.deleteByRoleId(mainId);
        permissionList.stream().forEach(item -> {
            AuthRolePermission rolePermission = new AuthRolePermission();
            rolePermission.setAuthPermissionId(item);
            rolePermission.setAuthRoleId(mainId);
            authRolePermissionRepository.insert(rolePermission);
        });
    }

    /**
     * 设置需要更新的字段
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(AuthRole entity,
        AuthRoleUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getRoleName())){
            entity.setRoleName(updateReq.getRoleName());
        }
        if(!CheckParam.isNull(updateReq.getRoleCode())){
            entity.setRoleCode(updateReq.getRoleCode());
        }
        if(!CheckParam.isNull(updateReq.getRoleRemark())){
            entity.setRoleRemark(updateReq.getRoleRemark());
        }
    }
}
