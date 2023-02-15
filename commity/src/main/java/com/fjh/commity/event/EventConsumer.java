package com.fjh.commity.event;

import com.alibaba.fastjson.JSONObject;
import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.entity.Event;
import com.fjh.commity.entity.Message;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.service.ElasticsearchServer;
import com.fjh.commity.service.MessageService;
import com.fjh.commity.util.CommunityConst;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConst {

    private final  static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private ElasticsearchServer elasticsearchServer;
    @Autowired
    private DiscussPostService discussPostService;
    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
    public void consumer(ConsumerRecord record){
        if(record == null || record.value() == null){
            logger.error("消息为空");
            return ;
        }
        //将消息转化为事件对象
        Event event = JSONObject.parseObject((String) record.value(),Event.class);
        if(event == null){
            logger.error("消息的格式错误");
            return ;
        }
        //将消息存储在MySQL数据库中
        Message message = new Message();
        message.setFromId(SYSTEM_USER);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String,Object> map = new HashMap<>();
        map.put("userId",event.getUserId());
        map.put("entityType",event.getEntityType());
        map.put("entityId",event.getEntityId());

        if(!event.getData().isEmpty()){
            for(String key : event.getData().keySet()){
                map.put(key,event.getData().get(key));
            }
        }
        message.setContent(JSONObject.toJSONString(map));
        messageService.addMessage(message);
    }
    @KafkaListener(topics = {TOPIC_PUSH_POST})
    public void handlePostMessage(ConsumerRecord record){
        if(record == null || record.value() == null){
            logger.error("消息为空");
            return ;
        }
        //将消息转化为事件对象
        Event event = JSONObject.parseObject((String) record.value(),Event.class);
        if(event == null){
            logger.error("消息的格式错误");
            return ;
        }
        DiscussPost discussPost = discussPostService.selectDiscussPostById(event.getEntityId());
        if(discussPost != null){
            elasticsearchServer.andPost(discussPost);
        }
    }
}
