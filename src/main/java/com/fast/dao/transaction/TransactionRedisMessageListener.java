package com.fast.dao.transaction;

import com.fast.config.FastDaoAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class TransactionRedisMessageListener {

    public static final String FAST_DAO_TRANSACTION_KEY = "FAST_DAO_TRANSACTION";

    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(FastDaoAttributes.getRedisConnectionFactory());
        //接受消息的key
        container.addMessageListener(listenerAdapter,new PatternTopic(FAST_DAO_TRANSACTION_KEY));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法
     * @return 监听
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(){
        return new MessageListenerAdapter(new TransactionReceiverRedisMessage(),"receiveMessage");
    }
}
