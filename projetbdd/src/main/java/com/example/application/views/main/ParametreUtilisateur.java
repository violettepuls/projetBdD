package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.Atelier;
import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class ParametreUtilisateur extends HorizontalLayout{
    private VueGererUtilisateur vgu;
    private MainView mv;
    private Gestionnaire gestionnaire;
    private Utilisateur utilisateur;
    private TextField nom;
    private TextField prenom;
    private TextField username;
    private TextField mdp;
    private Checkbox admin;
    private Checkbox operateur;
    private Button creer;
    private Button valider;
    private Button annuler;
    private Button supprimer;
    private VerticalLayout infosPerson;
    private VerticalLayout infosConnec;
    private VerticalLayout checkBox;
    private VerticalLayout boutons;

    public ParametreUtilisateur(VueGererUtilisateur v){
        //Déclaration
        this.vgu=v;
        this.gestionnaire=v.getGestionnaire();
        this.nom=new TextField("Nom");
        this.prenom=new TextField("Prénom");
        this.username=new TextField("Nom d'utilisateur");
        this.mdp=new TextField("Mot de passe");
        this.admin=new Checkbox("Admin", false);
        this.operateur=new Checkbox("Opérateur", false);
        this.creer=new Button("Créer");
        this.annuler=new Button("Annuler");
        this.infosPerson=new VerticalLayout(nom,prenom);
        this.infosConnec=new VerticalLayout(username,mdp);
        this.checkBox=new VerticalLayout(admin,operateur);
        this.boutons=new VerticalLayout(creer,annuler);
        this.add(infosPerson,infosConnec,checkBox,boutons);

        //Attribution des fonctions
        creer.addClickListener(clickevent->{
            creer();
        });
        annuler.addClickListener(clickevent->{
            annuler();
        });

        //Esthétique
    }

    public ParametreUtilisateur(MainView v,Utilisateur u){
        //Déclaration
        this.mv=v;
        this.gestionnaire=v.getGestionnaire();
        this.utilisateur=u;
        this.nom=new TextField("Nom");
        this.prenom=new TextField("Prénom");
        this.username=new TextField("Nom d'utilisateur");
        this.mdp=new TextField("Mot de passe");
        this.admin=new Checkbox("Admin", false);
        this.operateur=new Checkbox("Opérateur", false);
        this.valider=new Button("Valider");
        this.annuler=new Button("Annuler");
        this.supprimer=new Button("Supprimer mon compte");
        this.infosPerson=new VerticalLayout(nom,prenom);
        this.infosConnec=new VerticalLayout(username,mdp);
        this.checkBox=new VerticalLayout(admin,operateur);
        this.boutons=new VerticalLayout(valider,annuler,supprimer);
        this.add(infosPerson,infosConnec,checkBox,boutons);

        //Pré-remplissage
        nom.setValue(utilisateur.getNom());
        prenom.setValue(utilisateur.getPrenom());
        username.setValue(utilisateur.getNom_utilisateur());
        mdp.setValue(utilisateur.getMdp());
        admin.setValue(utilisateur.isAdmin());
        if(!this.utilisateur.isAdmin()){
            admin.setReadOnly(true);
        }
        operateur.setValue(utilisateur.isOperateur());

        //Attribution des fonctions
        valider.addClickListener(clickevent->{
            valider();
        });
        annuler.addClickListener(clickevent->{
            annuler2();
        });
        supprimer.addClickListener(clickevent->{
            supprimer();
        });

        //Esthétique
    }

    public void creer(){
        try{
            String role="";
            if(admin.getValue()){
                role="Admin";
            }
            else{
                role="Utilisateur";
            }
            Utilisateur.creerUtilisateur(prenom.getValue(), nom.getValue(), username.getValue(), role, mdp.getValue(), operateur.getValue(), null, gestionnaire.getCurAtelier(), gestionnaire.getConnection());
            this.vgu.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur creation utilisateur : "+e);
        }
    }
    
    public void valider(){
        try{
            String role="";
            if(admin.getValue()){
                role="Admin";
            }
            else{
                role="Utilisateur";
            }
            Utilisateur.modifierUtilisateur(this.utilisateur.getId(),prenom.getValue(), nom.getValue(), username.getValue(), role, mdp.getValue(), operateur.getValue(), null, gestionnaire.getConnection());
            this.mv.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur creation utilisateur : "+e);
        }
    }

    public void annuler(){
        try{
            this.vgu.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur annulation : "+e);
        }
    }

    public void annuler2(){
        try{
            this.mv.recharger();
        }
        catch(SQLException e){
            System.out.println("Erreur annulation : "+e);
        }
    }

    public void supprimer(){
        try{
            Utilisateur.supprimerUtilisateurGlobal(this.utilisateur.getId(), gestionnaire.getConnection());
            if(Utilisateur.listerUtilisateur(this.gestionnaire.getCurAtelier(), gestionnaire.getConnection()).size()==0){
                Atelier.supprimer(gestionnaire.getCurAtelier().getId(), gestionnaire.getConnection());
            }
            this.mv.deconnexion();
        }
        catch(SQLException e){
            System.out.println("Erreur annulation : "+e);
        }
    }
}
