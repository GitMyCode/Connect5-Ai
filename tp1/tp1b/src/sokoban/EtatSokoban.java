/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package sokoban;

import astar.Etat;

/**
 * Représente un état d'un monde du jeu Sokoban.
 */

public class EtatSokoban extends Etat {

    // À compléter ...
    // - Ajoutez la représentation d'un état.
    // - Indice : positions du bonhomme et des blocs.

    @Override
    public EtatSokoban clone() {
        EtatSokoban c = new EtatSokoban();
        // À compléter : vous devez faire une copie complète de l'objet.
        return c;
    }

    @Override
    public int compareTo(Etat o) {
        EtatSokoban es = (EtatSokoban) o;
        // À compléter.
        // La comparaison est essentielle pour ajouter des EtatSokoban dans un TreeSet open ou close dans l'algorithme A*.
        return 0;
    }
    
}
