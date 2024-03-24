package cn.common.service.impl.platform;

import cn.common.repository.entity.platform.PlatformDict;
import cn.common.repository.repository.platform.PlatformDictRepository;
import cn.common.service.data.ItemCriteriaBuilder;
import cn.common.service.platform.AuthUserService;
import cn.common.service.platform.PlatformDictService;
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
import pro.skywalking.req.base.BaseDeleteReq;
import pro.skywalking.req.platform.dict.PlatformDictAddReq;
import pro.skywalking.req.platform.dict.PlatformDictReq;
import pro.skywalking.req.platform.dict.PlatformDictUpdateReq;
import pro.skywalking.resp.page.Pagination;
import pro.skywalking.resp.platform.dict.PlatformDictResp;
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
@Service("platformDictService")
@Slf4j
public class PlatformDictServiceImpl implements PlatformDictService {

    @Resource
    private PlatformDictRepository platformDictRepository;

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
        public void addItem(PlatformDictAddReq addReq){
        log.info(">>>>>>>>>>>>>>>>>新增Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(addReq));
        PlatformDict entity = mapperFacade.map(addReq, PlatformDict.class);
        try {
            String authUserId = authUserService.currentAuthUserId();
            BaseUtil.setFieldValueNotNull(entity);
            entity.setPlatformDictId(SnowflakeIdWorker.uniqueMainId());
            entity.setOperatorId(authUserId);
        } catch (Exception e) {
            log.error("新增->设置为空的属性失败 {} , {} ",e.getMessage(),e);
            throw new BusinessException(ErrorCode.ERROR.getCode(),
                    ErrorCode.ERROR.getMessage()+ StrUtil.COLON+e.getMessage()+StrUtil.COLON+e);
        }
        platformDictRepository.insert(entity);
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
        List<PlatformDict> entityList = platformDictRepository.selectList(
            new LambdaQueryWrapper<PlatformDict>().in(PlatformDict::getPlatformDictId,mainIdList));
        entityList.stream().forEach(item -> {
            platformDictRepository.deleteById(item);
        });
    }

    /**
      * 根据字典类型查询字典数据
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-06
      * @param dictType 字典类型
      * @return java.util.List
      */
    @Override
    public List<PlatformDictResp> queryByType(String dictType){
        List<PlatformDict> platformDictList = platformDictRepository
                .selectList(new LambdaQueryWrapper<PlatformDict>().eq(PlatformDict::getDictType, dictType));
        if(CollectionUtils.isEmpty(platformDictList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(platformDictList,PlatformDictResp.class);
    }

    /**
     * 分页查询
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param  pageReq 分页查询Req
     * @return Pagination
     */
    @Override
    public Pagination<PlatformDictResp> queryByPage(
        PlatformDictReq pageReq){
        log.info(">>>>>>>>>>>>>>>>>分页查询Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(pageReq));
        //构建查询条件
        LambdaQueryWrapper<PlatformDict> pageWrapper = new LambdaQueryWrapper<>();
        itemCriteriaBuilder.rigidCriteria(pageWrapper,true);
        setPageCriteria(pageWrapper,pageReq);
        pageWrapper.orderBy(true,false,PlatformDict::getCreateTime);
        //开始分页
        Page<Object> page = PageHelper.startPage(pageReq.getCurrentPage(), pageReq.getItemsPerPage());
        List<PlatformDict> pageList = platformDictRepository.selectList(pageWrapper);
        if (CollectionUtils.isEmpty(pageList)) {
            return PageBuilder.buildPageResult(page,new ArrayList<>());
        }
        List<PlatformDictResp> respList =
            mapperFacade.mapAsList(pageList, PlatformDictResp.class);
        Integer startIndex = (pageReq.getItemsPerPage() * pageReq.getCurrentPage()) - pageReq.getItemsPerPage() + 1;
        AtomicInteger idBeginIndex = new AtomicInteger(startIndex);
            respList.stream().forEach(item -> {
                //item.setPlatformDictId(String.valueOf(item.getId()));
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
    private void setPageCriteria(LambdaQueryWrapper<PlatformDict> pageWrapper, PlatformDictReq pageReq){

         if(!CheckParam.isNull(pageReq.getDictType())){
             pageWrapper.like(PlatformDict::getDictType,pageReq.getDictType());
         }

         if(!CheckParam.isNull(pageReq.getDictKey())){
             pageWrapper.like(PlatformDict::getDictKey,pageReq.getDictKey());
         }

         if(!CheckParam.isNull(pageReq.getDictValue())){
             pageWrapper.like(PlatformDict::getDictValue,pageReq.getDictValue());
         }

         if(!CheckParam.isNull(pageReq.getSortIndex())){
             pageWrapper.eq(PlatformDict::getSortIndex,pageReq.getSortIndex());
         }

         if(!CheckParam.isNull(pageReq.getDictRemark())){
             pageWrapper.like(PlatformDict::getDictRemark,pageReq.getDictRemark());
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
    public void updateItem(PlatformDictUpdateReq updateReq){
        log.info(">>>>>>>>>>>>>>>>>更新请求参数 {} <<<<<<<<<<<<<<<<", JSON.toJSONString(updateReq));
        String mainId = updateReq.getPlatformDictId();
        PlatformDict result = platformDictRepository.selectOne(new LambdaQueryWrapper<PlatformDict>()
                    .eq(PlatformDict::getPlatformDictId,mainId));
        if (CheckParam.isNull(result)) {
            return;
        }
        setNeedUpdateItem(result,updateReq);
        platformDictRepository.updateById(result);
    }

    /**
     * 设置需要更新的字段
     * @author: create by singer - Singer email:singer-coder@qq.com
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(PlatformDict entity,
        PlatformDictUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getDictType())){
            entity.setDictType(updateReq.getDictType());
        }
        if(!CheckParam.isNull(updateReq.getDictKey())){
            entity.setDictKey(updateReq.getDictKey());
        }
        if(!CheckParam.isNull(updateReq.getDictValue())){
            entity.setDictValue(updateReq.getDictValue());
        }
        if(!CheckParam.isNull(updateReq.getSortIndex())){
            entity.setSortIndex(updateReq.getSortIndex());
        }
        if(!CheckParam.isNull(updateReq.getDictRemark())){
            entity.setDictRemark(updateReq.getDictRemark());
        }
    }
}
