package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;
import classe_tables.machine;
import traitement.Gestionnaire;

public class GroupeOperation extends HorizontalLayout{
    private OperationElementaire operation;
    private Gestionnaire gestionnaire;
    private TextField type;
    private TextArea machine;
    private VerticalLayout infos;

    public GroupeOperation(Gestionnaire g,OperationElementaire op)throws SQLException{
        //Déclaration
        this.gestionnaire=g;
        this.operation=op;
        this.type=new TextField();
        this.machine = new TextArea();
        this.infos = new VerticalLayout(type,machine);
        this.add(infos);

        //Pré-remplissage
        this.type.setValue(this.operation.getType());
        updateMachine();

        //Esthétique
        
    }

    public void updateMachine()throws SQLException{
        ArrayList<machine> listeMachine = OperationElementaire.listerMachineOperationElementaire(this.operation.getId(), this.gestionnaire.getCurAtelier().getId(), this.gestionnaire.getConnection());
        String contenu = "Machines capables :";
        for (int i=0;i<listeMachine.size();i++){
            contenu=contenu+"\n"+listeMachine.get(i).getNom()+" ["+listeMachine.get(i).getRef()+"]";
        }
        this.machine.setValue(contenu);
    }
}
