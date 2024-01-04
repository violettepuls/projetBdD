package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class GroupeProduit extends HorizontalLayout{
    private Produit prod;
    private TextField nom;
    private TextArea description;
    private Button supprimer;
    private Gestionnaire gestionnaire;
    private Button modifier;
    private VerticalLayout texte;
    private VerticalLayout boutons;

    public GroupeProduit(Gestionnaire g, Produit m)throws SQLException{
        this.gestionnaire=g;
        this.prod=m;
        this.nom=new TextField();
        this.description=new TextArea();
        this.modifier=new Button("Modifier");
        this.supprimer=new Button("Supprimer");
        this.texte=new VerticalLayout();
        this.boutons=new VerticalLayout();

        //paramétrage de la structure des éléments
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        this.setPadding(true);
        this.setWidth("auto");
        this.setHeight("100px");
        this.nom.setReadOnly(true);
        this.nom.getStyle().setBorder(null);
        this.description.setReadOnly(true);
        this.description.getStyle().setBorder(null);
        this.modifier.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.supprimer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        texte.add(this.nom,this.description);
        texte.setFlexGrow(1, this.description);
        texte.setAlignItems(FlexComponent.Alignment.STRETCH);
        texte.setSpacing(true);
        boutons.add(this.modifier,this.supprimer);
        boutons.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        boutons.setAlignItems(FlexComponent.Alignment.CENTER);

        //paramétrage des données des éléments
        this.nom.setValue(this.prod.getNom());
        this.description.setValue(decrire());
        this.supprimer.addClickListener(clickevent -> {
            supprimer();
        });
        this.modifier.addClickListener(clickevent -> {
            modifier();
        });

        //ajout des éléments à la page
        this.add(texte,boutons);
        this.setFlexGrow(1, texte);

    }

    public String decrire() throws SQLException{
        String d="";
        d=d+"Référence : "+this.prod.getReference();
        d=d+"\n"+"Gamme : "+Gamme.getGamme(this.prod.getIdGamme(), this.gestionnaire.getConnection()).getRef();
        return d;
    }

    public void supprimer(){
        try{
            Produit.supprimerProduit(this.prod.getId(), this.gestionnaire.getCurAtelier().getId(), this.gestionnaire.getConnection());
            this.removeAll();
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void recharger()throws SQLException{
        this.prod=Produit.getProduit(this.prod.getId(), this.gestionnaire.getConnection());
        this.nom.setValue(this.prod.getNom());
        this.description.setValue(decrire());
        this.add(texte,boutons);
    }

    public void modifier(){
        try{
            this.removeAll();
            //changer ici la hauteur de this
            this.add(new ParametreProduit(this));
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public Produit getProduit(){
        return this.prod;
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }
}
