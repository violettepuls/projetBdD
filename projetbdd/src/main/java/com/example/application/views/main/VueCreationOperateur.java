package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;
import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class VueCreationOperateur extends HorizontalLayout{
    private VueOperateur vo;
    private Gestionnaire gestionnaire;
    private MultiSelectComboBox<OperationElementaire> operation;
    private Select<Utilisateur> utilisateur;
    private Button valider;
    private Button annuler;
    private VerticalLayout boutons;
    private VerticalLayout champParametre;
    private VerticalLayout champOperation;
    private Button creerOperation;
    private Button validerNouvelleOperation;
    private TextField nouvelleOperation;
    private HorizontalLayout champNouvelleOperation;
    private ComboBox<String> disponible;

    public VueCreationOperateur(VueOperateur v)throws SQLException{
        //Déclaration des éléments
        this.vo=v;
        this.gestionnaire=v.getGestionnaire();
        this.nouvelleOperation = new TextField();
        this.nouvelleOperation.setPlaceholder("Type d'opération");
        this.utilisateur = new Select<Utilisateur>();
        this.utilisateur.setLabel("Utilisateur à rendre opérateur");
        this.operation = new MultiSelectComboBox<OperationElementaire>("Qualifications");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.creerOperation = new Button("Créer une opération");
        this.validerNouvelleOperation = new Button("Ajouter");
        this.disponible = new ComboBox<String>();
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.utilisateur,this.disponible);
        this.champOperation=new VerticalLayout(this.operation,this.creerOperation);
        this.champNouvelleOperation= new HorizontalLayout(this.nouvelleOperation,this.validerNouvelleOperation);
        this.add(this.champParametre,this.champOperation,this.boutons);

        //Paramètres initiaux des champs
        this.utilisateur.setItems(Utilisateur.listerNonOperateur(gestionnaire.getCurAtelier(), gestionnaire.getConnection()));
        this.utilisateur.setItemLabelGenerator(Utilisateur::getNomComplet);
        this.operation.setItems(OperationElementaire.listerOperationElementaire(gestionnaire.getConnection()));
        this.operation.setItemLabelGenerator(OperationElementaire::getType);
        this.disponible.setItems("Disponible","Indisponible");

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
            Utilisateur.modifierStatutOperateur(utilisateur.getValue().getId(), true, gestionnaire.getConnection());
            Utilisateur.dissocierOperationUtilisateurGlobal(utilisateur.getValue().getId(), gestionnaire.getConnection());
            Utilisateur.associerListeOperationUtilisateur(new ArrayList<OperationElementaire>(this.operation.getSelectedItems()),utilisateur.getValue().getId(),gestionnaire.getConnection());
            if(disponible.equals("Disponible")){
                Utilisateur.disponible(utilisateur.getValue().getId(), true, gestionnaire.getConnection());
            }
            else if(disponible.equals("Indisponible")){
                Utilisateur.disponible(utilisateur.getValue().getId(), false, gestionnaire.getConnection());
            }
            this.vo.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void annuler(){
        try{
            this.vo.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur d'annulation : "+e);
        }
    }

    public void creerOperation(){
        this.champOperation.remove(this.creerOperation);
        this.champOperation.add(this.champNouvelleOperation);
    }

    public void ajouterNouvelleOperation(){
        try{
            OperationElementaire.creerOperationElementaire(this.nouvelleOperation.getValue(), this.gestionnaire.getConnection());
            this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gestionnaire.getConnection()));
            this.operation.setItemLabelGenerator(OperationElementaire::getType);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }
}
