package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classe_tables.Atelier;
import classe_tables.Machine;

public class Main {

    private Connection con;
    private ArrayList<Machine> listeMachine;
    private ArrayList<Atelier> listeAtelier;

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

    public void setupCon(Atelier a){
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+a.getBdd(),a.getNom_utilisateur(),a.getMdp());
            connect(c);
            System.out.println("Connexion établie");
        }
        catch (Exception e){
            System.out.println("echec de connexion :"+e);
        }
    }

    public void creerAtelier(String n, String b, String n_u, String m){
        if(uniciteAtelier(n, b, n_u)&&atelierPossible(b, n_u, m)){
            Atelier at=new Atelier(n, b, n_u, m);
            listeAtelier.add(at);
        }
        else{
            System.out.println("Cet atelier ne peut pas être crée");
        }
    }

    public boolean uniciteAtelier(String n, String b, String n_u){ //regarde si existe déjà dans la bdd
        for (int i=0;i<this.listeAtelier.size();i++){
            if ((listeAtelier.get(i).getNom()==n)||(listeAtelier.get(i).getBdd()==b)||(listeAtelier.get(i).getNom_utilisateur()==n_u)){
                System.out.println("Cet atelier existe déjà sous le nom ["+listeAtelier.get(i).getNom()+"], la base de données ["+listeAtelier.get(i).getBdd()+"] et le nom d'utilisateur ["+listeAtelier.get(i).getNom_utilisateur()+"]");
                return false;
            }
        }
        return true;
    }
    public boolean atelierPossible(String b, String n_u, String m){ //regarde si la bdd existe
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+b,n_u,m);
            connect(c);
            System.out.println("Création atelier possible");
            c.close();
            return true;
        }
        catch (Exception e){
            System.out.println("Une ou plusieurs informations de la base de données sont fausses :"+e);
            return false;
        }
    }

    public void demarrage(Atelier atelier, String utilisateur, String motDePasse){
        try{
            this.setupCon(atelier);
        }
        catch (Exception e){
        }
        try{
            this.nouvelleEntree();
        }
        catch (SQLException e){
        }
        this.afficherTableEntiere();
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
        Main gestionnaire=new Main();
        gestionnaire.debut();
    }

    public static void interfaceTextuelle(String[] args){

    }
}