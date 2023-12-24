package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.websocket.Util;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import classe_tables.Atelier;
import classe_tables.Gamme;
import classe_tables.OperationElementaire;
import classe_tables.Produit;
import classe_tables.machine;
import classe_tables.Utilisateur;

public class Gestionnaire {

    private Connection con;
    private ArrayList<Atelier> listeAtelier;
    private Utilisateur cur_user;
    private Atelier cur_atelier;
    private ArrayList<Produit> cur_panier;

    public Gestionnaire() throws SQLException, ClassNotFoundException{
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

    public void initialiserConnection() throws SQLException, ClassNotFoundException{ // Initialisation de la connexion à notre base de données principale. C'est celle-ci qui contiendra la liste de tous les ateliers existants, dont chacun d'entre eux pourraient avoir ses données ailleurs.
        try {
            Connection c;
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql:"+"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01","976e74f9");
            c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("Connexion établie");
            connect(c);
        }
        catch (Exception e){
            System.out.println("echec de connexion :"+e);
        }
    }

    public void creerAtelier(String nom, String bdd, String nom_utilisateur, String mdp){
        if(uniciteAtelier(nom, mdp, nom_utilisateur)&&atelierPossible(bdd, nom_utilisateur, mdp)){
            Atelier at=new Atelier(nom, bdd, nom_utilisateur, mdp);
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

    public void refreshListeAtelier() throws SQLException{
        this.listeAtelier = Atelier.listerAtelier(this.con);
    }

    public void refreshListeMachine() throws SQLException{
        this.cur_atelier.setListeMachine(machine.listerMachine(cur_atelier, con));
    }

    public void refreshListeProduit() throws SQLException{
        this.cur_atelier.setListeProduit(Produit.listerProduit(cur_atelier, con));
    }

    public void refreshListeUtilisateur() throws SQLException{
        this.cur_atelier.setListeUtilisateur(Utilisateur.listerUtilisateur(cur_atelier, con));
    }

    //Possibilité : créer une fonction creerSchema qui, lorsqu'on crée un atelier sur une nouvelle base de données, créé son schéma et dupplique les informations de l'atelier relatif à cette bdd dans la table Atelier du schéma créé.

    //Possibilité : créer la fonction supprimerSchema relative à creerSchema.

    //Possibilité : créer une fonction verifierSchema qui vérifie si la base de donnée renseignée comporte déja une structure de bdd adéquate.

    //Possibilité : créer une fonction initialiserSchema, qui verifie l'existence d'un schema adéquat, et en crée un si ce n'est pas le cas.

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

    /*
    public repartitionMachine(){ // utilise le panier pour répartir les produits sur les différentes machines. Renvoie une liste de liste de double au format [id machine, id operation, temps début, temps fin]. Temps debut dépend de la disponibilité machine, temps fin = temps debut + temps opération, avec temps opération = (unité opération / vitesse machine). Vitesse machine est en [uo/s] et unité opération en [uo], une unité arbitraire donnée à chaque opération élémentaire (en réalité, le temps de fin dépend des conditions de coupe et du travail à effectuer).

    }
     */

    /*
    public calculDesTemps(){ // récupère la liste issue de repartition machine et calcule le temps écoulé entre le temps début minimum et le temps fin maximum

    }
     */

    /*
    public calculEnergie(){ // récupère la liste issue de repartition machine et calcule l'energie nécessaire à chaque étape pour finalement additionner le tout

    }
     */

    public static void main(String[] args) throws SQLException,ClassNotFoundException{
        interfaceTextuelle();
    }

    public static void interfaceTextuelle() throws SQLException,ClassNotFoundException{
        Gestionnaire gestionnaire = new Gestionnaire();
        String username = "";
        String mdp = "";
        String reponse = "-1";
        
        System.out.println("---------- Authentification ----------");
        System.out.println("Entrez votre nom d'utilisateur : ");
        username = Lire.S();
        System.out.println("Entrez votre mot de passe : ");
        mdp = Lire.S();
        gestionnaire.authentification(username,mdp);

        if(gestionnaire.etapeAtelier()){
        
            System.out.println("---------- Choix de l'espace ----------");
            while (reponse!="0"){
                System.out.println("0) Fermer");
                System.out.println("1) Production");
                System.out.println("2) Machine");
                System.out.println("3) Produit");
                System.out.println("4) Opérateur");
                System.out.println("5) Gestion Atelier");
                reponse=Lire.S();
                switch (reponse){
                    case "0":
                    reponse = "0";
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
                }
            }
        }
    }
    public boolean etapeAtelier(){
        boolean poursuiteDemarche = true;
        System.out.println("---------- Etape Atelier ----------");
        String reponse = "-1";
        while (reponse!="0"){
            System.out.println("0) Fermer");
            if (!Atelier.listerAtelier(this.con).isEmpty()){
                System.out.println("1) Se connecter à un atelier");
            }
            System.out.println("2) Créer un atelier");
            System.out.println("3) Supprimer un atelier");
            reponse = Lire.S();
            String reponse_2 = "";
            switch (reponse){
                case "0":
                reponse = "0";
                poursuiteDemarche = false;
                break;
                case "1":
                if (!Atelier.listerAtelier(this.con).isEmpty()){
                    this.listeAtelier=Atelier.listerAtelier(this.con);
                    System.out.println("A quel atelier se connecter ?");
                    for (int i=0;i<this.listeAtelier.size();i++){
                        System.out.println(this.listeAtelier.get(i).toString());
                    }
                    System.out.println("Numéro d'atelier : ");
                    reponse_2 = Lire.S();
                    this.setCurAtelier(Integer.parseInt(reponse_2));
                    // Ici, on devrait ajouter une ligne qui change la connexion de la base de données actuelle à celle de l'atelier. On ne le fait pas car il faudrait être en mesure de créer procéduralement le schéma sur une toute nouvelle base de donnée.
                    reponse = "0";
                    poursuiteDemarche = true;
                }
                else{
                    System.out.println("Il n'existe pas d'atelier. Créez un atelier pour commencer.");
                }
                break;
                case "2":
                System.out.println("Nom de l'atelier : ");
                String r1=Lire.S();
                System.out.println("Mot de passe : ");
                String r2 = Lire.S();
                this.creerAtelier(r1,"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01",r2);
                break;
                case "3":
                if (!Atelier.listerAtelier(this.con).isEmpty()){
                    this.listeAtelier=Atelier.listerAtelier(this.con);
                    System.out.println("Quel atelier supprimer ?");
                    for (int i=0;i<this.listeAtelier.size();i++){
                        System.out.println(this.listeAtelier.get(i).toString());
                    }
                    System.out.println("Numéro d'atelier : ");
                    reponse_2 = Lire.S();
                    Atelier.supprimer(Integer.parseInt(reponse_2), this.con);
                }
                else {
                    System.out.println("Il n'existe pas d'atelier.");
                }
                break;
                default :
                System.out.println("Réponse invalide !");
            }
        }
        return poursuiteDemarche;
    }
    public void etapeProduction() throws SQLException{
        System.out.println("---------- Etape Production ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des produits");
            System.out.println("2) Afficher le panier");
            System.out.println("3) Ajouter un produit au panier");
            System.out.println("4) Supprimer du panier");
            System.out.println("5) Lancer la production");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                refreshListeProduit();
                for (int i=0;i<this.cur_atelier.getListeProduit().size();i++){
                    System.out.println(this.cur_atelier.getListeProduit().get(i).toString());
                }
                break;
                case "2":
                for (int i=0;i<this.cur_panier.size();i++){
                    System.out.println(this.cur_panier.get(i).toString());
                }
                break;
                case "3":
                refreshListeProduit();
                for (int i=0;i<this.cur_atelier.getListeProduit().size();i++){
                    System.out.println(this.cur_atelier.getListeProduit().get(i).toString());
                }
                System.out.println("Entrez l'ID du produit à ajouter : ");
                String r1 = Lire.S();
                this.cur_panier.add(Produit.getProduit(Integer.parseInt(r1), this.con));
                break;
                case "4":
                for (int i=0;i<this.cur_panier.size();i++){
                    System.out.println(this.cur_panier.get(i).toString());
                }
                System.out.println("Entrez l'ID du produit à enlever : ");
                String r2 = Lire.S();
                try{
                    this.cur_panier.remove(Produit.getProduit(Integer.parseInt(r2), this.con));
                }
                catch (Exception e){
                    System.out.println("Cet objet n'est pas dans le panier.");
                }
                break;
                case "5":
                System.out.println("Produit en cours de production : ");
                for (int i=0;i<this.cur_panier.size();i++){
                    System.out.println(this.cur_panier.get(i).toString());
                }
                // repartition machine
                // calcul des temps de production
                // calcul de l'énergie de production
                reponse = "0";
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void etapeMachine() throws SQLException{
        System.out.println("---------- Etape Machine ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des machines");
            System.out.println("2) Ajouter une machine");
            System.out.println("3) Supprimer une machine");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                this.cur_atelier.setListeMachine(machine.listerMachine(cur_atelier,this.con));
                for(int i=0; i<this.cur_atelier.getListeMachine().size();i++){
                    System.out.println(this.cur_atelier.getListeMachine().get(i).toString());
                }
                break;
                case "2":
                System.out.println("Nom : ");
                String r1 = Lire.S();
                System.out.println("Reference : ");
                String r2 = Lire.S();
                System.out.println("Etat : ");
                String r3 = Lire.S();
                System.out.println("Puissance : ");
                double r4 = Lire.d();
                machine.creerMachine(r1, r2,r3, r4, this.cur_atelier, this.con);
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    
    public void etapeProduit(){
        System.out.println("---------- Etape Produit ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des produits");
            System.out.println("2) Ajouter un produit");
            System.out.println("3) Supprimer un produit");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void etapeOperateur(){
        System.out.println("---------- Etape Opérateur ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des opérateurs");
            System.out.println("2) Ajouter un opérateur");
            System.out.println("3) Supprimer un opérateur");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void etapeGestionAtelier(){
        System.out.println("---------- Etape Gestion atelier ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Paramètres d'atelier");
            System.out.println("2) Paramètres des utilisateurs");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                break;
                case "2":
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
}

//idée 1 utilisation BDD : dans les classes-tables, faire des methodes statiques qui renvoient un objet de la classe en question et qui applique la commande sql souhaitée
//idée 2 utilisation BDD : assimiler chaque table à une classe et recreer la structure en java. L'interet de la bdd ici serait de récuperer une liste complète de données à chaque rafraichissement, il faudra donc penser à réinitialiser les listes a chaque rafraichissement.
// ==> on gardera l'idée 2. De plus, on utilisera les classes-tables (autre que Atelier) pour gérer les modifications des objets dans les tables.