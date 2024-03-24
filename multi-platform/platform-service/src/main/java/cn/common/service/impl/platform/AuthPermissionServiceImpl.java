package cn.common.service.impl.platform;


import cn.common.repository.entity.platform.AuthPermission;
import cn.common.repository.repository.platform.AuthPermissionRepository;
import cn.common.repository.repository.platform.AuthRolePermissionRepository;
import cn.common.service.data.ItemCriteriaBuilder;
import cn.common.service.platform.AuthPermissionService;
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
import pro.skywalking.constants.AuthConstants;
import pro.skywalking.enums.ErrorCode;
import pro.skywalking.exception.BusinessException;
import pro.skywalking.helper.PageBuilder;
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionAddReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionReq;
import pro.skywalking.req.platform.auth.perm.AuthPermissionUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.auth.perm.AuthPermissionResp;
import pro.skywalking.utils.BaseUtil;
import pro.skywalking.utils.CheckParam;
import pro.skywalking.utils.SnowflakeIdWorker;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service
 * @Description: 系统菜单权限相关服务方法实现
 * @date 2024-03-06
 */
@Service("authPermissionService")
@Slf4j
public class AuthPermissionServiceImpl implements AuthPermissionService {

    @Resource
    private AuthPermissionRepository authPermissionRepository;

    @Resource
    private AuthRolePermissionRepository authRolePermissionRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private ItemCriteriaBuilder itemCriteriaBuilder;

    /**
     * @param pathList 业务主键ID
     * @return
     * @description: 根据菜单名称删除权限
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    public void deletePermByPath(List<String> pathList) {
        List<AuthPermission> needDeleteAuthPermissionList = authPermissionRepository.selectList(new LambdaQueryWrapper<AuthPermission>()
                .in(AuthPermission::getPermissionPath, pathList));
        if (CollectionUtils.isEmpty(needDeleteAuthPermissionList)) {
            return;
        }
        List<String> authPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        queryChildrenPerm(needDeleteAuthPermissionList, authPermissionIdList);
        log.info(">>>>>>>>>>>完整的树:{}<<<<<<<<<<", JSON.toJSONString(needDeleteAuthPermissionList));
        List<String> needDeleteAuthPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        authPermissionRepository.batchDeleteItem(needDeleteAuthPermissionIdList);
        authRolePermissionRepository.batchDeleteByAuthPermissionIdList(needDeleteAuthPermissionIdList);
    }

    /**
     * @param mainIdList 业务主键ID
     * @return
     * @description: 根据业务主键ID删除
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     */
    public void deletePermById(List<String> mainIdList) {
        List<AuthPermission> needDeleteAuthPermissionList = authPermissionRepository.selectList(new LambdaQueryWrapper<AuthPermission>()
                .in(AuthPermission::getAuthPermissionId, mainIdList));
        if (CollectionUtils.isEmpty(needDeleteAuthPermissionList)) {
            return;
        }
        List<String> authPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        queryChildrenPerm(needDeleteAuthPermissionList, authPermissionIdList);
        log.info(">>>>>>>>>>>完整的树:{}<<<<<<<<<<", JSON.toJSONString(needDeleteAuthPermissionList));
        List<String> needDeleteAuthPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        authPermissionRepository.batchDeleteItem(needDeleteAuthPermissionIdList);
        authRolePermissionRepository.batchDeleteByAuthPermissionIdList(needDeleteAuthPermissionIdList);
    }

    /**
     * 新增
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param addReq 新增Req
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addItem(AuthPermissionAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        String permissionPath = addReq.getPermissionPath();
        Integer permissionType = addReq.getPermissionType();
        Long permissionCount = authPermissionRepository.selectCount(new LambdaQueryWrapper<AuthPermission>()
                .eq(AuthPermission::getPermissionPath, permissionPath)
                .eq(AuthPermission::getPermissionType, permissionType));
        if(permissionCount > 0){
            throw new BusinessException(ErrorCode.PERMISSION_EXISTED_ERROR.getCode(),
                    ErrorCode.PERMISSION_EXISTED_ERROR.getMessage());
        }
        AuthPermission entity = mapperFacade.map(addReq, AuthPermission.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setAuthPermissionId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
            String parentId = addReq.getParentId();
            if(CheckParam.isNull(parentId)){
                entity.setParentId(AuthConstants.TOP_NODE_ID);
            }
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        authPermissionRepository.insert(entity);
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
        List<AuthPermission> needDeleteAuthPermissionList = authPermissionRepository.selectList(new LambdaQueryWrapper<AuthPermission>()
                .in(AuthPermission::getAuthPermissionId, mainIdList));
        if(CollectionUtils.isEmpty(needDeleteAuthPermissionList)){
            return;
        }
        List<String> authPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        queryChildrenPerm(needDeleteAuthPermissionList,authPermissionIdList);
        log.info(">>>>>>>>>>>完整的树:{}<<<<<<<<<<",JSON.toJSONString(needDeleteAuthPermissionList));
        List<String> needDeleteAuthPermissionIdList =
                needDeleteAuthPermissionList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        authPermissionRepository.batchDeleteItem(needDeleteAuthPermissionIdList);
        authRolePermissionRepository.batchDeleteByAuthPermissionIdList(needDeleteAuthPermissionIdList);
    }

    /**
     * 查询权限树集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询集合Req
     * @return java.util.List
     */
    @Override
    public List<AuthPermissionResp> queryPermissionTreeList(
            AuthPermissionReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>查询权限树集合Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<AuthPermission> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper,true);
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,AuthPermission::getCreateTime);
        //开始分页
        List<AuthPermission> entityList = authPermissionRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<String> authPermissionIdList =
                entityList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        //查询除所有子节点
        queryChildrenPerm(entityList,authPermissionIdList);
        //此处需要去重
        entityList = entityList.stream().filter(BaseUtil.distinctByKey(AuthPermission::getAuthPermissionId)).collect(Collectors.toList());
        List<AuthPermissionResp> respList = handlePermissionTree(entityList);
        AtomicInteger idBeginIndex = new AtomicInteger(BigInteger.ONE.intValue());
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return respList;
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return pro.skywalking.resp.page.Pagination
     */
    @Override
    public Pagination<AuthPermissionResp> queryByPage(
            AuthPermissionReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<AuthPermission> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper,true);
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,AuthPermission::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<AuthPermission> pageList = authPermissionRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<String> authPermissionIdList =
                pageList.stream().map(item -> item.getAuthPermissionId()).distinct().collect(Collectors.toList());
        queryChildrenPerm(pageList,authPermissionIdList);
        //此处需要去重
        pageList = pageList.stream().filter(BaseUtil.distinctByKey(AuthPermission::getAuthPermissionId)).collect(Collectors.toList());
        List<AuthPermissionResp> respList = handlePermissionTree(pageList);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().sorted(Comparator.comparing(AuthPermissionResp::getSortIndex)).forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
        });
        return PageBuilder.buildPageResult(page,respList);
    }

    /**
     * 根据主键ID查询菜单信息
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param mainId 菜单主键ID
     * @return pro.skywalking.resp.platform.auth.perm.AuthPermissionResp
     */
    @Override
    public AuthPermissionResp queryPermissionByMainId(String mainId){
        AuthPermission authPermission = authPermissionRepository.selectOne(new LambdaQueryWrapper<AuthPermission>()
                .eq(AuthPermission::getAuthPermissionId, mainId));
        if(CheckParam.isNull(authPermission)){
            return new AuthPermissionResp();
        }
        return mapperFacade.map(authPermission,AuthPermissionResp.class);
    }

    /**
     * 查询所有权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询所有权限集合Req
     * @return java.util.List
     */
    @Override
    public List<AuthPermissionResp> queryAllPermissionNodeList(
            AuthPermissionReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>查询所有权限集合Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<AuthPermission>();
        setPageCriteria(queryWrapper,pageReq);
        List<AuthPermission> pageList = authPermissionRepository.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return new ArrayList<>();
        }
        List<AuthPermissionResp> respList =
                mapperFacade.mapAsList(pageList, AuthPermissionResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
            //设置为根节点
            item.setRootStatus(Boolean.FALSE);
        });
        return respList;
    }

    /**
     * 查询权限集合
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 查询权限集合Req
     * @return java.util.List
     */
    @Override
    public List<AuthPermissionResp> queryPermissionNodeList(
            AuthPermissionReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>查询集合Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<AuthPermission>();
        setPageCriteria(queryWrapper,pageReq);
        List<AuthPermission> pageList = authPermissionRepository.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return new ArrayList<>();
        }
        List<AuthPermissionResp> respList =
                mapperFacade.mapAsList(pageList, AuthPermissionResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
        respList.stream().forEach(item -> {
            item.setId(Integer.valueOf(idBeginIndex.getAndIncrement()).longValue());
            //设置为根节点
            item.setRootStatus(Boolean.FALSE);
        });
        return respList;
    }

    /**
     * 处理查询所有子节点 最终authPermissionList将会包含所有节点数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param authPermissionList 权限集合
     * @param authPermissionIdList 节点ID
     */
    @Override
    public void queryChildrenPerm(List<AuthPermission> authPermissionList,
                                  List<String> authPermissionIdList){
        //查询出子级
        List<AuthPermission> subPermList = authPermissionRepository.selectList(new LambdaQueryWrapper<AuthPermission>()
                .in(AuthPermission::getParentId, authPermissionIdList));
        if(CollectionUtils.isEmpty(subPermList)){
            return;
        }
        authPermissionList.addAll(subPermList);
        List<String> subIdList = subPermList.stream().map(item -> item.getAuthPermissionId())
                .distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(subIdList)){
            return;
        }
        queryChildrenPerm(authPermissionList,subIdList);
    }

    /**
     * 处理树节点数据
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param dataList 数据
     * @return java.util.List
     */
    public static List<AuthPermissionResp> handlePermissionTree(List<AuthPermission> dataList) {
        List<AuthPermissionResp> resultList = new ArrayList<>();
        List<AuthPermissionResp> permissionNodeList = new ArrayList<>();
        dataList.forEach(item -> {
            AuthPermissionResp node = new AuthPermissionResp();
            node.setId(item.getId());
            node.setAuthPermissionId(item.getAuthPermissionId());
            node.setParentId(item.getParentId());
            node.setPermissionPath(item.getPermissionPath());
            node.setParentId(item.getParentId());
            node.setPermissionCode(item.getPermissionCode());
            node.setPermissionName(item.getPermissionName());
            node.setPermissionType(item.getPermissionType());
            node.setPermissionIcon(item.getPermissionIcon());
            node.setPermissionRemark(item.getPermissionRemark());
            node.setSortIndex(item.getSortIndex());
            node.setHasChildren(Boolean.FALSE);
            permissionNodeList.add(node);
        });
        // 遍历节点列表
        permissionNodeList.stream().forEach(item -> {
            if (StrUtil.equalsIgnoreCase(AuthConstants.TOP_NODE_ID, item.getParentId())) {
                // 查询子节点
                List<AuthPermissionResp> childrenNodeList = handleChildrenNode(item.getAuthPermissionId(),
                        permissionNodeList);
                if(!CollectionUtils.isEmpty(childrenNodeList)){
                    item.setChildren(childrenNodeList);
                    item.setHasChildren(Boolean.TRUE);
                    resultList.add(item);
                }else{
                    item.setChildren(Lists.newArrayList());
                    item.setHasChildren(Boolean.FALSE);
                    resultList.add(item);
                }
            }
        });
        return resultList;
    }

    /**
     * 拿到子节点的递归方法
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param nodeId 数据
     * @param permissionNodeList 节点数据
     * @return java.util.List
     */
    public static List<AuthPermissionResp> handleChildrenNode(String nodeId,
                                                              List<AuthPermissionResp> permissionNodeList) {
        List<AuthPermissionResp> resultList = new ArrayList<>();
        //选出子节点 排除顶级节点
        List<AuthPermissionResp> childrenNodeList =
                permissionNodeList.stream().filter(item -> StrUtil.equalsIgnoreCase(item.getParentId(), nodeId))
                        .filter(item -> !StrUtil.equalsIgnoreCase(AuthConstants.TOP_NODE_ID, item.getParentId()))
                        .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(childrenNodeList)){
            return resultList;
        }
        //循环子节点
        childrenNodeList.stream().forEach(item -> {
            resultList.add(item);
            List<AuthPermissionResp> childrenNode = handleChildrenNode(item.getAuthPermissionId(),
                    permissionNodeList);
            log.info(">>>>>>>>>>>>递归查询出来的子节点:<<<<<<<<<<<<<",JSON.toJSONString(childrenNode));
            if(!CollectionUtils.isEmpty(childrenNode)){
                // 递归获取子节点
                item.setChildren(childrenNode);
                //设置具备子节点
                item.setHasChildren(Boolean.TRUE);
            }else{
                item.setChildren(Lists.newArrayList());
                item.setHasChildren(Boolean.FALSE);
            }
        });
        return resultList;
    }

    /**
     * 设置分页条件
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param pageWrapper 查询条件
     * @param pageReq 分页插件
     * @return
     */
    private void setPageCriteria(LambdaQueryWrapper<AuthPermission> pageWrapper, AuthPermissionReq pageReq){

        if(!CheckParam.isNull(pageReq.getParentId())){
            pageWrapper.eq(AuthPermission::getParentId,pageReq.getParentId());
        }

        if(!CheckParam.isNull(pageReq.getPermissionType())){
            pageWrapper.eq(AuthPermission::getPermissionType,pageReq.getPermissionType());
        }

        if(!CheckParam.isNull(pageReq.getSortIndex())){
            pageWrapper.eq(AuthPermission::getSortIndex,pageReq.getSortIndex());
        }

        if(!CheckParam.isNull(pageReq.getPermissionName())){
            pageWrapper.like(AuthPermission::getPermissionName,pageReq.getPermissionName());
        }

        if(!CheckParam.isNull(pageReq.getPermissionPath())){
            pageWrapper.like(AuthPermission::getPermissionPath,pageReq.getPermissionPath());
        }

        if(!CheckParam.isNull(pageReq.getPermissionCode())){
            pageWrapper.like(AuthPermission::getPermissionCode,pageReq.getPermissionCode());
        }

        if(!CheckParam.isNull(pageReq.getPermissionRemark())){
            pageWrapper.like(AuthPermission::getPermissionRemark,pageReq.getPermissionRemark());
        }

    }

    /**
     * 更新
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param req 更新请求参数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateItem(AuthPermissionUpdateReq req){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        String mainId = req.getAuthPermissionId();
        String parentId = req.getParentId();
        String permissionPath = req.getPermissionPath();
        Integer permissionType = req.getPermissionType();
        Long permissionCount = authPermissionRepository.selectCount(new LambdaQueryWrapper<AuthPermission>()
                .eq(AuthPermission::getPermissionPath, permissionPath)
                .eq(AuthPermission::getPermissionType, permissionType)
                .ne(AuthPermission::getAuthPermissionId, mainId));
        if(permissionCount > 0){
            throw new BusinessException(ErrorCode.PERMISSION_EXISTED_ERROR.getCode(),
                    ErrorCode.PERMISSION_EXISTED_ERROR.getMessage());
        }
        if(StrUtil.equalsIgnoreCase(mainId,parentId)){
            throw new BusinessException(ErrorCode.PERMISSION_PARENT_ID_ERROR.getCode(),
                    ErrorCode.PERMISSION_PARENT_ID_ERROR.getMessage());
        }
        AuthPermission result = authPermissionRepository.selectOne(new LambdaQueryWrapper<AuthPermission>()
                .eq(AuthPermission::getAuthPermissionId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        String authUserId = authUserService.currentAuthUserId();
        result.setOperatorId(authUserId);
        setNeedUpdateItem(result,req);
        authPermissionRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 产业
     */
    private void setNeedUpdateItem(AuthPermission entity,
                                   AuthPermissionUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getParentId())){
            entity.setParentId(updateReq.getParentId());
        }
        if(!CheckParam.isNull(updateReq.getPermissionType())){
            entity.setPermissionType(updateReq.getPermissionType());
        }
        if(!CheckParam.isNull(updateReq.getSortIndex())){
            entity.setSortIndex(updateReq.getSortIndex());
        }
        if(!CheckParam.isNull(updateReq.getPermissionCode())){
            entity.setPermissionCode(updateReq.getPermissionCode());
        }
        if(!CheckParam.isNull(updateReq.getPermissionIcon())){
            entity.setPermissionIcon(updateReq.getPermissionIcon());
        }
        if(!CheckParam.isNull(updateReq.getPermissionName())){
            entity.setPermissionName(updateReq.getPermissionName());
        }
        if(!CheckParam.isNull(updateReq.getPermissionPath())){
            entity.setPermissionPath(updateReq.getPermissionPath());
        }
        if(!CheckParam.isNull(updateReq.getPermissionRemark())){
            entity.setPermissionRemark(updateReq.getPermissionRemark());
        }
    }
}
