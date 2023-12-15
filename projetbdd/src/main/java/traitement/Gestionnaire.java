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
        initialiserConnection();
    }

    public void setCurAtelier(int id){
        this.cur_atelier=Atelier.getAtelier(id, this.con);
    }

    public void setCurUser(int id){
        this.cur_user=Utilisateur.getUtilisateur(id, this.con);
    }

    public Connection getConnection(){
        return this.con;
    }

    public void connect(Connection c){
        this.con=c;
    }

    public void initialiserConnection(){
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01","976e74f9");
            connect(c);
            System.out.println("Connexion établie");
        }
        catch (Exception e){
            System.out.println("echec de connexion :"+e);
        }
    }

    public void creerAtelier(String nom, String bdd, String nom_utilisateur, String mdp){
        if(uniciteAtelier(nom, mdp, nom_utilisateur)&&atelierPossible(mdp, nom_utilisateur, mdp)){
            Atelier at=new Atelier(nom, mdp, nom_utilisateur, mdp);
            at.enregistrer(this.con);
        }
        else{
            System.out.println("Cet atelier ne peut pas être crée");
        }
    }

    public boolean uniciteAtelier(String nom, String bdd, String nom_utilisateur){ //regarde si l'atelier existe déjà dans la bdd
        this.listeAtelier=Atelier.listerAtelier(this.con);
        for (int i=0;i<this.listeAtelier.size();i++){
            if ((listeAtelier.get(i).getNom()==nom)||(listeAtelier.get(i).getBdd()==bdd)||(listeAtelier.get(i).getNom_utilisateur()==nom_utilisateur)){
                System.out.println("Cet atelier existe déjà sous le nom ["+listeAtelier.get(i).getNom()+"], la base de données ["+listeAtelier.get(i).getBdd()+"] et le nom d'utilisateur ["+listeAtelier.get(i).getNom_utilisateur()+"]");
                return false;
            }
        }
        return true;
    }
    public boolean atelierPossible(String bdd, String nom_utilisateur, String mdp){ //regarde si la bdd existe, i.e. si on peut s'y connecter
        try {
            Connection ancienneConnection = this.con;
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+bdd,nom_utilisateur,mdp);
            connect(c);
            System.out.println("Création atelier possible");
            c.close();
            connect(ancienneConnection);
            return true;
        }
        catch (Exception e){
            System.out.println("Une ou plusieurs informations de la base de données sont fausses :"+e);
            return false;
        }
    }

    public void connectionAtelier(int idAtelier){
        this.cur_atelier=Atelier.getAtelier(idAtelier, this.con);
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+this.cur_atelier.getBdd(),this.cur_atelier.getNom_utilisateur(),this.cur_atelier.getMdp());
            connect(c);
            System.out.println("Connexion à l'atelier établie");
        }
        catch (Exception e){
            System.out.println("Echec de connexion :"+e);
        }
    }

    //Possibilité : créer une fonction creerSchema qui, lorsqu'on crée un atelier sur une nouvelle base de données, créé son schéma

    public boolean authentification(String usr, String mdp){
        try(PreparedStatement ps = this.con.prepareStatement("SELECT * FROM Utilisateur WHERE Nom_Utilisateur = ? AND MDP = ?")){
            ps.setString(1,usr);
            ps.setString(2,mdp);
            //System.out.println("Info : "+usr+" , "+mdp);
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
                System.out.println("Erreur d'authentification : vérifiez votre nom d'utilisateur ou votre mot de passe");
                return false;
            }
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return false;
        }
    }

    public static void main(String[] args) {
        interfaceTextuelle();
    }

    public static void interfaceTextuelle(){
        Gestionnaire gestionnaire = new Gestionnaire();
        String username = "";
        String mdp = "";
        String reponse = "";
            System.out.println("---------- Authentification ----------");
            System.out.println("Entrez votre nom d'utilisateur : ");
            username = Lire.S();
            System.out.println("Entrez votre mot de passe : ");
            mdp = Lire.S();
            gestionnaire.authentification(username,mdp);
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
        if (!Atelier.listerAtelier(this.con).isEmpty()){
            System.out.println("1) Se connecter à un atelier");
        }
        System.out.println("2) Créer un atelier");
        System.out.println("3) Supprimer un atelier");
        System.out.println("0) Fermer");
        String reponse = Lire.S();
        String reponse_2 = "";
        while (reponse!="0"){
            switch (reponse){
                case "0":
                break;
                case "1":
                this.listeAtelier=Atelier.listerAtelier(this.con);
                System.out.println("A quel atelier se connecter ?");
                for (int i=0;i<this.listeAtelier.size();i++){
                    System.out.println(this.listeAtelier.get(i).toString());
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
                System.out.println("3) Suprimer un atelier");
                System.out.println("0) Fermer");
                reponse = Lire.S();
                break;
                case "3":
                this.listeAtelier=Atelier.listerAtelier(this.con);
                System.out.println("Quel atelier supprimer ?");
                for (int i=0;i<this.listeAtelier.size();i++){
                    System.out.println(this.listeAtelier.get(i).toString());
                }
                System.out.println("Numéro d'atelier : ");
                reponse_2 = Lire.S();
                Atelier.supprimer(Integer.parseInt(reponse_2), this.con);
                break;
                default :
                System.out.println("Réponse invalide !");
                System.out.println("1) Se connecter à un atelier");
                System.out.println("2) Créer un atelier");
                System.out.println("3) Supprimer un atelier");
                System.out.println("0) Fermer");
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