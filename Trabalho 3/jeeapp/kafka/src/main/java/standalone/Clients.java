package standalone;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

public class Clients {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<String> clients = new ArrayList<>();
        String client;
        String dbInfoTopic = "DBInfoTopic";
        Random rC = new Random();
        Random rP = new Random();
        Random rT = new Random();

        Properties propsProducer = new Properties();
        propsProducer.put("bootstrap.servers", "localhost:9092");
        propsProducer.put("acks", "all");
        propsProducer.put("retries", 0);
        propsProducer.put("batch.size", 16384);
        propsProducer.put("linger.ms", 1);
        propsProducer.put("buffer.memory", 33554432);

        Properties propsConsumer = propsProducer;

        propsProducer.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        propsProducer.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "ClientsConsumer");
        propsProducer.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsProducer.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Producer<String, String> producer = new KafkaProducer<>(propsProducer);

        Consumer<String, String> consumer = new KafkaConsumer<>(propsConsumer);
        consumer.subscribe(Collections.singletonList(dbInfoTopic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord<String, String> record : records) {
                client = JSONSchema.getClientId(record.value());
                if(!clients.contains(client)) {
                    clients.add(client);
                }
                //System.out.println(clients);
            }

            float rCurC = 0.5f + rC.nextFloat() * (10.0f - 0.5f);
            float rPriceC = 0.99f + rC.nextFloat() * (500.0f - 0.99f);
            JSONObject credit = new JSONObject();
            credit.put("id", clients.get(rC.nextInt(clients.size())));
            credit.put("currency", rCurC);
            credit.put("price", rPriceC);

            float rCurP = 0.5f + rP.nextFloat() * (10.0f - 0.5f);
            float rPriceP = 0.99f + rP.nextFloat() * (500.0f - 0.99f);
            JSONObject payment = new JSONObject();
            payment.put("id", clients.get(rP.nextInt(clients.size())));
            payment.put("currency", rCurP);
            payment.put("price", rPriceP);

            String creditsTopic = "CreditsTopic";
            String paymentsTopic = "PaymentsTopic";

            producer.send(new ProducerRecord<>(creditsTopic, "credit", credit.toString()));
            producer.send(new ProducerRecord<>(paymentsTopic, "payment", payment.toString()));

            Thread.sleep(rT.nextInt(10) * 1000);
        }

    }
}
