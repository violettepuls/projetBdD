package com.example.application.views.main;

import java.sql.SQLException;
import java.util.Collections;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class GroupePanier extends HorizontalLayout{
    private VuePanier vp;
    private Produit prod;
    private int nombre;
    private TextField qte;
    private TextField nom;
    private TextArea description;
    private Gestionnaire gestionnaire;
    private Button supprimer;
    private VerticalLayout texte;

    public GroupePanier(VuePanier v, Produit m)throws SQLException{
        //Déclaration
        this.vp=v;
        this.gestionnaire=v.getGestionnaire();
        this.prod=m;
        this.nombre=Collections.frequency(gestionnaire.getPanier(), m);
        this.qte = new TextField("Quantité");
        this.nom=new TextField();
        this.description=new TextArea();
        this.supprimer=new Button("Supprimer");
        this.texte=new VerticalLayout(this.nom,this.description);
      //  this.add(qte,texte,supprimer);

        //Pré-remplissage
        this.nom.setValue(this.prod.getNom());
        this.description.setValue(decrire());
        this.qte.setValue(String.valueOf(nombre));

        //Attribution des fonctions
        this.supprimer.addClickListener(clickevent->{
            supprimer();
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
        this.description.setReadOnly(true);
        this.description.getStyle().setBorder(null);
        this.supprimer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.supprimer.setIcon(new Icon(VaadinIcon.CLOSE));
        this.supprimer.getStyle().set("margin-top", "5em"); 
        this.add(qte,texte,supprimer);

    }

    public String decrire() throws SQLException{
        String d="";
        d=d+"Référence : "+this.prod.getReference();
        d=d+"\n"+"Gamme : "+Gamme.getGamme(this.prod.getIdGamme(), this.gestionnaire.getConnection()).getRef();
        return d;
    }

    public void supprimer(){
        this.gestionnaire.getPanier().remove(this.prod);
        if(nombre==1){
            try {
                this.vp.recharger();
            } catch (SQLException e) {
                System.out.println("Erreur suppression : "+e);
            }
        }
        else{
            this.nombre=this.nombre-1;
            this.qte.setValue(String.valueOf(nombre));
        }
    }
    
}
