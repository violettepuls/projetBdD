package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.application.views.main.GroupeMachine.Spacer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.machine;
import traitement.Gestionnaire;

public class VueMachine extends VerticalLayout{
    private TextField titre;
    private Button ajouter;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeMachine;

    public VueMachine(Gestionnaire g) throws SQLException{
        //déclaration des éléments
        this.gestionnaire=g;
        this.titre = new TextField();
        this.ajouter = new Button("Ajouter");
        this.corps = new Scroller();
        this.listeMachine = new VerticalLayout();

        //Acquisition des données
        this.titre.setValue("Liste des Machines");
        formater();

        //Placement et paramétrage des éléments
        this.setSizeFull(); // Définir la hauteur de la vue à 100%

        // Paramétrage du Scroller et de son conteneur parent
        this.corps.setContent(listeMachine);
        this.corps.setSizeFull();
        this.corps.getStyle().set("max-height", "100%");
        this.corps.getStyle().set("max-width", "100%");
        
        this.add(titre,corps,ajouter);
        
    }

    public void ajouter(){
        //ouvre une VueCreationMachine qui revient sur VueMachine à la fermeture et créé une machine dans la BDD quand bouton Validé appuyé
        //enleve le reste (this.removeAll()) de l'affichage
    }
    
    public void formater() throws SQLException{
        ArrayList<machine> liste = machine.listerMachine(this.gestionnaire.getCurAtelier(), this.gestionnaire.getConnection());
        for (int i=0;i<liste.size();i++){
            this.listeMachine.add(new GroupeMachine(gestionnaire, liste.get(i)));
              if (i < liste.size() - 1) {
                this.listeMachine.add(new Spacer());
            }
        }
    }
    public class Spacer extends Div {
        public Spacer() {
            setHeight("6em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }
}
