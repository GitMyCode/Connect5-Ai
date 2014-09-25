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
    private String[] dname = {"N","S","W","E"};



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

            new_etat.applyDeplacement(a);
          /*  Case temp = new Case(new_etat.bonhomme.x,new_etat.bonhomme.y,'%');
            temp.applyDeplacement(actionDeplacement.nom);
            if(new_etat.blocks.contains(temp)){
                new_etat.blocks.get(new_etat.blocks.indexOf(temp)).applyDeplacement(actionDeplacement.nom);
            }
            new_etat.bonhomme.applyDeplacement(actionDeplacement.nom);*/


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


        setGridWithSymbole(array_grid,e.blocks,'$');

        Case NORTH = array_grid[x-1][y];
        Case SOUTH = array_grid[x+1][y];
        Case EAST = array_grid[x][y+1];
        Case WEST = array_grid[x][y-1];


        for(int i=0; i<4;i++) {
            int new_x = dx[i] + x;
            int new_y = dy[i] + y;

            setGridWithSymbole(array_grid,e.blocks,'$');
            Case temp_case = array_grid[new_x][new_y];


            if (temp_case.symbole != '#') {
                if (temp_case.symbole == '$') { // if player move on a $ case check if the block can be move
                    // temp_case.setX(new_x+dx[i]);
                    // temp_case.setY(new_y + dy[i]);

                    if (array_grid[new_x + dx[i]][new_y + dy[i]].symbole == '.' || array_grid[new_x + dx[i]][new_y + dy[i]].symbole == ' ') {

                        List<Case> next_block_state = new ArrayList<Case>();
                        for (Case c : e.blocks) {
                            next_block_state.add((Case) c.clone());
                            if (c.equals(temp_case)) {

                                temp_case = next_block_state.get(next_block_state.size()-1);
                            }

                        }
                        int index_moved_block = e.blocks.indexOf(temp_case);

                        temp_case.symbole = ' ';
                        temp_case.setX(new_x + dx[i]);
                        temp_case.setY(new_y + dy[i]);
                        temp_case.symbole = '$';
/*

                        setGridWithSymbole(array_grid,e.blocks,' ');

                        setGridWithSymbole(array_grid,next_block_state,'$');
                        System.out.println("");*/
                        setGridWithSymbole(array_grid,next_block_state,' ');

                        setGridWithSymbole(array_grid,e.blocks,' ');

                        int[] matrix_distance = new int[e.blocks.size()];
                        if (is_not_blocked(next_block_state,temp_case,matrix_distance)) {

                            ActionDeplacement action = new ActionDeplacement(dname[i]);
                            action.index_moved_block = index_moved_block;
                            action.moves_to_goals = matrix_distance;
                            action.is_moving_block = true;
                            list.add(action);





                        }else{
                          /*  setGridWithSymbole(array_grid,next_block_state,'$');
                            System.out.println("");
                            setGridWithSymbole(array_grid,next_block_state,' ');*/
                        }


                    }
                } else {
                    ActionDeplacement action = new ActionDeplacement(dname[i]);
                    action.is_moving_block = false;
                    list.add(action);
                }
            }
        }
        setGridWithSymbole(array_grid,e.blocks,' ' );

        return list;
    }



    private boolean is_not_blocked(List<Case> blocks,Case ref_block_pos, int[] matrix_distance){



        setGridWithSymbole(array_grid,blocks,'$');
        for(Case c : blocks){

            if(les_buts.contains(c)){
                continue;
            }
            List<Case> stack = new LinkedList<Case>();
            if(!can_move(c,stack)){


               // System.out.println("FALSE");
               // StateToString(e);

                setGridWithSymbole(array_grid,blocks,' ');
                return false;
            }
        }
        setGridWithSymbole(array_grid,blocks,' ');
        return  cant_reach_goal(ref_block_pos, matrix_distance);
        //System.out.println("TRUE");
       // StateToString(e);
     //   return true;
    }

    private boolean cant_reach_goal(Case new_block_pos, int[] matrix_distance){

        boolean no_goal = false;



        int i=0;
        for(Case goal : les_buts){


            int distance = CheckPath.canGo(new_block_pos,goal,array_grid);
            if(distance != 9999){
                no_goal = true;
            }
            matrix_distance[i] = distance;
            i++;


        }
        return no_goal;
        /*

        for(Case block : blocks){
            no_goal = false;
            for(Case goal : les_buts){
                if (CheckPath.canGo(block,goal,array_grid)){
                    no_goal = true;
                    break;
                }
            }
            if(!no_goal){

                setGridWithSymbole(array_grid,blocks,' ');
                return false;
            }

        }

        setGridWithSymbole(array_grid,blocks,' ');
        return true;
*/
    }

    private void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }


    private boolean can_move(Case c, List<Case> stack){




        Case NORTH = array_grid[c.x-1][c.y];
        Case SOUTH = array_grid[c.x+1][c.y];
        Case WEST = array_grid[c.x][c.y-1];
        Case EAST = array_grid[c.x][c.y+1];


        if(WEST.symbole == ' ' && EAST.symbole == ' ') {
            return true;
        }

        if(NORTH.symbole == ' ' && SOUTH.symbole == ' '){
            return true;
        }


        stack.add(c);

        if( check(NORTH,stack) && check(SOUTH,stack) ){
            return true;
        }

        if( check(EAST,stack) && check(WEST,stack) ){
            return true;
        }

        return false;

    }
    private boolean check(Case c,List<Case> stack){

        if(stack.contains(c)){
            return false;
        }
        if(c.symbole == '#'){
            return false;
        }

        if(c.symbole == ' '){
            return true;
        }

        if(c.symbole == '$'){
            return can_move(c,stack);
        }

        return false;

    }


    private void printGrid(){

        for(int i =0; i< array_grid.length; i++){
            for (Case c : array_grid[i]){
                if(c != null)
                    System.out.print(c.symbole + "");
            }
            System.out.println();
        }

    }



    private void StateToString(EtatSokoban e,List<Case> blocks){
        char[][] printable = new char[100][100];



        for(int i =0; i< array_grid.length; i++){
            for (Case c : array_grid[i]){
                if(c != null)
                    printable[c.x][c.y] = c.symbole;
            }
            System.out.println();
        }

       /* for(Case c : obstacles){
            printable[c.x][c.y] = '#';
        }*/

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
