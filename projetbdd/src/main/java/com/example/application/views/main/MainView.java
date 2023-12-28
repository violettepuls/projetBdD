package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import classe_tables.Atelier;
import traitement.Gestionnaire;
@Route(value = "")

/* 
public class MainView extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField username;
    private TextField mdp;
    private ComboBox<String> listeAtelier;
    private Button validerAuthentification;
    private Button creerAtelier;
    private Notification messageErreur;
    private TextField name;

    public MainView(){
        name = new TextField("Your name");
        setHorizontalComponentAlignment(Alignment.CENTER, name);
        add(name);
    } 
    
}*/



public class MainView extends VerticalLayout {

    public MainView() {
        // Création des champs d'entrée
        TextField usernameField = new TextField("Identifiant");
        PasswordField passwordField = new PasswordField("Mot de passe");

        // Bouton de connexion
        Button loginButton = new Button("Se connecter", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            // Vérifiez les identifiants (c'est ici que vous ajouteriez votre logique d'authentification)
            if (isValidCredentials(username, password)) {
                // Si les identifiants sont valides, naviguez vers la nouvelle vue
                getUI().ifPresent(ui -> ui.navigate("accueil"));
            } else {
                // Affichez un message d'erreur
                Notification.show("Identifiant ou mot de passe incorrect");
            }
        });

        // Ajout des composants à la mise en page
        add(usernameField, passwordField, loginButton);
        setAlignItems(Alignment.CENTER);
        setWidth("300px");

        // Lien vers une autre vue (pour la démo)
      //  RouterLink accueilLink = new RouterLink("Accueil", Vuedebase.class);
      //  add(accueilLink);
    }
private boolean isValidCredentials(String username, String password) {
        // Ajoutez votre logique d'authentification ici
        // Vous pouvez vous connecter à une base de données, vérifier dans un service d'authentification, etc.
        // Pour cet exemple, nous considérons que les identifiants sont valides si le champ "Identifiant" n'est pas vide
        return !username.isEmpty();
    }
}
 /*    public MainView(Gestionnaire g){
         this.gestionnaire=g;
        this.messageErreur = new Notification();
        this.listeAtelier = new ComboBox<String>("Se connecter à l'atelier");
        remplirListeAtelier();
        this.username = new TextField("Nom d'utilisateur");
        this.mdp = new TextField("Mot de passe");
        this.validerAuthentification = new Button("Se connecter");
        this.validerAuthentification.addClickListener(clickEvent -> {
            try{
                if(gestionnaire.authentification(this.username.getValue(), this.mdp.getValue())){
                    if(gestionnaire.autorisationAtelier(Atelier.getIdAtelier(listeAtelier.getValue(),gestionnaire.getConnection()))){
                        System.out.println("Authentification réussie !");
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
   

    } */
