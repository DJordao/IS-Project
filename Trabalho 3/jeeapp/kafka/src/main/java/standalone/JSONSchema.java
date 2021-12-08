package standalone;

class JSONSchema {
    public String serializeClient(String client, String operation) {
        String clientSchema = "{\"schema\":{\"type\":\"struct\",\"fields\":" +
                "[{\"type\":\"int64\",\"optional\":false,\"field\":\"id\"}, " +
                "{\"type\":\"string\",\"optional\":true,\"field\":\"name\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"balance\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"credit\"}, " +
                "{\"type\":\"float\",\"optional\":true,\"field\":\"payment\"}, " +
                "{\"type\":\"int64\",\"optional\":false,\"field\":\"manager_id\"}],\"optional\":false}, ";


        return "";
    }

    public String deserializeClient(String client) {
        System.out.println(client.length());
        client = client.substring(367);
        System.out.println(client);
        return "";
    }

    public static String getClientId(String client) {
        return client.substring(375).substring(0, 1);
    }
}
//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},{"type":"string","optional":true,"field":"name"},{"type":"float","optional":true,"field":"balance"},{"type":"float","optional":true,"field":"credit"},{"type":"float","optional":true,"field":"payment"},{"type":"int64","optional":false,"field":"manager_id"}],"optional":false},"payload":{"id":1,"name":"client","balance":1.0,"credit":1.0,"payment":0.0,"manager_id":1}}
//{"schema":{"type":"struct","fields":[{"type":"int64","optional":false,"field":"id"},{"type":"string","optional":true,"field":"name"},{"type":"float","optional":true,"field":"balance"},{"type":"float","optional":true,"field":"credit"},{"type":"float","optional":true,"field":"payment"},{"type":"int64","optional":false,"field":"manager_id"}],"optional":false},"payload":{"id":2,"name":"client2","balance":0.0,"credit":0.0,"payment":0.0,"manager_id":1}}