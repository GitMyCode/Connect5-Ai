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


        int etat_generer=0;
        int nb_visite = 0;

        Etat arrive = etatInitial;
        while(open.size() > 0){
            nb_visite++;
            Etat etat_init = find_best_in_open(open);



            if(but.butSatisfait(etat_init)){
                arrive = etat_init;
                break;
            }
            open.remove(etat_init);
            close.add(etat_init);

            voisins(etat_init,monde,etatInitial,but,heuristique);

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
        Etat pas = arrive;
        while(pas.actionDepuisParent != null){
            plan.add(pas.actionDepuisParent);
            pas= pas.parent;
        }

        Collections.reverse(plan);

        etat_generer = open.size()+ close.size();

        long lastDuration = System.currentTimeMillis() - starttime;
        // Les lignes écrites débutant par un dièse '#' seront ignorées par le valideur de solution.
        System.out.println("# Nombre d'états générés : " + etat_generer);
        System.out.println("# Nombre d'états visités : " + nb_visite);
        System.out.println("# Durée : " + lastDuration + " ms");
        System.out.println("# Coût : " + nf.format(arrive.g));
        return plan;
    }


    private static Etat find_best_in_open(TreeSet<Etat> t){
        Etat best = t.first();
        for( Etat e : t){


            if( e.f < best.f ){
                best = e;
            }
        }
        return best;
    }

    private static void voisins(Etat current, Monde monde, Etat etatInitial, But but, Heuristique heuristique){

        List<Action> action_voisin = monde.getActions(current);

        for( Action a : action_voisin ){
            Etat voisin = monde.executer(current,a);
            if( !close.contains(voisin)){

                double newG = a.cout + current.g;
                Etat open_voisin = getEtat(open,voisin);
                if( open_voisin==null || newG < open_voisin.g ){

                    voisin = (open_voisin == null) ? voisin : open_voisin;

                    voisin.parent = current;
                    voisin.actionDepuisParent = a;
                    voisin.h = heuristique.estimerCoutRestant(voisin,but);
                    voisin.g = newG;
                    voisin.f = voisin.g + voisin.h;



                    if (open_voisin == null)
                        open.add(voisin);
                }


            }else{
                System.out.println("test");
            }

        }



    }

    static Etat getEtat(TreeSet<Etat> treeSet, Etat equivalent){

        for(Etat e: treeSet){
            if( e.equals(equivalent)){
                return  e;
            }
        }
        return null;
    }

    static final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    static {
        nf.setMaximumFractionDigits(1);
    }
}
