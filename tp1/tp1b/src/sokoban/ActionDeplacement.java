/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package sokoban;

import astar.Action;

/**
 * Représente un déplacement («coup») dans le jeux.
 * 
 */
public class ActionDeplacement extends Action {
    
    // À compléter ...
    
    public ActionDeplacement(String nom){
        super(1);
        this.nom = nom;
    }
    
    /** Nom de l'action : N,S,W,E pour Nord, Sud, W(Ouest), Est. */
    protected String nom;
    
    public String toString(){
        return nom;
    }
}
