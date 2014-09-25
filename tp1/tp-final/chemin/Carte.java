/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package chemin;

import java.util.*;

/**
 * Représente une Carte.
 * Le «Monde» est une carte. Une carte est un ensemble de noeuds et de routes.
 * 
 * @author Éric Beaudry
 */
public class Carte implements astar.Monde{
    
    protected Map<Long, Noeud> noeuds = new TreeMap<Long, Noeud>();
    protected Map<Long, Route> routes = new TreeMap<Long, Route>();
    
    
    protected void addNode(Noeud node){
        noeuds.put(node.id, node);
    }
    
    protected void addWay(Route way){ 
        routes.put(way.id, way);
        for(int i=1;i<way.noeuds.size();i++){
            Noeud n1 = way.noeuds.get(i-1);
            Noeud n2 = way.noeuds.get(i);
            double d = n1.coor.distanceTerre(n2.coor);
            n1.tronconsSortants.put(n2, d);
            if(!way.sensunique) 
                n2.tronconsSortants.put(n1, d);
        }
    }
    @Override
    public List<astar.Action> getActions(astar.Etat e) {
        Etat etat = (Etat) e;
        ArrayList<astar.Action> actions = new ArrayList<astar.Action>(etat.emplacement.tronconsSortants.size());
        for(Map.Entry<Noeud, Double> entry : etat.emplacement.tronconsSortants.entrySet()){
            actions.add(new ActionGoto(entry.getValue(), entry.getKey()));
        }
        return actions;
    }

    /** Exécute une action de déplacement dans à partir de l'état e du monde. */
    @Override
    public Etat executer(astar.Etat e, astar.Action a) {
        // L'interface Monde.executer étant générique, il faut effectuer les 2 conversions de type suivantes :
        Etat etat = (Etat) e;
        ActionGoto action = (ActionGoto) a;
        assert etat.emplacement.tronconsSortants.containsKey(action.prochain);
        Etat resultat = new Etat(action.prochain);
        return resultat;
    }
    
    public Noeud getNoeud(String n){
        if(n.charAt(0)=='n')
            n = n.substring(1);
        long id = Long.parseLong(n);
        return noeuds.get(id);
    }

}
