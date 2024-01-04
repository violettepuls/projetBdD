package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.OperationElementaire;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class VueGererGroupe extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField chercherDans;
    private Select<String> categorie;
    private Scroller contenu;
    private VerticalLayout listeElement;

    public VueGererGroupe(Gestionnaire g){
        //Déclaration
        this.gestionnaire=g;
        this.chercherDans=new TextField();
        this.categorie=new Select<String>();
        this.listeElement = new VerticalLayout();
        this.contenu = new Scroller();

        //Pré-remplissage
        this.chercherDans.setPlaceholder("Chercher dans : ");
        this.chercherDans.setReadOnly(true);
        this.categorie.setItems("Produit","Gamme","Opérations réalisables");
        this.categorie.setPlaceholder("Catégorie");
        this.contenu.setContent(listeElement);

        //Attribution des fonctions
        this.categorie.addValueChangeListener(event->{
            afficher();
        });

        //Esthétique

    }

    public void afficher(){
        try{
            this.listeElement.removeAll();
            if(this.categorie.getValue().equals("Produit")){
                ArrayList<Produit> listeProduit = Produit.listerProduitGlobal(this.gestionnaire.getConnection());
                for (int i=0;i<listeProduit.size();i++){
                    this.listeElement.add(new GroupeProduit(gestionnaire, listeProduit.get(i)));
                }
            }
            else if(this.categorie.getValue().equals("Gamme")){
                ArrayList<Gamme> listeGamme = Gamme.listerGammeGlobal(this.gestionnaire.getConnection());
                for (int i=0;i<listeGamme.size();i++){
                    this.listeElement.add(new GroupeGamme(gestionnaire,listeGamme.get(i)));
                }
            }
            else if(this.categorie.getValue().equals("Opérations réalisables")){
                ArrayList<OperationElementaire> listeOperation = OperationElementaire.listerOperationElementaire(this.gestionnaire.getConnection());
                for(int i=0;i<listeOperation.size();i++){
                    this.listeElement.add(new GroupeOperation(gestionnaire,listeOperation.get(i)));
                }
            }
        }
        catch (SQLException e){
            System.out.println("Erreur d'affichage : "+e);
        }
    }
}
