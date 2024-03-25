package cn.common.service.impl.disruptor;

import com.alibaba.fastjson2.JSON;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName skywalking-common
 * @Description: 处理数据的Handler
 * @date 2024-03-25
 */
@Slf4j
@Service
public class DataEventHandler implements EventHandler<NeedHandleEvent> {

    /**
      *
      * @description: 收到事件后的处理
      * @author: create by singer - Singer email:singer-coder@qq.com
      * @date 2024-03-25
      * @param  event
      * @param  sequence
      * @param  endOfBatch
      * @return
      */
    @Override
    public void onEvent(NeedHandleEvent event, long sequence, boolean endOfBatch) throws Exception {
        String jsonString = JSON.toJSONString(event);
        log.info(">>>>>>>>开始通过EventHandler.onEvent()处理事件{}<<<<<<",jsonString);
        log.info(">>>>>>>>处理的线程信息:{},{}<<<<<<",Thread.currentThread().getId(),
                Thread.currentThread().getName());
        /*RedisRepository redisRepository = SpringContextHandler.getBean(RedisRepository.class);
        //处理+1
        redisRepository.incr("EventHandler.onEvent()");*/
    }
}
