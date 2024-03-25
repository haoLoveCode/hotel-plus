package cn.common.service.impl.platform;

import cn.common.repository.entity.platform.AuthRole;
import cn.common.repository.repository.platform.AuthRoleRepository;
import cn.common.service.platform.StatisticsService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.repository.result.AuthRoleCount;
import pro.skywalking.resp.platform.other.AuthRoleCountResp;
import pro.skywalking.utils.CheckParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Singer create by Singer email:singer-coder@qq.com
 * @packageName cn.common.service.impl
 * @Description: 统计相关服务方法实现
 * @date 2024-03-25
 */
@Service("statisticsService")
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {


    @Resource
    private AuthRoleRepository authRoleRepository;

    @Resource
    private MapperFacade mapperFacade;


    /**
      * 查询系统角色对应的用户数量
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-25
      * @param
      * @return java.util.List
      */
    @Override
    public List<AuthRoleCountResp> queryAuthRoleCount(){
        List<AuthRoleCount> authRoleCountList = authRoleRepository.countUserRole();
        if(CollectionUtils.isEmpty(authRoleCountList)){
            return Lists.newArrayList();
        }
        List<AuthRole> authRoleList = authRoleRepository.selectList(new LambdaQueryWrapper<>());
        HashMap<String, AuthRole> authRoleHashMap = authRoleList.stream()
                .collect(Collectors.toMap(AuthRole::getAuthRoleId, a -> a, (k1, k2) -> k1, HashMap::new));
        List<AuthRoleCountResp> respList = mapperFacade.mapAsList(authRoleCountList, AuthRoleCountResp.class);
        respList.stream().forEach(item -> {
            String authRoleId = item.getAuthRoleId();
            AuthRole authRole = authRoleHashMap.get(authRoleId);
            if(!CheckParam.isNull(authRole)){
                item.setRoleName(authRole.getRoleName());
            }else{
                item.setRoleName(StrUtil.EMPTY);
            }
        });
        return respList;
    }
}
