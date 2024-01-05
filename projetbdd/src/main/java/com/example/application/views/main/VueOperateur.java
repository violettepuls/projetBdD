package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class VueOperateur extends VerticalLayout{
    private TextField titre;
    private Button ajouter;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeOperateur;

    public VueOperateur(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.ajouter = new Button("Ajouter");
        this.corps = new Scroller();
        this.listeOperateur = new VerticalLayout();
        this.add(titre,corps,ajouter);

        //Pré-remplissage
        this.titre.setValue("Liste des Opérateurs");
        formater();
        this.corps.setContent(listeOperateur);

        //Atribution des fonctions
        this.ajouter.addClickListener(clickevent->{
            ajouter();
        });
        
        //Esthétique
        this.corps.setSizeFull();

    }


    public void ajouter(){
        try {
            this.removeAll();
            this.add(new VueCreationOperateur(this));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur ajout operateur : "+e);
        }
    }
    
    public void formater() throws SQLException{
        this.listeOperateur.removeAll();
        ArrayList<Utilisateur> liste = Utilisateur.listerOperateur(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeOperateur.add(new GroupeOperateur(gestionnaire, liste.get(i)));
            if (i < liste.size() - 1) {
                this.listeOperateur.add(new Spacer());
            }
        }
    }
    public class Spacer extends Div {
        public Spacer() {
            setHeight("4em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }

    public void recharger()throws SQLException{
        this.removeAll();
        formater();
        this.add(titre,corps,ajouter);
    }
}
