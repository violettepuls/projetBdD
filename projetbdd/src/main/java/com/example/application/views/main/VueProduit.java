package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
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

    public VueProduit(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.ajouter = new Button("Ajouter");
        this.corps = new Scroller();
        this.listeProduit = new VerticalLayout();

        //Acquisition des données
        this.titre.setValue("Liste des Produits");
        formater();

        //Placement et paramétrage des éléments
        this.corps.setContent(listeProduit);
        this.add(titre,corps,ajouter);
        
    }


    public void ajouter(){
        //ouvre une VueCreationMachine qui revient sur VueMachine à la fermeture et créé une machine dans la BDD quand bouton Validé appuyé
        //enleve le reste (this.removeAll()) de l'affichage
    }
    
    public void formater() throws SQLException{
        ArrayList<Produit> liste = Produit.listerProduit(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeProduit.add(new GroupeProduit(gestionnaire, liste.get(i)));
        }
    }

}
