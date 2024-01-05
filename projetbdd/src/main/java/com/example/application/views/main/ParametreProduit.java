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
import classe_tables.Produit;

public class ParametreProduit extends HorizontalLayout{
    private GroupeProduit gpProduit;
    private TextField nom;
    private TextField ref;
    private TextField ou;
    private TextField nouvelleOperationType;
    private TextField nouvelleOperationUO;
    private ComboBox<Gamme> gamme;
    private ComboBox<OperationElementaire> operationExistante;
    private Button valider;
    private Button annuler;
    private Button creerOperation;
    private Button validerNouvelleOperation;
    private HorizontalLayout ajoutOperation;
    private HorizontalLayout nouvelleOperation;
    private VerticalLayout boutons;
    private VerticalLayout champParametre;
    private VerticalLayout champGamme;
    private VerticalLayout champOperation;
    private ArrayList<OperationElementaire> listeOperation;
    private int lastIndex;

    public ParametreProduit(GroupeProduit gp)throws SQLException{
        //Déclaration des éléments
        this.gpProduit=gp;
        this.listeOperation=Gamme.getOperationGamme(this.gpProduit.getProduit().getIdGamme(), this.gpProduit.getGestionnaire().getConnection());
        this.lastIndex = listeOperation.size()-1;
        this.nom=new TextField("Nom du produit");
        this.ref=new TextField("Référence");
        this.gamme = new ComboBox<Gamme>("Gamme");
        this.valider = new Button("Valider");
        this.annuler = new Button("Annuler");
        this.ou=new TextField();
        this.nouvelleOperationType=new TextField();
        this.nouvelleOperationUO=new TextField();
        this.operationExistante = new ComboBox<OperationElementaire>();
        this.creerOperation = new Button("Créer opération");
        this.validerNouvelleOperation=new Button(new Icon(VaadinIcon.PLUS));
        this.validerNouvelleOperation.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        this.nouvelleOperation = new HorizontalLayout(nouvelleOperationType,nouvelleOperationUO,validerNouvelleOperation);
        this.ajoutOperation = new HorizontalLayout(operationExistante,ou,creerOperation);
        this.boutons = new VerticalLayout(this.valider,this.annuler);
        this.champParametre=new VerticalLayout(this.nom,this.ref);
        this.champOperation= new VerticalLayout();
        this.champGamme=new VerticalLayout(this.gamme,this.champOperation,this.ajoutOperation);
        this.add(this.champParametre,this.champGamme,this.boutons);

        //Paramètres initiaux des champs
        this.nom.setValue(this.gpProduit.getProduit().getNom());
        this.ref.setValue(this.gpProduit.getProduit().getReference());
        this.gamme.setItems(Gamme.listerGammeGlobal(this.gpProduit.getGestionnaire().getConnection()));
        this.gamme.setItemLabelGenerator(Gamme::getRef);
        this.gamme.setValue(Gamme.getGamme(this.gpProduit.getProduit().getIdGamme(), this.gpProduit.getGestionnaire().getConnection()));
        changementGamme(Gamme.getGamme(this.gpProduit.getProduit().getIdGamme(), this.gpProduit.getGestionnaire().getConnection()));
        this.ou.setValue(" ou ");
        this.ou.setReadOnly(true);
        this.operationExistante.setPlaceholder("Ajouter existante");
        this.operationExistante.setItems(OperationElementaire.listerOperation(this.gpProduit.getGestionnaire().getConnection()));
        this.operationExistante.setItemLabelGenerator(OperationElementaire::affichage);
        this.nouvelleOperationType.setPlaceholder("Type d'opération");
        this.nouvelleOperationUO.setPlaceholder("Unité opération");

        //Mise en fonction des boutons et actions
        this.gamme.addValueChangeListener(event ->{
            changementGamme(this.gamme.getValue());
        });
        this.operationExistante.addValueChangeListener(event ->{
            this.lastIndex=lastIndex+1;
            this.champOperation.add(new HBoxOperation(this.operationExistante.getValue(), this,this.lastIndex));
            refreshOrdreOperation();
        });
        this.creerOperation.addClickListener(clickevent ->{
            this.champGamme.remove(this.ajoutOperation);
            this.champGamme.add(this.nouvelleOperation);
        });
        this.validerNouvelleOperation.addClickListener(clickevent->{
            try{
                this.lastIndex=lastIndex+1;
                OperationElementaire.creerOperation(this.nouvelleOperationType.getValue(), Double.valueOf(this.nouvelleOperationUO.getValue()), this.gpProduit.getGestionnaire().getConnection());
                this.champOperation.add(new HBoxOperation(OperationElementaire.getOperation(OperationElementaire.getIdOperation(this.nouvelleOperationType.getValue(), Double.valueOf(this.nouvelleOperationUO.getValue()),this.gpProduit.getGestionnaire().getConnection()), this.gpProduit.getGestionnaire().getConnection()), this,this.lastIndex));
                refreshOrdreOperation();
                this.operationExistante.setItems(OperationElementaire.listerOperation(this.gpProduit.getGestionnaire().getConnection()));
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

    public void changementGamme(Gamme selectedGamme){
        try{
            this.champOperation.removeAll();
            this.listeOperation=Gamme.getOperationGamme(selectedGamme.getId(), this.gpProduit.getGestionnaire().getConnection());
            this.lastIndex=listeOperation.size()-1;
            for (int i=0;i<listeOperation.size();i++){
                this.champOperation.add(new HBoxOperation(listeOperation.get(i), this,i));
            }
        }
        catch(SQLException e){
            System.out.println("Erreur changement gamme : "+e);
        }
    }

    public ArrayList<OperationElementaire> getOperation(){
        return this.listeOperation;
    }

    public void valider(){
        try{
            Produit.modifierProduit(this.gpProduit.getProduit().getId(),this.nom.getValue(),this.ref.getValue(),this.gamme.getValue(),this.gpProduit.getGestionnaire().getConnection());
            Gamme.modifierGamme(this.gamme.getValue().getId(), reconstructionGamme(), this.gpProduit.getGestionnaire().getConnection());
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
            ((HBoxOperation)this.champOperation.getComponentAt(i)).setIndex(i);
        }
    }

    public int getLastIndex(){
        return this.lastIndex;
    }
}
