package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;
import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class GroupeOperateur extends HorizontalLayout{
    private Utilisateur oper;
    private TextField nom;
    private TextArea description;
    private Button supprimer;
    private Gestionnaire gestionnaire;
    private Button modifier;
    private TextField etat;
    private VerticalLayout texte;
    private VerticalLayout boutons;

    public GroupeOperateur(Gestionnaire g, Utilisateur m)throws SQLException{
        this.gestionnaire=g;
        this.oper=m;
        this.nom=new TextField();
        this.etat=new TextField();
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
        this.etat.setReadOnly(true);
        this.etat.getStyle().setBorder(null);
        this.modifier.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.supprimer.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        HorizontalLayout entete = new HorizontalLayout();
        entete.add(this.nom,this.etat);
        entete.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        texte.add(entete,this.description);
        texte.setFlexGrow(1, this.description);
        texte.setAlignItems(FlexComponent.Alignment.STRETCH);
        texte.setSpacing(true);
        boutons.add(this.modifier,this.supprimer);
        boutons.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        boutons.setAlignItems(FlexComponent.Alignment.CENTER);

        //paramétrage des données des éléments
        this.nom.setValue(this.oper.getNom()+" "+this.oper.getPrenom());
        if (this.oper.getEtat() != null){
            this.etat.setValue(this.oper.getEtat());
            if(this.oper.getEtat().equals("Disponible")){
                this.etat.getStyle().setColor("green");
            }
            else{
                this.etat.getStyle().setColor("red");
            }
        }
        this.description.setValue(decrire());
        this.supprimer.addClickListener(clickevent -> {
            supprimer();
        });
        this.modifier.addClickListener(clickevent ->{
            modifier();
        });

        //ajout des éléments à la page
        this.add(texte,boutons);
        this.setFlexGrow(1, texte);

    }

    public String decrire() throws SQLException{
        ArrayList<OperationElementaire> listeOperation = Utilisateur.listerOperationUtilisateur(this.oper.getId(), this.gestionnaire.getConnection());
        String d="";
        d=d+"Qualifications :";
        for (int i =0;i<listeOperation.size();i++){
            d=d+"\n"+"- "+listeOperation.get(i).getType();
        }
        return d;
    }

    public void supprimer(){
        try{
            Utilisateur.supprimerOperateur(this.oper.getId(), this.gestionnaire.getConnection());
            this.removeAll();
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void recharger()throws SQLException{
        this.oper=Utilisateur.getUtilisateur(this.oper.getId(), this.gestionnaire.getConnection());
        this.nom.setValue(this.oper.getNom()+" "+this.oper.getPrenom());
        if(this.oper.getEtat() !=null){
            this.etat.setValue(this.oper.getEtat());
            if(this.oper.getEtat().equals("Disponible")){
                this.etat.getStyle().setColor("green");
            }
            else{
                this.etat.getStyle().setColor("red");
            }
        }
        this.description.setValue(decrire());
        this.add(this.texte,this.boutons);
    }

    public void modifier(){
        try{
            this.removeAll();
            //changer ici la hauteur de this
            this.add(new ParametreOperateur(this));
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public Utilisateur getOperateur(){
        return this.oper;
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }
}
