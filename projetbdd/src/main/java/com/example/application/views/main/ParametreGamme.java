package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Gamme;
import classe_tables.OperationElementaire;
import traitement.Gestionnaire;

public class ParametreGamme extends HorizontalLayout{
    private GroupeGamme gpGamme;
    private Gestionnaire gestionnaire;
    private Gamme gamme;
    private TextField ou;
    private TextField ref;
    private TextField nouvelleOperationType;
    private TextField nouvelleOperationUO;
    private VerticalLayout champOperation;
    private VerticalLayout boutons;
    private VerticalLayout champGamme;
    private ComboBox<OperationElementaire> operationExistante;
    private Button creerOperation;
    private Button validerNouvelleOperation;
    private Button valider;
    private Button annuler;
    private HorizontalLayout ajoutOperation;
    private HorizontalLayout nouvelleOperation;
    private ArrayList<OperationElementaire> listeOperation;
    private int lastIndex;

    public ParametreGamme(GroupeGamme gp)throws SQLException{
        //Déclaration
        this.gpGamme=gp;
        this.gestionnaire=gp.getGestionnaire();
        this.gamme=gp.getGamme();
        this.listeOperation=Gamme.getOperationGamme(this.gamme.getId(), this.gestionnaire.getConnection());
        this.lastIndex = listeOperation.size()-1;
        this.ref=new TextField();
        this.nouvelleOperationType = new TextField();
        this.nouvelleOperationUO = new TextField();
        this.operationExistante = new ComboBox<OperationElementaire>();
        this.creerOperation = new Button("Créer opération");
        this.valider=new Button("Valider");
        this.annuler=new Button("Annuler");
        this.ou = new TextField();
        this.validerNouvelleOperation=new Button(new Icon(VaadinIcon.PLUS));
        this.validerNouvelleOperation.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.nouvelleOperation = new HorizontalLayout(nouvelleOperationType,nouvelleOperationUO,validerNouvelleOperation);
        this.ajoutOperation = new HorizontalLayout(operationExistante,ou,creerOperation);
        this.boutons = new VerticalLayout(valider,annuler);
        this.champOperation=new VerticalLayout();
        this.champGamme=new VerticalLayout(ref,champOperation,ajoutOperation);

        //Pré-remplissage
        this.add(champGamme,boutons);
        this.ref.setValue(this.gamme.getRef());
        this.ou.setValue(" ou ");
        this.ou.setReadOnly(true);
        chargerGamme();
        this.operationExistante.setPlaceholder("Ajouter existante");
        this.operationExistante.setItems(OperationElementaire.listerOperation(this.gestionnaire.getConnection()));
        this.operationExistante.setItemLabelGenerator(OperationElementaire::affichage);
        this.nouvelleOperationType.setPlaceholder("Type d'opération");
        this.nouvelleOperationUO.setPlaceholder("Unité opération");

        //Attribution des fonctions
        this.operationExistante.addValueChangeListener(event ->{
            this.lastIndex=lastIndex+1;
            this.champOperation.add(new HBoxOperation2(this.operationExistante.getValue(), this,this.lastIndex));
            refreshOrdreOperation();
        });
        this.creerOperation.addClickListener(clickevent ->{
            this.champGamme.remove(this.ajoutOperation);
            this.champGamme.add(this.nouvelleOperation);
        });
        this.validerNouvelleOperation.addClickListener(clickevent->{
            try{
                this.lastIndex=lastIndex+1;
                OperationElementaire.creerOperation(this.nouvelleOperationType.getValue(), Double.valueOf(this.nouvelleOperationUO.getValue()), this.gestionnaire.getConnection());
                this.champOperation.add(new HBoxOperation2(OperationElementaire.getOperation(OperationElementaire.getIdOperation(this.nouvelleOperationType.getValue(), Double.valueOf(this.nouvelleOperationUO.getValue()),this.gestionnaire.getConnection()), this.gestionnaire.getConnection()), this,this.lastIndex));
                refreshOrdreOperation();
                this.operationExistante.setItems(OperationElementaire.listerOperation(this.gestionnaire.getConnection()));
            }
            catch(SQLException e){
                System.out.println("Erreur création opération : "+e);
            }
            this.champGamme.remove(this.nouvelleOperation);
            this.champGamme.add(this.ajoutOperation);
        });
        this.valider.addClickListener(clickevent -> {
            valider();
        });
        this.annuler.addClickListener(clickevent -> {
            annuler();
        });

        //Esthétique
        
    }

    public void chargerGamme(){
        try{
            this.champOperation.removeAll();
            this.listeOperation=Gamme.getOperationGamme(this.gamme.getId(), this.gestionnaire.getConnection());
            this.lastIndex=listeOperation.size()-1;
            for (int i=0;i<listeOperation.size();i++){
                this.champOperation.add(new HBoxOperation2(listeOperation.get(i), this,i));
            }
        }
        catch(SQLException e){
            System.out.println("Erreur changement gamme : "+e);
        }
    }

    public void valider(){
        try{
            Gamme.modifierGamme(this.gamme.getId(), reconstructionGamme(), this.gestionnaire.getConnection());
            this.gpGamme.recharger();
            this.gpGamme.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }
    }

    public void annuler(){
        try{
            this.gpGamme.recharger();
            this.gpGamme.remove(this);
        }
        catch(SQLException e){
            System.out.println("Erreur d'annulation : "+e);
        }
    }

    public VerticalLayout getChampOperation(){
        return this.champOperation;
    }

    public ArrayList<OperationElementaire> reconstructionGamme(){
        ArrayList<OperationElementaire> liste = new ArrayList<OperationElementaire>();
        int nombreOperation = this.champOperation.getComponentCount();
        for (int i=0;i<nombreOperation;i++){
            liste.add(((HBoxOperation)this.champOperation.getComponentAt(i)).getOperation());
        }
        return liste;
    }

    public void refreshOrdreOperation(){
        this.lastIndex=this.champOperation.getComponentCount()-1;
        for (int i=0;i<this.champOperation.getComponentCount();i++){
            ((HBoxOperation2)this.champOperation.getComponentAt(i)).setIndex(i);
        }
    }

    public int getLastIndex(){
        return this.lastIndex;
    }
}
