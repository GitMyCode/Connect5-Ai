/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

/**
 *
 * Interface générique d'une heuristique.
 */
public interface Heuristique {
    
    /** Retourne une estimation du coût restant pour atteindre le but b à partir de l'état du monde e.
     *  Pour être admissible, cette fonction ne doit jamais surestimer le coût restant.
     */
    public double estimerCoutRestant(Etat e, But b);
    
}
