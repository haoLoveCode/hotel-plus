package cn.common.service.impl.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName skywalking-common
 * @Description:
 * @date 2024-03-25
 */
@Slf4j
public class DisruptorTest {

    public static void main(String[] args) {

        //初始化一个线程工厂
        ThreadFactory threadFactory = runnable -> new Thread(runnable, "ThreadFactory-Thread");

        EventFactory<NeedHandleEvent> eventFactory = new DataFactory();
        // RingBuffer 大小，必须是 2 的 N 次方；
        int ringBufferSize = 1024 * 1024;

        Disruptor<NeedHandleEvent> disruptor = new Disruptor<>(eventFactory,
                ringBufferSize, threadFactory, ProducerType.MULTI,
                new YieldingWaitStrategy());

        EventHandler<NeedHandleEvent> eventHandler = new DataEventHandler();
        disruptor.handleEventsWith(eventHandler);

        //开启disruptor
        disruptor.start();

        IntStream.range(0,10000).forEach(item -> {
            //环形的buffer
            RingBuffer<NeedHandleEvent> ringBuffer = disruptor.getRingBuffer();
            //下一个事件序号
            long sequence = ringBuffer.next();
            log.info(">>>>>>>>>>当前拿到的环形事件序列号:{}<<<<<<<<<",sequence);
            try {
                //获取该序号对应的事件对象
                NeedHandleEvent event = ringBuffer.get(sequence);
                event.setData("当前是第"+item+"循环的数据，此时拿到的sequence为:"+sequence);
            } finally{
                ringBuffer.publish(sequence);//发布事件；

            }
            log.info(">>>>>>启动disruptor成功<<<<<<");
        });

    }
}
