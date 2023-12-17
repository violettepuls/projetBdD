package com.example.application.views.main;

import classe_tables.Produit;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;

public class VueProduction extends VerticalLayout  {

    public VueProduction(){
        Grid<Produit> grid = new Grid<>();
        addClassName("listproduction");
        setSizeFull();
        configureGrid(grid);
    }

    private void configureGrid(Grid grid){
        grid.setSizeFull();
        grid.setColumns("Photo","Nomination");
        

    }
    
}
