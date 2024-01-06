package com.example.application.views.main;

import java.sql.SQLException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import traitement.Gestionnaire;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;

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
    private final Tabs tabs;
	private final VerticalLayout content;
    private Gestionnaire gestionnaire;
    private HorizontalLayout entete;
    private HorizontalLayout corps;
    private TextField connecte;
    private TextField currentUser;
    private Button parametre;
    private Button deconnecter;
    private VueAuthentification va;
    private Image logoatelier;
    

    public MainView(Gestionnaire g,VueAuthentification v) throws SQLException{
        

        //Le gestionnaire
        this.gestionnaire=g;
        this.va=v;

        //L'entête
        this.connecte=new TextField();
        this.currentUser = new TextField();
        this.parametre = new Button("Paramètres de compte");
        this.parametre.setIcon(new Icon(VaadinIcon.COG));
        this.deconnecter = new Button("se déconnecter");
        this.deconnecter.setIcon(new Icon(VaadinIcon.SIGN_OUT));
        this.entete=new HorizontalLayout(connecte,currentUser,parametre,deconnecter);

        this.connecte.setValue("Connecté(e) : ");
        this.currentUser.setValue(this.gestionnaire.getCurUser().getNomComplet());
        
        this.parametre.addClickListener(clickevent->{
            this.removeAll();
            TextField titre = new TextField();
            titre.setReadOnly(true);
            titre.setValue("Paramètres de compte");
            this.add(titre,new ParametreUtilisateur(this, gestionnaire.getCurUser()));
        });
        this.deconnecter.addClickListener(clickevent->{
            this.va.recharger();
        });

        this.connecte.setReadOnly(true);
        this.currentUser.setReadOnly(true);

        //Les onglets
        production = new Tab("Production");
		edt = new Tab("Emploi du temps");
        produits = new Tab("Produits");
		operateurs = new Tab ("Opérateurs");
        machines = new Tab ("Machines");
        atelier = new Tab ("Gestion d'atelier");

        production.addComponentAsFirst(VaadinIcon.CART.create());
        edt.addComponentAsFirst(VaadinIcon.CALENDAR.create());
        produits.addComponentAsFirst(VaadinIcon.PACKAGE.create());
        operateurs.addComponentAsFirst(VaadinIcon.USERS.create());
        machines.addComponentAsFirst(VaadinIcon.WRENCH.create());
        atelier.addComponentAsFirst(VaadinIcon.LIST.create());

		this.tabs = new Tabs(production, edt, produits, operateurs, machines, atelier);
		tabs.addSelectedChangeListener(event ->
			{
                try {
                    setContent(event.getSelectedTab());
                } catch (SQLException e) {
                    System.out.println("Erreur : "+e);
                }
            }
		);

		content = new VerticalLayout();
		setContent(tabs.getSelectedTab());

        H1 title = new H1("Atelier de fabrication");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        setHorizontalComponentAlignment(Alignment.CENTER,tabs);
        this.corps=new HorizontalLayout(tabs,content);

        this.logoatelier = new Image("https://th.bing.com/th/id/OIP.X_wQMpV7GQLbXuETP7cNUwHaHa?rs=1&pid=ImgDetMain","");
        logoatelier.setHeight("5em");
        logoatelier.getStyle().set("position","fixed").set("bottom","3em").set("left","3em");
        
        add(entete,corps,logoatelier);

        //Esthétique
        this.tabs.setOrientation(Tabs.Orientation.VERTICAL);
		content.setSpacing(false);
        setHorizontalComponentAlignment(Alignment.START,tabs);
        getStyle().set("overflow", "hidden");
        getElement().getStyle().set("overflow", "hidden");
        getElement().getStyle().set("position", "fixed");
        getElement().getStyle().set("top", "0");
        getElement().getStyle().set("left", "0");
        getElement().getStyle().set("width", "100%");
        getElement().getStyle().set("height", "100%");
        getElement().getStyle().set("overflow", "hidden");
        
    }

    private void setContent(Tab tab) throws SQLException{
		content.removeAll();

		if (tab.equals(production)) {
			content.add(new VueProduction(gestionnaire));
		} else if (tab.equals(edt)) {
			content.add(new Paragraph("This is the Payment tab"));
		} else if(tab.equals(produits)){
			content.add(new VueProduit(gestionnaire));
		} else if(tab.equals(operateurs)){
            content.add(new VueOperateur(gestionnaire));
        
        } else if(tab.equals(machines)){
            content.add(new VueMachine(gestionnaire));
        } else if(tab.equals(atelier)){
            content.add(new VueGestionAtelier(gestionnaire));
        }

	}

    public Gestionnaire getGestionnaire(){
        return this.gestionnaire;
    }

    public void recharger()throws SQLException{
        this.removeAll();
        this.gestionnaire.setCurUser(gestionnaire.getCurUser().getId());
        this.currentUser.setValue(this.gestionnaire.getCurUser().getNomComplet());
        this.add(entete,corps);
    }
}

