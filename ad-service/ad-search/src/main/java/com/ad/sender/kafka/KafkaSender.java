package com.ad.sender.kafka;

import com.ad.mysql.dto.MySqlRowData;
import com.ad.sender.ISender;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 将增量数据投递到Kafka
 */
@Component("kafkaSender")
public class KafkaSender implements ISender {

    @Value("${adconf.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMySQLRowData(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessange = Optional.ofNullable(record.value());
        if (kafkaMessange.isPresent()) {
            Object message = kafkaMessange.get();
            MySqlRowData rowData = JSON.parseObject(message.toString(), MySqlRowData.class);
            System.out.println("kafka processMySQLRowData: " + JSON.toJSONString(rowData));
        }
    }
}