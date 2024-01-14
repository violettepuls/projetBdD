package classe_tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.vaadin.stefan.fullcalendar.Entry;

public class EDT {
    public static ArrayList<Entry> listerIndisponibiliteMachineAll(Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierMachine")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Entry entree = new Entry();
                entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                entree.setTitle(rs.getString("NomIndisponibilite"));
                entree.setColor("#ff3333");
                liste.add(entree);
            }
        }
        return liste;
    }

    public static ArrayList<Entry> listerIndisponibiliteMachineGroupe(ArrayList<Integer> listeId, Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        for(int i=0;i<listeId.size();i++){
            try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierMachine WHERE IDMachine = ?")){
                ps.setInt(1,listeId.get(i));
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Entry entree = new Entry();
                    entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                    entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                    entree.setTitle(rs.getString("NomIndisponibilite"));
                    entree.setColor("#ff3333");
                    liste.add(entree);
                }
            }
        }
        return liste;
    }

    public static ArrayList<Entry> listerIndisponibiliteMachine(int idmachine, Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierMachine WHERE IDMachine = ?")){
            ps.setInt(1,idmachine);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Entry entree = new Entry();
                entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                entree.setTitle(rs.getString("NomIndisponibilite"));
                entree.setColor("#ff3333");
                liste.add(entree);
            }
        }
        return liste;
    }

    public static ArrayList<Entry> listerIndisponibilite(Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierMachine")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Entry entree = new Entry();
                entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                entree.setTitle(rs.getString("NomIndisponibilite"));
                entree.setColor("#ff3333");
                liste.add(entree);
            }
        }
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierOperateur")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Entry entree = new Entry();
                entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                entree.setTitle(rs.getString("NomIndisponibilite"));
                entree.setColor("#ff3333");
                liste.add(entree);
            }
        }
        return liste;
    }

    public static ArrayList<Entry> listerIndisponibiliteOperateurAll(Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierOperateur")){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Entry entree = new Entry();
                entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                entree.setTitle(rs.getString("NomIndisponibilite"));
                entree.setColor("#ff3333");
                liste.add(entree);
            }
        }
        return liste;
    }

    public static ArrayList<Entry> listerIndisponibiliteOperateurGroupe(ArrayList<Integer> listeId, Connection con)throws SQLException{
        ArrayList<Entry> liste = new ArrayList<Entry>();
        for(int i=0;i<listeId.size();i++){
            try(PreparedStatement ps = con.prepareStatement("SELECT * FROM CalendrierOperateur WHERE IDOperateur = ?")){
                ps.setInt(1,listeId.get(i));
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Entry entree = new Entry();
                    entree.setStart(rs.getTimestamp("DebutIndisponibilite").toLocalDateTime());
                    entree.setEnd(rs.getTimestamp("FinIndisponibilite").toLocalDateTime());
                    entree.setTitle(rs.getString("NomIndisponibilite"));
                    entree.setColor("#ff3333");
                    liste.add(entree);
                }
            }
        }
        return liste;
    }
}
