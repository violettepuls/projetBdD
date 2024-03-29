package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;

import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class VueOperateur extends VerticalLayout{
    private H1 titre;
    private Button ajouter;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeOperateur;

    public VueOperateur(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new H1("Liste des Opérateurs");
        this.ajouter = new Button("Ajouter");
        this.corps = new Scroller();
        this.listeOperateur = new VerticalLayout();
        //this.add(titre,corps,ajouter);

        //Pré-remplissage
        
        formater();
        this.corps.setContent(listeOperateur);

        //Atribution des fonctions
        this.ajouter.addClickListener(clickevent->{
            ajouter();
        });
        
        //Esthétique
       
        this.corps.setSizeFull();
        this.corps.getStyle().set("max-height", "100%");
        this.corps.getStyle().set("max-width", "100%");
        this.listeOperateur.setWidth("100%");
        this.corps.setWidth("70em");
        this.corps.setHeight("30em%");
        this.setSizeFull();
        this.ajouter.setIcon(new Icon(VaadinIcon.PLUS));
        titre.getElement().getStyle().set("margin", "auto");
      //  this.ajouter.getStyle().set("position","fixed").set("bottom","6em").set("left","8em");
        this.add(titre,corps,ajouter);

    }


    public void ajouter(){
        try {
            this.removeAll();
            this.add(new VueCreationOperateur(this));
        } catch (Exception e) {
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
