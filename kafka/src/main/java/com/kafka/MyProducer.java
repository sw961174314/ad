package com.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 生产者
 */
public class MyProducer {

    private static KafkaProducer<String, String> producer;

    static {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.10.100:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 配置消息分区分配器
        properties.put("partitions.class", "com.kafka.CustomPartition");
        producer = new KafkaProducer<>(properties);
    }

    /**
     * 不接受返回
     */
    public static void sendMessageForgetResult() {
        ProducerRecord<String, String> record = new ProducerRecord<>("test", "name", "ForgetResult"
        );
        producer.send(record);
        producer.close();
    }

    /**
     * 接受回复
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void sendMessageSync() throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>("test", "name", "SyncResult"
        );
        RecordMetadata result = producer.send(record).get();
        System.out.println(result.topic());
        System.out.println(result.partition());
        System.out.println(result.offset());
        producer.close();
    }

    /**
     * 异步消息
     */
    private static void sendMessageCallback() {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "test",
                "name",
                "name"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "test",
                "name-x",
                "name-1"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "test",
                "name-y",
                "name-y"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "test",
                "name-z",
                "name-z"
        );
        producer.send(record, new MyProducerCallback());

        record = new ProducerRecord<>(
                "test",
                "done",
                "done"
        );
        producer.send(record, new MyProducerCallback());

        producer.close();
    }

    private static class MyProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                e.printStackTrace();
                return;
            }
            System.out.println("MyProducerCallback");
            System.out.println("topic = " + recordMetadata.topic());
            System.out.println("partition = " + recordMetadata.partition());
            System.out.println("offset = " + recordMetadata.offset());
        }
    }

    public static void main(String[] args) throws Exception {
//        sendMessageForgetResult();
//        sendMessageSync();
        sendMessageCallback();
    }
}