package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class machine {

    private int id;
    private String nom;
    private String ref;
    private String etat; // Etats possibles : Disponible, En panne, En reparation, Indisponible-X-heures
    private double puissance;
    private ArrayList<Double> dimension; //format [x,y,z]
    private ArrayList<OperationElementaire> listeOperation;
    private int IdAtelier;
    private double vitesse;

    public int getId(){
        return this.id;
    }
    
    public String getRef(){
        return this.ref;
    }

    public String getEtat(){
        return this.etat;
    }

    public double getPuissance(){
        return this.puissance;
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

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setPuissance(double puissance) {
        this.puissance = puissance;
    }

    public ArrayList<Double> getDimension() {
        return dimension;
    }

    public void setDimension(ArrayList<Double> dimension) {
        this.dimension = dimension;
    }

    public ArrayList<OperationElementaire> getListeOperation() {
        return listeOperation;
    }

    public void setListeOperation(ArrayList<OperationElementaire> listeOperation) {
        this.listeOperation = listeOperation;
    }

    @Override
    public String toString(){
        String s = this.nom + ", " + this.ref + ", ID : " + this.id;
        return s;
    }
    
    public machine(int id, String nom, String ref,String etat,double puissance, int Atelier){
        this.id=id;
        this.ref=ref;
        this.etat=etat;
        this.puissance=puissance;
        this.nom=nom;
        this.IdAtelier=Atelier;
    }

    public machine(int id, String nom, String ref,String etat,double puissance){
        this.id=id;
        this.ref=ref;
        this.etat=etat;
        this.puissance=puissance;
        this.nom=nom;
    }

    public static void creerMachine(String nom, String ref, String Etat, double P, double vitesse, Atelier Atelier, Connection con) throws SQLException { // cas où une machine ne peut appartenir qu'à 1 seul atelier
        try(PreparedStatement pst = con.prepareStatement(
            """
            insert into Machine (Nom,Reference,Etat,Puissance,Vitesse,IDAtelier)
            values (?,?,?,?,?,?)
            """)) {
                pst.setString(1, nom);
                pst.setString(2, ref);
                pst.setString(3, Etat);
                pst.setDouble(4, P);
                pst.setDouble(5,vitesse);
                pst.setInt(6, Atelier.getId());
                pst.executeUpdate();
            }
        }

    public static void supprimerMachine(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("DELETE FROM Machine WHERE ID = ?")){
            OperationElementaire.dissocierMachineOperation(id, con);
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    public static ArrayList<machine> listerMachine(Atelier AtelierActuel, Connection con) throws SQLException{
        ArrayList<machine> listeMachine = new ArrayList<machine>();

        try ( PreparedStatement st = con.prepareStatement(
                "select * from Machine WHERE IDAtelier = ?")) {
            st.setInt(1,AtelierActuel.getId());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                listeMachine.add(new machine(res.getInt("ID"), res.getString("Nom"), res.getString("Reference"), res.getString("Etat"), res.getDouble("Puissance"), res.getInt("IDAtelier")));
            }
        }

        return listeMachine;

    }

    /*
    public static void creerMachine(String nom, String ref, String Etat, double P, Atelier AtelierActuel, Connection con) throws SQLException { // cas où une machine peut appartenir à plusieurs atelier
        int id = -1;
        try(PreparedStatement pst = con.prepareStatement(
            """
            insert into Machine (Nom,Reference,Etat,Puissance,IDAtelier)
            values (?,?,?,?,?)
            """, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, nom);
            pst.setString(2, ref);
            pst.setString(3, Etat);
            pst.setDouble(4, P);
            pst.setInt(5, AtelierActuel.getId());
            pst.executeUpdate();
            try (ResultSet resultat = pst.getGeneratedKeys()){
                resultat.next();
                id = resultat.getInt(1);
            }
        }
        try(PreparedStatement pst = con.prepareStatement(
            """
            insert into AtelierMachine (IDAtelier,IDMachine)
            values (?,?)
            """)) {
            pst.setInt(1, AtelierActuel.getId());
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }

    public static ArrayList<machine> listerMachine(Atelier AtelierActuel, Connection con) throws SQLException{
    ArrayList<machine> listeMachine = new ArrayList<machine>();

    try ( PreparedStatement st = con.prepareStatement(
            "select * from Machine JOIN AtelierMachine on AtelierMachine.IDMachine = Machine.ID WHERE AtelierMachine.IDAtelier = ?")) {
            st.setInt(1,AtelierActuel.getId());
            ResultSet res = st.executeQuery();
            while (res.next()) {
                listeMachine.add(new machine(res.getInt("ID"), res.getString("Nom"), res.getString("Reference"), res.getString("Etat"), res.getDouble("Puissance")));
            }
        }

        return listeMachine;

    }
    */

    public static machine getMachine(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM Machine WHERE ID = ?")){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new machine(id, rs.getString("Nom"), rs.getString("Reference"), rs.getString("Etat"), rs.getDouble("Puissance"), rs.getInt("Atelier"));
            }
            else{
                return null;
            }
        }
    }

    public static int getIdMachine(String nom, String ref, String etat, double puissance, double vitesse, Atelier atelier, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT ID FROM Machine WHERE (Nom,Reference,Etat,Puissance,Vitesse,IDAtelier) = (?,?,?,?,?,?)")){
            ps.setString(1, nom);
            ps.setString(2,ref);
            ps.setString(3,etat);
            ps.setDouble(4,puissance);
            ps.setDouble(5,vitesse);
            ps.setInt(6,atelier.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("ID");
            }
            else{
                return -1;
            }
        }
    }

    public static ArrayList<OperationElementaire> listerOperationElementaireMachine(int idmachine, Connection con) throws SQLException{
        ArrayList<OperationElementaire> listeOperation = new ArrayList<OperationElementaire>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM OperationElementaire JOIN MachineOperation on MachineOperation.IDOperation = OperationElementaire.ID WHERE IDMachine = ?")){
            ps.setInt(1,idmachine);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                listeOperation.add(new OperationElementaire(rs.getInt("OperationElementaire.ID"), rs.getString("Type"), 0));
            }
            return listeOperation;
        }
    }

    public static boolean estDisponible(int id, Connection con) throws SQLException{
        machine machine = getMachine(id, con);
        if (machine.getEtat()=="Disponible"){
            return true;
        }
        else{
            return false;
        }
    }

    public static double quandDisponible(int id, Connection con){
        return 0;
    }
}
