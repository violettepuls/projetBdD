package classe_tables;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String nom_utilisateur;
    private String role;

    public Utilisateur(String nom, String prenom, String usr, String role){
        this.nom=nom;
        this.prenom=prenom;
        this.nom_utilisateur=usr;
        this.role=role;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
