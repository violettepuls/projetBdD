package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import classe_tables.Machine;

public class Main {

    private Connection con;
    private ArrayList<Machine> listeMachine;

    public Main(){
    }
    public Main(Connection con){
        this.con=con;
    }

    public ArrayList<Machine> getListeMachine(){
        return this.listeMachine;
    }

    public void setListMachine(ArrayList<Machine> l){
        this.listeMachine=l;
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
            //gestionnaire.supprimerSchema();
            //gestionnaire.creerSchema();
            gestionnaire.nouvelleEntree();
        }
        catch (SQLException e){
        }
        gestionnaire.afficherTableEntiere();
    }

    public void creerSchema() throws SQLException{
        this.con.setAutoCommit(false);
        try(Statement st = this.con.createStatement()){
            st.executeUpdate(
                "create table machine_bis (\n"
                +" id integer not null primary key AUTO_INCREMENT,\n"
                +" ref varchar(30) not null unique,\n"
                +" etat varchar(30) not null, \n"
                +" puissance double not null\n"
                +")\n");
            st.executeUpdate(
                "create table utilisateur (\n"
                +" id integer not null primary key AUTO_INCREMENT,\n"
                +" prenom varchar(30) not null unique,\n"
                +" nom varchar(30) not null\n"
                +")\n");
        }
        catch (SQLException ex){
            this.con.rollback();
            throw ex;
        }
        finally{
            this.con.setAutoCommit(true);
        }
    }

    public void supprimerSchema() throws SQLException{
        try (Statement st = this.con.createStatement()){
            //try {
            //    st.executeUpdate("alter table machine_bis drop constraint fk_li_likes_u1");
            //}
            //catch (SQLException ex) {
            //}
            st.executeUpdate("drop table machine_bis");
        }
        catch(SQLException sqle){
        }
    }

    public void afficherTableEntiere(){
         if (this.con != null) {
            try (Statement statement = this.con.createStatement()){
                String sqlQuery = "SELECT * FROM machine_bis";
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("ref");
                    String e= resultSet.getString("etat");
                    double p=resultSet.getDouble("puissance");
                    listeMachine.add(new Machine(id, nom, e,p));
                    System.out.println("ID : " + id + ", Nom : " + nom);
                }

                resultSet.close();
                statement.close();
                this.con.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }
        }
    }

    public void nouvelleEntree() throws SQLException{
        this.con.setAutoCommit(false);
        try (PreparedStatement st = this.con.prepareStatement("insert into machine_bis (ref,etat,puissance) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setString(1,"3TT_evo_IV");
            st.setString(2,"HS");
            st.setInt(3,120);
            st.executeUpdate();
            System.out.println("Donnée envoyée");
        }
        catch(SQLException sqle){
            System.out.println("Echec d'envoie : " + sqle);
        }
        finally{
            this.con.setAutoCommit(true);
        }
    }

    public void supprimerEntree(){

    }

    public static void main(String[] args) {
        debut();
    }

    public static void interfaceTextuelle(){

    }
}