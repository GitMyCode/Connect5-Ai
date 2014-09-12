/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import astar.Etat;

import java.util.List;

/**
 * Représente un but.
 */
public class But implements astar.But, astar.Heuristique {

    // À compléter.
    // Indice : les destinations des blocs.
    List<Case> les_buts;

    public But(List<Case> les_buts){
        this.les_buts = les_buts;
    }

    @Override
    public boolean butSatisfait(astar.Etat e) {
        EtatSokoban etat = (EtatSokoban) e;

        for(Case c : les_buts){



            if(!etat.blocks.contains(c)){
                return false;
            }
        }

        return true;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {

        EtatSokoban etat = (EtatSokoban) e;

        return 0;
    }
    
}
