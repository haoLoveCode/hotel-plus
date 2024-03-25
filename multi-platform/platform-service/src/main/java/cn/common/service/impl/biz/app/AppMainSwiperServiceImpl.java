package cn.common.service.impl.biz.app;

import cn.common.repository.entity.biz.MainSwiper;
import cn.common.repository.repository.biz.MainSwiperRepository;
import cn.common.resp.platform.MainSwiperResp;
import cn.common.service.biz.app.AppMainSwiperService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import pro.skywalking.collection.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName dining-platform
 * @Description: APP端查询轮播图
 * @date 2024-03-25
 */
@Service("appMainSwiperService")
@Slf4j
public class AppMainSwiperServiceImpl implements AppMainSwiperService {

    @Resource
    private MainSwiperRepository mainSwiperRepository;
    @Resource
    private MapperFacade mapperFacade;

    /**
     * 查询所有信息
     * @author: Singer
     * @date 2024-03-25
     * @param
     * @return java.util.List
     */
    @Override
    public List<MainSwiperResp> queryAllMainSwiper(){
        List<MainSwiper> entityList = mainSwiperRepository.selectList(new LambdaQueryWrapper<>());
        if(CollectionUtils.isEmpty(entityList)){
            return Lists.newArrayList();
        }
        return mapperFacade.mapAsList(entityList,MainSwiperResp.class);
    }
}
