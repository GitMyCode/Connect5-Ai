package sokoban;

import java.util.*;

/**
 * Created by MB on 9/29/2014.
 */
public class AstarHangar {


    static Case[][] grid_player;
    static CompareCase compareCase = new CompareCase();
    /*
    *
    *
    * Pour une distance entre deux case avec un deplacement
    * libre
    * passe a travers les boite et ne demande pas d<avoir un espace
    * libre pour la pousser

    * */
    static Case ref_player;
    static Case ref_to;
    static Case ref_current;

    public static int distance_player_block(Case player, Case to, Case[][] graph){
        ref_player = player;
        ref_to = to;
        grid_player = graph;

        PriorityQueue<Case> open = new PriorityQueue<Case>(350,compareCase );
        Map<Case,Case> hash_open = new HashMap<Case, Case>();
        HashSet<Case> close = new HashSet<Case>();

        if(player==null){
            return 0;
        }

        open.add(player);
        hash_open.put(player,player);
        int total_move = 9999;

        while (!open.isEmpty()){
            Case current = open.poll();
            ref_current= current;
            if(current.equals(to)){
                total_move =0;
                Case path = current;
                while (path!= null){
                    path = path.parent;
                    total_move = (path!=null)? total_move+1 : total_move ;
                }

                cleanGrid();
                return total_move;
            }
            voisinage(current,to,hash_open,open,close);

            hash_open.remove(current);
            close.add(current);
        }
        cleanGrid();
        return total_move;
    }


    public static void voisinage(Case current,Case to, Map<Case,Case> map_open,PriorityQueue<Case> open, HashSet<Case> close){

        List<Case> voisins = voisin_deplacement_libre(current);

        for(Case v : voisins){

            if(!close.contains(v)){

                double new_g = current.g + 1;
                Case ref_voisin = map_open.get(v);
                if(ref_voisin == null || new_g < ref_voisin.g){

                    if(ref_voisin != null){
                        v = ref_voisin;
                    }

                    v.parent = current;
                    v.h = distance(v,to);
                    v.g = new_g;
                    v.f = v.g + v.h;

                    if(ref_voisin == null){
                        map_open.put(v,v);
                    }
                    open.add(v);
                }
            }
        }
    }




    private static List<Case> voisin_deplacement_libre(Case current){
        Case c =  current;

        List<Case> voisins = new ArrayList<Case>();

        if(in_grid_skip_block(c.x - 1, c.y) ) {
            voisins.add(grid_player[c.x - 1][c.y]);
        }
        if(in_grid_skip_block(c.x + 1, c.y)){
            voisins.add(grid_player[c.x+1][c.y]);
        }
        if(in_grid_skip_block(c.x, c.y + 1)){
            voisins.add(grid_player[c.x][c.y+1]);
        }
        if(in_grid_skip_block(c.x, c.y - 1)){
            voisins.add(grid_player[c.x][c.y-1]);
        }

        return voisins;
    }

    private static boolean in_grid_skip_block(int x, int y){



        if(!((x >=0 && x < grid_player.length) && (y >= 0 && y < grid_player[0].length))){
            return false;
        }



        if(grid_player[x][y].symbole == ' ' || grid_player[x][y].symbole == '.' || grid_player[x][y].symbole == '$'){
            return true;
        }
        return grid_player[x][y].equals(ref_to);
    }



    private static int distance(Case start, Case to){

        int D =0;
        D = Math.abs(start.x - to.x) + Math.abs(start.y - to.y);

        if(D==0){
            int j=0;
        }
        return D;
    }
    public static class CompareCase implements Comparator<Case>{

        @Override
        public int compare(Case a, Case b){


            if(a.f < b.f){
                return -1;
            }
            if( a.f > b.f){
                return 1;
            }

            if(a.equals(b)){
                return 0;
            }
            return a.compareTo(b);


        }
    }


    private static Case[][] copyGrid(Case[][] grid_to_cpy){
        Case[][] cpy_grid = new Case[grid_to_cpy.length][grid_to_cpy[0].length];

        for(int i=0; i< grid_to_cpy.length; i++){
            for(Case c: grid_to_cpy[i]){
                if( c != null)
                    cpy_grid[i][c.y] = new Case(c);
            }
        }



        return cpy_grid;

    }

    private static void cleanGrid(EtatSokoban etat){

        for(int i=0; i< grid_player.length; i++){
            for(Case c : grid_player[i]){
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

    private static void cleanGrid(){

        for(int i=0; i< grid_player.length; i++){
            for(Case c : grid_player[i]){
                if( c != null){
                    c.g = 0.0;
                    c.f = 0.0;
                    c.h = 0.0;
                    c.parent = null;
                }

            }
        }

    }

    private static void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }

    private static void printGrid(Case[][] grid){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid_player.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid_player[i]){
                if( c!= null)
                    print.get(i).add(c.symbole);
            }
        }

        print.get(ref_player.x).set(ref_player.y,'P');
        print.get(ref_to.x).set(ref_to.y,ref_to.symbole);

          for(int i=0; i< print.size(); i++){
            for(Character c : print.get(i)){
                System.out.print(c);
            }
            System.out.println();
        }

       /*
        for(int i =0; i< grid.length; i++){
            for (Case c : grid[i]){
                if(c != null){
                    System.out.print(c.symbole );

                }
            }
            System.out.println();
        }*/

    }

    private static void printPath(Case end){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid_player.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid_player[i]){
                if( c!= null)
                    print.get(i).add(c.symbole);
            }
        }



        Case path = end;
        Case start  =null;
        while (path!= null){
            print.get(path.x).set(path.y,'+');
            path = path.parent;
            if (path!= null){
                start = path;
            }
        }

        if(start!= null)
            print.get(start.x).set(start.y,'S');

        for(int i=0; i< print.size(); i++){
            for(Character c : print.get(i)){
                System.out.print(c);
            }
            System.out.println();
        }

    }

}
