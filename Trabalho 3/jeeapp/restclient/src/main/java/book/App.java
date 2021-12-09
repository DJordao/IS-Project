package book;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.InputMismatchException;
import java.util.Scanner;



public class App {

    private static void interfaceManager(){
        System.out.println("----------------------------");
        System.out.println("Escolha uma opção:");
        System.out.println("1-> Add managers to the database");
        System.out.println("----------------------------");

    }

    private static void addManagers(){

        System.out.printf("Nome: ");
        Scanner managerInput = new Scanner(System.in);
        String managerName = managerInput.nextLine();

        while (true){
            try {

                if (String.valueOf(managerName).length() != 0 && !managerName.isBlank()) {
                    System.out.println("ENTEI AQUI!");


                    Client client = ClientBuilder.newClient();
                    WebTarget target = client.target("http://localhost:8080/rest/services/myservice/managerAdd");
                    Manager manager = new Manager(managerName);
                    Entity<Manager> input = Entity.entity(manager, MediaType.APPLICATION_JSON);
                    Response response = target.request().post(input);
                    String value = response.readEntity(String.class);
                    System.out.println("RESPONSE2: " + value);
                    response.close();
                    break;

                } else {
                    System.out.println("Insira nome do manager ");
                }

            }catch (InputMismatchException e){
                System.out.println("Insira nome do manager");
            }

        }
    }

    public static void main(String[] args) {
        //CLIENT

        //Exemplo
        Client client = ClientBuilder.newClient();

        /*WebTarget target = client.target("http://localhost:8080/rest/services/myservice/add");
        Response response = target.request().get();
        String value = response.readEntity(String.class);
        System.out.println("RESPONSE1: " + value);
        response.close();*/

        WebTarget target = client.target("http://localhost:8080/rest/services/myservice/person");
        Response response = target.request().get();
        String value = response.readEntity(String.class);
        System.out.println("RESPONSE3: " + value);
        response.close();

        /*Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/rest/services/myservice/addManager");
        target = target.queryParam("name", "xpto");
        Response response = target.request().get();
        String value = response.readEntity(String.class);
        System.out.println("RESPONSE2: " + value);
        response.close();*/

        while(true){
            interfaceManager();
            String option = "";

            try{
                System.out.printf("OPÇÃO: ");
                Scanner input = new Scanner(System.in);
                option = input.nextLine();
                Integer.parseInt(option);
                if(Integer.parseInt(option) <= 1){
                    switch(option){
                        case "1":
                            //Requisito 1 Add managers to the database
                            addManagers();
                    }

                }

            }catch(NumberFormatException e){}

        }


    }
}