package standalone;

import data.Client;
import data.Currency;
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
        ArrayList<Client> clients = new ArrayList<>();
        Client client;
        ArrayList<Currency> currencies = new ArrayList<>();
        Currency currency;
        String dbInfoTopicClient = "DBInfoTopicClient";
        String dbInfoTopicCurrency = "DBInfoTopicCurrency";
        String creditsTopic = "CreditsTopic";
        String paymentsTopic = "PaymentsTopic";
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
        propsProducer.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        propsProducer.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Properties propsConsumerClient = new Properties();
        propsConsumerClient.put("bootstrap.servers", "localhost:9092");
        propsConsumerClient.put("acks", "all");
        propsConsumerClient.put("retries", 0);
        propsConsumerClient.put("batch.size", 16384);
        propsConsumerClient.put("linger.ms", 1);
        propsConsumerClient.put("buffer.memory", 33554432);
        propsConsumerClient.put(ConsumerConfig.GROUP_ID_CONFIG, "ClientsConsumer");
        propsConsumerClient.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumerClient.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Properties propsConsumerCurrency = new Properties();
        propsConsumerCurrency.put("bootstrap.servers", "localhost:9092");
        propsConsumerCurrency.put("acks", "all");
        propsConsumerCurrency.put("retries", 0);
        propsConsumerCurrency.put("batch.size", 16384);
        propsConsumerCurrency.put("linger.ms", 1);
        propsConsumerCurrency.put("buffer.memory", 33554432);
        propsConsumerCurrency.put(ConsumerConfig.GROUP_ID_CONFIG, "ClientsConsumer2");
        propsConsumerCurrency.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumerCurrency.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Producer<String, String> producer = new KafkaProducer<>(propsProducer);

        Consumer<String, String> consumerClient = new KafkaConsumer<>(propsConsumerClient);
        consumerClient.subscribe(Collections.singletonList(dbInfoTopicClient));

        Consumer<String, String> consumerCurrency = new KafkaConsumer<>(propsConsumerCurrency);
        consumerCurrency.subscribe(Collections.singletonList(dbInfoTopicCurrency));

        while (true) {
            ConsumerRecords<String, String> recordsClient = consumerClient.poll(5000);
            for (ConsumerRecord<String, String> record : recordsClient) {
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
                }
            }
            //System.out.println("Clients: " + clients);

            ConsumerRecords<String, String> recordsCurrency = consumerCurrency.poll(5000);
            for (ConsumerRecord<String, String> record : recordsCurrency) {
                currency = JSONSchema.deserializeCurrency(record.value());
                int check = 0;
                for(int i = 0; i < currencies.size(); i++) {
                    if(currencies.get(i).getInitials().equals(currency.getInitials())) {
                        check = 1;
                        break;
                    }
                }
                if(check == 0) {
                    currencies.add(currency);
                }
            }
            //System.out.println("Currencies: " + currencies);
            try {
                System.out.println("Entrei");
                float rPriceC = 0.99f + rC.nextFloat() * (100.0f - 0.99f);
                JSONObject credit = new JSONObject();
                client = clients.get(rC.nextInt(clients.size()));
                credit.put("id", client.getId());
                credit.put("name", client.getName());
                credit.put("balance", client.getBalance());
                credit.put("credit", client.getCredit());
                credit.put("payment", client.getPayment());
                credit.put("manager_id", client.getManager_id());
                credit.put("price", rPriceC);
                credit.put("currency", currencies.get(rC.nextInt(currencies.size())).getRate());

                producer.send(new ProducerRecord<>(creditsTopic, client.getId(), credit.toString()));

                float rPriceP = 0.99f + rP.nextFloat() * (100.0f - 0.99f);
                JSONObject payment = new JSONObject();
                client = clients.get(rP.nextInt(clients.size()));
                payment.put("id", client.getId());
                payment.put("name", client.getName());
                payment.put("balance", client.getBalance());
                payment.put("credit", client.getCredit());
                payment.put("payment", client.getPayment());
                payment.put("manager_id", client.getManager_id());
                payment.put("price", rPriceP);
                payment.put("currency", currencies.get(rP.nextInt(currencies.size())).getRate());

                producer.send(new ProducerRecord<>(paymentsTopic, client.getId(), payment.toString()));
            } catch (java.lang.IllegalArgumentException e) {
                System.out.println("Reading clients and currencies...");
            }

            System.out.println("Enviado");
            Thread.sleep(/*rT.nextInt(1) **/ 10000);
        }

    }
}
