/*package com.example.application.views.main;

import classe_tables.Produit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import classe_tables.machine;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class VueMachine extends VerticalLayout  {

    private ListDataProvider<machine> dataProvider;
    private Binder<machine> binder;

    public VueMachine() throws SQLException{
        // Créer un DataProvider pour la liste de machines
        dataProvider = new ListDataProvider<>(traitement.Gestionnaire.getListMachineAtelier(DriverManager.getConnection("jdbc:mysql:"+"//92.222.25.165:3306/m3_rmbola_tembo01","m3_rmbola_tembo01","976e74f9")));


        // Créer la liste des machines
        Grid <machine> machineGrid = new Grid<>(machine.class);
        machineGrid.setDataProvider(dataProvider);
        configureGrid(machineGrid);
        add(machineGrid);

    }

    private void configureGrid(Grid<machine> machineGrid) {
        // Configurer les colonnes du grid
        machineGrid.setColumns("id", "nom");

        // Configurer le Binder pour la sélection
        binder = new Binder<>(machine.class);
        binder.bindInstanceFields(this);

        // Réagir à la sélection d'une machine dans le grid
        machineGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.setBean(event.getValue());
            }
        });
    }

    





}
*/

package com.example.application.views.main;

import classe_tables.machine;
import classe_tables.Atelier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import com.vaadin.flow.component.html.Div;

public class VueMachine extends Div{
    public VueMachine() {
        Grid<machine> grid = new Grid<>(machine.class, false);
        grid.addColumn(machine::getId).setHeader("Identifiant");
        grid.addColumn(machine::getRef).setHeader("Référence");

       // List<machine> ref = machine.listerMachine(3,con);
       // grid.setItems(ref);

        add(grid); 
    }

}

/* 
public class VueMachine extends VerticalLayout {

    private ListDataProvider<machine> dataProvider;
    private Grid<machine> machineGrid;
    private TextField name;

    public VueMachine() throws SQLException {
       // Récupérer la connexion à la base de données
         try (Connection connection = DriverManager.getConnection("jdbc:mysql://92.222.25.165:3306/m3_rmbola_tembo01", "m3_rmbola_tembo01", "976e74f9")) {
            // Créer un DataProvider pour la liste de machines
            List<machine> machines = traitement.Gestionnaire.getListMachineAtelier(connection);
            dataProvider = new ListDataProvider<>(machines);
            setSizeFull();
            // Créer la liste des machines
            machineGrid = new Grid<>(machine.class);
            machineGrid.setDataProvider(dataProvider);
            machineGrid.setColumns("ID", "Nom");

        // Personnaliser les en-têtes des colonnes
        machineGrid.getColumnByKey("ID").setHeader("ID de la Machine");
        machineGrid.getColumnByKey("Nom").setHeader("Nom de la Machine");


        // Réagir à la sélection d'une machine dans le grid
        machineGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                // Vous pouvez réagir à la sélection ici si nécessaire
            }
        });
           
        } 

        name = new TextField("Your name");
        setHorizontalComponentAlignment(Alignment.CENTER, name);
        add(name,machineGrid);
    }
    
    
    private void configureGrid(Grid<machine> machineGrid) {
        // Configurer les colonnes du grid
        machineGrid.setColumns("ID", "Nom");

        // Personnaliser les en-têtes des colonnes
        machineGrid.getColumnByKey("ID").setHeader("ID de la Machine");
        machineGrid.getColumnByKey("Nom").setHeader("Nom de la Machine");


        // Réagir à la sélection d'une machine dans le grid
        machineGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                // Vous pouvez réagir à la sélection ici si nécessaire
            }
        });
    }
    
}

*/