package com.cn.summer.api.rocketMq.impl;

import ch.qos.logback.classic.Logger;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.cn.summer.api.rocketMq.IProducerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * .
 * 生产者.
 * @author YangYK
 * @create: 2019-11-20 15:08
 * @since 1.0
 */
@Service(value = "producerService")
public class ProducerImpl implements IProducerService{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProducerImpl.class);

    @Value("${apache.rocketmq.producer.groupName}")
    private String producerGroup;//生产者的组名
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;//NameServer 地址
    @Value("${apache.rocketmq.producer.topic}")
    private String topic;
    @Override
    public String defaultMqProducer(String messageInfo) throws Exception {
        // 生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        String resultMsg = "";
        try {
            /**
             * Producer对象在使用之前必须要调用start初始化,初始化一次即可
             * 注意:切记不可以在每次发送消息时都调用start方法
             */
            producer.start();
            // 创建一个小时实例,包含 topic.tag,和消息体
            // 如下: topic 为TopicTest tag 为 push
            Message message = new Message(topic, "",messageInfo.getBytes());
            StopWatch stop = new StopWatch();
            stop.start();
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    Integer id = (Integer) o;
                    int index = id % list.size();
                    return list.get(index);
                }
            },1);
            LOGGER.info("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
            stop.stop();
            resultMsg = result.getSendStatus().toString();
            LOGGER.info("----------------发送1条消息耗时:" + stop.getTotalTimeMillis());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }
        return resultMsg;
    }
}
