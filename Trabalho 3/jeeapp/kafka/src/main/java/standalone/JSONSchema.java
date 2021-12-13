package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Currency;
import data.Manager;
import data.Operation;


class JSONSchema {
    public static String serializeClient(String data) {
        String clientSchema = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"string\",\"optional\":true,\"field\":\"name\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"balance\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"credit\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"payment\"}, " +
                "{\"type\":\"int64\",\"optional\":false,\"field\":\"manager_id\"}],\"optional\":false}, ";

        Gson g = new Gson();
        Client client = g.fromJson(data, Client.class);

        String payload = "\"payload\":{\"id\":" + client.getId() +",\"name\":\"" + client.getName() + "\",\"balance\":"
                + client.getBalance() + ",\"credit\":" + client.getCredit() + ",\"payment\":" + client.getPayment()
                + ",\"manager_id\":" + client.getManager_id() + "}}";
        System.out.println("Updated client");
        return clientSchema + payload;
    }

    public static Client deserializeClient(String data) {
        data = data.substring(369);
        String[] atts = data.split(",");
        Client client = new Client(atts[0].split(":")[1], atts[1].split(":")[1], atts[2].split(":")[1], atts[3].split(":")[1], atts[4].split(":")[1], atts[5].split(":")[1].substring(0, atts[5].split(":")[1].length() - 2));

        return client;
    }

    public static Currency deserializeCurrency(String data) {
        data = data.substring(169);
        String[] atts = data.split(",");
        Currency currency = new Currency(atts[0].split(":")[1], atts[1].split(":")[1].substring(0, atts[1].split(":")[1].length() - 2));

        return currency;
    }

    public static String serializeManager(String data) {
        String managerSchema = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"revenue\"}],\"optional\":false}, ";

        Gson g = new Gson();
        Manager manager  = g.fromJson(data, Manager.class);

        String payload = "\"payload\":{\"id\":" + manager.getId() + ",\"revenue\":"
                + manager.getBalance() + "}}";
        System.out.println("Updated manager");
        return managerSchema + payload;
    }

    public static boolean deserializeFlag(String data) {
        Gson g = new Gson();
        Operation op = g.fromJson(data, Operation.class);

        return Boolean.parseBoolean(op.getFlag());
    }

    public static String serializeClientWindow(String data) {
        String clientWindowSchema = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"bill\"}],\"optional\":false}, ";

        Gson g = new Gson();
        Client client  = g.fromJson(data, Client.class);

        String payload = "\"payload\":{\"id\":" + client.getId() + ",\"bill\":" + client.getBalance() + "}}";
        System.out.println("Updated client_window");
        return clientWindowSchema + payload;
    }

    public static String serializeClientPaymentsMonth(String data) {
        String clientPaymentsMonth = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"int32\",\"optional\":true,\"field\":\"n_payments\"}],\"optional\":false}, ";

        String id = data.split(" ")[0];
        String count = data.split(" ")[1];

        String payload = "\"payload\":{\"id\":" + id + ",\"n_payments\":" + count + "}}";
        System.out.println("Updated client_payments_month");
        return clientPaymentsMonth + payload;
    }
}
