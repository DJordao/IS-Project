package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Operation;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Streams {
    public static void main(String[] args) throws InterruptedException {
        String creditsTopic = "CreditsTopic";
        String paymentsTopic = "PaymentsTopic";
        String clientTopic = "client";
        String managerTopic = "manager";
        String clientWindowTopic = "client_window";
        String clientPaymentsMonth = "client_payments_month";

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
        KStream<String, String> streamCredits = builder.stream(creditsTopic);
        KStream<String, String> streamPayments = builder.stream(paymentsTopic);

        // Credit table
        KTable<String, String> tableCredits = streamCredits.groupByKey().reduce((oldval, newval) -> {
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float credit = Float.parseFloat(oldOp.getCredit()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setCredit(credit.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });

        // Payment table
        KTable<String, String> tablePayments = streamPayments.filterNot((k, v) -> JSONSchema.deserializeFlag(v)).groupByKey().reduce((oldval, newval) -> {
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float payment = Float.parseFloat(oldOp.getPayment()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setPayment(payment.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });

        // Revenue talbe
        KTable<String, String> tableRevenues = streamPayments.filter((k, v) -> JSONSchema.deserializeFlag(v)).groupByKey().reduce((oldval, newval) -> {
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float payment = Float.parseFloat(oldOp.getBalance()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setBalance(payment.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });
        tableRevenues.toStream().mapValues((k, v) -> JSONSchema.serializeManager(v)).to(managerTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Balance table
        KTable<String, String> tableBalance = tableCredits.join(tablePayments, (leftValue, rightValue) -> {
            Gson g = new Gson();
            Client left = g.fromJson(leftValue, Client.class);
            Client right = g.fromJson(rightValue, Client.class);
            System.out.println("Join: " + right);
            left.setPayment(right.getPayment());

            Float balance = Float.parseFloat(left.getCredit()) - Float.parseFloat(right.getPayment());
            left.setBalance(balance.toString());

            return g.toJson(left);
        });
        tableBalance.toStream().mapValues((k, v) -> JSONSchema.serializeClient(v)).to(clientTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Windowed credit table
        KTable<Windowed<String>, String> tableCreditsW = streamCredits.groupByKey().windowedBy(TimeWindows.of(TimeUnit.DAYS.toMillis(30))).reduce((oldval, newval) -> {
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float credit = Float.parseFloat(oldOp.getCredit()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setCredit(credit.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });
        KStream<String, String> streamCreditsS = tableCreditsW.toStream().map((key, value) -> KeyValue.pair(key.key(), value));
        KTable<String, String> tableCreditsS = streamCreditsS.groupByKey().reduce((oldval, newval) -> newval);
        tableCreditsS.toStream().mapValues((k, v) -> JSONSchema.serializeClientWindow(v)).to(clientWindowTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Windowed payment table
        KTable<Windowed<String>, String> tablePaymentsW = streamPayments.filterNot((k, v) -> JSONSchema.deserializeFlag(v)).groupByKey().windowedBy(TimeWindows.of(TimeUnit.DAYS.toMillis(30))).reduce((oldval, newval) -> {
            Gson g = new Gson();
            Operation oldOp = g.fromJson(oldval, Operation.class);
            Operation newOp = g.fromJson(newval, Operation.class);

            Float payment = Float.parseFloat(oldOp.getPayment()) + Float.parseFloat(newOp.getPrice()) * Float.parseFloat(newOp.getCurrency());
            oldOp.setPayment(payment.toString());

            oldval = g.toJson(oldOp);
            return oldval;
        });
        KStream<String, String> streamPaymentsS = tablePaymentsW.toStream().map((key, value) -> KeyValue.pair(key.key(), value));
        KTable<String, String> tablePaymentsS = streamPaymentsS.groupByKey().reduce((oldval, newval) -> newval);
        tablePaymentsS.toStream().mapValues((k, v) -> JSONSchema.serializeClientWindow(v)).to(clientWindowTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Windowed balance table
        KTable<Windowed<String>, String> tableBalanceW = tableCreditsW.join(tablePaymentsW, (leftValue, rightValue) -> {
            Gson g = new Gson();
            Client left = g.fromJson(leftValue, Client.class);
            Client right = g.fromJson(rightValue, Client.class);
            System.out.println("Windowed join: " + right);
            left.setPayment(right.getPayment());

            Float balance = Float.parseFloat(left.getCredit()) - Float.parseFloat(right.getPayment());
            left.setBalance(balance.toString());

            return g.toJson(left);
        });
        KStream<String, String> streamBalanceS = tableBalanceW.toStream().map((key, value) -> KeyValue.pair(key.key(), value));
        KTable<String, String> tableBalanceS = streamBalanceS.groupByKey().reduce((oldval, newval) -> newval);
        tableBalanceS.toStream().mapValues((k, v) -> JSONSchema.serializeClientWindow(v)).to(clientWindowTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Windowed payment table by month
        KTable<Windowed<String>, String> tablePaymentsMW = streamPayments.filterNot((k, v) -> JSONSchema.deserializeFlag(v)).groupByKey().windowedBy(TimeWindows.of(TimeUnit.DAYS.toMillis(60))).count().mapValues((k, v) -> v + "");
        KStream<String, String> streamPaymentsMS = tablePaymentsMW.toStream().map((key, value) -> KeyValue.pair(key.key(), value));
        KTable<String, String> tablePaymentsMS = streamPaymentsMS.groupByKey().reduce((oldval, newval) -> newval);
        tablePaymentsMS.toStream().mapValues((k, v) -> JSONSchema.serializeClientPaymentsMonth(k + " " + v)).to(clientPaymentsMonth, Produced.with(Serdes.String(), Serdes.String()));


        // Credit stream
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.cleanUp();
        streams.start();

        // Payment stream
        KafkaStreams streams2 = new KafkaStreams(builder.build(), props2);
        streams2.cleanUp();
        streams2.start();


        while(true) {

        }
    }
}
