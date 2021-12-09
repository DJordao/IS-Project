package book;



import data.Manager;

import java.sql.*;
import java.util.Calendar;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {
    //SERVER

    @GET
    @Path("/add")
    public String method2() {
        System.out.println("M2 executing....");
        String name = "name_" + new Time(Calendar.getInstance().getTimeInMillis());
        return name;
    }

    @GET
    @Path("/person")
    public String person() {
        String value = "Test";
        System.out.println("M3 executing... args=" + value);
        return value;
    }



    @POST
    @Path("/managerAdd")
    @Consumes(MediaType.APPLICATION_JSON)
    public String managerAdd(Manager m) {
        System.out.println("ENTREI NO SERVER!");

        DBConnection app = new DBConnection();
        System.out.println("1-Adding manager");
        app.connectDB();

        System.out.println("2-Adding manager");
        String str = "Person received : " + m.getName();

        if(app.addManager(m.getName()))
            System.out.println("Manager adicionado com sucesso");
        else
            System.out.println("Erro a adicionar o manager");

        return m.getName();
    }

}