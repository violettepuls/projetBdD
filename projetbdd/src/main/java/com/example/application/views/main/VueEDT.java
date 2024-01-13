package com.example.application.views.main;

import org.vaadin.stefan.fullcalendar.FullCalendar;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import traitement.Gestionnaire;

public class VueEDT extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField titre;
    private FullCalendar calendrier;
    private TextField titreFiltre;
    private MultiSelectComboBox<String> filtreLarge;
    private MultiSelectComboBox<String> filtreFin;
    private HorizontalLayout filtre;

    public VueEDT(Gestionnaire g){
        //Déclaration
        this.gestionnaire=g;
        this.titre = new TextField();
        this.calendrier = new FullCalendar();
        this.titreFiltre = new TextField();
        this.filtreLarge = new MultiSelectComboBox<String>();
        this.filtreFin = new MultiSelectComboBox<String>();
        this.filtre = new HorizontalLayout(titreFiltre, filtreLarge, filtreFin);
        this.add(titre,filtre,calendrier);

        //Pré-Remplissage
        titre.setValue("Emploi du temps");
        filtreLarge.setItems("Tous","Operateur","Machine");
        filtreLarge.setValue("Tous");

        //Mise en fonction
        filtreLarge.addValueChangeListener(event->{
            if(filtreLarge.getValue().equals("Tous")){
                filtreFin.setEnabled(false);
                //ligne qui remove tous les items affichée
                //ligne qui récupère tous les items et les affiches
            }
            else{
                filtreFin.setEnabled(true);
                parametrerFiltreFin();
                //ligne qui remove tous les items affichés
                filtreFin.setValue("Tous");
                // A TESTER (car peut etre que forcer une valeur trigger le valueChangeListener) : ligne qui récupère tous les items du type considéré et les affiche
        });
        filtreFin.addValueChangeListener(event->{
            //ligne qui remove tous les items affichés
            //ligne qui récupère tous les items selectionnés et les affiche
        });

        //Esthétique
        filtreFin.setEnabled(false); // au début, le filtre large affiche tous, donc on ne doit pas être en mesure de forcer la sélection d'objets précis
    }

    public void parametrerFiltreFin(){
        try {
            
        } catch (Exception e) {
            System.out.println("Erreur filtre fin : "+e);
        }
    }
}
