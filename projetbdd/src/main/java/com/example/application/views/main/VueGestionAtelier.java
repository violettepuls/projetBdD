package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import traitement.Gestionnaire;

public class VueGestionAtelier extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private VerticalLayout content;
    private Tabs tabs;
    private Tab gererAtelier;
    private Tab gererGroupe;
    private Tab gererUtilisateur;

    public VueGestionAtelier (Gestionnaire g){
        //Déclaration
        this.gestionnaire=g;
        this.content=new VerticalLayout();
        this.gererAtelier = new Tab("Gérer l'atelier");
        this.gererGroupe = new Tab("Gérer le groupe");
        this.gererUtilisateur = new Tab("Gérer les utilisateurs");
        this.tabs = new Tabs(gererAtelier,gererGroupe,gererUtilisateur);
        this.add(tabs,content);
        this.content.add(new VueGererAtelier(gestionnaire));

        //Attribution des fonctions
        this.tabs.addSelectedChangeListener(event ->{
            try{
                setContent(tabs.getSelectedTab());
            }
            catch(SQLException e){
                System.out.println("Erreur tabs : " + e);
            }
        });

        //Esthétique
        setSizeFull();
        this.content.setWidth("70em");
        this.content.setHeight("30em");

    }

    public void setContent(Tab tab) throws SQLException{
		this.content.removeAll();
		if (tab.equals(gererAtelier)) {
			content.add(new VueGererAtelier(gestionnaire));
		} else if (tab.equals(gererGroupe)) {
			content.add(new VueGererGroupe(gestionnaire));
		} else if(tab.equals(gererUtilisateur)){
			content.add(new VueGererUtilisateur(gestionnaire));
		}
	}
}
