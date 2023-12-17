package classe_tables;

import java.util.ArrayList;

public class machine {

    private int id;
    private String nom;
    private String ref;
    private String etat;
    private double puissance;
    private ArrayList<Double> dimension; //format [x,y,z]
    private ArrayList<OperationElementaire> listeOperation;
    private int Atelier;

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
    
    public machine(int id, String nom, String ref,String etat,double puissance, int Atelier){
        this.id=id;
        this.ref=ref;
        this.etat=etat;
        this.puissance=puissance;
        this.nom=nom;
        this.Atelier=Atelier;
    }

    public void printf(){
        System.out.println(this.);
    }

    public static machine rechercheMachine(){
        return null; 
    }
}
