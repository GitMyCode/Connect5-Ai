/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
import chemin.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
/**
 * Point d'entrée de la partie A du TP1 (planification de chemin).
 * Attention : 
 *  - Vous ne devriez pas avoir à modifier ce fichier source.
 *  - Ne déplacez pas ce fichier source. Il doit être dans le package racine.
 */
public class TP1a {
    public static void main(String args[]) throws Exception{
        Carte carte = null;
        carte = ParseurCarteTxt.parseCarte(new BufferedReader(new FileReader("src/carte-uqam.txt")));
        Noeud ni = carte.getNoeud("n1");
        if(ni==null) throw new Exception("Noeud " + args[1] + " inexistant dans la carte!");
        Etat etatinitial = new Etat(ni);
        Noeud nb = carte.getNoeud("n8");
        if(nb==null) throw new Exception("Noeud " + args[2] + " inexistant dans la carte!");        
        But but = new But(nb);
        astar.Heuristique h = but;
        List<astar.Action> plan = astar.AStar.genererPlan(carte, etatinitial, but, h);
        System.out.print("Chemin:");
        if(plan==null)
            System.out.println("<Aucune solution>");
        else
            for(astar.Action action : plan)
                System.out.print(" " + action);
        System.out.println();
    }
}
