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

    public SousPanier(Gestionnaire g, Produit p, int n){
        this.produit = p;
        this.occurence = n;
        this.uo=0;
        this.nombreOperation=0;
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
            uo = uo+gammeInitiale.get(i).getUniteOperation();
        }
    }

    public void etapeSuivante(){
        this.gammeInitiale.remove(0);
    }
}
