package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

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

    public GroupeMachine(Gestionnaire g, machine m)throws SQLException{
        this.gestionnaire=g;
        this.mach=m;
        this.nom=new TextField();
        this.description=new TextArea();
        this.modifier=new Button("Modifier");
        this.supprimer=new Button("Supprimer");
        this.etat=new TextField();

        //paramétrage de la structure des éléments
        HorizontalLayout entete = new HorizontalLayout();
        entete.add(this.nom,this.etat);
        VerticalLayout texte=new VerticalLayout();
        texte.add(entete,this.description);
        VerticalLayout boutons=new VerticalLayout();
        boutons.add(this.modifier,this.supprimer);

        //paramétrage des données des éléments
        this.nom.setValue(this.mach.getNom());
        this.etat.setValue(this.mach.getEtat());
        this.description.setValue(decrire());

        //ajout des éléments à la page
        this.add(texte,boutons);
    }

    public String decrire() throws SQLException{
        ArrayList<OperationElementaire> listeOperation = machine.getOperationElementaireMachine(this.mach.getId(), gestionnaire.getConnection());
        String d="Vitesse : "+/*this.machine.getPuissance()+*/"\n"+"Puissance : "+this.mach.getPuissance();
        for (int i =0;i<listeOperation.size();i++){
            d=d+"\n"+"- "+listeOperation.get(i).getType();
        }
        d=d+"\n"+"Référence : "+this.mach.getRef();
        return d;
    }
}
