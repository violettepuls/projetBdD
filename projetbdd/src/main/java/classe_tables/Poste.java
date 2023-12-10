package classe_tables;

import java.util.ArrayList;

public class Poste {
    private int id;
    private String nom;
    private ArrayList<Operateur> operateur;
    private ArrayList<Machine> machine;
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
    public ArrayList<Operateur> getOperateur() {
        return operateur;
    }
    public void setOperateur(ArrayList<Operateur> operateur) {
        this.operateur = operateur;
    }
    public ArrayList<Machine> getMachine() {
        return machine;
    }
    public void setMachine(ArrayList<Machine> machine) {
        this.machine = machine;
    }
}
