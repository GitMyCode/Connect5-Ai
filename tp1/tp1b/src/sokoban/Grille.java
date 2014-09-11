/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import astar.Action;

import javax.xml.stream.events.Characters;
import java.io.*;
import java.util.*;

/**
 * Dans le jeu de sokoban, le «Monde» est une «Grille».
 */
public class Grille implements astar.Monde, astar.But {
    
    // À compléter.
    
    // Mettre la représentation d'une grille ici.
    // Indice : tableau pour stocker les obstacles et les buts.

    private List<Case> obstacles;
    private List<Case> les_buts;


    private int[] dx = {-1,1,0,0};
    private int[] dy = {0,0,-1,1};
    private String[] dname = {"S","N","W","E"};


    public List<List<Character>> grid ;

    public Grille(List<String> lecture){
        grid = new ArrayList<List<Character>>();
        for(String s : lecture){
            List<Character> char_line = new ArrayList<Character>();
            grid.add(char_line);
            for(Character c : s.toCharArray() ){
                if( c == '@' || c=='$' || c=='*' || c=='+'){
                    char_line.add(' ');
                }else{
                    char_line.add(c);
                }
            }
        }

    }

    
    @Override
    public List<astar.Action> getActions(astar.Etat e) {
        EtatSokoban etat = (EtatSokoban) e;

        return checkPossibleActions(etat);
    }

    @Override
    public astar.Etat executer(astar.Etat e, astar.Action a) {


        return e;
    }
    
    /** Retourne */
    @Override
    public boolean butSatisfait(astar.Etat e){
        return false;
    }


    private List<Action> checkPossibleActions(EtatSokoban e){
        List<Action> list = new ArrayList<Action>();
        int x = e.bonhomme.x;
        int y = e.bonhomme.y;


        for(int i=0; i<4;i++){
            int new_x = dx[i];
            int new_y = dy[i];

            char grid_position = grid.get(new_x).get(new_y);
            if(isINgrid(new_x,new_y) && grid_position != '#' ){
                char etat_position = e.blocks
                if(new_position ==)
            }
        }
        if(isINgrid(x-1,y) && grid.get(x-1).get(y) != '#')
            list.add(new ActionDeplacement("W"));
        if(isINgrid(x+1,y) && grid.get(x+1).get(y) != '#')
            list.add(new ActionDeplacement("E"));
        if(isINgrid(x,y-1) && grid.get(x).get(y-1) != '#')
            list.add(new ActionDeplacement("N"));
        if(isINgrid(x,y+1) && grid.get(x).get(y+1) != '#')
            list.add(new ActionDeplacement("S"));

        return list;
    }

    private boolean isINgrid(int x,int y){
        return (x >= 0 && x < grid.size())&&(y >= 0 && y < grid.get(x).size());
    }

    private

}
