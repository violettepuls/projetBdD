package classe_tables;

public class Machine {

    private int id;
    private String ref;
    private String etat;
    private double puissance;
    private ArrayList<float> dimension; //format [x,y,z]

    public Machine(int id,String ref,String etat,double puissance){
        this.id=id;
        this.ref=ref;
        this.etat=etat;
        this.puissance=puissance;
    }

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
}
