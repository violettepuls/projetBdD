package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class VueOperateur extends VerticalLayout{
    private TextField titre;
    private Button ajouter;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeOperateur;

    public VueOperateur(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.ajouter = new Button("Ajouter");
        this.corps = new Scroller();
        this.listeOperateur = new VerticalLayout();

        //Acquisition des données
        this.titre.setValue("Liste des Opérateurs");
        formater();

        //Placement et paramétrage des éléments
        this.corps.setContent(listeOperateur);
        this.corps.setSizeFull();
        this.add(titre,corps,ajouter);
        
        //Esthétique

    }


    public void ajouter(){
        //ouvre une VueCreationMachine qui revient sur VueMachine à la fermeture et créé une machine dans la BDD quand bouton Validé appuyé (COPIE COLLE, MAIS IDEE IDENTIQUE AVEC OPERATEur)
        //enleve le reste (this.removeAll()) de l'affichage
    }
    
    public void formater() throws SQLException{
        ArrayList<Utilisateur> liste = Utilisateur.listerOperateur(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeOperateur.add(new GroupeOperateur(gestionnaire, liste.get(i)));
            if (i < liste.size() - 1) {
                this.listeOperateur.add(new Spacer());
            }
        }
    }
    public class Spacer extends Div {
        public Spacer() {
            setHeight("4em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }
}
