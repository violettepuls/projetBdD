package classe_tables;

import java.util.ArrayList;

public class Indisponibilite {
    private ArrayList<Double> periode;
    private double debutIndisponibilite;
    private double finIndisponibilite;

    public Indisponibilite(double debut, double fin){
        this.debutIndisponibilite=debut;
        this.finIndisponibilite=fin;
        this.periode=new ArrayList<Double>();
        this.periode.add(debutIndisponibilite);
        this.periode.add(finIndisponibilite);
    }

    public double getDebutIndisponibilite(){
        return this.debutIndisponibilite;
    }

    public double getFinIndisponibilite(){
        return this.finIndisponibilite;
    }

    public ArrayList<Double> getPeriode(){
        return this.periode;
    }
}
