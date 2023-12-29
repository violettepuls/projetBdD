package com.example.application.views.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import traitement.Gestionnaire;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

@PageTitle("Main")
@Route(value = "accueil")
public class MainView extends VerticalLayout {

    //private TextField name;
    //private Button sayHello;
    private final Tab production;
	private final Tab edt;
	private final Tab produits;
    private final Tab operateurs;
    private final Tab machines;
    private final Tab atelier;
	private final VerticalLayout content;
    private Gestionnaire gestionnaire;
    

    public MainView(Gestionnaire g) throws SQLException{
        this.gestionnaire=g;
        //name = new TextField("Your name");
        //sayHello = new Button("Say hello");
        //sayHello.addClickListener(e -> {
        //    Notification.show("Hello " + name.getValue());
        //});
        //sayHello.addClickShortcut(Key.ENTER);

        //setMargin(true);
        //setHorizontalComponentAlignment(Alignment.CENTER, name, sayHello);

        //add(name, sayHello);
        //TEST VAADIN BOUTON+CALENDRIER
        AppLayoutNavbarPlacement navBar=new AppLayoutNavbarPlacement();

       /*  private Tab createTab(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
            
            RouterLink link = new RouterLink();
            link.add(icon, new Span(viewName));
            // Demo has no routes
            // link.setRoute(viewClass.java);
            link.setTabIndex(-1);
            return new Tab(link)
        } */

        production = new Tab("Production");
		edt = new Tab("Emploi du temps");
        produits = new Tab("Produits");
		operateurs = new Tab ("Opérateurs");
        machines = new Tab ("Machines");
        atelier = new Tab ("Gestion d'atelier");

        //Tab production = tabs.add("Help", () -> { ... });
        production.addComponentAsFirst(VaadinIcon.CART.create());
        edt.addComponentAsFirst(VaadinIcon.CALENDAR.create());
        produits.addComponentAsFirst(VaadinIcon.PACKAGE.create());
        operateurs.addComponentAsFirst(VaadinIcon.USERS.create());
        machines.addComponentAsFirst(VaadinIcon.WRENCH.create());
        atelier.addComponentAsFirst(VaadinIcon.LIST.create());

		Tabs tabs = new Tabs(production, edt, produits, operateurs, machines, atelier);
		tabs.addSelectedChangeListener(event ->
			{
                try {
                    setContent(event.getSelectedTab());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    System.out.println("Erreur : "+e);
                }
            }
		);

		content = new VerticalLayout();
		content.setSpacing(false);
		setContent(tabs.getSelectedTab());

		//add(tabs, content);

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Atelier de fabrication");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        //Tabs tabs = getTabs();

       // addToDrawer(tabs,content);
    
        
        setHorizontalComponentAlignment(Alignment.CENTER,tabs);
       // setVerticalComponentAlignment(Alignment.START,tabs);
        add(tabs,content);

    }

    private void setContent(Tab tab) throws SQLException{
		content.removeAll();

		if (tab.equals(production)) {
			content.add(new Paragraph("This is the Details tab"));
		} else if (tab.equals(edt)) {
			content.add(new Paragraph("This is the Payment tab"));
		} else if(tab.equals(produits)){
			content.add(new Paragraph("This is the Shipping tab"));
		} else if(tab.equals(operateurs)){
            content.add(new Paragraph("opérateurs"));
        
        } else if(tab.equals(machines)){
            content.add(new VueMachine(gestionnaire));
        } else if(tab.equals(atelier)){
            content.add(new Paragraph("gestion atelier"));
        }

	}



    
}

