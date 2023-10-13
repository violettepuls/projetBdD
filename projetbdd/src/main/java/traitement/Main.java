package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private Connection con;

    public Main(){
    }
    public Main(Connection con){
        this.con=con;
    }

    public Connection getConnection(){
        return this.con;
    }

    public void connect(Connection c){
        this.con=c;
    }
    
    public void setupCon(String url, String username, String mdp){
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(url,username,mdp);
            connect(c);
            System.out.println("Connexion établie");
        }
        catch (Exception e){
            System.out.println("echec de connexion :"+e);
        }
    }

    public static void debut(){
        Main gestionnaire = new Main();
        try{
            gestionnaire.setupCon("jdbc:mysql://92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01","976e74f9");
        }
        catch (Exception e){
        }
        try{
            gestionnaire.creerSchema();
        }
        catch (SQLException e){
        }
    }

    public void creerSchema() throws SQLException{
        this.con.setAutoCommit(false);
        try(Statement st = this.con.createStatement()){
            //st.executeUpdate();
            //st.executeUpdate();
        }
        catch (SQLException ex){
            this.con.rollback();
            throw ex;
        }
        finally{
            this.con.setAutoCommit(true);
        }
    }



    public static void main(String[] args) {
        debut();
    }

    public static void interfaceTextuelle(){

    }
}