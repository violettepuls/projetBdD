package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H2;
import classe_tables.Produit;
import traitement.Gestionnaire;

public class VuePanier extends VerticalLayout{
    private VueProduction vp;
    private H2 titre;
    private Button lancer;
    private Button retour;
    private Gestionnaire gestionnaire;
    private Scroller corps;
    private VerticalLayout listeProduit;
    private HorizontalLayout boutons;

    public VuePanier(VueProduction v) throws SQLException{
        //déclaration des éléments
        this.vp=v;
        this.gestionnaire=v.getGestionnaire();
        this.titre = new H2("Panier");
        this.lancer = new Button("Lancer la production");
        this.retour = new Button("Retour");
        this.corps = new Scroller();
        this.listeProduit = new VerticalLayout();
        this.boutons=new HorizontalLayout(lancer,retour);
        

        //Pré-remplissage
      
        formater();
        this.corps.setContent(listeProduit);

        //Attribution des fonctions
        this.lancer.addClickListener(clickevent->{
            try{
                this.vp.recharger();
                System.out.println(gestionnaire.getPanier());
                gestionnaire.repartitionMachine();
                this.gestionnaire.reinitialiserPanier();
            }
            catch(SQLException e){
                System.out.println("Erreur afficher panier : "+e);
            }
        });
        this.retour.addClickListener(clickevent->{
            try{
                this.vp.recharger();
                System.out.println(gestionnaire.getPanier());
            }
            catch(SQLException e){
                System.out.println("Erreur afficher panier : "+e);
            }
        });

        //Esthétique
        this.corps.setSizeFull();
        this.setSizeFull(); // Définir la hauteur de la vue à 100%
        this.corps.setSizeFull();
        this.corps.getStyle().set("max-height", "100%");
        this.corps.getStyle().set("max-width", "100%");
        this.corps.setWidth("75em");
        this.corps.setHeight("30em");
        titre.getElement().getStyle().set("margin", "auto");

        this.add(titre,corps,boutons);
    }
    
    public void formater() throws SQLException{
        this.listeProduit.removeAll();
        ArrayList<Produit> liste = gestionnaire.getPanier();
        ArrayList<Produit> listeDistinct=new ArrayList<Produit>();
        for (int i=0;i<liste.size();i++){
            if (!listeDistinct.contains(liste.get(i))){
                listeDistinct.add(liste.get(i));
                this.listeProduit.add(new GroupePanier(this, liste.get(i)));
            }
            if (i < liste.size() - 1) {
                this.listeProduit.add(new Spacer());
            }
        }
    }
    public class Spacer extends Div {
        public Spacer() {
            setHeight("4em"); // Vous pouvez ajuster la hauteur selon vos besoins
        }
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }

    public void recharger()throws SQLException{
        this.removeAll();
        formater();
        this.add(titre,corps,boutons);
    }
}
