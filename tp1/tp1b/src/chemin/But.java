/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package chemin;

/**
 * Représente un but dans un problème de planification de chemin.
 * @author Éric Beaudry
 */
public class But implements astar.But, astar.Heuristique{
    
    public But(Noeud noeudBut){
        this.noeudBut = noeudBut;
    }
    
    /** But = destination finale. */
    protected Noeud noeudBut;

    @Override
    public boolean butSatisfait(astar.Etat e) {
        Etat etat = (Etat) e;
        return etat.emplacement == noeudBut;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {
        Etat etat = (Etat) e;
        return etat.emplacement.coor.distanceTerre(noeudBut.coor);
    }
}
