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


        double nb_but_non_atteint = les_buts.size();
        for(Case c : les_buts){
           if(((EtatSokoban) e).blocks.contains(c)){
               nb_but_non_atteint--;
           }
        }

        double distance_total =0;
        for(Case but : les_buts){
            for(Case block : ((EtatSokoban) e).blocks ){
                double xx  = Math.pow(but.getX() - block.getX(),2);
                double yy  = Math.pow(but.getY() - block.getY(),2);
              double d = Math.pow((xx+yy),1/2);
                distance_total += d;
            }
        }



        return distance_total-nb_but_non_atteint;
    }
    
}
