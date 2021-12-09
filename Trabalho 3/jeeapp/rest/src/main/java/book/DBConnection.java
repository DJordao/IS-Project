package book;

import java.sql.*;

public class DBConnection {
    private Connection dbConnection;

    public Connection connectDB(){
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project3", "postgres", "My01pass");
            System.out.println("Connected to the PostgreSQL server successfully.");
            dbConnection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    public boolean addManager(String name){
        Connection c = connectDB();

        try{
            Statement stmt = c.createStatement();

            String sql = "INSERT INTO manager (name) " + "VALUES (?);";
            PreparedStatement myStmt = c.prepareStatement(sql);
            myStmt.setString(1, name);
            System.out.println("All good");

            myStmt.executeUpdate();
            myStmt.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

}
