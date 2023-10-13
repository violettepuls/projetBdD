package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
        gestionnaire.afficherTable();
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

    public void afficherTable(){
         if (this.con != null) {
            try (Statement statement = this.con.createStatement()){
                String sqlQuery = "SELECT * FROM Machine";
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) {
                    // Traitez les données récupérées ici
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("ref");
                    // ...

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
//Assurez-vous de remplacer nom_de_la_table par le nom de la table que vous souhaitez afficher.

//Exécuter l'application :
//Exécutez votre application Java. Elle se connectera à la base de données, exécutera la requête SQL et affichera les données de la table.
//N'oubliez pas de gérer les exceptions et les erreurs appropriées dans votre code pour garantir une manipulation sécurisée de la base de données.


    public static void main(String[] args) {
        debut();
    }

    public static void interfaceTextuelle(){

    }
}