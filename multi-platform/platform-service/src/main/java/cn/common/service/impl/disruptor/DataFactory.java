package cn.common.service.impl.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author Singer create by singer - Singer email:singer-coder@qq.com
 * @projectName skywalking-common
 * @Description:
 * @date 2024-03-06
 */
public class DataFactory implements EventFactory<NeedHandleEvent> {


    //返回一个新的实例
    @Override
    public NeedHandleEvent newInstance() {
        return new NeedHandleEvent();
    }
}
