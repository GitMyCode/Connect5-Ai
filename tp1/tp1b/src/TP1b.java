/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

import java.io.*;
import java.util.*;
import sokoban.Probleme;

/**
 * Point d'entrée de la partie B du TP1 (jeu sokoban).
 * Attention :
 *  - Vous ne devriez pas avoir à modifier ce fichier source.
 *  - Ne déplacez pas ce fichier source. Il doit être dans le package racine.
 */
public class TP1b {

    public static void main(String args[]) throws IOException {
        //Reader reader = args.length==1 ? new FileReader("sokoban00.txt") : new InputStreamReader(System.in);
        Reader reader =  new FileReader("src/tests/sokoban14.txt");
        BufferedReader br = new BufferedReader(reader);
        Probleme probleme = Probleme.charger(br);
        List<astar.Action> plan = astar.AStar.genererPlan(probleme.grille, probleme.etatInitial, probleme.but, probleme.but);
        if(plan==null)
            System.out.println("<Aucune solution>");
        else
            for(astar.Action action : plan){
                System.out.print(" " + action);
            }
    }
}
