package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Produit;

import traitement.Gestionnaire;




public class VueProduit extends VerticalLayout {
    private TextField titre;
    private Button ajouter;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeProduit;
    private ComboBox<Produit> ajouterExistant;
    private HorizontalLayout boutons;

    public VueProduit(Gestionnaire g) throws SQLException{
        //Déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.ajouter = new Button("Ajouter");
        this.ajouterExistant=new ComboBox<Produit>();
        this.corps = new Scroller();
        this.listeProduit = new VerticalLayout();
        this.boutons=new HorizontalLayout(ajouter,ajouterExistant);
        this.corps.setContent(listeProduit);
        this.add(titre,corps,boutons);

        //Pré-remplissage
        this.titre.setValue("Liste des Produits");
        this.ajouterExistant.setPlaceholder("Ajouter un produit existant");
        this.ajouterExistant.setItems(Produit.listerProduitHorsAtelier(gestionnaire.getCurAtelier(),gestionnaire.getConnection()));
        this.ajouterExistant.setItemLabelGenerator(Produit::getNom);
        formater();

        //Attribution des fonctions
        this.ajouter.addClickListener(clickevent->{
            ajouter();
        });
        this.ajouterExistant.addValueChangeListener(event->{
            ajouterExistant();
        });
        
        //Esthétique
        this.corps.setSizeFull();
    }


    public void ajouter(){
        try {
            this.removeAll();
            this.add(new ParametreProduit(this));  
        } catch (Exception e) {
            System.out.println("Erreur ajouter produit : "+e);
        }
    }

    public void ajouterExistant(){
        try {
            Produit.associerAtelierProduit(gestionnaire.getCurAtelier().getId(), ajouterExistant.getValue().getId(), gestionnaire.getConnection());
            formater();
        } catch (Exception e) {
            System.out.println("Erreur ajouter produit : "+e);
        }
    }
    
    public void formater() throws SQLException{
        this.listeProduit.removeAll();
        ArrayList<Produit> liste = Produit.listerProduit(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeProduit.add(new GroupeProduit(gestionnaire, liste.get(i),""));
            if (i < liste.size() - 1) {
                this.listeProduit.add(new Spacer());
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
        this.add(titre,corps,boutons);
    }
}
