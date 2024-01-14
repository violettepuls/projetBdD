package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.EDT;
import classe_tables.Utilisateur;
import classe_tables.machine;
import traitement.Gestionnaire;

public class VueEDT extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private TextField titre;
    private FullCalendar calendrier;
    private TextField titreFiltre;
    private ComboBox<String> filtreLarge;
    private MultiSelectComboBox<machine> filtreFinMachine;
    private MultiSelectComboBox<Utilisateur> filtreFinOperateur;
    private HorizontalLayout filtre;
    private machine toutMachine;
    private Utilisateur toutOperateur;
    private ArrayList<machine> listeMachine;
    private ArrayList<Utilisateur> listeOperateur;
    private ArrayList<Entry> listeEntries;

    public VueEDT(Gestionnaire g)throws SQLException{
        //Déclaration
        this.gestionnaire=g;
        this.titre = new TextField();
        this.calendrier = FullCalendarBuilder.create().build();
        this.titreFiltre = new TextField();
        this.filtreLarge = new ComboBox<String>();
        this.filtreFinMachine = new MultiSelectComboBox<machine>();
        this.filtreFinOperateur = new MultiSelectComboBox<Utilisateur>();
        this.filtre = new HorizontalLayout(titreFiltre, filtreLarge);
        this.add(titre,filtre,calendrier);

        //Pré-Remplissage
        toutMachine = new machine(-1, "Tout", "", "", 0);
        toutOperateur = new Utilisateur(-1, "Tout", "", "", "", true, "");
        titre.setValue("Emploi du temps");
        titreFiltre.setValue("Filtrer la liste par : ");
        filtreLarge.setItems("Tous","Operateur","Machine");
        filtreLarge.setValue("Tous");
        listeEntries = EDT.listerIndisponibilite(gestionnaire.getConnection());
        calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
        listeMachine=machine.listerMachine(gestionnaire.getCurAtelier(), gestionnaire.getConnection());
        listeMachine.add(toutMachine);
        filtreFinMachine.setItems(listeMachine);
        filtreFinMachine.setItemLabelGenerator(machine::getNom);
        listeOperateur=Utilisateur.listerOperateur(gestionnaire.getCurAtelier(), gestionnaire.getConnection());
        listeOperateur.add(toutOperateur);
        filtreFinOperateur.setItems(listeOperateur);
        filtreFinOperateur.setItemLabelGenerator(Utilisateur::getNomComplet);
        filtreFinMachine.setPlaceholder("Choisir les machines à afficher");
        filtreFinOperateur.setPlaceholder("Choisir les opérateurs à afficher");

        //Mise en fonction
        filtreLarge.addValueChangeListener(event->{
            calendrier.getEntryProvider().asInMemory().removeAllEntries();
            calendrier.getEntryProvider().refreshAll();
            if(filtreLarge.getValue().equals("Tous")){
                try{
                    listeEntries = EDT.listerIndisponibilite(gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
                catch(SQLException e){
                    System.out.println("Erreur reception entree : "+e);
                }
            }
            else if(filtreLarge.getValue().equals("Machine")){
                this.filtre.add(filtreFinMachine);
                this.filtre.remove(filtreFinOperateur);
                try{
                    listeEntries = EDT.listerIndisponibiliteMachineAll(gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
                catch(SQLException e){
                    System.out.println("Erreur reception entree : "+e);
                }
            }
            else if(filtreLarge.getValue().equals("Operateur")){
                this.filtre.add(filtreFinOperateur);
                this.filtre.remove(filtreFinMachine);
                try{
                    listeEntries = EDT.listerIndisponibiliteOperateurAll(gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
                catch(SQLException e){
                    System.out.println("Erreur reception entree : "+e);
                }
            }
        });
        filtreFinMachine.addValueChangeListener(event->{
            try{
                calendrier.getEntryProvider().asInMemory().removeAllEntries();
                calendrier.getEntryProvider().refreshAll();
                if(filtreFinMachine.getValue().contains(toutMachine)){
                    listeEntries = EDT.listerIndisponibiliteMachineAll(gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
                else{
                    calendrier.getEntryProvider().asInMemory().removeEntries(listeEntries);
                    ArrayList<machine> listeItems = new ArrayList<machine>(filtreFinMachine.getSelectedItems());
                    ArrayList<Integer> listeId = new ArrayList<Integer>();
                    for (int i=0;i<listeItems.size();i++){
                        listeId.add(listeItems.get(i).getId());
                    }
                    listeEntries = EDT.listerIndisponibiliteMachineGroupe(listeId, gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
            }
            catch(SQLException e){
                System.out.println("Erreur reception entree : "+e);
            }
        });
        filtreFinOperateur.addValueChangeListener(event->{
            try{
                calendrier.getEntryProvider().asInMemory().removeAllEntries();
                calendrier.getEntryProvider().refreshAll();
                if(filtreFinOperateur.getValue().contains(toutOperateur)){
                    listeEntries = EDT.listerIndisponibiliteOperateurAll(gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
                else{
                    ArrayList<Utilisateur> listeItems = new ArrayList<Utilisateur>(filtreFinOperateur.getSelectedItems());
                    ArrayList<Integer> listeId = new ArrayList<Integer>();
                    for (int i=0;i<listeItems.size();i++){
                        listeId.add(listeItems.get(i).getId());
                    }
                    listeEntries = EDT.listerIndisponibiliteOperateurGroupe(listeId, gestionnaire.getConnection());
                    calendrier.getEntryProvider().asInMemory().addEntries(listeEntries);
                }
            }
            catch(SQLException e){
                System.out.println("Erreur reception entree : "+e);
            }
        });

        //Esthétique
        calendrier.setEditable(false);
        this.setHeightFull();
        this.setWidthFull();
        this.setFlexGrow(1, calendrier);
        calendrier.setHeight("400px");
        calendrier.setWidthFull();
    }
}
