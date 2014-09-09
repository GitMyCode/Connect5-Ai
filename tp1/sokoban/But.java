/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

/**
 * Représente un but.
 */
public class But implements astar.But, astar.Heuristique {

    // À compléter.
    // Indice : les destinations des blocs.
    
    @Override
    public boolean butSatisfait(astar.Etat e) {
        return false;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {
        return 0;
    }
    
}
