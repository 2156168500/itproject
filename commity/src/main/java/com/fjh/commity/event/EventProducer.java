package com.fjh.commity.event;

import com.alibaba.fastjson.JSONObject;
import com.fjh.commity.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void producer(Event event){
        if(event == null){
            return ;
        }
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
