package classe_tables;

import java.rmi.server.Operation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class OperationElementaire {
    private int id;
    private String type; // Le nom affiché devra etre de la forme "Type - uniteOperation [UO]"
    private double uniteOperation;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public OperationElementaire(int id, String type, double uo){
        this.id=id;
        this.type=type;
        this.uniteOperation=uo;
    }

    public static ArrayList<OperationElementaire> listerOperationElementaire(Connection con) throws SQLException{
        ArrayList<OperationElementaire> liste = new ArrayList<OperationElementaire>();
        try(Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery("SELECT * FROM OperationElementaire WHERE Unite_Operation = 0");
            while (rs.next()){
                liste.add(new OperationElementaire(rs.getInt("ID"), rs.getString("Type"), 0));
            }
            return liste;
        }
    }

    public static ArrayList<OperationElementaire> listerOperation(Connection con) throws SQLException{
        ArrayList<OperationElementaire> liste = new ArrayList<OperationElementaire>();
        try(Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery("SELECT * FROM OperationElementaire WHERE Unite_Operation != 0");
            while (rs.next()){
                liste.add(new OperationElementaire(rs.getInt("ID"), rs.getString("Type"), rs.getDouble("Unite_Operation")));
            }
            return liste;
        }
    }

    public static void creerOperationElementaire(String type, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO OperationElementaire (Type) values (?)")){
            ps.setString(1,type);
            ps.executeUpdate();
        }
    }

    public static void creerOperation(String type, double uniteOperation, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO OperationElementaire (Type,Unite_Operation) values (?,?)")){
            ps.setString(1,type);
            ps.setDouble(2,uniteOperation);
            ps.executeUpdate();
        }
    }

    public static OperationElementaire getOperation(int id, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM OperationElementaire WHERE ID = ?")){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new OperationElementaire(rs.getInt("ID"), rs.getString("Type"), rs.getDouble("Unite_Operation"));
            }
            else{
                return null;
            }
        }
    }

    public static void associerListeMachineOperation(ArrayList<OperationElementaire> liste, int idmachine, Connection con) throws SQLException{
        for (int i=0; i<liste.size();i++){
            associerMachineOperation(liste.get(i), idmachine, con);
        }
    }

    public static void associerMachineOperation(OperationElementaire operation, int idmachine, Connection con)throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("INSERT INTO MachineOperation (IDMachine,IDOperation) values (?,?)")){
            ps.setInt(1,idmachine);
            ps.setInt(2,operation.getId());
            ps.executeUpdate();
        }
    }

    public static void dissocierMachineOperationGlobal(int idmachine, Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("DELETE FROM MachineOperation WHERE IDMachine = ?")){
            ps.setInt(1,idmachine);
            ps.executeUpdate();
        }
    }

    public static void dissocierMachineOperation(int idoperation, int idmachine, Connection con)throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("DELETE FROM MachineOperation WHERE (IDMachine,IDOperation) = (?,?)")){
            ps.setInt(1,idmachine);
            ps.setInt(2,idoperation);
            ps.executeUpdate();
        }
    }

    @Override
    public String toString(){
        String s = this.type;
        if (this.uniteOperation==0){
            s=s+ " - " + this.uniteOperation + ", ID : " + this.id;
        }
        else{
            s=s+ ", ID : " + this.id;
        }
        return s;
    }

    public static int getIdOperation(String type, double uo,Connection con) throws SQLException{
        try(PreparedStatement ps = con.prepareStatement("SELECT ID FROM OperationElementaire WHERE (Type,Unite_Operation)=(?,?)")){
            ps.setString(1,type);
            ps.setDouble(2,uo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("ID");
            }
            else {
                return -1;
            }
        }
    }
}
