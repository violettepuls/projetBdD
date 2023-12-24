package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gamme {
    private int id;
    private String ref;
    private ArrayList<OperationElementaire> listeOperation;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    public ArrayList<OperationElementaire> getListeOperation() {
        return listeOperation;
    }
    public void setListeOperation(ArrayList<OperationElementaire> listeOperation) {
        this.listeOperation = listeOperation;
    }
    
    public Gamme(int id, String ref) {
        this.id = id;
        this.ref = ref;
    }

    public static Gamme getGammeProduit(Produit produit, Connection con) throws SQLException{
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Gamme JOIN GammeProduit on GammeProduit.IDGamme = Gamme.ID WHERE IDProduit = ?")){
            ps.setInt(1, produit.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Gamme(rs.getInt("ID"), rs.getString("Reference"));
            }
            else {
                return null;
            }
        }
    }

    public static ArrayList<Gamme> listerGamme(Atelier atelier, Connection con) throws SQLException{
        ArrayList<Gamme> liste = new ArrayList<Gamme>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM Gamme JOIN GammeProduit on GammeProduit.IDGamme = Gamme.ID JOIN AtelierProduit on AtelierProduit.IDProduit on Produit.ID WHERE IDAtelier = ?")){
            ps.setInt(1, atelier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Gamme(rs.getInt("ID"), rs.getString("Reference")));
            }
        }
        return liste;
    }
}
