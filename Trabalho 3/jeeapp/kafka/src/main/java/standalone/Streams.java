package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Operation;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.json.JSONObject;

import java.time.Duration;
import java.util.Properties;

public class Streams {
    public static void main(String[] args) throws InterruptedException {
        String creditsTopic = "CreditsTopic";
        String paymentsTopic = "PaymentsTopic";
        String resultTopic = "client";

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

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(creditsTopic);
        KTable<String, String> outlines = lines.groupByKey().reduce((oldval, newval) -> {
            System.out.println(oldval + "-" + newval);
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float balance = Float.parseFloat(oldOp.getBalance()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setBalance(balance.toString());

            Float credit = Float.parseFloat(oldOp.getCredit()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setCredit(credit.toString());

            oldval = g.toJson(oldOp);
            //System.out.println(oldval);
            return oldval;
                });
        outlines.toStream().foreach((k, v) -> {
            System.out.println(k + " outlines " + v);
        });
        StreamsBuilder builder2 = new StreamsBuilder();
        KStream<String, String> lines2 = builder2.stream(paymentsTopic);
        KTable<String, String> outlines2 = lines2.groupByKey().reduce((oldval, newval) -> {
            System.out.println(oldval + "-" + newval);
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float balance = Float.parseFloat(oldOp.getBalance()) - Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setBalance(balance.toString());

            Float payment = Float.parseFloat(oldOp.getPayment()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setPayment(payment.toString());

            oldval = g.toJson(oldOp);
            //System.out.println(oldval);
            return oldval;
        });

        outlines2.toStream().foreach((k, v) -> {
            System.out.println(k + " outlines2 " + v);
        });
        KTable<String, String> outlines3 = outlines.join(outlines2, (leftValue, rightValue) -> {
            System.out.println("Left: " + leftValue);
            System.out.println("Right: " + rightValue);

            return "";
        });

        /*outlines.mapValues((value) -> JSONSchema.serializeClient(value))
                .toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));
        outlines2.mapValues((value) -> JSONSchema.serializeClient(value))
                .toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));*/

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.cleanUp();
        streams.start();

        KafkaStreams streams2 = new KafkaStreams(builder2.build(), props2);
        streams2.cleanUp();
        streams2.start();


        while(true) {
            Thread.sleep(10000);
        }
    }


}
