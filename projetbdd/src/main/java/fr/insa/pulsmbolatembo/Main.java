package fr.insa.pulsmbolatembo;

import java.sql.Connection;

public class Main {

    private Connection con;

    public Main(Connection con){
        this.con=con;
    }

    public Connection getConnection(){
        return this.con;
    }

    public void Connect(Connection c){
        this.con=c;
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
}