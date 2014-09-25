/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */

package chemin;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Représente une route.
 * @author Éric Beaudry
 */
public class Route implements Comparable<Route>, Cloneable {
    protected long id;
    protected String nom = "?";
    protected ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    protected boolean sensunique = false;
    
    public Route(long id){
        this.id = id;
    }
    
    public void setName(String nom){
        nom = Normalizer.normalize(nom, Normalizer.Form.NFD);
        nom = nom.replaceAll("[^\\p{ASCII}]", "");
        nom = nom//.replace(".", " ")
                //.replace("'", "_")
                //.replace("-", "_")
                //.replace("`", "_")
                .replace(" ", "_")
                //.replace(',', '_')
                .replace(';', '_')
                .replace(':', '_')
                .replace('"', '_')
                //.replace('+', '_')
                ;
        this.nom = nom;
    }

    @Override
    public int compareTo(Route o) {
        if(id<o.id) return -1;
        if(id>o.id) return +1;
        return 0;
    }
    
    @Override
    public Route clone(){
        Route copie = new Route(id);
        copie.nom = nom;
        copie.sensunique = sensunique;
        copie.noeuds = noeuds;
        return copie;
    }
}
