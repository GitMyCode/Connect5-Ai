/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

import java.text.NumberFormat;
import java.util.*;

public class AStar {

    public static List<Action> genererPlan(Monde monde, Etat etatInitial, But but, Heuristique heuristique){
        long starttime = System.currentTimeMillis();
        
        // À Compléter.
        // Implémentez l'algorithme A* ici.
        
        // Étapes suggérées :
        //  - Restez simple.
        //  - Ajoutez : TreeSet<Etat> open, close;.
        //  - Ajoutez etatInitial dans open.
        //  - Numérotez les itérations.
        //  - Pour chaque itération :
        //  --  Affichez le numéro d'itération.
        //  --  Faites une boucles qui itère tous les états e dans open pour trouver celui avec e.f minimal.
        //  --  Affichez l'état e sélectionné (les e.f affichés devraient croître);
        //  --  Vérifiez si l'état e satisfait le but. 
        //  ---   Si oui, sortez du while.
        //  ---   Une autre boucle remonte les pointeurs parents.
        //  --  Générez les successeurs de e.
        //  --  Pour chaque état successeur s de e:
        //  ---   Vérifiez si s.etat est dans closed.
        //  ---   Calculez s.etat.g = e.g + s.cout.
        //  ---   Vérifiez si s.etat existe dans open.
        //  ----    Si s.etat est déjà dans open, vérifiez son .f.
        //  ---   Ajoutez s.etat dans open si nécessaire.
        //  - Exécutez le programme sur un problème très simple.
        //  --  Vérifiez le bon fonctionnement de la génération des états.
        //  --  Vérifiez que e.f soit croissant (>=).
        //  - Une fois que l'algorithme :
        //  -- Ajoutez un TreeSet<Etat> open2 avec un comparateur basé sur f.
        //  -- Évaluez la pertinence d'un PriorityQueue.
        //  - Commentez les lignes propres au déboggage.
        
        // Un plan est une séquence (liste) d'actions.


        long lastDuration = System.currentTimeMillis() - starttime;
        // Les lignes écrites débutant par un dièse '#' seront ignorées par le valideur de solution.
        System.out.println("# Nombre d'états générés : " + 0);
        System.out.println("# Nombre d'états visités : " + 0);
        System.out.println("# Durée : " + lastDuration + " ms");
        System.out.println("# Coût : " + nf.format(Double.POSITIVE_INFINITY));
        return null;
    }
    
    static final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    static {
        nf.setMaximumFractionDigits(1);
    }
}
