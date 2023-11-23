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
    private String current_user;

    public Main(){
        this.listeAtelier=new ArrayList<Atelier>();
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
    //Possibilité : créer une fonction creerSchema qui, lorsqu'on crée un atelier sur une nouvelle base de données, créé son schéma

    //Vérifie la correspondance des données utilisateurs avec la base de données de l'atelier. Renvoie un String de la catégorie d'utilisateur (Admin, Utilisateur ou Aucun)
    public void demarrage(Atelier atelier, String utilisateur, String motDePasse){
        try{
            this.setupCon(atelier);
        }
        catch (Exception e){
        }
        try(PreparedStatement st = this.con.prepareStatement("SELECT role FROM utilisateur WHERE username EQUALS ? AND password EQUALS ?")){
            st.setString(1,utilisateur);
            st.setString(2,motDePasse);
            ResultSet resultat = st.executeQuery();
            if (resultat.next()){
                this.current_user=resultat.getString("username");
            }
            else{
                this.current_user="Aucun";
            }
        }
        catch (SQLException e){
            this.current_user="Aucun";
        }
        System.out.println(this.current_user);
    }

    public void afficherTableEntiere(){
         if (this.con != null) {
            try (Statement statement = this.con.createStatement()){
                String sqlQuery = "SELECT role FROM utilisateur";
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                while (resultSet.next()) {
                    //int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    //String e= resultSet.getString("etat");
                    //double p=resultSet.getDouble("puissance");
                    //listeMachine.add(new Machine(id, nom, e,p));
                    //System.out.println("ID : " + id + ", Nom : " + nom);
                    System.out.println(role);
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
        gestionnaire.creerAtelier("INSA", "//92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9");
        gestionnaire.demarrage(gestionnaire.listeAtelier.get(0),"Régis","regis03");
        gestionnaire.afficherTableEntiere();
    }

    public static void interfaceTextuelle(String[] args){

    }
}