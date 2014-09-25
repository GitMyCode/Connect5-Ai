/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

/**
 * Classe abstraite représentant un but.
 * Un but peut être satisfait dans un état du monde.
 */
public interface But {
    /** Retourne vrai ssi l'état e satisfait ce but. */
    public boolean butSatisfait(Etat e);
}
