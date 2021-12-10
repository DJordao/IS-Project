package standalone;

import com.google.gson.Gson;
import data.Client;
import data.Currency;
import data.Operation;
import org.json.JSONObject;

import java.util.ArrayList;

class JSONSchema {
    public static String serializeClient(ArrayList<Client> clients, ArrayList<Currency> currencies, String operation, String data) {
        String clientSchema = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"string\",\"optional\":true,\"field\":\"name\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"balance\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"credit\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"payment\"}, " +
                "{\"type\":\"int64\",\"optional\":false,\"field\":\"manager_id\"}],\"optional\":false}, ";
        String payload = "\"payload\":{\"id\":999,\"name\":\"error\",\"balance\":0.0,\"credit\":0.0,\"payment\":0.0,\"manager_id\":1}}";

        Gson g = new Gson();
        Operation op = g.fromJson(data, Operation.class);

        String id = op.getId();
        String price = op.getPrice();
        String currency = op.getCurrency();

        for(Client c : clients) {
            if(id.equals(c.getId())) {
                for(Currency cur : currencies) {
                    if(currency.equals(cur.getInitials())) {
                        String name = c.getName();
                        String balance = c.getBalance();
                        String credit = c.getCredit();
                        String payment = c.getPayment();
                        String manager_id = c.getManager_id();
                        String rate = cur.getRate();

                        if(operation.equals("credit")) {
                            balance = Float.toString(Float.parseFloat(balance) + Float.parseFloat(price) * Float.parseFloat(rate));
                            credit = Float.toString(Float.parseFloat(credit) + Float.parseFloat(price) * Float.parseFloat(rate));
                        }
                        else if(operation.equals("payment")){
                            balance = Float.toString(Float.parseFloat(balance) - Float.parseFloat(price) * Float.parseFloat(rate));
                            payment = Float.toString(Float.parseFloat(payment) + Float.parseFloat(price) * Float.parseFloat(rate));
                        }

                        payload = "\"payload\":{\"id\":" + id +",\"name\":\"" + name + "\",\"balance\":" + balance + ",\"credit\":" + credit + ",\"payment\":" + payment + ",\"manager_id\":" + manager_id + "}}";

                        return clientSchema + payload;
                    }

                }
                break;
            }
        }
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