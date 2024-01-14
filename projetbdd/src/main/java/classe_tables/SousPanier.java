package classe_tables;

import java.util.ArrayList;

import traitement.Gestionnaire;

public class SousPanier {
    private Gestionnaire gestionnaire;
    private Produit produit;
    private ArrayList<OperationElementaire> gammeInitiale;
    private int occurence;
    private double uo;
    private int nombreOperation;
    private long stadeHoraire;
    private long debutTemp;

    public SousPanier(Gestionnaire g, Produit p, int n){
        this.gestionnaire=g;
        this.produit = p;
        this.occurence = n;
        this.uo=0;
        this.nombreOperation=0;
        this.debutTemp = gestionnaire.getTempsDebut();
        this.stadeHoraire = gestionnaire.getTempsDebut();
        try {
            this.gammeInitiale=Gamme.getOperationGamme(produit.getIdGamme(), gestionnaire.getConnection());
            calculerUoRestante();
            this.nombreOperation=gammeInitiale.size();
        } catch (Exception e) {
            System.out.println("Erreur getGamme sous panier : "+e);
        }
    }

    public Produit getProduit(){
        return this.produit;
    }

    public int getOccurrence(){
        return this.occurence;
    }

    public void setOccurrence(int o){
        this.occurence=o;
    }

    public int getNombreOperation(){
        return this.nombreOperation;
    }

    public long getStadeHoraire(){
        return this.stadeHoraire;
    }

    public void setStadeHoraire(long d){
        this.stadeHoraire = d;
    }

    public long getDebutTemp(){
        return this.debutTemp;
    }

    public void setDebutTemp(long d){
        this.debutTemp = d;
    }

    public double getUo(){
        calculerUoRestante();
        return this.uo;
    }

    public ArrayList<OperationElementaire> getOperationRestante(){
        return this.gammeInitiale;
    }

    public void calculerUoRestante(){
        uo=0;
        for (int i=0;i< gammeInitiale.size();i++){
            uo = uo+Double.valueOf(gammeInitiale.get(i).getUniteOperation()).longValue();
        }
    }

    public void etapeSuivante(){
        this.gammeInitiale.remove(0);
    }

    public String nommerStade(){
        return gammeInitiale.get(0).getType()+" de "+occurence+" "+produit.getNom();
    }
}
