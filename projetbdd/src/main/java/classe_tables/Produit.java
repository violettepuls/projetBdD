package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.jdbc.exceptions.SQLError;

public class Produit {
    private int id;
    private String nom;
    private String reference;
    private int IdGamme;
    public int getId() {
        return id;
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
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public int getIdGamme() {
        return IdGamme;
    }
    public void setIdGamme(int gamme) {
        this.IdGamme = gamme;
    }

    public Produit(int id, String nom, String reference, int idGamme) {
        this.id = id;
        this.nom = nom;
        this.reference = reference;
        IdGamme = idGamme;
    }

    public static ArrayList<Produit> listerProduit(Atelier AtelierActuel, Connection con) throws SQLException{
        ArrayList<Produit> liste = new ArrayList<Produit>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Produit JOIN AtelierProduit on AtelierProduit.IDProduit = Produit.ID WHERE IDAtelier = ?")){
            ps.setInt(1, AtelierActuel.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Produit(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Reference"), rs.getInt("IDGamme")));
            }
        }
        return liste;
    }

    public static ArrayList<Produit> listerProduitGlobal(Connection con) throws SQLException{
        ArrayList<Produit> liste = new ArrayList<Produit>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Produit")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Produit(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Reference"), rs.getInt("IDGamme")));
            }
        }
        return liste;
    }

    @Override
    public String toString(){
        String s = this.nom + ", ID : " + this.id;
        return s;
    }

    public static Produit getProduit(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Produit WHERE ID = ?")){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Produit(rs.getInt("ID"), rs.getString("Nom"), rs.getString("Reference"), rs.getInt("IDGamme"));
            }
            else{
                return null;
            }
        }
    }

    public static void associerAtelierProduit(int idatelier, int idproduit, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO AtelierProduit (IDAtelier,IDProduit) values (?,?)")){
            ps.setInt(1,idatelier);
            ps.setInt(2,idproduit);
            ps.executeUpdate();
        }
    }

    public static void dissocierAtelierProduit(int idproduit, int idatelier, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM AtelierProduit WHERE (IDProduit,IDAtelier) = (?,?)")){
            ps.setInt(1,idproduit);
            ps.setInt(2,idatelier);
            ps.executeUpdate();
        }
    }

    public static void dissocierAtelierProduitGlobal(int idproduit, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM AtelierProduit WHERE IDProduit = ?")){
            ps.setInt(1,idproduit);
            ps.executeUpdate();
        }
    }

    public static void creerProduit(String nom, String ref, Gamme gamme, Atelier atelier, Connection con) throws SQLException {
        int id=-1;
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO Produit (Nom,Reference,IDGamme) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1,nom);
            ps.setString(2,ref);
            ps.setInt(3,gamme.getId());
            ps.executeUpdate();
            try (ResultSet resultat = ps.getGeneratedKeys()){
                resultat.next();
                id = resultat.getInt(1);
            }
        }
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO AtelierProduit (IDAtelier,IDProduit) values (?,?)")){
            ps.setInt(1,atelier.getId());
            ps.setInt(2,id);
            ps.executeUpdate();
        }
    }

    public static void supprimerProduit(int id, int idatelier, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM Produit WHERE ID = ?")){
            dissocierAtelierProduit(id, idatelier, con);
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    public static void supprimerProduitGlobal(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("REMOVE FROM Produit WHERE ID = ?")){
            dissocierAtelierProduitGlobal(id, con);
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }
}
