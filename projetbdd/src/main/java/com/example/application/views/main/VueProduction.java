package com.example.application.views.main;

import classe_tables.Produit;
import traitement.Gestionnaire;

import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;

public class VueProduction extends VerticalLayout  {
    private TextField titre;
    private Button panier;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeProduit;

    public VueProduction(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.panier = new Button("Panier");
        this.corps = new Scroller();
        this.listeProduit = new VerticalLayout();
        this.add(titre,corps,panier);

        //Pré-remplissage
        this.titre.setValue("Liste des Produits");
        formater();
        this.corps.setContent(listeProduit);

        //Attribution des fonctions
        this.panier.addClickListener(clickevent->{
            try{
                this.removeAll();
                this.add(new VuePanier(this));
            }
            catch(SQLException e){
                System.out.println("Erreur afficher panier : "+e);
            }
        });
        
        //Esthétique
        this.corps.setSizeFull();
    }
    
    public void formater() throws SQLException{
        this.listeProduit.removeAll();
        ArrayList<Produit> liste = Produit.listerProduit(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeProduit.add(new GroupeProduction(gestionnaire, liste.get(i)));
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
        this.add(titre,corps,panier);
    }
}
