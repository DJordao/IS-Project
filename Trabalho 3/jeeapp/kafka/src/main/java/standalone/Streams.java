package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Operation;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

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
            return oldval;
                });
                outlines.mapValues((value) -> JSONSchema.serializeClient(value))
                        .toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.cleanUp();
        streams.start();


        StreamsBuilder builder2 = new StreamsBuilder();
        KStream<String, String> lines2 = builder2.stream(paymentsTopic);
        KTable<String, String> outlines2 = lines2.groupByKey().reduce((oldval, newval) -> {
            System.out.println(oldval + "-" + newval);
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float balance = Float.parseFloat(oldOp.getBalance()) - Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setBalance(balance.toString());

            Float credit = Float.parseFloat(oldOp.getPayment()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setPayment(credit.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });
        outlines2.mapValues((value) -> JSONSchema.serializeClient(value))
                .toStream().to(resultTopic, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams2 = new KafkaStreams(builder2.build(), props2);
        streams2.cleanUp();
        streams2.start();

        while(true) {
            Thread.sleep(10000);
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

