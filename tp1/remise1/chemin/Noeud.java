/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package chemin;
import java.util.Map;
import java.util.TreeMap;
/**
 * Représente un noeud d'une carte.
 * @author Éric Beaudry
 */
public class Noeud implements Comparable<Noeud> {
    protected long   id;
    protected Coordonnees coor;
    protected Map<Noeud, Double> tronconsSortants = new TreeMap<Noeud, Double>();

    @Override
    public int hashCode() {
        int hash =7;
        hash = 127 * hash +(int)(coor.getLat());
        hash = 7 * hash +(int)(coor.getLon());
        hash = 7 * hash + (int)id;

        return hash;
    }

    public Noeud(long id, double latitude, double longitude){
        this.id = id;
        coor = new Coordonnees(latitude, longitude);
    }
    public Noeud(long id, Coordonnees c){
        this.id = id;
        coor = c;
    }    
    public double distanceDeg2(Noeud n2){
        double dlon = coor.getLon() - n2.coor.getLon();
        double dlat = coor.getLat() - n2.coor.getLat();
        return dlon*dlon + dlat*dlat;
    }

    @Override
    public int compareTo(Noeud o) {
        if(id<o.id) return -1;
        if(id>o.id) return +1;
        return 0;
    }
    
    @Override
    public String toString(){
        return "n" + id;
    }
}
