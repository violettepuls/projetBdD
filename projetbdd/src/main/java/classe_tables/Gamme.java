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
        try (PreparedStatement ps = con.prepareStatement("SELECT Gamme.ID,Gamme.Reference FROM Gamme JOIN Produit on Produit.IDGamme = Gamme.ID JOIN AtelierProduit on AtelierProduit.IDProduit = Produit.ID WHERE AtelierProduit.IDAtelier = ?")){
            ps.setInt(1, atelier.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                liste.add(new Gamme(rs.getInt("Gamme.ID"), rs.getString("Gamme.Reference")));
            }
        }
        return liste;
    }

    @Override
    public String toString(){
        String s = this.ref + ", ID : " + this.id;
        return s;
    }

    public static Gamme getGamme(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Gamme WHERE ID = ?")){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Gamme(id, rs.getString("Reference"));
            }
            return null;
        }
    }

    public static int getIdGamme(String ref,Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT ID FROM Gamme WHERE Reference = ?")){
            ps.setString(1,ref);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("ID");
            }
            else {
                return -1;
            }
        }
    }

    public static void creerGamme(String reference, ArrayList<OperationElementaire> operation, Connection con) throws SQLException{
        int id = -1;
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO Gamme (Reference) values (?)", PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1,reference);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()){
                rs.next();
                id=rs.getInt(1);
            }
        }
        for(int i = 0;i<operation.size();i++){
            try(PreparedStatement ps = con.prepareStatement("INSERT INTO OperationGamme (IDOperation,IDGamme,Ordre) values (?,?,?)")){
                ps.setInt(1,operation.get(i).getId());
                ps.setInt(2,id);
                ps.setInt(3,i);
                ps.executeUpdate();
            }
        }
    }
}
