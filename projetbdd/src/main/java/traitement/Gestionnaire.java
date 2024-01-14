package traitement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;


import classe_tables.Atelier;
import classe_tables.Gamme;
import classe_tables.OperationElementaire;
import classe_tables.Produit;
import classe_tables.SousPanier;
import classe_tables.machine;
import classe_tables.Utilisateur;

public class Gestionnaire {

    private Connection con;
    private ArrayList<Atelier> listeAtelier;
    private Utilisateur cur_user;
    private Atelier cur_atelier;
    private ArrayList<Produit> cur_panier;
    private ArrayList<SousPanier> panierArrange;
    private long tempsDebut;
    private long tempsFin;
    private double energie; // en w.h

    public Gestionnaire() throws SQLException, ClassNotFoundException{
        this.listeAtelier=new ArrayList<Atelier>();
        initialiserConnection();
    }

    public void setListeAtelier(ArrayList<Atelier> listeAtelier){
        this.listeAtelier = listeAtelier;
    }

    public ArrayList<Atelier> getListeAtelier(){
        return this.listeAtelier;
    }

    public long getTempsDebut(){
        return this.tempsDebut;
    }

    public long getTempsFin(){
        return this.tempsFin;
    }

    public double getEnergie(){
        return this.energie;
    }

    public void setCurAtelier(int id){
        this.cur_atelier=Atelier.getAtelier(id, this.con);
    }

    public void setCurUser(int id){
        this.cur_user=Utilisateur.getUtilisateur(id, this.con);
    }

    public Utilisateur getCurUser(){
        return this.cur_user;
    }

    public Connection getConnection(){
        return this.con;
    }

    public void connect(Connection c){
        this.con=c;
    }

    public Atelier getCurAtelier(){
        return this.cur_atelier;
    }

    public ArrayList<Produit> getPanier(){
        return this.cur_panier;
    }

    public void ajouterAuPanier(Produit p){
        this.cur_panier.add(p);
    }

    public void reinitialiserPanier(){
        this.cur_panier=new ArrayList<Produit>();
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

    public void creerAtelier(String nom, String bdd, String nom_utilisateur, String mdp)throws SQLException{
        if(uniciteAtelier(nom, mdp, nom_utilisateur)&&atelierPossible(bdd, nom_utilisateur, mdp)){
            Atelier at=new Atelier(nom, bdd, nom_utilisateur, mdp);
            at.enregistrer(this.con);
            Utilisateur.associerAtelierUtilisateur(this.cur_user.getId(),Atelier.getIdAtelier(nom,bdd,nom_utilisateur,mdp,this.con),this.con);
            this.setCurAtelier(Atelier.getIdAtelier(nom,bdd,nom_utilisateur,mdp,this.con));
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
    /*
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
    */

    //Possibilité : créer la fonction supprimerSchema relative à creerSchema.

    //Possibilité : créer une fonction verifierSchema qui vérifie si la base de donnée renseignée comporte déja une structure de bdd adéquate.

    //Possibilité : créer une fonction initialiserSchema, qui verifie l'existence d'un schema adéquat, et en crée un si ce n'est pas le cas.

    public boolean authentification(String usr, String mdp) throws SQLException{
        this.cur_user=Utilisateur.verifierUtilisateur(usr, mdp, con);
        if (this.cur_user==null){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean autorisationAtelier(int idAtelier)throws SQLException{
        if (Utilisateur.listerAtelierUtilisateur(this.cur_user.getId(), con).contains(idAtelier)){
            this.setCurAtelier(idAtelier);
            this.cur_panier=new ArrayList<Produit>();
            return true;
        }
        else{
            return false;
        }
    }

    public void arrangerPanier(){
        this.panierArrange =new ArrayList<SousPanier>();
        this.tempsDebut = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli(); //modifier pour l'heure locale
        System.out.println("Temps début : "+tempsDebut);
        ArrayList<Produit> liste = new ArrayList<Produit>();
        for (int i=0;i<cur_panier.size();i++){
            if (!liste.contains(cur_panier.get(i))){
                liste.add(cur_panier.get(i));
            }
        }
        for (int i=0;i<liste.size();i++){
            panierArrange.add(new SousPanier(this,liste.get(i), Collections.frequency(cur_panier, liste.get(i))));
        }
    }

    public void repartitionMachine()throws SQLException{ // utilise le panier pour répartir les produits sur les différentes machines. Renvoie une liste de liste de double au format [id machine, id operation, temps début, temps fin]. Temps debut dépend de la disponibilité machine, temps fin = temps debut + temps opération, avec temps opération = (unité opération / vitesse machine). Vitesse machine est en [uo/h] et unité opération en [uo], une unité arbitraire donnée à chaque opération élémentaire (en réalité, le temps de fin dépend des conditions de coupe et du travail à effectuer).
        con.setAutoCommit(false);
        try{
            this.energie=0;
            arrangerPanier();
            long finFabrication = 0;
            int nombreMaxOperation = 0;
            for (int i=0;i<panierArrange.size();i++){
                if(nombreMaxOperation<panierArrange.get(i).getNombreOperation()){
                    nombreMaxOperation=panierArrange.get(i).getNombreOperation();
                }
            }
            for(int i=0;i<nombreMaxOperation;i++){
                for(int j=0;j<panierArrange.size();j++){
                    double dureeSousPanier = 0;
                    for (int k=j;k<panierArrange.size();k++){
                        if(dureeSousPanier<panierArrange.get(k).getUo()){
                            dureeSousPanier=panierArrange.get(k).getUo();
                            SousPanier temp = panierArrange.get(k);
                            panierArrange.remove(panierArrange.get(k));
                            panierArrange.add(j,temp);
                        }
                    }
                    if(dureeSousPanier!=0){
                        machine choixMachine = choisirMachine(panierArrange.get(j));
                        //System.out.println(choixMachine.getNom());
                        //System.out.println("Début retenu : "+panierArrange.get(j).getDebutTemp());
                        //System.out.println("Fin opération : "+panierArrange.get(j).getStadeHoraire());
                        if(finFabrication<=panierArrange.get(j).getStadeHoraire()){
                            this.tempsFin=panierArrange.get(j).getStadeHoraire();
                        }
                        machine.ajouterIndisponibilite(choixMachine.getId(), choixMachine.getNom()+" : "+panierArrange.get(j).nommerStade(),panierArrange.get(j).getDebutTemp(), panierArrange.get(j).getStadeHoraire(), this.con);
                        panierArrange.get(j).etapeSuivante();
                    }
                }
            }
            System.out.println("Terminé !");
        }
        catch(SQLException e){
            System.out.println("Erreur répartition : "+e);
            con.rollback();
        }
        finally{
            con.setAutoCommit(true);
        }
    }

    public machine choisirMachine(SousPanier ssPanier)throws SQLException{
        OperationElementaire operation = ssPanier.getOperationRestante().get(0);
        long debut = ssPanier.getStadeHoraire();
        ArrayList<machine> listeMachine = OperationElementaire.listerMachineOperationElementaire(operation.getType(), this.cur_atelier.getId(), con);
        machine machineChoisie = listeMachine.get(0);
        double uo = operation.getUniteOperation()*ssPanier.getOccurrence();
        long tempsMin = machine.quandDisponibleApres(listeMachine.get(0).getId(), debut, con).get(0)+Double.valueOf(uo/(listeMachine.get(0).getVitesse()/3600000)).longValue(); //on converti les uo/h en uo/ms
        long temps = 0;
        long debutRetenu = machine.quandDisponibleApres(listeMachine.get(0).getId(), debut, con).get(0);
        for(int i=0;i<listeMachine.size();i++){
            ArrayList<Long> disponibilites = machine.quandDisponibleApres(listeMachine.get(i).getId(), debut, con);
            for(int j=0;j<disponibilites.size();j++){
                long debutIntermediaire = disponibilites.get(j);
                temps = debutIntermediaire+Double.valueOf(uo/(listeMachine.get(i).getVitesse()/3600000)).longValue();
                if((temps<=tempsMin)&&(machine.estDisponibleBetween(listeMachine.get(i).getId(), debutIntermediaire, temps, con))){
                    tempsMin=temps;
                    machineChoisie=listeMachine.get(i);
                    debutRetenu = debutIntermediaire;
                }
            }
        }
        ssPanier.setDebutTemp(debutRetenu);
        ssPanier.setStadeHoraire(debutRetenu+Double.valueOf(uo/(machineChoisie.getVitesse()/3600000)).longValue());
        this.energie = this.energie + machineChoisie.getPuissance()*uo/machineChoisie.getVitesse();
        return machineChoisie;
    }

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

        while(!gestionnaire.authentification(username,mdp)){
            System.out.println("Erreur d'authentification : vérifiez vos données.");
            System.out.println("Entrez votre nom d'utilisateur : ");
            username = Lire.S();
            System.out.println("Entrez votre mot de passe : ");
            mdp = Lire.S();
        }

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
    public boolean etapeAtelier() throws SQLException{
        boolean poursuiteDemarche = true;
        System.out.println("---------- Etape Atelier ----------");
        String reponse = "-1";
        while (reponse!="0"){
            System.out.println("0) Fermer");
            if (!Atelier.listerAtelier(this.con).isEmpty()){
                System.out.println("1) Se connecter à un atelier");
            }
            System.out.println("2) Créer un atelier");
            //System.out.println("3) Supprimer un atelier"); // On l'enlève car ne doit pas être faisable depuis l'authentification
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
                    if (autorisationAtelier(Integer.parseInt(reponse_2))){
                        this.setCurAtelier(Integer.parseInt(reponse_2));
                        // Ici, on devrait ajouter une ligne qui change la connexion de la base de données actuelle à celle de l'atelier. On ne le fait pas car il faudrait être en mesure de créer procéduralement le schéma sur une toute nouvelle base de donnée.
                        reponse = "0";
                        poursuiteDemarche = true;
                    }
                    else {
                        System.out.println("Vous n'avez pas accès à cet atelier.");
                        reponse = "-1";
                    }
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
                /*case "3":
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
                break;*/
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
                System.out.println("Vitesse : ");
                double r9 = Lire.d();
                machine.creerMachine(r1,r2,r3, r4,r9, this.cur_atelier, this.con);
                int r5 = -1;
                ArrayList<OperationElementaire> listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                while (r5!=0){
                    System.out.println("Quelles opérations cette machine peut réaliser ? Indiquez l'ID, 0 pour fermer ou -1 pour en créer une nouvelle : ");
                    listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                    for (int i=0;i< listeOperation.size();i++){
                        System.out.println(listeOperation.get(i).toString());
                    }
                    r5 = Lire.i();
                    if (r5==-1){
                        System.out.println("Quel est le type d'opération ? ");
                        String r6 = Lire.S();
                        OperationElementaire.creerOperationElementaire(r6,this.con);
                    }
                    else if (r5!=0){
                        listeOperation.add(OperationElementaire.getOperation(r5, this.con));
                    }
                }
                OperationElementaire.associerListeMachineOperation(listeOperation, machine.getIdMachine(r1,r2,r3, r4,r9, this.cur_atelier, this.con), con);
                break;
                case "3":
                System.out.println("Quelle machine supprimer ? ");
                refreshListeMachine();
                for(int i=0; i<this.cur_atelier.getListeMachine().size();i++){
                    System.out.println(this.cur_atelier.getListeMachine().get(i).toString());
                }
                int r8 = Lire.i();
                machine.supprimerMachine(r8, this.con);
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    
    public void etapeProduit() throws SQLException{
        System.out.println("---------- Etape Produit ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des produits");
            System.out.println("2) Créer un produit");
            System.out.println("3) Supprimer un produit");
            System.out.println("4) Ajouter un produit existant");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                refreshListeProduit();
                for (int i=0;i<this.cur_atelier.getListeProduit().size();i++){
                    System.out.println(this.cur_atelier.getListeProduit().get(i).toString());
                }
                break;
                case "2":
                System.out.println("Nom : ");
                String r1 = Lire.S();
                System.out.println("Reference : ");
                String r2 = Lire.S();
                ArrayList<Gamme> listeGamme = Gamme.listerGammeGlobal(/*cur_atelier,*/ con);
                for (int i=0;i<listeGamme.size();i++){
                    System.out.println(listeGamme.get(i).toString());
                }
                System.out.println("Quelle gamme utiliser ? (-1 pour créer une gamme)");
                int r3 = Lire.i();
                while (r3==-1){
                    ArrayList<OperationElementaire> gamme = new ArrayList<OperationElementaire>();
                    System.out.println("Entrez la nouvelle référence : ");
                    String r6=Lire.S();
                    String r7 = "o";
                    while (r7.equals("o")){
                        System.out.println("Créer une opération ou utiliser une existante ? (c/e)");
                        String r10 = Lire.S();
                        if (r10.equals("c")){
                            System.out.println("Type d'opération : ");
                            String r8 = Lire.S();
                            System.out.println("Unite d'opération : ");
                            double r9 = Lire.d();
                            OperationElementaire.creerOperation(r8, r9, con);
                            gamme.add(OperationElementaire.getOperation(OperationElementaire.getIdOperation(r8,r9,con), con));
                        }
                        else if(r10.equals("e")){
                            ArrayList<OperationElementaire> listeOperation = OperationElementaire.listerOperation(con);
                            for (int i=0;i<listeOperation.size();i++){
                                System.out.println(listeOperation.get(i).toString());
                            }
                            System.out.println("Quelle opération utiliser ? ");
                            int r11 = Lire.i();
                            gamme.add(OperationElementaire.getOperation(r11, con));
                        }
                        else {
                            System.out.println("Réponse invalide, opération non ajoutée.");
                        }
                        System.out.println("Ajouter une opération ? (o/n)");
                        r7 = Lire.S();
                    }
                    Gamme.creerGamme(r6,gamme,con);
                    listeGamme = Gamme.listerGamme(cur_atelier, con); // ne marche pas, mais ce qui est avant si
                    for (int i=0;i<listeGamme.size();i++){
                        System.out.println(listeGamme.get(i).toString());
                    }
                    System.out.println("Quelle gamme utiliser ? (-1 pour créer une gamme)");
                    r3 = Lire.i();
                }
                Produit.creerProduit(r1,r2,Gamme.getGamme(r3, this.con), this.cur_atelier,this.con);
                break;
                case "3":
                System.out.println("Quel produit supprimer ? ");
                int r4=Lire.i();
                Produit.supprimerProduit(r4,this.cur_atelier.getId(),this.con);
                break;
                case "4":
                ArrayList<Produit> listeProduit = Produit.listerProduitGlobal(con);
                for (int i=0;i<listeProduit.size();i++){
                    System.out.println(listeProduit.get(i).toString());
                }
                System.out.println("Quel produit ajouter ? ");
                int r5 = Lire.i();
                Produit.associerAtelierProduit(this.cur_atelier.getId(), r5, con);
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void etapeOperateur() throws SQLException{
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
                ArrayList<Utilisateur> listeOperateur = Utilisateur.listerOperateur(this.cur_atelier,this.con);
                for (int i=0; i< listeOperateur.size();i++){
                    System.out.println(listeOperateur.get(i).toString());
                }
                break;
                case "2":
                System.out.println("Créer un utilisateur opérateur ou ajouter comme opérateur un utilisateur déjà existant ? (c/e)");
                String r1 = Lire.S();
                if(r1.equals("c")){
                    System.out.println("Prenom : ");
                    String r2 = Lire.S();
                    System.out.println("Nom : ");
                    String r3 = Lire.S();
                    System.out.println("Nom d'utilisateur : ");
                    String r4 = Lire.S();
                    System.out.println("Role : ");
                    String r5 = Lire.S();
                    System.out.println("Mot de passe : ");
                    String r6 = Lire.S();
                    System.out.println("Etat : ");
                    String r7 = Lire.S();
                    Utilisateur.creerUtilisateur(r2, r3, r4, r5, r6, true, r7, this.cur_atelier, con);
                    int r8 = -1;
                    ArrayList<OperationElementaire> listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                    while (r8!=0){
                        System.out.println("Quelles opérations cet opérateur peut réaliser ? Indiquez l'ID, 0 pour fermer ou -1 pour en créer une nouvelle : ");
                        listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                        for (int i=0;i< listeOperation.size();i++){
                            System.out.println(listeOperation.get(i).toString());
                        }
                        r8 = Lire.i();
                        if (r8==-1){
                            System.out.println("Quel est le type d'opération ? ");
                            String r9 = Lire.S();
                            OperationElementaire.creerOperationElementaire(r9,this.con);
                        }
                        else if (r8!=0){
                            listeOperation.add(OperationElementaire.getOperation(r8, this.con));
                        }
                    }
                    Utilisateur.associerListeOperationUtilisateur(listeOperation, Utilisateur.getIdUtilisateur(r2, r3, r4, r5, r6, true, r7, con), con);
                }
                else if (r1.equals("e")){
                    ArrayList<Utilisateur> listeUtilisateur = Utilisateur.listerUtilisateur(this.cur_atelier,this.con);
                    for (int i=0; i< listeUtilisateur.size();i++){
                        System.out.println(listeUtilisateur.get(i).toString());
                    }
                    System.out.println("Quel utilisateur devient opérateur ? ");
                    int r10 = Lire.i();
                    Utilisateur.modifierStatutOperateur(r10, true, con);
                    int r11 = -1;
                    ArrayList<OperationElementaire> listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                    while (r11!=0){
                        System.out.println("Quelles opérations cet opérateur peut réaliser ? Indiquez l'ID, 0 pour fermer ou -1 pour en créer une nouvelle : ");
                        listeOperation = OperationElementaire.listerOperationElementaire(this.con);
                        for (int i=0;i< listeOperation.size();i++){
                            System.out.println(listeOperation.get(i).toString());
                        }
                        r11 = Lire.i();
                        if (r11==-1){
                            System.out.println("Quel est le type d'opération ? ");
                            String r12 = Lire.S();
                            OperationElementaire.creerOperationElementaire(r12,this.con);
                        }
                        else if (r11!=0){
                            listeOperation.add(OperationElementaire.getOperation(r11, this.con));
                        }
                    }
                    Utilisateur.associerListeOperationUtilisateur(listeOperation, r10, con);
                }
                else{
                    System.out.println("Réponse invalide !");
                }
                break;
                case "3":
                ArrayList<Utilisateur> listeOperateur2 = Utilisateur.listerOperateur(this.cur_atelier,this.con);
                for (int i=0; i< listeOperateur2.size();i++){
                    System.out.println(listeOperateur2.get(i).toString());
                }
                System.out.println("Quel opérateur supprimer ? ");
                int r13 = Lire.i();
                Utilisateur.supprimerOperateur(r13, con);
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void etapeGestionAtelier()throws SQLException{
        System.out.println("---------- Etape Gestion atelier ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            System.out.println("1) Paramètres d'atelier");
            System.out.println("2) Paramètres des utilisateurs");
            reponse = Lire.S();
            switch(reponse){
                case "1":
                parametreAtelier();
                break;
                case "2":
                parametreUtilisateur();
                break;
                case "0":
                reponse = "0";
                break;
                default:
                System.out.println("Réponse invalide !");
            }
        }
    }
    public void parametreAtelier() throws SQLException{
        System.out.println("---------- Paramètres d'atelier ----------");
        String reponse = "-1";
        while (reponse != "0"){
            System.out.println("0) Fermer");
            reponse = Lire.S();
            switch(reponse){
                case "0":
                reponse="0";
                break;
                default:
                System.out.println("Réponse invalide");
            }
        }
    }
    public void parametreUtilisateur()throws SQLException{
        System.out.println("---------- Paramètres des utilisateurs ----------");
        String reponse = "-1";
        while (reponse!="0"){
            System.out.println("0) Fermer");
            System.out.println("1) Liste des utilisateurs");
            System.out.println("2) Créer un utilisateur");
            System.out.println("3) Ajouter un utilisateur existant");
            System.out.println("3) Supprimer un utilisateur de l'atelier");
            reponse = Lire.S();
            switch(reponse){
                case "0":
                reponse="0";
                break;
                case "1":
                ArrayList<Utilisateur> listeUtilisateur = Utilisateur.listerUtilisateur(cur_atelier, con);
                for (int i=0;i<listeUtilisateur.size();i++){
                    System.out.println(listeUtilisateur.get(i).toString());
                }
                break;
                case "2":
                System.out.println("Prenom : ");
                String r2 = Lire.S();
                System.out.println("Nom : ");
                String r3 = Lire.S();
                System.out.println("Nom d'utilisateur : ");
                String r4 = Lire.S();
                System.out.println("Role : ");
                String r5 = Lire.S();
                System.out.println("Mot de passe : ");
                String r6 = Lire.S();
                System.out.println("Etat : ");
                String r7 = Lire.S();
                Utilisateur.creerUtilisateur(r2, r3, r4, r5, r6, false, r7, this.cur_atelier, con);
                break;
                case "3":
                ArrayList<Utilisateur> listeUtilisateur2 = Utilisateur.listerUtilisateurGlobal(con);
                for (int i=0;i<listeUtilisateur2.size();i++){
                    System.out.println(listeUtilisateur2.get(i).toString());
                }
                System.out.println("Quel utilisateur ajouter ? ");
                int r9 = Lire.i();
                Utilisateur.associerAtelierUtilisateur(r9, this.cur_atelier.getId(), con);
                break;
                case "4":
                ArrayList<Utilisateur> listeUtilisateur3 = Utilisateur.listerUtilisateur(cur_atelier, con);
                for (int i=0;i<listeUtilisateur3.size();i++){
                    System.out.println(listeUtilisateur3.get(i).toString());
                }
                System.out.println("Quel utilisateur supprimer ? ");
                int r8 = Lire.i();
                Utilisateur.supprimerUtilisateur(r8, cur_atelier, con);
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