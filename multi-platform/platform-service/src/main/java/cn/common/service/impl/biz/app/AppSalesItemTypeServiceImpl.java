package cn.common.service.impl.biz.app;

import cn.common.repository.entity.biz.SalesItemType;
import cn.common.repository.repository.biz.SalesItemTypeRepository;
import cn.common.req.biz.openBiz.SalesItemTypeReq;
import cn.common.req.biz.openBiz.SalesItemTypeUpdateReq;
import cn.common.resp.biz.openBiz.SalesItemTypeResp;
import cn.common.service.biz.app.AppSalesItemTypeService;
import cn.common.service.platform.AuthUserService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.collection.CollectionUtils;
import pro.skywalking.constants.BaseConstant;
import pro.skywalking.utils.CheckParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Singer
 * @packageName cn.common.service
 * @Description: 商品类型信息相关服务方法实现
 * @date 2024-03-06
 */
@Service("appSalesItemTypeService")
@Slf4j
public class AppSalesItemTypeServiceImpl implements AppSalesItemTypeService {

    @Resource
    private SalesItemTypeRepository salesItemTypeRepository;

    @Resource
    private MapperFacade mapperFacade;

    @Resource
    private BaseConstant baseConstant;

    @Resource
    private AuthUserService authUserService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    /**
     * 查询商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public List<SalesItemTypeResp> querySalesItemType(SalesItemTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemType::getCreateTime);
        List<SalesItemType> entityList = salesItemTypeRepository.selectList(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,SalesItemTypeResp.class);
    }

    /**
     * 查询单个商品类型信息
     * @author: Singer
     * @date 2024-03-06
     * @param
     * @return java.util.List
     */
    @Override
    public SalesItemTypeResp queryOneSalesItemType(SalesItemTypeReq req){
        log.info(">>>>>>>>>>>>>>>>>查询单个商品类型信息Req {} <<<<<<<<<<<<<<<<", JSON.toJSONString(req));
        //构建查询条件
        LambdaQueryWrapper<SalesItemType> wrapper = new LambdaQueryWrapper<>();
        setCriteria(wrapper,req);
        wrapper.orderBy(true,false,SalesItemType::getCreateTime);
        SalesItemType entity = salesItemTypeRepository.selectOne(wrapper);
        if(CheckParam.isNull(entity)){
            return new SalesItemTypeResp();
        }
        return mapperFacade.map(entity,SalesItemTypeResp.class);
    }

    /**
     * 设置查询条件
     * @author: Singer
     * @date 2024-03-06
     * @param wrapper 查询条件
     * @param req 查询参数
     * @return
     */
    private void setCriteria(LambdaQueryWrapper<SalesItemType> wrapper,
                        SalesItemTypeReq req){

        if(!CheckParam.isNull(req.getTypeName())){
            wrapper.like(SalesItemType::getTypeName,req.getTypeName());
        }

        if(!CheckParam.isNull(req.getRemarkData())){
            wrapper.like(SalesItemType::getRemarkData,req.getRemarkData());
        }

        if(!CheckParam.isNull(req.getDataStatus())){
            wrapper.like(SalesItemType::getDataStatus,req.getDataStatus());
        }
    }

    /**
     * 设置需要更新的字段
     * @author: Singer
     * @date 2024-03-06
     * @param updateReq 更新参数
     * @param entity 实体
     */
    private void setNeedUpdateItem(SalesItemType entity,
        SalesItemTypeUpdateReq updateReq){
        if(!CheckParam.isNull(updateReq.getTypeName())){
            entity.setTypeName(updateReq.getTypeName());
        }
        if(!CheckParam.isNull(updateReq.getRemarkData())){
            entity.setRemarkData(updateReq.getRemarkData());
        }
        if(!CheckParam.isNull(updateReq.getDataStatus())){
            entity.setDataStatus(updateReq.getDataStatus());
        }
    }
}
