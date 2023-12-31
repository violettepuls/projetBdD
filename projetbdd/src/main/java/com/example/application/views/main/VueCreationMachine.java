package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.machine;
import traitement.Gestionnaire;
import classe_tables.OperationElementaire;

public class VueCreationMachine extends HorizontalLayout {

    private VueMachine vm;
    private TextField nom;
    private TextField ref;
    private TextField Etat;
    private TextField puissance;
    private TextField vitesse;
    private MultiSelectComboBox<OperationElementaire> operation;
    private Button valider;
    private Button annuler;
    private VerticalLayout boutons;
    private VerticalLayout champParametre;
    private VerticalLayout champOperation;
    private Button creerOperation;
    private Button validerNouvelleOperation;
    private TextField nouvelleOperation;
    private HorizontalLayout champNouvelleOperation;
    private int indicateur;

    public VueCreationMachine(Gestionnaire g,VueMachine v)throws SQLException{

        //Déclaration des éléments
        this.vm=v;
        this.indicateur = 0;
        this.nom=new TextField("Nom de la machine");
        this.ref=new TextField("Référence");
        this.puissance = new TextField("Puissance");
        this.vitesse = new TextField("Vitesse");
        this.Etat = new TextField("Etat");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.creerOperation = new Button("Créer une opération");
        this.validerNouvelleOperation = new Button("Ajouter");
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.nom,this.ref,this.puissance,this.Etat,this.vitesse);
        this.add(this.champParametre,this.boutons);

        //Mise en fonction des boutons et actions
        //while (indicateur == 0){
            this.valider.addClickListener(clickevent -> {
            try {
                machine.creerMachine(this.nom.getValue(), this.ref.getValue(), this.Etat.getValue(),Double.valueOf(this.puissance.getValue()) , Double.valueOf(this.vitesse.getValue()), g.getCurAtelier(),g.getConnection());
                this.indicateur=1;
                this.vm.recharger();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }
            });
            this.annuler.addClickListener(clickevent -> {
            annuler();
            });

        //}

        //Esthétique

    }

    public int getIndicateur(){
        return this.indicateur;
    }

    public void annuler(){
            this.removeAll();
            this.indicateur=1;
    }
}


