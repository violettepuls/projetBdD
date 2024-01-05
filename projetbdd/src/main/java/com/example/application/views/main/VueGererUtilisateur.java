package com.example.application.views.main;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;

import classe_tables.Utilisateur;
import traitement.Gestionnaire;

public class VueGererUtilisateur extends VerticalLayout{
    private Gestionnaire gestionnaire;
    private Grid<Utilisateur> tableau;
    private Button ajouterExistant;
    private Button creerNouveau;
    private HorizontalLayout boutons;
    private TextField searchField;
    private GridListDataView<Utilisateur> dataView;

    public VueGererUtilisateur(Gestionnaire g)throws SQLException{
        //Déclaration
        this.gestionnaire=g;
        this.ajouterExistant=new Button("Ajouter utilisateur existant");
        this.creerNouveau = new Button("Créer un nouvel utilisateur");
        this.searchField = new TextField();
        this.boutons = new HorizontalLayout(ajouterExistant,creerNouveau);
        this.tableau = new Grid<Utilisateur>();
        this.add(searchField,tableau,boutons);

        //Pré-remplissage
        tableau.addColumn(creerVignetteUtilisateur()).setHeader("Utilisateur");
        tableau.addColumn(Utilisateur::getOperateur).setHeader("Opérateur");
        tableau.addColumn(createStatusComponentRenderer()).setHeader("Disponibilité");
        tableau.addColumn(new NativeButtonRenderer<>("Modifier admin",clickedItem->{
            modifier(clickedItem);
        }));
        tableau.addColumn(new NativeButtonRenderer<>("Supprimer",clickedItem->{
            supprimer(clickedItem);
        }));

        //autrement (pour le bouton)
        /*

        */

        dataView = tableau.setItems(Utilisateur.listerUtilisateur(gestionnaire.getCurAtelier(), gestionnaire.getConnection()));
        searchField.setPlaceholder("Rechercher");
        dataView.addFilter(user -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(user.getNomComplet(),
                    searchTerm);
            boolean matchesRole = matchesTerm(user.getRole(), searchTerm);
            boolean matchesOperateur = matchesTerm(user.getOperateur(),
                    searchTerm);

            return matchesFullName || matchesRole || matchesOperateur;
        });

        //Attribution des fonctions
        this.ajouterExistant.addClickListener(clickevent->{
            ajouterExistant();
        });
        this.creerNouveau.addClickListener(clickevent->{
            creer();
        });

        //Esthétique
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    }

    public void actualiser()throws SQLException{
        dataView = tableau.setItems(Utilisateur.listerUtilisateur(gestionnaire.getCurAtelier(), gestionnaire.getConnection()));
    }

    public void modifier(Utilisateur user){
        try{
            Utilisateur.modifierStatutAdmin(user.getId(), !user.isAdmin(), gestionnaire.getConnection());
            actualiser();
        }
        catch(SQLException e){
            System.out.println("Erreur modif admin : "+e);
        }
    }

    public void supprimer(Utilisateur user){
        try {
            Utilisateur.supprimerUtilisateur(user.getId(), gestionnaire.getCurAtelier(), gestionnaire.getConnection());
            actualiser();
        } catch (SQLException e) {
            System.out.println("Erreur suppression : "+e);
        }
    }

    public void ajouterExistant(){
        try{
            this.removeAll();
            Button terminer = new Button("Terminer");
            terminer.addClickListener(clickevent->{
                try {
                    recharger();
                } catch (SQLException e) {
                    System.out.println("Erreur chargement : "+e);
                }
            });
            Grid<Utilisateur> listeUtilisateur = new Grid<Utilisateur>();
            listeUtilisateur.addColumn(creerVignetteUtilisateur()).setHeader("Utilisateur");
            listeUtilisateur.addColumn(Utilisateur::getOperateur).setHeader("Opérateur");
            listeUtilisateur.addColumn(createStatusComponentRenderer()).setHeader("Disponibilité");
            tableau.addColumn(new NativeButtonRenderer<>("Ajouter à l'atelier",clickedItem->{
                try {
                    Utilisateur.associerAtelierUtilisateur(clickedItem.getId(), gestionnaire.getCurAtelier().getId(), gestionnaire.getConnection());
                } catch (SQLException e) {
                    System.out.println("Erreur ajout : "+e);
                }
            }));
            dataView = listeUtilisateur.setItems(Utilisateur.listerUtilisateurHorsAtelier(gestionnaire.getCurAtelier(), gestionnaire.getConnection()));
        }
        catch(SQLException e){
            System.out.println("Erreur ajout existant : "+e);
        }
    }

    public void creer(){
        this.removeAll();
        this.add(new ParametreUtilisateur(this));
    }

    private static Renderer<Utilisateur> creerVignetteUtilisateur() {
        return LitRenderer.<Utilisateur> of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>"
                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                        + "    <span> ${item.fullName} </span>"
                        + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                        + "      ${item.email}" + "    </span>"
                        + "  </vaadin-vertical-layout>"
                        + "</vaadin-horizontal-layout>")
                .withProperty("fullName", Utilisateur::getNomComplet)
                .withProperty("email", Utilisateur::getRole);
    }

    private static final SerializableBiConsumer<Span, Utilisateur> statusComponentUpdater = (
        span, person) -> {
        boolean isAvailable = "Disponible".equals(person.getEtat());
        String theme = String.format("badge %s",
            isAvailable ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(person.getEtat());
    };

    private static ComponentRenderer<Span, Utilisateur> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }

    public void recharger()throws SQLException{
        this.add(tableau,boutons);
        this.actualiser();
    }
}
