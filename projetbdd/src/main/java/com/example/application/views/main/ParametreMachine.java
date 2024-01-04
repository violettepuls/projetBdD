package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;
import classe_tables.machine;

public class ParametreMachine extends HorizontalLayout{
    private GroupeMachine gpMachine;
    private TextField nom;
    private TextField ref;
    private TextField puissance;
    private TextField vitesse;
    private MultiSelectComboBox<OperationElementaire> operation; //n'a pas encore été traitée
    private Button valider;
    private Button annuler;
    private VerticalLayout boutons;
    private VerticalLayout champParametre;
    private VerticalLayout champOperation;
    private Button creerOperation;
    private Button validerNouvelleOperation;
    private TextField nouvelleOperation;
    private HorizontalLayout champNouvelleOperation;

    public ParametreMachine(GroupeMachine gm)throws SQLException{
        //Déclaration des éléments
        this.gpMachine=gm;
        this.nom=new TextField("Nom de la machine");
        this.ref=new TextField("Référence");
        this.puissance = new TextField("Puissance");
        this.vitesse = new TextField("Vitesse");
        this.nouvelleOperation = new TextField();
        this.nouvelleOperation.setPlaceholder("Type d'opération");
        this.operation = new MultiSelectComboBox<OperationElementaire>("Opérations réalisables");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.creerOperation = new Button("Créer une opération");
        this.validerNouvelleOperation = new Button("Ajouter");
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.nom,this.ref,this.puissance,this.vitesse);
        this.champOperation=new VerticalLayout(this.operation,this.creerOperation);
        this.champNouvelleOperation= new HorizontalLayout(this.nouvelleOperation,this.validerNouvelleOperation);
        
        //Ajout de tout dans l'élément principal
        this.add(this.champParametre,this.champOperation,this.boutons);

        //Paramètres initiaux des champs
        this.nom.setValue(this.gpMachine.getMachine().getNom());
        this.ref.setValue(this.gpMachine.getMachine().getRef());
        this.puissance.setValue(String.valueOf(this.gpMachine.getMachine().getPuissance()));
        this.vitesse.setValue(String.valueOf(this.gpMachine.getMachine().getVitesse()));
        this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpMachine.getGestionnaire().getConnection()));
        this.operation.setItemLabelGenerator(OperationElementaire::getType);
        this.operation.setValue(machine.listerOperationElementaireMachine(this.gpMachine.getMachine().getId(), this.gpMachine.getGestionnaire().getConnection()));

        //Mise en fonction des boutons et actions
        this.valider.addClickListener(clickevent -> {
            valider();
        });
        this.annuler.addClickListener(clickevent -> {
            annuler();
        });
        this.validerNouvelleOperation.addClickListener(clickevent -> {
            ajouterNouvelleOperation();
        });
        this.creerOperation.addClickListener(clickevent -> {
            creerOperation();
        });

        //Esthétique

    }

    public void valider(){
        try{
            machine.modifierMachine(this.gpMachine.getMachine().getId(), this.nom.getValue(), this.ref.getValue(), Double.valueOf(this.puissance.getValue()), Double.valueOf(this.vitesse.getValue()), this.gpMachine.getGestionnaire().getConnection());
            OperationElementaire.dissocierMachineOperationGlobal(this.gpMachine.getMachine().getId(), this.gpMachine.getGestionnaire().getConnection());
            OperationElementaire.associerListeMachineOperation(new ArrayList<OperationElementaire>(this.operation.getSelectedItems()),this.gpMachine.getMachine().getId(),this.gpMachine.getGestionnaire().getConnection());
            this.gpMachine.recharger();
            this.gpMachine.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void annuler(){
        try{
            this.gpMachine.recharger();
            this.gpMachine.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur d'annulation : "+e);
        }
    }

    public void creerOperation(){
        this.remove(this.creerOperation);
        this.add(this.champNouvelleOperation);
    }

    public void ajouterNouvelleOperation(){
        try{
            OperationElementaire.creerOperationElementaire(this.nouvelleOperation.getValue(), this.gpMachine.getGestionnaire().getConnection());
            this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpMachine.getGestionnaire().getConnection()));
            this.operation.setItemLabelGenerator(OperationElementaire::getType);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }
}