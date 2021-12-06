package standalone;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.Properties;
import java.util.Random;

public class Clients {
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++){
            Random r = new Random();

            float rCurC = 0.5f + r.nextFloat() * (10.0f - 0.5f);
            float rPriceC = 0.99f + r.nextFloat() * (500.0f - 0.99f);
            JSONObject credit = new JSONObject();
            credit.put("currency", rCurC);
            credit.put("price", rPriceC);

            float rCurP = 0.5f + r.nextFloat() * (10.0f - 0.5f);
            float rPriceP = 0.99f + r.nextFloat() * (500.0f - 0.99f);
            JSONObject payment = new JSONObject();
            payment.put("currency", rCurP);
            payment.put("price", rPriceP);

            String creditsTopic = "CreditsTopic";
            String paymentsTopic = "PaymentsTopic";

            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            Producer<String, String> producer = new KafkaProducer<>(props);

            producer.send(new ProducerRecord<>(creditsTopic, "credit", credit.toString()));
            producer.send(new ProducerRecord<>(paymentsTopic, "payment", payment.toString()));
        }

    }
}
