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
    private ArrayList<machine> listeMachine;
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

    public ArrayList<machine> getListeMachine() {
        return listeMachine;
    }

    public void setListeMachine(ArrayList<machine> listeMachine) {
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
                String usr=resultat.getString("Nom_Utilisateur_BDD");
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

    public static Atelier getAtelier(int id, Connection con){
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Atelier WHERE ID = ?")){
            ps.setInt(1,id);
            ResultSet resultat = ps.executeQuery();
            if(resultat.next()){
                return new Atelier(resultat.getInt("ID"),resultat.getString("Nom"),resultat.getString("BDD"),resultat.getString("Nom_Utilisateur_BDD"),resultat.getString("MDP_BDD"));
            }
            else{
                System.out.println("Aucun atelier de trouvé");
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }

    public void enregistrer(Connection con){
        try{
            con.setAutoCommit(false);
            try (PreparedStatement st = con.prepareStatement("insert into Atelier (Nom,BDD,Nom_Utilisateur_BDD,MDP_BDD) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
                st.setString(1,this.nom);
                st.setString(2,this.bdd);
                st.setString(3,this.nom_utilisateur);
                st.setString(4,this.mdp);
                st.executeUpdate();
                System.out.println("Atelier créé !");
            }
            catch(SQLException sqle){
                System.out.println("Echec de création : " + sqle);
            }
            finally{
                con.setAutoCommit(true);
            }
        }
        catch(SQLException f){
            System.out.println("Erreur autoCommit : "+f);
        }
    }

    public static void supprimer(int id, Connection con){
        try{
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Atelier WHERE ID = ?")){
                ps.setInt(1,id);
                ps.executeUpdate();
                System.out.println("Atelier supprimé !");
            }
            catch (SQLException e){
                System.out.println("Erreur de suppression : " + e);
            }
            finally {
                con.setAutoCommit(true);
            }
        }
        catch (SQLException f){
            System.out.println("Erreur setAutoCommit : " + f);
        }
    }

    @Override
    public String toString(){
        String s = this.nom + ", ID : " + this.id;
        return s;
    }

    public static int getIdAtelier(String nom, String bdd, String nom_utilisateur, String mdp, Connection con)throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT ID FROM Atelier WHERE (Nom,BDD,Nom_Utilisateur_BDD,MDP_BDD)=(?,?,?,?)")){
            ps.setString(1,nom);
            ps.setString(2,bdd);
            ps.setString(3,nom_utilisateur);
            ps.setString(4,mdp);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("ID");
            }
            else{
                return -1;
            }
        }
    }
}
