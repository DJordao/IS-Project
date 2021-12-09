package standalone;

import data.Client;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Streams {
    public static void main(String[] args) throws InterruptedException {
        String creditsTopic = "CreditsTopic";
        String paymentsTopic = "PaymentsTopic";
        String resultTopic = "ResultTopic";
        String dbInfoTopicClient = "DBInfoTopicClient";
        ArrayList<Client> clients = new ArrayList<>();
        Client client;

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "credit-topic");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Properties props2 = new Properties();
        props2.put(StreamsConfig.APPLICATION_ID_CONFIG, "payment-topic");
        props2.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props2.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props2.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Properties propsConsumer = new Properties();
        propsConsumer.put("bootstrap.servers", "localhost:9092");
        propsConsumer.put("acks", "all");
        propsConsumer.put("retries", 0);
        propsConsumer.put("batch.size", 16384);
        propsConsumer.put("linger.ms", 1);
        propsConsumer.put("buffer.memory", 33554432);
        propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "StreamsConsumer");
        propsConsumer.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumer.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(creditsTopic);
        //KTable<String, Long> outlines = lines.groupByKey().count();
        //outlines.mapValues(v -> "" + v).toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));
        /*lines.foreach((key, value) -> {
            System.out.println("entrei");
            JSONSchema.serializeClient(clients, value);
            System.out.println("sai");
        });*/
        lines.peek((key, value) -> System.out.println("Key: " + key + " --- Value: " + value));
        lines.mapValues((key, value) -> JSONSchema.serializeClient(clients, key, value))
                .to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        StreamsBuilder builder2 = new StreamsBuilder();
        KStream<String, String> lines2 = builder2.stream(paymentsTopic);
        //KTable<String, Long> outlines = lines.groupByKey().count();
        //outlines.mapValues(v -> "" + v).toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));
        lines2.peek((key, value) -> System.out.println("Key: " + key + " --- Value: " + value));
        lines2.mapValues((key, value) -> JSONSchema.serializeClient(clients, key, value))
                .to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams2 = new KafkaStreams(builder2.build(), props2);
        streams2.start();

        Consumer<String, String> consumer = new KafkaConsumer<>(propsConsumer);
        consumer.subscribe(Collections.singletonList(dbInfoTopicClient));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(5000);
            for (ConsumerRecord<String, String> record : records) {
                client = JSONSchema.deserializeClient(record.value());
                int check = 0;
                for(int i = 0; i < clients.size(); i++) {
                    if(clients.get(i).getId().equals(client.getId())) {
                        check = 1;
                        break;
                    }
                }
                if(check == 0) {
                    clients.add(client);
                    System.out.println(client);
                }
            }
        }
    }


}

//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},
// {"type":"string","optional":true,"field":"name"},{"type":"float","optional":true,"field":"balance"},
// {"type":"float","optional":true,"field":"credit"},{"type":"float","optional":true,"field":"payment"},
// {"type":"int64","optional":false,"field":"manager_id"}],"optional":false},
// "payload":{"id":1,"name":"client","balance":1.0,"credit":1.0,"payment":0.0,"manager_id":1}}

//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},
// {"type":"string","optional":true,"field":"name"}, {"type":"float","optional":true,"field":"balance"},
// {"type":"float","optional":true,"field":"credit"}, {"type":"float","optional":true,"field":"payment"},
// {"type":"int64","optional":false,"field":"manager_id"}],"optional":false},
// "payload":{"id":999,"name":"error","balance":0.0,"credit":0.0,"payment":0.0,"manager_id":1}}

//bin/connect-standalone.sh config/connect-standalone.properties config/connect-jdbc-source.properties config/connect-jdbc-sink.properties

