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
import classe_tables.Utilisateur;

public class Gestionnaire {

    private Connection con;
    private ArrayList<Machine> listeMachine;
    private ArrayList<Atelier> listeAtelier;
    private String current_user;
    private Utilisateur cur_user;
    private Atelier cur_atelier;

    public Gestionnaire(){
        this.listeAtelier=new ArrayList<Atelier>();
    }
    public Gestionnaire(Connection con){
        this.con=con;
    }

    public ArrayList<Machine> getListeMachine(){
        return this.listeMachine;
    }

    public void setListMachine(ArrayList<Machine> l){
        this.listeMachine=l;
    }

    public ArrayList<Atelier> getListeAtelier(){
        ArrayList<Atelier> liste= new ArrayList<Atelier>();
        //recuperer les ateliers de la base de données, créer pour chaque ligne un atelier (renseigné completement), l'ajouter à la liste
        return liste;
    }

    public void setCurAtelier(int id){
        this.cur_atelier=this.listeAtelier.get(id);
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

    public boolean authentification(String usr, String mdp){
        try(PreparedStatement ps = this.con.prepareStatement("SELECT * FROM utilisateur WHERE username EQUALS ? AND password EQUALS ?")){

        }
    }

    public static void main(String[] args) {
        Gestionnaire gestionnaire=new Gestionnaire();
        gestionnaire.creerAtelier("INSA", "//92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9");
        gestionnaire.demarrage(gestionnaire.listeAtelier.get(0),"Régis","regis03");
        gestionnaire.afficherTableEntiere();
    }

    public static void interfaceTextuelle(String[] args){
        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.creerAtelier("INSA", "//92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9");
        String username = "";
        String mdp = "";
        String reponse = "";
        while (!authentification(username,mdp)){
            System.out.println("---------- Authentification ----------");
            System.out.println("Entrez votre nom d'utilisateur : ");
            username = Lire.S();
            System.out.println("Entrez votre mot de passe : ");
            mdp = Lire.S();
        }
        etapeAtelier(gestionnaire);
        while (reponse!="0"){
            System.out.println("---------- Choix de l'espace ----------");
            System.out.println("0) Fermer");
            System.out.println("1) Production");
            System.out.println("2) Machine");
            System.out.println("3) Produit");
            System.out.println("4) Opérateur");
            System.out.println("5) Gestion Atelier");
            reponse=Lire.S();
            switch (reponse)
        }
    }
    public static void etapeAtelier(Gestionnaire g){
        System.out.println("---------- Etape l'atelier ----------");
        System.out.println("1) Se connecter à un atelier");
        System.out.println("2) Créer un atelier");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        String reponse_2 = "";
        while (reponse =="2"){
            System.out.println("Nom de l'atelier : ");
            String r1=Lire.S();
            System.out.println("Mot de passe : ");
            String r2 = Lire.S();
            g.creerAtelier(r1,"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01",r2);
            System.out.println("---------- Etape atelier ----------");
            System.out.println("1) Se connecter à un atelier");
            System.out.println("2) Créer un atelier");
            System.out.println("3) Fermer");
            reponse = Lire.S();
        }
        if (reponse=="1"){
            System.out.println("A quel atelier se connecter ?");
            for (int i=0;i<g.getListeAtelier().size();i++){
                System.out.println(i+") "+g.getListeAtelier().get(i).getNom());
            }
            System.out.println("Numéro d'atelier : ");
            reponse_2 = Lire.S();
            g.setCurAtelier(Integer.parseInt(reponse_2));
        }
    }
    public void etapeProduction(Gestionnaire g){

    }
    public void etapeMachine(Gestionnaire g){
        
    }
    public void etapeProduit(Gestionnaire g){
        
    }
    public void etapeOperateur(Gestionnaire g){
        
    }
    public void etapeGestionAtelier(Gestionnaire g){
        
    }
}