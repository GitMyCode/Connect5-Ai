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
    protected int[][] matrix_distance_goal;
    protected boolean last_action_move_block;


    public EtatSokoban(Case bonhomme,List<Case> blocks){
        this.bonhomme = bonhomme;
        this.blocks = blocks;
        this.is_resolvable = true;


        matrix_distance_goal = new int[blocks.size()][blocks.size()];


        tree_blocks = new TreeSet<Case>();
        for(Case c : blocks){
            tree_blocks.add(c);
        }

    }



    public void applyDeplacement(Action a){
        ActionDeplacement new_action = (ActionDeplacement) a;

        Case temp = new Case(bonhomme.x,bonhomme.y,'$');
        temp.applyDeplacement(new_action.nom);


        int index_test = new_action.index_moved_block;

        int index_ref = blocks.indexOf(temp);


        if(index_ref != -1){
            Case ref = blocks.get(index_ref);
            tree_blocks.remove(ref);

            ref.applyDeplacement(new_action.nom);

            //update matrix distance
            for(int i=0; i< blocks.size(); i++){
                matrix_distance_goal[new_action.index_moved_block][i] = new_action.moves_to_goals[i];
            }

            last_action_move_block = ((ActionDeplacement) a).is_moving_block;

            tree_blocks.add(ref);


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
        for(int i=0; i< blocks.size(); i++){
            for(int j=0; j< blocks.size(); j++){
                cloned.matrix_distance_goal[i][j] = matrix_distance_goal[i][j];
            }
        }
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



/*        if(this.compareTo(that) != 0){
            System.out.println("probleme");
        }*/

        return true;
    }

    @Override
    public int hashCode() {
        long result = 17;
        result = 17 * result + bonhomme.hashCode();

        for(Case c : blocks){
           result = 19*result + c.hashCode();
        }

        //result = 7 * result + (int) (blocks_res ^ (blocks_res >> 32));
        return (int) (result ^ (result >> 32));
    }

    @Override
    public int compareTo(Etat o) {
        EtatSokoban es = (EtatSokoban) o;

        if(this == es) return 0;

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
        it = null;
        it2 = null;
        return 0;
    }
    
}
