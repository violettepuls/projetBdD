package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import classe_tables.OperationElementaire;
import classe_tables.machine;
import traitement.Gestionnaire;

public class GroupeMachine extends HorizontalLayout{
    private machine mach;
    private TextField nom;
    private TextArea description;
    private Button supprimer;
    private Gestionnaire gestionnaire;
    private Button modifier;
    private TextField etat;
    private Button panne;
    private VerticalLayout texte;
    private VerticalLayout boutons;

    public GroupeMachine(Gestionnaire g, machine m)throws SQLException{
        this.gestionnaire=g;
        this.mach=m;
        this.nom=new TextField();
        this.description=new TextArea();
        this.modifier=new Button("Modifier");
        this.supprimer=new Button("Supprimer");
        this.etat=new TextField();
        this.panne=new Button("Signaler une panne");
        this.texte=new VerticalLayout();
        HorizontalLayout boutonsModif = new HorizontalLayout(this.modifier,this.supprimer);
        this.boutons=new VerticalLayout(this.panne,boutonsModif,new Spacer());
        HorizontalLayout entete = new HorizontalLayout(this.nom,this.etat);
        texte.add(entete,this.description,new Spacer());
        this.add(texte,new Spacerw(), boutons);

        //Pré-remplissage
        this.nom.setValue(this.mach.getNom());
        this.etat.setValue(this.mach.getEtat());
        this.description.setValue(decrire());
        if(this.mach.getEtat().equals("Disponible")){
            this.etat.getStyle().setColor("green");
        }
        else if (this.mach.getEtat().equals("En réparation")){
            this.etat.getStyle().setColor("orange");
        }
        else{
            this.etat.getStyle().setColor("red");
        }

        //Attribution des fonctions
        this.supprimer.addClickListener(clickevent -> {
            supprimer();
        });
        this.modifier.addClickListener(clickevent -> {
            modifier();
        });
        
        //Esthétique
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
        //this.setPadding(true);
        this.setWidth("auto");
        this.setHeight("100px");
        this.nom.setReadOnly(true);
        this.nom.getStyle().setBorder(null);
        this.nom.getStyle().set("border", "none");
        this.nom.getStyle().set("color", "black").set("background-color", "rgba(173, 216, 230, 0.2)");
        this.description.setReadOnly(true);
        this.description.getStyle().setBorder(null);
        this.etat.setReadOnly(true);
        this.etat.getStyle().setBorder(null);
        this.modifier.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.supprimer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.panne.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR);
        entete.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        texte.setFlexGrow(1, this.description);
        texte.setAlignItems(FlexComponent.Alignment.STRETCH);
        texte.setSpacing(true);
        texte.getElement().getStyle().set("margin-bottom", "70px");
        boutonsModif.setAlignItems(FlexComponent.Alignment.CENTER);
        boutonsModif.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        boutons.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        boutons.setAlignItems(FlexComponent.Alignment.CENTER);
        boutons.getStyle().set("margin-top", "5em"); 
        this.setFlexGrow(1, texte);
    }
    public class Spacer extends Div {
        public Spacer() {
            setHeight("6em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }

    public String decrire() throws SQLException{
        ArrayList<OperationElementaire> listeOperation = machine.listerOperationElementaireMachine(this.mach.getId(), gestionnaire.getConnection());
        String d="Vitesse : "+this.mach.getVitesse()+"\n"+"Puissance : "+this.mach.getPuissance();
        for (int i =0;i<listeOperation.size();i++){
            d=d+"\n"+"- "+listeOperation.get(i).getType();
        }
        d=d+"\n"+"Référence : "+this.mach.getRef();
        return d;
    }

    public void supprimer(){
        try{
            machine.supprimerMachine(this.mach.getId(), this.gestionnaire.getConnection());
            this.removeAll();
        }
        catch (SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void recharger() throws SQLException{
        this.mach=machine.getMachine(this.mach.getId(), this.gestionnaire.getConnection());
        this.nom.setValue(this.mach.getNom());
        this.etat.setValue(this.mach.getEtat());
        this.description.setValue(decrire());
        if(this.mach.getEtat().equals("Disponible")){
            this.etat.getStyle().setColor("green");
        }
        else if (this.mach.getEtat().equals("En réparation")){
            this.etat.getStyle().setColor("orange");
        }
        else{
            this.etat.getStyle().setColor("red");
        }
        this.add(texte,boutons);
    }

    public void modifier(){
        try{
            this.removeAll();
            //changer ici la hauteur de this
            this.add(new ParametreMachine(this));
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public machine getMachine(){
        return this.mach;
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }
    public class Spacerw extends Div {
        public Spacerw() {
            setWidth("27em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }
}
