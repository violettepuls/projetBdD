package com.example.application.views.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("app-layout-navbar-placement")
public class AppLayoutNavbarPlacement extends AppLayout {
    private final Tab details;
	private final Tab payment;
	private final Tab shipping;
	private final VerticalLayout content;


    public AppLayoutNavbarPlacement() {

        details = new Tab("Details");
		payment = new Tab("Payment");
		shipping = new Tab("Shipping");

		Tabs tabs = new Tabs(details, payment, shipping);
		tabs.addSelectedChangeListener(event ->
			setContent(event.getSelectedTab())
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

        addToDrawer(tabs,content);
        addToNavbar(toggle, title);

    }

    private void setContent(Tab tab) {
		content.removeAll();

		if (tab.equals(details)) {
			content.add(new Paragraph("This is the Details tab"));
		} else if (tab.equals(payment)) {
			content.add(new Paragraph("This is the Payment tab"));
		} else {
			content.add(new Paragraph("This is the Shipping tab"));
		}
	}

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.CART, "Production"),
                createTab(VaadinIcon.CALENDAR, "Emploi du temps"),
                createTab(VaadinIcon.PACKAGE, "Produits"),
                createTab(VaadinIcon.USERS, "Opérateur"),
                createTab(VaadinIcon.WRENCH, "Machines"),
                createTab(VaadinIcon.LIST, "Gestion Atelier"));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName) {
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

        return new Tab(link);
    }

}
