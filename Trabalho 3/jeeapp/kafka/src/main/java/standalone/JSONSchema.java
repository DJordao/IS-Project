package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Currency;

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

        System.out.println(payload);
        return clientSchema + payload;
    }

    //{"id":207.66785,"price":4.1942263,"curency":"1"}

    public static Client deserializeClient(String data) {
        data = data.substring(369);
        String[] atts = data.split(",");
        Client client = new Client(atts[0].split(":")[1], atts[1].split(":")[1], atts[2].split(":")[1], atts[3].split(":")[1], atts[4].split(":")[1], atts[5].split(":")[1].substring(0, atts[5].split(":")[1].length() - 2));

        return client;
    }

    public static String getClientId(String client) {
        return client.substring(375).split(",")[0];
    }

    public static Currency deserializeCurrency(String data) {
        data = data.substring(169);
        String[] atts = data.split(",");
        Currency currency = new Currency(atts[0].split(":")[1], atts[1].split(":")[1].substring(0, atts[1].split(":")[1].length() - 2));

        return currency;
    }
}
//{"id":1
//"name":"client"
//"balance":1.0
//"credit":1.0
//"payment":0.0
//"manager_id":1}}

//{"schema":{"type":"struct","fields":[{"type":"string","optional":false,"field":"initials"},
// {"type":"float","optional":false,"field":"rate"}],"optional":false},"payload":{"initials":"USD","rate":1.1311}}

//"payload":{"id":2,"name":"client2","balance":0.0,"credit":0.0,"payment":0.0,"manager_id":1}}
//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},{"type":"string","optional":true,"field":"name"},{"type":"float","optional":true,"field":"balance"},{"type":"float","optional":true,"field":"credit"},{"type":"float","optional":true,"field":"payment"},{"type":"int64","optional":false,"field":"manager_id"}],"optional":false},"payload":{"id":3,"name":"client","balance":1.0,"credit":1.0,"payment":10.0,"manager_id":1}}
//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},{"type":"string","optional":true,"field":"name"},{"type":"float","optional":true,"field":"balance"},{"type":"float","optional":true,"field":"credit"},{"type":"float","optional":true,"field":"payment"},{"type":"int64","optional":false,"field":"manager_id"}],"optional":false},"payload":{"id":2,"name":"client2","balance":0.0,"credit":0.0,"payment":0.0,"manager_id":1}}