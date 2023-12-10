package classe_tables;

import java.util.ArrayList;

public class Gamme {
    private int id;
    private String ref;
    private ArrayList<OperationElementaire> listeOperation;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
    public ArrayList<OperationElementaire> getListeOperation() {
        return listeOperation;
    }
    public void setListeOperation(ArrayList<OperationElementaire> listeOperation) {
        this.listeOperation = listeOperation;
    }
    
}
