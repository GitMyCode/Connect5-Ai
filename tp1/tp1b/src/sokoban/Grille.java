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
    private String[] dname = {"N","S","E","W"};


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

    public void setLes_buts(List<Case> les_buts) {
        this.les_buts = les_buts;
    }

    public void setObstacles(List<Case> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public List<astar.Action> getActions(astar.Etat e) {
        EtatSokoban etat = (EtatSokoban) e;

        return checkPossibleActions(etat);
    }

    @Override
    public astar.Etat executer(astar.Etat e, astar.Action a) {

        EtatSokoban etat = (EtatSokoban) e;
        ActionDeplacement actionDeplacement = (ActionDeplacement) a;
        try {
            EtatSokoban new_etat = etat.clone();

            Case temp = new Case(new_etat.bonhomme.x,new_etat.bonhomme.y,'%');
            temp.applyDeplacement(actionDeplacement.nom);
            if(new_etat.blocks.contains(temp)){
                new_etat.blocks.get(new_etat.blocks.indexOf(temp)).applyDeplacement(actionDeplacement.nom);
            }
            new_etat.bonhomme.applyDeplacement(actionDeplacement.nom);


            etat = new_etat;

        }catch (CloneNotSupportedException ex){
            System.out.println(ex);
        }



        return etat;
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
            int new_x = dx[i] +x;
            int new_y = dy[i] +y;
            Case temp_case = new Case(new_x,new_y,'%');


            if(!obstacles.contains(temp_case)){
                if(e.blocks.contains(temp_case)){
                    temp_case.setX(new_x+dx[i]);
                    temp_case.setY(new_y+dy[i]);
                    if(!obstacles.contains(temp_case) && !e.blocks.contains(temp_case)){
                        list.add(new ActionDeplacement(dname[i]));
                    }

                }else{
                    list.add(new ActionDeplacement(dname[i]));
                }

            }

        }

        return list;
    }

    private boolean isINgrid(int x,int y){
        return (x >= 0 && x < grid.size())&&(y >= 0 && y < grid.get(x).size());
    }


}
