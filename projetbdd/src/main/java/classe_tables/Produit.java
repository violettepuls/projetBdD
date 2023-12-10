package classe_tables;

public class Produit {
    private int id;
    private String nom;
    private String reference;
    private Gamme gamme;
    public int getId() {
        return id;
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
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public Gamme getGamme() {
        return gamme;
    }
    public void setGamme(Gamme gamme) {
        this.gamme = gamme;
    }
}
