package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;
import classe_tables.Utilisateur;

public class ParametreOperateur extends HorizontalLayout{
    private GroupeOperateur gpOperateur;
    private TextField nom;
    private TextField prenom;
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
    private Button disponible;

    public ParametreOperateur(GroupeOperateur go)throws SQLException{
        //Déclaration des éléments
        this.gpOperateur=go;
        this.nom=new TextField("Nom");
        this.prenom=new TextField("Prénom");
        this.nouvelleOperation = new TextField();
        this.nouvelleOperation.setPlaceholder("Type d'opération");
        this.operation = new MultiSelectComboBox<OperationElementaire>("Opérations réalisables");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.creerOperation = new Button("Créer une opération");
        this.validerNouvelleOperation = new Button("Ajouter");
        this.disponible = new Button();
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.nom,this.prenom);
        this.champOperation=new VerticalLayout(this.operation,this.creerOperation);
        this.champNouvelleOperation= new HorizontalLayout(this.nouvelleOperation,this.validerNouvelleOperation);
        
        //Ajout de tout dans l'élément principal
        this.add(this.champParametre,this.champOperation,this.boutons);

        //Paramètres initiaux des champs
        this.nom.setReadOnly(true);
        this.prenom.setReadOnly(true);
        this.nom.setValue(this.gpOperateur.getOperateur().getNom());
        this.prenom.setValue(this.gpOperateur.getOperateur().getPrenom());
        this.disponible.setText(this.gpOperateur.getOperateur().getEtat());
        if(this.disponible.getText().equals("Disponible")){
            this.disponible.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        }
        else{
            this.disponible.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        }
        this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpOperateur.getGestionnaire().getConnection()));
        this.operation.setItemLabelGenerator(OperationElementaire::getType);
        this.operation.setValue(Utilisateur.listerOperationUtilisateur(this.gpOperateur.getOperateur().getId(), this.gpOperateur.getGestionnaire().getConnection()));

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
        this.disponible.addClickListener(clickevent -> {
            modifierDisponibilite();
        });

        //Esthétique

    }

    public void valider(){
        try{
            Utilisateur.dissocierOperationUtilisateurGlobal(this.gpOperateur.getOperateur().getId(), this.gpOperateur.getGestionnaire().getConnection());
            Utilisateur.associerListeOperationUtilisateur(new ArrayList<OperationElementaire>(this.operation.getSelectedItems()),this.gpOperateur.getOperateur().getId(),this.gpOperateur.getGestionnaire().getConnection());
            this.gpOperateur.recharger();
            this.gpOperateur.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void annuler(){
        try{
            this.gpOperateur.recharger();
            this.gpOperateur.remove(this);
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
            OperationElementaire.creerOperationElementaire(this.nouvelleOperation.getValue(), this.gpOperateur.getGestionnaire().getConnection());
            this.operation.setItems(OperationElementaire.listerOperationElementaire(this.gpOperateur.getGestionnaire().getConnection()));
            this.operation.setItemLabelGenerator(OperationElementaire::getType);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void modifierDisponibilite(){
        try{
            if(this.gpOperateur.getOperateur().getEtat().equals("Disponible")){
                Utilisateur.disponible(this.gpOperateur.getOperateur().getId(),false,this.gpOperateur.getGestionnaire().getConnection());
                this.disponible.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
            }
            else{
                Utilisateur.disponible(this.gpOperateur.getOperateur().getId(),true,this.gpOperateur.getGestionnaire().getConnection());
                this.disponible.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
            }
            this.disponible.setText(Utilisateur.getUtilisateur(this.gpOperateur.getOperateur().getId(), this.gpOperateur.getGestionnaire().getConnection()).getEtat());
        }
        catch (SQLException e){
            System.out.println("Erreur de dispo : "+e);
        }
    }
}