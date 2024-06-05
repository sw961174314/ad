package com.kafka;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * 消费者
 */
public class MyConsumer {

    private static KafkaConsumer<String, String> consumer;
    private static Properties properties;

    static {
        properties = new Properties();
        properties.put("bootstrap.servers", "192.168.10.100:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "kafka");
    }

    /**
     * 自动提交位移的消费者
     */
    private static void generalConsumeMessageAutoCommit() {
        properties.put("enable.auto.commit", true);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("test"));

        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic=%s, partition=%s, key=%s, value=%s",
                            record.topic(), record.partition(), record.key(), record.value()));
                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动提交位移
     */
    private static void generalConsumerMessageSyncCommit() {
        properties.put("enable.auto.commit", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("test"));

        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic=%s, partition=%s, key=%s, value=%s",
                            record.topic(), record.partition(), record.key(), record.value()));

                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }

                try {
                    consumer.commitSync();
                } catch (CommitFailedException e) {
                    System.out.println("commit failed error:" + e.getMessage());
                }

                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 异步提交位移
     */
    private static void generalConsumerMessageASyncCommit() {
        properties.put("enable.auto.commit", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("test"));

        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic=%s, partition=%s, key=%s, value=%s",
                            record.topic(), record.partition(), record.key(), record.value()));

                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }

                consumer.commitAsync();

                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 异步提交
     */
    private static void generalConsumerMessageASyncCommitWithCallback() {
        properties.put("enable.auto.commit", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("test"));

        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic=%s, partition=%s, key=%s, value=%s",
                            record.topic(), record.partition(), record.key(), record.value()));

                    if (record.value().equals("done")) {
                        flag = false;
                    }
                }

                consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        System.out.println("offsets:" + offsets);
                        System.out.println("commit failed for offsets:" + exception.getMessage());
                    }

                });

                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 混合提交
     */
    private static void mixSyncAndAsyncCommit() {
        properties.put("enable.auto.commit", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("test"));

        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic=%s, partition=%s, key=%s, value=%s",
                            record.topic(), record.partition(), record.key(), record.value()));
                }

                // 异步提交，可能发生异常
                consumer.commitAsync();
            }
        } catch (Exception e) {
            System.out.println("commit async error:" + e.getMessage());
        } finally {
            try {
                // 同步提交
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
//        generalConsumerMessageAutoCommit();
//        generalConsumerMessageSyncCommit();
//        generalConsumerMessageASyncCommit();
//        generalConsumerMessageASyncCommitWithCallback();
        mixSyncAndAsyncCommit();
    }
}