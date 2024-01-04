package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.OperationElementaire;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class GroupeGamme extends HorizontalLayout{
    private Gestionnaire gestionnaire;
    private Gamme gamme;
    private TextField ref;
    private TextArea produit;
    private TextArea operation;
    private Button modifier;
    private Button supprimer;
    private HorizontalLayout boutons;
    private VerticalLayout infos;

    public GroupeGamme(Gestionnaire g, Gamme ga)throws SQLException{
        //Déclaration
        this.gestionnaire=g;
        this.gamme=ga;
        this.ref = new TextField();
        this.produit = new TextArea();
        this.operation = new TextArea();
        this.modifier = new Button("Modifier");
        this.supprimer = new Button("Supprimer");
        this.infos = new VerticalLayout(ref,produit,operation);
        this.boutons = new HorizontalLayout(modifier,supprimer);
        this.add(infos,boutons);

        //Pré-remplissage
        this.ref.setValue(this.gamme.getRef());
        updateProduit();
        updateOperation();

        //Attribution des fonctions
        this.modifier.addClickListener(clickevent ->{
            modifier();
        });
        this.supprimer.addClickListener(clickevent->{
            supprimer();
        });

        //Esthétique

    }

    public void updateProduit()throws SQLException{
        ArrayList<Produit> listeProduit = Gamme.listerProduitGamme(this.gamme.getId(), this.gestionnaire.getConnection());
        String content = "Produits liés :";
        for (int i=0;i<listeProduit.size();i++){
            content = content +"\n" + listeProduit.get(i).getNom() + " [" + listeProduit.get(i).getReference()+"]";
        }
        this.produit.setValue(content);
    }

    public void updateOperation()throws SQLException{
        ArrayList<OperationElementaire> listeOperation = Gamme.getOperationGamme(this.gamme.getId(), this.gestionnaire.getConnection());
        String content = "Opérations :";
        for (int i=0;i<listeOperation.size();i++){
            content = content +"\n" + listeOperation.get(i).affichage();
        }
        this.operation.setValue(content);
    }

    public void modifier(){
        try{
            this.removeAll();
            this.add(new ParametreGamme(this));
        }
        catch(SQLException e){
            System.out.println("Erreur ouverture modif : "+e);
        }
    }

    public void supprimer(){
        try{
            Gamme.supprimerGammeGlobal(this.gamme.getId(), this.gestionnaire.getConnection());
        }
        catch(SQLException e){
            System.out.println("Erreur suppression gamme : "+e);
        }
    }

    public void recharger()throws SQLException{
        this.gamme=Gamme.getGamme(this.gamme.getId(), this.gestionnaire.getConnection());
        this.ref.setValue(this.gamme.getRef());
        updateProduit();
        updateOperation();
        this.add(infos,boutons);
    }

    public Gamme getGamme(){
        return this.gamme;
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }
}
