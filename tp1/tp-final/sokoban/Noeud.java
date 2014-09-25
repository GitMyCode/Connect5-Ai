package sokoban;

/**
 * Created by MB on 9/16/2014.
 */
public abstract class Noeud {

    // Variables internes pour l'algorithme A*.
    /**
     * État précédent permettant d'atteindre cet état.
     */
    protected Case parent;
    /**
     * Action à partir de parent permettant d'atteindre cet état.
     */
    /**
     * f=g+h.
     */
    protected double f;
    /**
     * Meilleur coût trouvé pour atteindre cet été à partir de l'état initial.
     */
    protected double g;
    /**
     * Estimation du coût restant pour atteindre le but.
     */
    protected double h;

}


