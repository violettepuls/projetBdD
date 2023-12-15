package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Utilisateur {
    private int id;
    private String prenom;
    private String nom;
    private String nom_utilisateur;
    private String role;
    private String mdp;
    private boolean operateur;

    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isOperateur() {
        return operateur;
    }

    public void setOperateur(boolean operateur) {
        this.operateur = operateur;
    }
    
    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Utilisateur(int id, String nom, String prenom, String usr, String role, boolean op){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.nom_utilisateur=usr;
        this.role=role;
        this.operateur=op;
    }
    
    public static ArrayList<Utilisateur> rechercheUtilisateur(int id, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE id EQUALS ?")){
            ps.setInt(1,id);
            ResultSet resultat = ps.executeQuery();
            while(resultat.next()){
                int ide=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String role = resultat.getString("Role");
                boolean op=resultat.getBoolean("Operateur");
                String usr=resultat.getString("Nom_Utilisateur");
                listeUtilisateur.add(new Utilisateur(ide,nom, prenom, usr, role,op));
            }
            return listeUtilisateur;
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }
    public static ArrayList<Utilisateur> rechercheUtilisateur(boolean op, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Operateur EQUALS ?")){
            ps.setBoolean(1,op);
            ResultSet resultat = ps.executeQuery();
            while(resultat.next()){
                int id=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String role = resultat.getString("Role");
                boolean ope=resultat.getBoolean("Operateur");
                String usr=resultat.getString("Nom_Utilisateur");
                listeUtilisateur.add(new Utilisateur(id,nom, prenom, usr, role,ope));
            }
            return listeUtilisateur;
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }
    public static ArrayList<Utilisateur> rechercheUtilisateur(String role, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Role EQUALS ?")){
            ps.setString(1,role);
            ResultSet resultat = ps.executeQuery();
            if(resultat.next()){
                int id=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String rolee = resultat.getString("Role");
                boolean op=resultat.getBoolean("Operateur");
                String usr=resultat.getString("Nom_Utilisateur");
                listeUtilisateur.add(new Utilisateur(id,nom, prenom, usr, rolee,op));
            }
            return listeUtilisateur;
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }
    public static ArrayList<Utilisateur> rechercheUtilisateur(String p, String n, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Prenom EQUALS ? AND Nom EQUALS ?")){
            ps.setString(1,p);
            ps.setString(2,n);
            ResultSet resultat = ps.executeQuery();
            while(resultat.next()){
                int id=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String role = resultat.getString("Role");
                boolean op=resultat.getBoolean("Operateur");
                String usr=resultat.getString("Nom_Utilisateur");
                listeUtilisateur.add(new Utilisateur(id,nom, prenom, usr, role,op));
            }
            return listeUtilisateur;
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }

    public void enregistrer(Connection con)throws SQLException{
        con.setAutoCommit(false);
        try (PreparedStatement st = con.prepareStatement("insert into Utilisateur (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur) values (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setString(1,this.prenom);
            st.setString(2,this.nom);
            st.setString(3,this.nom_utilisateur);
            st.setString(4,this.role);
            st.setString(5,this.mdp);
            st.setBoolean(6, this.operateur);
            st.executeUpdate();
            System.out.println("Utilisateur créé !");
        }
        catch(SQLException sqle){
            System.out.println("Echec de création : " + sqle);
        }
        finally{
            con.setAutoCommit(true);
        }
    }

    public void supprimer(Connection con)throws SQLException{
        con.setAutoCommit(false);
        try (PreparedStatement st = con.prepareStatement("DELETE FROM Utilisateur WHERE (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur) EQUALS (?,?,?,?,?,?)")){
            st.setString(1,this.prenom);
            st.setString(2,this.nom);
            st.setString(3,this.nom_utilisateur);
            st.setString(4,this.role);
            st.setString(5,this.mdp);
            st.setBoolean(6, this.operateur);
            st.executeUpdate();
            System.out.println("Utilisateur supprimé !");
        }
        catch(SQLException sqle){
            System.out.println("Echec de suppression : " + sqle);
        }
        finally{
            con.setAutoCommit(true);
        }
    }

    @Override
    public String toString(){
        String s = this.prenom + " " + this.nom + ", ID : " + this.id;
        return s;
    }
}
