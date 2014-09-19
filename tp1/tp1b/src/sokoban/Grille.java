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

        return checkPossibleActions2(etat);
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

    private List<Action> checkPossibleActions2(EtatSokoban e){
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
                        if (is_not_blocked(next_block_state)) {
                            list.add(new ActionDeplacement(dname[i]));
                        }else{
                          /*  setGridWithSymbole(array_grid,next_block_state,'$');
                            System.out.println("");
                            setGridWithSymbole(array_grid,next_block_state,' ');*/
                        }


                    }
                } else {
                    list.add(new ActionDeplacement(dname[i]));
                }
            }
        }
        setGridWithSymbole(array_grid,e.blocks,' ' );

        return list;
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


        for(int i=0; i<4;i++){
            int new_x = dx[i] +x;
            int new_y = dy[i] +y;
            Case temp_case = new Case(new_x,new_y,'%');


            if(!obstacles.contains(temp_case)){
                if(e.blocks.contains(temp_case)){
                    temp_case.setX(new_x+dx[i]);
                    temp_case.setY(new_y + dy[i]);
                    if(!obstacles.contains(temp_case) && !e.blocks.contains(temp_case)){
                        List<Case> next_blocks = new ArrayList<Case>(e.blocks);
                        temp_case.setX((new_x-dx[i]));
                        temp_case.setY((new_y-dy[i]));

                        Case sdf = next_blocks.get(next_blocks.indexOf(temp_case));
                        sdf.setX(new_x +dx[i]);
                        sdf.setY(new_y +dy[i]);

                        if(is_not_blocked(next_blocks)){
                            list.add(new ActionDeplacement(dname[i]));
                        }else{
                            System.out.println("");
                        }
                    }
                }else{
                    list.add(new ActionDeplacement(dname[i]));
                }

            }

        }

        return list;
    }


    private boolean is_not_blocked(List<Case> blocks){



        setGridWithSymbole(array_grid,blocks,'$');
        for(Case c : blocks){
            List<Case> stack = new LinkedList<Case>();
            if(!can_move2(c,stack)){

               // System.out.println("FALSE");
               // StateToString(e);

                setGridWithSymbole(array_grid,blocks,' ');
                return false;
            }
        }
        setGridWithSymbole(array_grid,blocks,' ');
        return  cant_reach_goal(blocks);
        //System.out.println("TRUE");
       // StateToString(e);
     //   return true;
    }

    private boolean cant_reach_goal(List<Case> blocks){

        boolean no_goal = false;




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

    }

    private void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }


    private boolean can_move2(Case c, List<Case> stack){


        if(les_buts.contains(c)){
            return true;
        }

        Case NORTH = array_grid[c.x-1][c.y];
        Case SOUTH = array_grid[c.x+1][c.y];
        Case WEST = array_grid[c.x][c.y-1];
        Case EAST = array_grid[c.x][c.y+1];

        if(NORTH.symbole =='.'){
            System.out.println("fuck");
        }
        if(SOUTH.symbole =='.'){
            System.out.println("fuck");
        }
        if(EAST.symbole =='.'){
            System.out.println("fuck");
        }
        if(WEST.symbole =='.'){
            System.out.println("fuck");
        }

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
            return can_move2(c,stack);
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

    private void printGrid(){

        for(int i =0; i< array_grid.length; i++){
            for (Case c : array_grid[i]){
                if(c != null)
                    System.out.print(c.symbole + "");
            }
            System.out.println();
        }

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
