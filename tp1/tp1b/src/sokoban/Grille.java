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



    private int max_x=0;
    private int max_y=0;


    Case[][] array_grid;
    public List<List<Character>> grid ;

    public Grille(List<String> lecture){
        grid = new ArrayList<List<Character>>();
        for(String s : lecture){
            List<Character> char_line = new ArrayList<Character>();
            grid.add(char_line);

            max_y = (s.length()>max_y)? s.length() : max_y;

            for(Character c : s.toCharArray() ){
                if( c == '@' || c=='$' || c=='*' || c=='+'){
                    char_line.add(' ');
                }else{
                    char_line.add(c);
                }
            }
        }
        max_x= grid.size();

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
                    temp_case.setY(new_y + dy[i]);
                    if(!obstacles.contains(temp_case) && !e.blocks.contains(temp_case)){
                        if(is_not_blocked(e)){
                            list.add(new ActionDeplacement(dname[i]));
                        }
                    }
                }else{
                    list.add(new ActionDeplacement(dname[i]));
                }

            }

        }

        return list;
    }


    private boolean is_not_blocked(EtatSokoban e){



        List<Case> stack = new LinkedList<Case>(e.blocks);
        while (!stack.isEmpty()){
            Case c = stack.remove(0);
            if(!can_move2(e,c,stack)){

                System.out.println("FALSE");
                StateToString(e);
                return false;
            }
        }
        return  cant_reach_goal(e);
        //System.out.println("TRUE");
       // StateToString(e);
     //   return true;
    }

    private boolean cant_reach_goal(EtatSokoban e){

        boolean no_goal = false;


        setGridWithSymbole(array_grid,e.blocks,'$');


        for(Case block : e.blocks){
            no_goal = false;
            for(Case goal : les_buts){
                if (CheckPath.canGo(block,goal,array_grid)){
                    no_goal = true;
                    break;
                }
            }
            if(!no_goal){
                return false;
            }

        }
        return true;

    }

    private void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }


    private boolean can_move2(EtatSokoban e, Case c, List<Case> stack){


        if(les_buts.contains(c))
            return true;

        Case NORTH = array_grid[c.x-1][c.y];
        Case SOUTH = array_grid[c.x+1][c.y];
        Case WEST = array_grid[c.x][c.y-1];
        Case EAST = array_grid[c.x][c.y+1];

        if(WEST.symbole != '#' && EAST.symbole != '#'){



            if(WEST.symbole == '$' && !can_move2(e,WEST,stack) ){

            }
            if(EAST.symbole == '$' && can_move2(e,EAST,stack)){
                return true;
            }
        }

        if(NORTH.symbole != '#' && SOUTH.symbole != '#'){
            if(WEST.symbole == '$' && can_move2(e,WEST,stack)){
                return true;
            }
            if(EAST.symbole == '$' && can_move2(e,EAST,stack)){
                return true;
            }
        }
        return false;
    }


    private boolean can_move(EtatSokoban e, Case c,List<Case> stack){

        if(les_buts.contains(c)){
            return true;
        }

        Case temp = new Case(c);

        temp.applyDeplacement("W");
        if(!obstacles.contains(temp) && checkLine("Y",stack,temp)){


            temp.applyDeplacement("E");
            temp.applyDeplacement("E");
            if(!obstacles.contains(temp) && checkLine("Y",stack,temp)){

                   return true;
            }
        }

        temp.setX(c.x);
        temp.setY(c.y);
        temp.applyDeplacement("N");
        if(!obstacles.contains(temp) && checkLine("X",stack,temp)){

            temp.applyDeplacement("S");
            temp.applyDeplacement("S");
            if(!obstacles.contains(temp) && checkLine("X", stack, temp)){
                return true;
            }
        }



        return false;
    }

    private boolean checkLine(String axes,List<Case> stack, Case c){

        Case temp = new Case(c);

        if(!stack.contains(c)){
            return true;
        }

        if(axes == "X"){
            temp.applyDeplacement("W");
            if(!obstacles.contains(temp) && !stack.contains(temp)){


                temp.applyDeplacement("E");
                temp.applyDeplacement("E");
                if(!obstacles.contains(temp) && !stack.contains(temp)){
                    return true;
                }
            }

        }else{
            temp.applyDeplacement("N");
            if(!obstacles.contains(temp) && !stack.contains(temp)){


                temp.applyDeplacement("S");
                temp.applyDeplacement("S");
                if(!obstacles.contains(temp) && !stack.contains(temp)){
                    return true;
                }
            }
        }



        return false;
    }


    private boolean is_resolvable(Case c){


        Case temp = new Case(c.x,c.y,'%');


        temp.applyDeplacement("W");
        if(!obstacles.contains(temp)){
            temp.applyDeplacement("E");
            temp.applyDeplacement("E");
            if(!obstacles.contains(temp)){
                return true;

            }
        }


        temp = new Case(c.x,c.y,'%');
        temp.applyDeplacement("N");
        if(!obstacles.contains(temp)){
            temp.applyDeplacement("S");
            temp.applyDeplacement("S");
            if(!obstacles.contains(temp)){
                return true;
            }
        }

        return false;


    }


    private void StateToString(EtatSokoban e){
        char[][] printable = new char[100][100];


        for(Case c : obstacles){
            printable[c.x][c.y] = '#';
        }

        for(Case c: les_buts){
            printable[c.x][c.y] = '.';
        }
        for(Case c : e.blocks){
            printable[c.x][c.y] = '$';
        }

        printable[e.bonhomme.x][e.bonhomme.y] = '@';



        for(int i=0; i<max_x;i++){
            for(int j=0; j<max_y; j++){
                System.out.print(printable[i][j]);
            }
            System.out.println();
        }

        System.out.println("-----------------");



    }


    private boolean isINgrid(int x,int y){
        return (x >= 0 && x < grid.size())&&(y >= 0 && y < grid.get(x).size());
    }


}
