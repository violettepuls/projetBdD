package com.example.application.views.main;

import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

    private TextField name;
    private Button sayHello;

    public MainView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setHorizontalComponentAlignment(Alignment.CENTER, name, sayHello);

        //add(name, sayHello);
        //TEST VAADIN BOUTON+CALENDRIER
        AppLayoutNavbarPlacement navBar=new AppLayoutNavbarPlacement();

        add(navBar,name, sayHello);

    }



}

