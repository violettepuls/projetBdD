package classe_tables;

import java.util.ArrayList;

import org.vaadin.stefan.fullcalendar.Entry;

public class Indisponibilite {
    private ArrayList<Long> periode;
    private long debutIndisponibilite;
    private long finIndisponibilite;
    private Entry creneau;

    public Indisponibilite(long debut, long fin){
        this.debutIndisponibilite=debut;
        this.finIndisponibilite=fin;
        this.periode=new ArrayList<Long>();
        this.periode.add(debutIndisponibilite);
        this.periode.add(finIndisponibilite);
        //this.creneau.setStart(debut);
        //this.creneau.setEnd(fin);
    }

    public long getDebutIndisponibilite(){
        return this.debutIndisponibilite;
    }

    public long getFinIndisponibilite(){
        return this.finIndisponibilite;
    }

    public ArrayList<Long> getPeriode(){
        return this.periode;
    }

    public Entry getCreneau(){
        return this.creneau;
    }
}
