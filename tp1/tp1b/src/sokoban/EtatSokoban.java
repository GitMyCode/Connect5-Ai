/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package sokoban;

import astar.Action;
import astar.Etat;

import java.util.*;

/**
 * Représente un état d'un monde du jeu Sokoban.
 */

public class EtatSokoban extends Etat {

    // À compléter ...
    // - Ajoutez la représentation d'un état.
    // - Indice : positions du bonhomme et des blocs.

    protected Case bonhomme;

    protected List<Case> blocks;
    protected TreeSet<Case> tree_blocks;

    protected boolean is_resolvable;

    public EtatSokoban(Case bonhomme,List<Case> blocks){
        this.bonhomme = bonhomme;
        this.blocks = blocks;
        this.is_resolvable = true;

        tree_blocks = new TreeSet<Case>();
        for(Case c : blocks){
            tree_blocks.add(c);
        }

    }



    public void applyDeplacement(Action a){
        ActionDeplacement new_action = (ActionDeplacement) a;

        Case temp = new Case(bonhomme.x,bonhomme.y,'$');
        temp.applyDeplacement(new_action.nom);

        int index_ref = blocks.indexOf(temp);
        if(index_ref != -1){
            Case ref = blocks.get(index_ref);
            blocks.remove(index_ref);
            tree_blocks.remove(ref);

            temp.applyDeplacement(new_action.nom);

            blocks.add(temp);
            tree_blocks.add(temp);


        }


        bonhomme.applyDeplacement(new_action.nom);


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
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        EtatSokoban that = (EtatSokoban) o;

        int cmp=0;
        //if (blocks != null ? !blocks.equals(that.blocks) : that.blocks != null) return false;

        if(!bonhomme.equals(that.bonhomme)) {
            return false;
        }

        if(that.blocks.size() != blocks.size()){
            return false;
        }



        for(int i =0; i< that.blocks.size(); i++){

            if(!blocks.contains(that.blocks.get(i))){
                return false;
            }

        }




        return true;
    }

    @Override
    public int hashCode() {
        long result = 17;
        result = 37 * result + bonhomme.hashCode();

        long blocks_res = 0;
        int last_feed = blocks.size()-1;
        for(Case c : blocks){
           result =  ((c.x*(blocks.get(last_feed).y+1))* result +   c.hashCode());
            last_feed--;
        }

        //result = 7 * result + (int) (blocks_res ^ (blocks_res >> 32));
        return (int) (result ^ (result >> 32));
    }

    @Override
    public int compareTo(Etat o) {
        EtatSokoban es = (EtatSokoban) o;

        if(this.equals(es)){
            return 0;
        }

        int cmp  = bonhomme.compareTo(es.bonhomme);
        if(cmp !=0){
            return cmp;
        }

        Iterator<Case> it = tree_blocks.iterator();
        Iterator<Case> it2 = es.tree_blocks.iterator();
        while (it.hasNext()){
            cmp = it.next().compareTo(it2.next());
            if(cmp !=0){
                return cmp;
            }
        }
        /*for(Case c : tree_blocks){

        }

        for(int i =0; i< es.blocks.size(); i++){
            Case block_ici = blocks.get(i);
            Case block_o   = es.blocks.get(i);

           cmp = block_ici.compareTo(block_o);

            if(cmp !=0){
                return cmp;
            }

        }
*/
        // À compléter.
        // La comparaison est essentielle pour ajouter des EtatSokoban dans un TreeSet open ou close dans l'algorithme A*.
        return 0;
    }
    
}
