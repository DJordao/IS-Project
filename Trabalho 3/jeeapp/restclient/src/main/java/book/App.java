package book;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class App {
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/rest/services/myservice/add");
        Response response = target.request().get();
        String value = response.readEntity(String.class);
        System.out.println("RESPONSE1: " + value);
        response.close();
    }
}