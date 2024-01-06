package com.example.application.views.main;

import java.sql.SQLException;
import java.util.Collections;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class GroupeProduction extends HorizontalLayout{
    private Produit prod;
    private TextField nom;
    private TextArea description;
    private Gestionnaire gestionnaire;
    private Button ajouter;
    private VerticalLayout texte;

    public GroupeProduction(Gestionnaire g, Produit m)throws SQLException{
        //Déclaration
        this.gestionnaire=g;
        this.prod=m;
        this.nom=new TextField();
        this.description=new TextArea();
        this.ajouter=new Button("Ajouter");
        this.texte=new VerticalLayout(this.nom,this.description);
        this.add(texte,new Spacerw(),ajouter);

        //paramétrage des données des éléments
        this.nom.setValue(this.prod.getNom());
        this.description.setValue(decrire());

        //Attribution des fonctions
        this.ajouter.addClickListener(clickevent->{
            ajouterAuPanier();
        });

        //Esthétique
        this.setFlexGrow(1, texte);
        texte.setFlexGrow(1, this.description);
        texte.setAlignItems(FlexComponent.Alignment.STRETCH);
        texte.setSpacing(true);
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.setPadding(true);
        this.setWidth("auto");
        this.setHeight("100px");
        this.nom.setReadOnly(true);
        this.nom.getStyle().setBorder(null);
        this.nom.getStyle().set("border", "none");
        this.nom.getStyle().set("color", "black").set("background-color", "rgba(173, 216, 230, 0.2)");
        this.description.setReadOnly(true);
        this.description.getStyle().setBorder(null);
        this.ajouter.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.ajouter.getStyle().set("margin-top", "5em");

    }

    public String decrire() throws SQLException{
        String d="";
        d=d+"Référence : "+this.prod.getReference();
        d=d+"\n"+"Gamme : "+Gamme.getGamme(this.prod.getIdGamme(), this.gestionnaire.getConnection()).getRef();
        return d;
    }

    public void ajouterAuPanier(){
        this.gestionnaire.ajouterAuPanier(this.prod);
        Notification notification = Notification.show("Ajouté au panier. Total : "+Collections.frequency(this.gestionnaire.getPanier(),this.prod), 1000, Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    public class Spacerw extends Div {
        public Spacerw() {
            setWidth("40em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }
}
