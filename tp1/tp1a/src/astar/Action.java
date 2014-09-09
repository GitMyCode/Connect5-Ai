/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

/**
 * Classe abstraite représentant une action.
 * Une action peut être exécutée dans un état du monde (voir méthode Monde::executer).
 */
public class Action {
    
    public Action(double cout){
        this.cout = cout;
    }
    protected double cout;
    
}
