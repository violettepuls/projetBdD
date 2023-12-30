package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;

public class ParametreProduit {
    private GroupeProduit gpProduit;
    private TextField nom;
    private TextField ref;
    private TextField gamme; //a changer en simple combo box
    private Button valider;
    private Button annuler;
    private VerticalLayout boutons;
    private VerticalLayout champParametre;
    private VerticalLayout champGamme;
    //creer une classe composantGamme qui possede 1 textfield (operation), 1 textfield (unite operation), 1 bouton (supprimer operation) qui servira a pouvoir creer une gamme au sein de la modif produit

    public ParametreProduit(GroupeProduit gp)throws SQLException{
        //Déclaration des éléments
        this.gpProduit=gp;
        this.nom=new TextField("Nom de la machine");
        this.ref=new TextField("Référence");
        this.gamme = new TextField("Puissance");
        this.vitesse = new TextField("Vitesse");
        this.nouvelleOperation = new TextField();
        this.nouvelleOperation.setPlaceholder("Type d'opération");
        this.operation = new MultiSelectComboBox<OperationElementaire>("Opérations réalisables");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.creerOperation = new Button("Créer une opération");
        this.validerNouvelleOperation = new Button("Ajouter");
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.nom,this.ref,this.gamme,this.vitesse);
        this.champGamme=new VerticalLayout(this.operation,this.creerOperation);
        this.champNouvelleOperation= new HorizontalLayout(this.nouvelleOperation,this.validerNouvelleOperation);
        
        //Ajout de tout dans l'élément principal
        this.add(this.champParametre,this.champGamme,this.boutons);

        //Paramètres initiaux des champs
        this.nom.setValue(this.gpProduit.getMachine().getNom());
        this.ref.setValue(this.gpProduit.getMachine().getRef());
        this.gamme.setValue(String.valueOf(this.gpProduit.getMachine().getPuissance()));
        this.vitesse.setValue(String.valueOf(this.gpProduit.getMachine().getVitesse()));
        this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpProduit.getGestionnaire().getConnection()));
        this.operation.setItemLabelGenerator(OperationElementaire::getType);
        this.operation.setValue(machine.listerOperationElementaireMachine(this.gpProduit.getMachine().getId(), this.gpProduit.getGestionnaire().getConnection()));

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
            machine.modifierMachine(this.gpProduit.getMachine().getId(), this.nom.getValue(), this.ref.getValue(), Double.valueOf(this.gamme.getValue()), Double.valueOf(this.vitesse.getValue()), this.gpProduit.getGestionnaire().getConnection());
            OperationElementaire.dissocierMachineOperationGlobal(this.gpProduit.getMachine().getId(), this.gpProduit.getGestionnaire().getConnection());
            OperationElementaire.associerListeMachineOperation(new ArrayList<OperationElementaire>(this.operation.getSelectedItems()),this.gpProduit.getMachine().getId(),this.gpProduit.getGestionnaire().getConnection());
            this.gpProduit.recharger();
            this.gpProduit.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void annuler(){
        try{
            this.gpProduit.recharger();
            this.gpProduit.remove(this);
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
            OperationElementaire.creerOperationElementaire(this.nouvelleOperation.getValue(), this.gpProduit.getGestionnaire().getConnection());
            this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpProduit.getGestionnaire().getConnection()));
            this.operation.setItemLabelGenerator(OperationElementaire::getType);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }
}
