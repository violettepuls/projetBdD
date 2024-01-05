package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
    private HorizontalLayout boxAtelier;
    private Image imagelogin;
    private Image logoinsa;
    private HorizontalLayout layout2;
    private VerticalLayout layout;
    
    public VueAuthentification() throws ClassNotFoundException,SQLException{
        this.gestionnaire=new Gestionnaire();
        this.messageErreur = new Notification();
        this.listeAtelier = new ComboBox<String>("Selectionner Atelier");
      //  this.listeAtelier.setPlaceholder("Atelier");
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
        this.boxAtelier = new HorizontalLayout();
        boxAtelier.add(this.listeAtelier,this.creerAtelier);
        boxAtelier.setAlignItems(FlexComponent.Alignment.CENTER);

        this.imagelogin = new Image("https://cdn.icon-icons.com/icons2/1769/PNG/512/4092564-about-mobile-ui-profile-ui-user-website_114033.png","");
        imagelogin.setHeight("8em");

        this.logoinsa = new Image("https://www.eduopinions.com/wp-content/uploads/2018/02/logo-coul-e1472661333433.jpg","");
        logoinsa.setHeight("5em");
        logoinsa.getStyle().set("position","fixed").set("bottom","3em").set("right","3em");


     //   creerAtelier.getStyle().set("position","fixed").set("top","25em").set("right","15em");


        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
       // layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setSizeFull();

        layout.add(this.imagelogin,this.username, this.mdp,this.listeAtelier, this.validerAuthentification);

        HorizontalLayout layout2 = new HorizontalLayout();
        layout2.setSizeFull();
        layout2.add(layout,this.creerAtelier);

        add(layout2,this.logoinsa);
    
    }
     public class Spacer extends Div {
        public Spacer() {
            setHeight("10em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
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
                        MainView mv = new MainView(gestionnaire,this);
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

    public void recharger(){
        try{
            this.gestionnaire=new Gestionnaire();
            this.removeAll();
            remplirListeAtelier();
            add(layout2,this.logoinsa);
        }
        catch(Exception e){
            System.out.println("Erreur chargement VA : "+e);
        }
    }
}
