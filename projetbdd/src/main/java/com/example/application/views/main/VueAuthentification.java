package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import classe_tables.Atelier;
import traitement.Gestionnaire;

@Route(value = "")
public class VueAuthentification extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField username;
    private PasswordField mdp;
    private ComboBox<String> listeAtelier;
    private Button validerAuthentification;
    private Button creerAtelier;
    private Notification messageErreur;
    
    public VueAuthentification() throws ClassNotFoundException,SQLException{
        this.gestionnaire=new Gestionnaire();
        this.messageErreur = new Notification();
        this.listeAtelier = new ComboBox<String>("Se connecter à l'atelier");
        remplirListeAtelier();
        this.username = new TextField("Nom d'utilisateur");
        this.mdp = new PasswordField("Mot de passe");
        this.validerAuthentification = new Button("Se connecter");
        this.validerAuthentification.addClickListener(clickEvent -> {
            authentifier();
        });
        this.creerAtelier = new Button("Créer un atelier");
        this.creerAtelier.addClickListener(clickEvent -> {

        });
        HorizontalLayout boxAtelier = new HorizontalLayout();
        boxAtelier.add(this.listeAtelier,this.creerAtelier);
        this.add(this.username,this.mdp,boxAtelier,this.validerAuthentification);
    }

    public void remplirListeAtelier(){
        ArrayList<Atelier> ateliers = Atelier.listerAtelier(gestionnaire.getConnection());
        ArrayList<String> ateliersNom = new ArrayList<String>();
        for (int i=0;i<ateliers.size();i++){
            ateliersNom.add(ateliers.get(i).getNom());
        }
        this.listeAtelier.setItems(ateliersNom);
    }

    public void authentifier(){
        try{
                if(gestionnaire.authentification(this.username.getValue(), this.mdp.getValue())){
                    if(gestionnaire.autorisationAtelier(Atelier.getIdAtelier(listeAtelier.getValue(),gestionnaire.getConnection()))){
                        System.out.println("Authentification réussie !");
                        //getUI().ifPresent(ui -> ui.navigate("accueil"));
                        MainView mv = new MainView(gestionnaire);
                        this.removeAll();
                        this.add(mv);
                    }
                    else{
                        messageErreur.show("Vous n'avez pas accès à cet atelier.");
                    }
                }
                else{
                    messageErreur.show("Votre nom d'utilisateur ou mot de passe est incorrect.");
                }
            }
            catch (SQLException e){
                messageErreur.show("Une erreur est survenue : "+e);
            }
    }
}
