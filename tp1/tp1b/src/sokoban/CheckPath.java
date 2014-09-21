package sokoban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MB on 9/16/2014.
 */
public class CheckPath {

    static Case[][] grid;

    static int moves_to_goal = 9999;


    public static int canGo(Case start, Case end, Case[][] grid2){
        grid = grid2;

        moves_to_goal = 9999;
        List<Case> open = new ArrayList<Case>();
        List<Case> close = new ArrayList<Case>();


        open.add(start);
        while (open.size() !=0){
            Case current = best_one(open);

            if(end.equals(current)){
                if(start.equals(end)){
                    return 0;
                }

                end.parent = current.parent;

                moves_to_goal=0;
                Case path = end;
                while(path != null){
                    moves_to_goal++;
                   path = path.parent;
                }

                cleanGrid();
                return moves_to_goal;
            }

            open.remove(current);
            close.add(current);

          //  printCurrentPath(close);

            calculate_voisinage(grid, open, close, current, end);
        }



        cleanGrid();
        return moves_to_goal;

    }

    private static void calculate_voisinage(Case[][] grid,List<Case> open, List<Case> close, Case current,Case to ){


        List<Case> voisins = voisin(current,grid );
        for(Case v : voisins){

            if ( !close.contains(v)){

                double newG = current.g +1;

                if( newG > v.g  ){

                    v.parent = current;
                    v.h = distance(v,to);
                    v.g = newG;
                    v.f = v.h + v.g;


                    if( !open.contains(v)){
                        open.add(v);
                    }

                }

            }
        }
    }
    private static List<Case> voisin(Case current, Case[][] grid){
        Case c = (Case) current;
        List<Case> voisins = new ArrayList<Case>();
        if(in_grid(c.x -1,c.y) && in_grid(c.x+1,c.y) ){
            voisins.add(grid[c.x-1][c.y]);
        }

        if(in_grid(c.x+1,c.y) && in_grid(c.x-1,c.y)   ){

            voisins.add(grid[c.x+1][c.y]);
        }
        if(in_grid(c.x,c.y+1) && in_grid(c.x,c.y-1)  ){
            voisins.add(grid[c.x][c.y+1]);
        }
        if(in_grid(c.x,c.y-1) && in_grid(c.x,c.y+1)   ){
            voisins.add(grid[c.x][c.y-1]);
        }

        return voisins;
    }

    private static boolean in_grid(int x,int y){

        if(!(x >=0 && x < grid.length) && (y >= 0 && y < grid[0].length)){
            return false;
        }

        return  grid[x][y].symbole == ' ' || grid[x][y].symbole == '.';
    }

    private static int distance(Case start, Case to){

        int D =0;
        D = Math.abs(start.x - to.x) + Math.abs(start.y - to.y);

        return D;
    }

    private static Case best_one(List<Case> open){
        Case best =  open.get(0);
        for(Case c : open){
            if(  best.f > c.f){
                best = c;
            }
        }
        return best;
    }

    private static void cleanGrid(){

        for(int i=0; i< grid.length; i++){
            for(Case c : grid[i]){
                if( c != null){
                    c.g = 0.0;
                    c.f = 0.0;
                    if( c.symbole != '#')
                        c.symbole = ' ';
                    c.h = 0.0;
                    c.parent = null;
                }

            }
        }
        /*for(Case c : etat.blocks){
            grid[c.x][c.y].symbole = ' ';
        }*/
    }
    private static void printPath(Case start,Case end ){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid[i]){
                if( c!= null)
                    print.get(i).add(c.symbole);
            }
        }



        Case path = end;
        while (path!= null){
            print.get(path.x).set(path.y,'+');
            path = path.parent;
        }

        print.get(start.x).set(start.y,'S');

        for(int i=0; i< print.size(); i++){
            for(Character c : print.get(i)){
                System.out.print(c + "");
            }
            System.out.println();
        }

    }

    private static void printCurrentPath(List<Case> visited ){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid[i]){
                if( c!= null)
                    print.get(i).add(c.symbole);
            }
        }



        for(Case c : visited){
            print.get(c.x).set(c.y,'+');
        }



        for(int i=0; i< print.size(); i++){
            for(Character c : print.get(i)){
                System.out.print(c + "");
            }
            System.out.println();
        }

    }

}
