package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

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
        this.add(titre,corps,ajouter);

        //Pré-remplissage
        this.titre.setValue("Liste des Machines");
        formater();
        this.corps.setContent(listeMachine);

        //Attribution des fonctions
        this.ajouter.addClickListener(clickevent -> {
            ajouter();
            
        });
        
        //Esthétique
        this.setSizeFull(); // Définir la hauteur de la vue à 100%
        this.corps.setSizeFull();
        this.corps.getStyle().set("max-height", "100%");
        this.corps.getStyle().set("max-width", "100%");
        this.corps.setWidth("75em");
        this.corps.setHeight("100%");
       // this.ajouter.getStyle().set("position","fixed").set("bottom","6em").set("left","8em");

    }

    public void ajouter(){
        //ouvre une VueCreationMachine qui revient sur VueMachine à la fermeture et créé une machine dans la BDD quand bouton Validé appuyé
        //enleve le reste (this.removeAll()) de l'affichage
        try{
            //changer ici la hauteur de this
            
            VueCreationMachine ajout = new VueCreationMachine(gestionnaire);
            this.add(ajout);
            listeMachine.removeAll();
            formater();
           
        }
        catch(SQLException e){
            System.out.println("Erreur : "+e);
        }

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
