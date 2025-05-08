package org.example.pi_dev.DataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private final String url = "jdbc:mysql://localhost:3306/pi_dev";
    private final String user = "root";
    private final String password = "";
    private Connection cnx;
    private static Connexion instance;

    public Connexion(){
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Connexion getInstance(){
        if(instance == null)
            instance = new Connexion();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}