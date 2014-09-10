/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

/**
 * Heuristique par défaut ne donnant aucune information.
 */
public class HeuristiqueNonInformative implements Heuristique{

    @Override
    public double estimerCoutRestant(Etat e, But b) {
        return 0.0;
    }
    
    
}
