package com.example.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import classe_tables.OperationElementaire;

public class HBoxOperation2 extends HorizontalLayout{
    private int index;
    private OperationElementaire operation;
    private ParametreGamme pg;
    private TextField uniteOperation;
    private TextField ordre;
    private TextField type;
    private Button up;
    private Button down;
    private Button delete;

    public HBoxOperation2(OperationElementaire op,ParametreGamme pP, int id){
        //Déclaration
        this.index=id;
        this.operation = op;
        this.pg = pP;
        this.ordre = new TextField();
        this.type = new TextField();
        this.uniteOperation = new TextField();
        this.up=new Button(new Icon(VaadinIcon.ARROW_UP));
        this.down = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        this.delete = new Button(new Icon(VaadinIcon.TRASH));
        this.add(up,down,ordre,type,uniteOperation,delete);

        //Remplissage du texte
        this.ordre.setValue(String.valueOf(this.index));
        this.type.setValue(this.operation.getType());
        this.uniteOperation.setValue(String.valueOf(this.operation.getUniteOperation()));

        //Mise en fonction des boutons
        if(this.index==0){
            this.up.setEnabled(false);
        }
        if (this.index==(this.pg.getLastIndex())){
            this.down.setEnabled(false);
        }
        this.up.addClickListener(clickevent -> {
            monter2();
        });
        this.down.addClickListener(clickevent ->{
            descendre2();
        });
        this.delete.addClickListener(clickevent->{
            supprimer2();
        });

        //Esthétique
        this.delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        this.ordre.setReadOnly(true);
        this.type.setReadOnly(true);
        this.uniteOperation.setReadOnly(true);

    }

    public void monter2(){
        this.pg.getChampOperation().remove(this);
        this.pg.getChampOperation().addComponentAtIndex(this.index-1, this);
        this.pg.refreshOrdreOperation();
    }

    public void descendre2(){
        this.pg.getChampOperation().remove(this);
        this.pg.getChampOperation().addComponentAtIndex(this.index+1, this);
        this.pg.refreshOrdreOperation();
    }

    public void supprimer2(){
        this.pg.getChampOperation().remove(this);
        this.pg.refreshOrdreOperation();
    }

    public OperationElementaire getOperation(){
        return this.operation;
    }

    public void setIndex(int i){
        this.index = i;
        this.ordre.setValue(String.valueOf(this.index));
        if(this.index==0){
            this.up.setEnabled(false);
        }
        else{
            this.up.setEnabled(true);
        }
        if (this.index==(this.pg.getLastIndex())){
            this.down.setEnabled(false);
        }
        else{
            this.down.setEnabled(true);
        }
    }

    public int getIndex(){
        return this.index;
    }
}
