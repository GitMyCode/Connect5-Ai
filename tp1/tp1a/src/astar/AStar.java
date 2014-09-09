/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package astar;

import java.text.NumberFormat;
import java.util.*;

public class AStar {

    public static TreeSet<Etat> open, close;



    public static List<Action> genererPlan(Monde monde, Etat etatInitial, But but, Heuristique heuristique){
        long starttime = System.currentTimeMillis();
        
        // À Compléter.
        // Implémentez l'algorithme A* ici.

         open = new TreeSet<Etat>();
         close = new TreeSet<Etat>();
         List<Action> plan = new LinkedList<Action>();

        open.add(etatInitial);

        while(open.size() > 0){

            Etat etat_init = open.first();

            List<Action> actions =  monde.getActions(etat_init);
            Action current_action = find_best_open(actions,etat_init,monde);

            Etat current_etat = monde.executer(etat_init,current_action);


            open.remove(etat_init);
            close.add(etat_init);
            plan.add(current_action);

            if(but.butSatisfait(current_etat)){
                break;
            }
            voisins(current_etat,monde,etatInitial,but,heuristique);

            System.out.println(" ici");


        }



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
        return plan;
    }


    private static Action find_best_open(List<Action> l,Etat etat, Monde monde){
        Etat best = monde.executer(etat,l.get(0));
        best.actionDepuisParent = l.get(0);
        for(Action a: l){
            Etat etat_voisin = monde.executer(etat,a);
            if( best.f < etat_voisin.f){
                best = etat_voisin;
                best.actionDepuisParent = a;
            }
        }
        return best.actionDepuisParent;
    }

    private static void voisins(Etat etat, Monde monde, Etat etatInitial, But but, Heuristique heuristique){

        List<Action> action_voisin = monde.getActions(etat);

        for( Action a : action_voisin ){
            Etat voisin = monde.executer(etat,a);
            if( !close.contains(voisin)){

                double newG = a.cout ;
                open.add(voisin);


            }

        }



    }
    
    static final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    static {
        nf.setMaximumFractionDigits(1);
    }
}
