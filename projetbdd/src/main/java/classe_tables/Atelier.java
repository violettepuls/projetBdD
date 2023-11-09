package classe_tables;

public class Atelier {
    private String nom;
    private String bdd; // //92.222.25.165:3306/m3_rmbola_tembo01
    private String nom_utilisateur; // m3_rmbola_tembo01
    private String mdp; // 976e74f9

    public Atelier(String n, String b, String n_u, String m){
        this.nom=n;
        this.bdd=b;
        this.nom_utilisateur=n_u;
        this.mdp=m;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBdd() {
        return bdd;
    }

    public void setBdd(String bdd) {
        this.bdd = bdd;
    }
    
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMdp() {
        return mdp;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }
    
}
