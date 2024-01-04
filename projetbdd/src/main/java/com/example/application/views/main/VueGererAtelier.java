package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Atelier;
import traitement.Gestionnaire;

public class VueGererAtelier extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField nomAtelier;
    private TextField bddAtelier;
    private TextField usernameBdd;
    private TextField mdpBdd;
    private Button supprimerAtelier;
    private Button valider;
    private Button annuler;
    private HorizontalLayout boutons;

    public VueGererAtelier(Gestionnaire g){
        //Déclaration
        this.gestionnaire=g;
        this.nomAtelier = new TextField("Nom de l'atelier");
        this.bddAtelier = new TextField("Base de données (BDD)");
        this.usernameBdd = new TextField("Nom d'utilisateur BDD");
        this.mdpBdd = new TextField("Mot de passe BDD");
        this.supprimerAtelier = new Button("Supprimer l'atelier");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.boutons = new HorizontalLayout(this.valider,this.annuler);
        this.add(nomAtelier,bddAtelier,usernameBdd,mdpBdd,supprimerAtelier,boutons);

        //Pré-remplissage
        this.nomAtelier.setValue(this.gestionnaire.getCurAtelier().getNom());
        this.bddAtelier.setValue(this.gestionnaire.getCurAtelier().getBdd());
        this.usernameBdd.setValue(this.gestionnaire.getCurAtelier().getNom_utilisateur());
        this.mdpBdd.setValue(this.gestionnaire.getCurAtelier().getMdp());

        //Attribution des fonctions
        this.supprimerAtelier.addClickListener(clickevent->{
            supprimer();
        });
        this.valider.addClickListener(clickevent->{
            valider();
        });
        this.annuler.addClickListener(clickevent->{
            annuler();
        });

        //Esthétique
    }

    public void supprimer(){
        Atelier.supprimer(this.gestionnaire.getCurAtelier().getId(),this.gestionnaire.getConnection());
    }

    public void annuler(){
        this.nomAtelier.setValue(this.gestionnaire.getCurAtelier().getNom());
        this.bddAtelier.setValue(this.gestionnaire.getCurAtelier().getBdd());
        this.usernameBdd.setValue(this.gestionnaire.getCurAtelier().getNom_utilisateur());
        this.mdpBdd.setValue(this.gestionnaire.getCurAtelier().getMdp());
    }

    public void valider(){
        try{
            Atelier.modifierAtelier(this.gestionnaire.getCurAtelier().getId(), nomAtelier.getValue(), bddAtelier.getValue(), usernameBdd.getValue(), mdpBdd.getValue(), this.gestionnaire.getConnection());
        }
        catch(SQLException e){
            System.out.println("Erreur modification atelier : "+e);
        }
    }
}
