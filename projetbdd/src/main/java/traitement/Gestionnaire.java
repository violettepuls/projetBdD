package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.websocket.Util;

import classe_tables.Atelier;
import classe_tables.Machine;
import classe_tables.Utilisateur;

public class Gestionnaire {

    private Connection con;
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
            System.out.println("Atelier créé avec succès");
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

    /*public void afficherTableEntiere(){
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
    }*/

    public boolean authentification(String usr, String mdp){
        try(PreparedStatement ps = this.con.prepareStatement("SELECT * FROM Utilisateur WHERE Nom_Utilisateur EQUALS ? AND MDP EQUALS ?")){
            ps.setString(1,usr);
            ps.setString(2,mdp);
            ResultSet resultat = ps.executeQuery();
            if(resultat.next()){
                int id=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String role = resultat.getString("Role");
                boolean op=resultat.getBoolean("Operateur");
                this.cur_user=new Utilisateur(id,nom, prenom, usr, role,op);
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("Erreur d'authentification : vérifiez votre nom d'utilisateur ou votre mot de passe");
            return false;
        }
    }

    public static void main(String[] args) {
        Gestionnaire gestionnaire=new Gestionnaire();
        gestionnaire.creerAtelier("INSA", "//92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9");
        gestionnaire.demarrage(gestionnaire.listeAtelier.get(0),"Régis","regis03");
        //gestionnaire.afficherTableEntiere();
    }

    public static void interfaceTextuelle(String[] args){
        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.creerAtelier("INSA", "//92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9"); //modifier cette fonction pour que l'atelier soit ajouté à la bdd
        String username = "";
        String mdp = "";
        String reponse = "";
        while (!gestionnaire.authentification(username,mdp)){
            System.out.println("---------- Authentification ----------");
            System.out.println("Entrez votre nom d'utilisateur : ");
            username = Lire.S();
            System.out.println("Entrez votre mot de passe : ");
            mdp = Lire.S();
        }
        gestionnaire.etapeAtelier();
        while (reponse!="0"){
            System.out.println("---------- Choix de l'espace ----------");
            System.out.println("0) Fermer");
            System.out.println("1) Production");
            System.out.println("2) Machine");
            System.out.println("3) Produit");
            System.out.println("4) Opérateur");
            System.out.println("5) Gestion Atelier");
            reponse=Lire.S();
            while (reponse!="0"){
                switch (reponse){
                    case "0":
                    break;
                    case "1":
                    gestionnaire.etapeProduction();
                    break;
                    case "2":
                    gestionnaire.etapeMachine();
                    break;
                    case "3":
                    gestionnaire.etapeProduit();
                    break;
                    case "4":
                    gestionnaire.etapeOperateur();
                    break;
                    case "5":
                    gestionnaire.etapeGestionAtelier();
                    break;
                    default:
                    System.out.println("Réponse invalide !");
                    System.out.println("0) Fermer");
                    System.out.println("1) Production");
                    System.out.println("2) Machine");
                    System.out.println("3) Produit");
                    System.out.println("4) Opérateur");
                    System.out.println("5) Gestion Atelier");
                    reponse=Lire.S();
                }
            }
        }
    }
    public void etapeAtelier(){
        System.out.println("---------- Etape Atelier ----------");
        System.out.println("1) Se connecter à un atelier");
        System.out.println("2) Créer un atelier");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        String reponse_2 = "";
        while (reponse!="3"){
            switch (reponse){
                case "1":
                System.out.println("A quel atelier se connecter ?");
                for (int i=0;i<Atelier.listerAtelier(this.con).size();i++){
                    System.out.println(Atelier.listerAtelier(this.con).get(i).toString());
                }
                System.out.println("Numéro d'atelier : ");
                reponse_2 = Lire.S();
                this.setCurAtelier(Integer.parseInt(reponse_2));
                break;
                case "2":
                System.out.println("Nom de l'atelier : ");
                String r1=Lire.S();
                System.out.println("Mot de passe : ");
                String r2 = Lire.S();
                this.creerAtelier(r1,"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01",r2);
                System.out.println("1) Se connecter à un atelier");
                System.out.println("2) Créer un atelier");
                System.out.println("3) Fermer");
                reponse = Lire.S();
                break;
                case "3":
                break;
                default :
                System.out.println("Réponse invalide !");
                System.out.println("1) Se connecter à un atelier");
                System.out.println("2) Créer un atelier");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
    public void etapeProduction(){
        System.out.println("---------- Etape Production ----------");
        System.out.println("1) Liste des produits");
        System.out.println("2) Panier");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        while (reponse != "3"){
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "3":
                break;
                default:
                System.out.println("Réponse invalide !");
                System.out.println("---------- Etape Production ----------");
                System.out.println("1) Liste des produits");
                System.out.println("2) Panier");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
    public void etapeMachine(){
        System.out.println("---------- Etape Machine ----------");
        System.out.println("1) Liste des machines");
        System.out.println("2) Ajouter une machine");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        while (reponse != "3"){
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "3":
                break;
                default:
                System.out.println("Réponse invalide !");
                System.out.println("---------- Etape Machine ----------");
                System.out.println("1) Liste des machines");
                System.out.println("2) Ajouter une machine");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
    public void etapeProduit(){
        System.out.println("---------- Etape Produit ----------");
        System.out.println("1) Liste des produits");
        System.out.println("2) Ajouter un produit");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        while (reponse != "3"){
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "3":
                break;
                default:
                System.out.println("Réponse invalide !");
                System.out.println("---------- Etape Produit ----------");
                System.out.println("1) Liste des produits");
                System.out.println("2) Ajouter un produit");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
    public void etapeOperateur(){
        System.out.println("---------- Etape Opérateur ----------");
        System.out.println("1) Liste des opérateurs");
        System.out.println("2) Ajouter un opérateur");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        while (reponse != "3"){
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "3":
                break;
                default:
                System.out.println("Réponse invalide !");
                System.out.println("---------- Etape Opérateur ----------");
                System.out.println("1) Liste des opérateur");
                System.out.println("2) Ajouter un opérateur");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
    public void etapeGestionAtelier(){
        System.out.println("---------- Etape Gestion atelier ----------");
        System.out.println("1) Paramètres d'atelier");
        System.out.println("2) Liste des utilisateurs");
        System.out.println("3) Fermer");
        String reponse = Lire.S();
        while (reponse != "3"){
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "3":
                break;
                default:
                System.out.println("Réponse invalide !");
                System.out.println("---------- Etape Opérateur ----------");
                System.out.println("1) Paramètre d'atelier");
                System.out.println("2) Liste des utilisateurs");
                System.out.println("3) Fermer");
                reponse = Lire.S();
            }
        }
    }
}

//idée 1 utilisation BDD : dans les classes-tables, faire des methodes statiques qui renvoient un objet de la classe en question et qui applique la commande sql souhaitée
//idée 2 utilisation BDD : assimiler chaque table à une classe et recreer la structure en java. L'interet de la bdd ici serait de récuperer une liste complète de données à chaque rafraichissement, il faudra donc penser à réinitialiser les listes a chaque rafraichissement.
// ==> on gardera l'idée 2. De plus, on utilisera les classes-tables (autre que Atelier) pour gérer les modifications des objets dans les tables.