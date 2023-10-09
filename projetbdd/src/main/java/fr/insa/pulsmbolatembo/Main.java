package fr.insa.pulsmbolatembo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    private Connection con;

    public Main(Connection con){
        this.con=con;
    }

    public Connection getConnection(){
        return this.con;
    }

    public void connect(Connection c){
        this.con=c;
    }
    
    public Connection setupCon(String url, String username, String mdp){
        Connection c;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url,username,mdp);
            System.out.println("Connexion établie");
        }
        catch (Exception e){
            System.out.println("echec de connexion :"+e);
        }
        return c;
    }

    public static void debut(){
        try (Connection con = connectSurServeurM3()){
            System.out.println("connecté");
            Main gestionnaire = new Main(con);
            gestionnaire.
        }
    }

    public static void main(String[] args) {
    }

    public static void interfaceTextuelle(){

    }
}