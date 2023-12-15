package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Atelier {
    private int id;
    private String nom;
    private String bdd; // //92.222.25.165:3306/m3_rmbola_tembo01
    private String nom_utilisateur; // m3_rmbola_tembo01
    private String mdp; // 976e74f9
    private ArrayList<Machine> listeMachine;
    private ArrayList<Utilisateur> listeUtilisateur;
    private ArrayList<Produit> listeProduit;
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBdd() {
        return bdd;
    }

    public void setBdd(String bdd) {
        this.bdd = bdd;
    }
    
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMdp() {
        return mdp;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Machine> getListeMachine() {
        return listeMachine;
    }

    public void setListeMachine(ArrayList<Machine> listeMachine) {
        this.listeMachine = listeMachine;
    }

    public ArrayList<Utilisateur> getListeUtilisateur() {
        return listeUtilisateur;
    }

    public void setListeUtilisateur(ArrayList<Utilisateur> listeUtilisateur) {
        this.listeUtilisateur = listeUtilisateur;
    }

    public ArrayList<Produit> getListeProduit() {
        return listeProduit;
    }

    public void setListeProduit(ArrayList<Produit> listeProduit) {
        this.listeProduit = listeProduit;
    }

    //En SQL
    public Machine getMachine(int id){

    }

    //En SQL
    public void addMachine(String nom, String ref, String etat, Double puissance, ArrayList<Double> dim, ArrayList<String> typeOperationElementaire){

    }

    //En SQL
    public void deleteMachine(int id){

    }

    //En SQL
    public Produit getProduit(int id){

    }

    //En SQL
    public void addProduit(String nom, String ref, String gammeRef){

    }

    //En SQL
    public void deleteProduit(){

    }

    public Atelier(String n, String b, String n_u, String m){
        this.nom=n;
        this.bdd=b;
        this.nom_utilisateur=n_u;
        this.mdp=m;
    }

    public Atelier(int id, String nom, String bdd, String nom_utilisateur, String mdp){
        this.id=id;
        this.nom=nom;
        this.bdd=bdd;
        this.nom_utilisateur=nom_utilisateur;
        this.mdp=mdp;
    }

    public Atelier(String nomAtelier){

    }

    public static ArrayList<Atelier> listerAtelier(Connection con){
        ArrayList<Atelier> listeAtelier = new ArrayList<Atelier>();
        try(Statement ps = con.createStatement()){
            ResultSet resultat = ps.executeQuery("SELECT * FROM Atelier");
            while(resultat.next()){
                int id=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String bdd = resultat.getString("BDD");
                String usr=resultat.getString("Nom_Utilisateur");
                String mdp=resultat.getString("MDP_BDD");
                listeAtelier.add(new Atelier(id,nom, bdd, usr, mdp));
            }
            return listeAtelier;
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }

    @Override
    public String toString(){
        String s = this.nom + ", ID : " + this.id;
        return s;
    }
}
