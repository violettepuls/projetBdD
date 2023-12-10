package classe_tables;

import java.util.ArrayList;

public class Operateur extends Utilisateur{
    private ArrayList<OperationElementaire> qualification;
    private String etat;

    public ArrayList<OperationElementaire> getQualification() {
        return qualification;
    }
    public void setQualification(ArrayList<OperationElementaire> qualification) {
        this.qualification = qualification;
    }
    public String getEtat() {
        return etat;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Operateur(String nom, String prenom, String usr, String role) {
        super(nom, prenom, usr, role);
    }
}
