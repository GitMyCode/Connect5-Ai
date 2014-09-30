/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package chemin;

/**
 *
 * @author Éric Beaudry
 */
public class Etat extends astar.Etat{

    public Etat(Noeud emplacement){
        this.emplacement = emplacement;
    }
    
    protected Noeud emplacement;
    
    @Override
    public int compareTo(astar.Etat o) {
        Etat e = (Etat) o;
        return emplacement.compareTo(e.emplacement);
    }

    @Override
    public boolean equals(Object obj) {
        Etat e = (Etat) obj;
        return emplacement.id == e.emplacement.id;
    }

    @Override
    public int hashCode() {

        return emplacement.hashCode();
    }
}
