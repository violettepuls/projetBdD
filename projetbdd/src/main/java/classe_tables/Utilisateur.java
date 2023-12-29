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
    private String etat;
    private ArrayList<Integer> idAtelier;

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

    public String getEtat(){
        return this.etat;
    }

    public Utilisateur(int id, String nom, String prenom, String usr, String role, boolean op, String etat){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.nom_utilisateur=usr;
        this.role=role;
        this.operateur=op;
        this.etat=etat;
    }
    
    /*
    public Utilisateur(String prenom, String nom, String nom_utilisateur, String role, String mdp, boolean operateur, String etat) {
        this.prenom = prenom;
        this.nom = nom;
        this.nom_utilisateur = nom_utilisateur;
        this.role = role;
        this.mdp = mdp;
        this.operateur = operateur;
        this.etat = etat;
    }
    */

    public static Utilisateur getUtilisateur(int id, Connection con){
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE id = ?")){
            ps.setInt(1,id);
            ResultSet resultat = ps.executeQuery();
            if(resultat.next()){
                int ide=resultat.getInt("ID");
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                String role = resultat.getString("Role");
                boolean op=resultat.getBoolean("Operateur");
                String usr=resultat.getString("Nom_Utilisateur");
                String etat = resultat.getString("Etat");
                return new Utilisateur(ide,nom, prenom, usr, role,op,etat);
            }
            else{
                System.out.println("Utilisateur inexistant");
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
            return null;
        }
    }

    public static Utilisateur verifierUtilisateur(String usr, String mdp, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE (Nom_Utilisateur,MDP)=(?,?)")){
            ps.setString(1,usr);
            ps.setString(2,mdp);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utilisateur(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Nom_Utilisateur"), rs.getString("Role"), rs.getBoolean("Operateur"),rs.getString("Etat"));
            }
            else{
                return null;
            }
        }
    }

    public static ArrayList<Integer> listerAtelierUtilisateur(int id, Connection con) throws SQLException{
        ArrayList<Integer> listeAtelier = new ArrayList<Integer>();
        try(PreparedStatement ps = con.prepareStatement("SELECT IDAtelier FROM AtelierUtilisateur WHERE IDUtilisateur = ?")){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                listeAtelier.add(rs.getInt("IDAtelier"));
            }
            return listeAtelier;
        }
    }

    public static void associerAtelierUtilisateur(int IDUtilisateur,int idAtelier, Connection con) throws SQLException{
        try(PreparedStatement ps= con.prepareStatement("INSERT INTO AtelierUtilisateur (IDAtelier,IDUtilisateur) values (?,?)")){
            ps.setInt(1,idAtelier);
            ps.setInt(2,IDUtilisateur);
            ps.executeUpdate();
        }
    }

    public static void dissocierAtelierUtilisateur(int IDUtilisateur, int idAtelier, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM AtelierUtilisateur WHERE (IDAtelier,IDUtilisateur) = (?,?)")){
            ps.setInt(1,idAtelier);
            ps.setInt(2,IDUtilisateur);
            ps.executeUpdate();
        }
    }

    public static void dissocierAtelierUtilisateurGlobal(int IDUtilisateur, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM AtelierUtilisateur WHERE (IDUtilisateur) = (?)")){
            ps.setInt(1,IDUtilisateur);
            ps.executeUpdate();
        }
    }
    
    /*
    public static ArrayList<Utilisateur> rechercheUtilisateur(boolean op, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Operateur = ?")){
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
    */
    /*
    public static ArrayList<Utilisateur> rechercheUtilisateur(String role, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Role = ?")){
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
    */
    /*
    public static ArrayList<Utilisateur> rechercheUtilisateur(String p, String n, Connection con){
        ArrayList<Utilisateur> listeUtilisateur = new ArrayList<Utilisateur>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur WHERE Prenom = ? AND Nom = ?")){
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
    */

    /*
    public void enregistrer(Connection con){
        try{
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
        catch(SQLException f){
            System.out.println("Erreur autoCommit : "+f);
        }
    }

    public void supprimer(Connection con)throws SQLException{
        con.setAutoCommit(false);
        try (PreparedStatement st = con.prepareStatement("DELETE FROM Utilisateur WHERE (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur) = (?,?,?,?,?,?)")){
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
    */

    @Override
    public String toString(){
        String s = this.prenom + " " + this.nom + ", ID : " + this.id;
        return s;
    }

    public static ArrayList<Utilisateur> listerUtilisateur(Atelier atelier, Connection con) throws SQLException{
        ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur JOIN AtelierUtilisateur ON AtlierUtilisateur.IDUtilisateur = Utilisateur.ID WHERE IDAtelier = ?")){
            ps.setInt(1,atelier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Utilisateur(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Nom_Utilisateur"), rs.getString("Role"), rs.getBoolean("Operateur"),rs.getString("Etat")));
            }
        }
        return liste;
    }

    public static ArrayList<Utilisateur> listerUtilisateurGlobal(Connection con) throws SQLException{
        ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Utilisateur(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Nom_Utilisateur"), rs.getString("Role"), rs.getBoolean("Operateur"),rs.getString("Etat")));
            }
        }
        return liste;
    }

    public static ArrayList<Utilisateur> listerOperateur(Atelier atelier, Connection con) throws SQLException{
        ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Utilisateur JOIN AtelierUtilisateur ON AtlierUtilisateur.IDUtilisateur = Utilisateur.ID WHERE (IDAtelier,Operateur) = (?,?)")){
            ps.setInt(1,atelier.getId());
            ps.setBoolean(2, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Utilisateur(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Nom_Utilisateur"), rs.getString("Role"), rs.getBoolean("Operateur"),rs.getString("Etat")));
            }
        }
        return liste;
    }

    public static void creerUtilisateurGlobal(String prenom, String nom, String nom_utilisateur, String role, String mdp, boolean operateur, String etat, Connection con) throws SQLException {
        try{
            con.setAutoCommit(false);
            try (PreparedStatement st = con.prepareStatement("insert into Utilisateur (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur,Etat) values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
                st.setString(1,prenom);
                st.setString(2,nom);
                st.setString(3,nom_utilisateur);
                st.setString(4,role);
                st.setString(5,mdp);
                st.setBoolean(6,operateur);
                st.setString(7,etat);
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
        catch(SQLException f){
            System.out.println("Erreur autoCommit : "+f);
        }
    }

    public static void creerUtilisateur(String prenom, String nom, String nom_utilisateur, String role, String mdp, boolean operateur, String etat, Atelier atelier, Connection con) throws SQLException {
        int id = -1;
        try{
            con.setAutoCommit(false);
            try (PreparedStatement st = con.prepareStatement("insert into Utilisateur (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur,Etat) values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
                st.setString(1,prenom);
                st.setString(2,nom);
                st.setString(3,nom_utilisateur);
                st.setString(4,role);
                st.setString(5,mdp);
                st.setBoolean(6,operateur);
                st.setString(7,etat);
                st.executeUpdate();
                try(ResultSet rs = st.getGeneratedKeys()){
                    rs.next();
                    id=rs.getInt(1);
                }
                System.out.println("Utilisateur créé !");
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
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO AtelierUtilisateur (IDAtelier,IDUtilisateur) = (?,?)")){
            ps.setInt(1,atelier.getId());
            ps.setInt(2,id);
            ps.executeUpdate();
        }
    }

    public static void associerOperationUtilisateur(ArrayList<OperationElementaire> listeOperation, int IDUtilisateur, Connection con) throws SQLException{
        for (int i=0;i<listeOperation.size();i++){
            try(PreparedStatement ps = con.prepareStatement("INSERT INTO Qualification (IDOperateur,IDOperationElementaire) values (?,?)")){
                ps.setInt(1,IDUtilisateur);
                ps.setInt(2,listeOperation.get(i).getId());
                ps.executeUpdate();
            }
        }
    }

    public static void dissocierOperationUtilisateur(int IDUtilisateur, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM Qualification WHERE (IDOperateur) = (?)")){
            ps.setInt(1,IDUtilisateur);
            ps.executeUpdate();
        }
    }

    public static ArrayList<OperationElementaire> listerOperationUtilisateur(int idutilisateur,Connection con)throws SQLException{
        ArrayList<OperationElementaire> listeOperation = new ArrayList<OperationElementaire>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM OperationElementaire JOIN Qualification on Qualification.IDOperation = OperationElementaire.ID WHERE IDOperateur = ?")){
            ps.setInt(1,idutilisateur);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                listeOperation.add(new OperationElementaire(rs.getInt("OperationElemenataire.ID"),rs.getString("Type"),rs.getDouble("Unite_Operation")));
            }
            return listeOperation;
        }
    }

    public static int getIdUtilisateur(String prenom, String nom, String nom_utilisateur, String role, String mdp, boolean operateur, String etat, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT ID FROM Utilisateur WHERE (Prenom,Nom,Nom_Utilisateur,Role,MDP,Operateur,Etat)=(?,?,?,?,?,?,?)")){
            ps.setString(1,prenom);
            ps.setString(2,nom);
            ps.setString(3,nom_utilisateur);
            ps.setString(4,role);
            ps.setString(5,mdp);
            ps.setBoolean(6,operateur);
            ps.setString(7,etat);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("ID");
            }
            else{
                return -1;
            }
        }
    }

    public static void modifierOperateur(int idutilisateur, boolean operateur, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("UPDATE Utilisateur SET Operateur = ? WHERE ID = ?")){
            ps.setBoolean(1,operateur);
            ps.setInt(2,idutilisateur);
            ps.executeUpdate();
        }
    }

    public static void supprimerOperateur(int idutilisateur,Connection con) throws SQLException{
        dissocierOperationUtilisateur(idutilisateur, con);
        modifierOperateur(idutilisateur, false, con);
    }

    public static void supprimerUtilisateur(int idutilisateur,Atelier atelier,Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM Utilisateur JOIN AtelierUtilisateur on AtelierUtilisateur.IDUtilisateur = Utilisateur.ID WHERE (Utilisateur.ID,AtelierUtilisateur.IDAtelier) = (?,?)")){
            dissocierAtelierUtilisateur(idutilisateur,atelier.getId(), con);
            dissocierOperationUtilisateur(idutilisateur, con);
            ps.setInt(1,idutilisateur);
            ps.setInt(2,atelier.getId());
            ps.executeUpdate();
        }
    }

    public static void supprimerUtilisateurGlobal(int idutilisateur,Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM Utilisateur WHERE ID = ?")){
            dissocierAtelierUtilisateurGlobal(idutilisateur, con);
            dissocierOperationUtilisateur(idutilisateur, con);
            ps.setInt(1,idutilisateur);
            ps.executeUpdate();
        }
    }
}
