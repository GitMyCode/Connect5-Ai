/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package sokoban;

import astar.Etat;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un état d'un monde du jeu Sokoban.
 */

public class EtatSokoban extends Etat {

    // À compléter ...
    // - Ajoutez la représentation d'un état.
    // - Indice : positions du bonhomme et des blocs.

    protected Case bonhomme;
    protected List<Case> blocks;

    public EtatSokoban(Case bonhomme,List<Case> blocks){
        this.bonhomme = bonhomme;
        this.blocks = blocks;
    }

    @Override
    public EtatSokoban clone() throws CloneNotSupportedException{

        List<Case> cloned_blocks = new ArrayList<Case>();
        for(Case c: blocks){
            cloned_blocks.add((Case)c.clone());
        }


        Case cloned_bonhomme = (Case) bonhomme.clone();
        EtatSokoban cloned =  new EtatSokoban(cloned_bonhomme,cloned_blocks);

        // À compléter : vous devez faire une copie complète de l'objet.
        return cloned;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatSokoban that = (EtatSokoban) o;

        if (blocks != null ? !blocks.equals(that.blocks) : that.blocks != null) return false;
        if (bonhomme != null ? !bonhomme.equals(that.bonhomme) : that.bonhomme != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bonhomme != null ? bonhomme.hashCode() : 0;
        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Etat o) {
        EtatSokoban es = (EtatSokoban) o;

        int cmp  = bonhomme.compareTo(es.bonhomme);
        if(cmp !=0){
            return cmp;
        }
        for(int i =0; i< es.blocks.size(); i++){
            Case block_ici = blocks.get(i);
            Case block_o   = es.blocks.get(i);

           cmp = block_ici.compareTo(block_o);
            if(cmp !=0){
                return cmp;
            }
        }

        // À compléter.
        // La comparaison est essentielle pour ajouter des EtatSokoban dans un TreeSet open ou close dans l'algorithme A*.
        return 0;
    }
    
}
