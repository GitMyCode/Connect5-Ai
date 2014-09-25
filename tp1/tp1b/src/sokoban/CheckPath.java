package sokoban;

import java.util.*;

/**
 * Created by MB on 9/16/2014.
 */
public class CheckPath {

    static Case[][] grid;

    static int moves_to_goal = 9999;

    static PriorityQueue<Case> open;
    static Map<Case,Case> open_map;

    static HashSet<Case> close;


    public static Map<Case,Integer> dijkstra(Case start, Case[][] graph){
        Map<Case,Integer> map_distances = new HashMap<Case, Integer>();
        PriorityQueue<Case> Q = new PriorityQueue<Case>(350, new Comparator<Case>() {
            @Override
            public int compare(Case a, Case b) {

                if(a.g < b.g){
                    return -1;
                }
                if(a.g > b.g ){
                    return 1;
                }
                return a.compareTo(b);
            }
        });


        Case[][] cpy_graph = new Case[graph.length][graph[0].length];
        for(int i=0; i<graph.length; i++){
            for(int j=0; j<graph[0].length; j++){
                if(graph[i][j] !=null){
                    Case c = (Case) graph[i][j].clone();
                    cpy_graph[i][j] = c;
                }
            }
        }

        grid = cpy_graph;
        for(int i=0; i< cpy_graph.length; i++){
            for(int j=0; j< cpy_graph[0].length;j++){
                if(cpy_graph[i][j] != null && cpy_graph[i][j].symbole != '#'){
                    Case c = cpy_graph[i][j];
                    c.g = Integer.MAX_VALUE;

                    map_distances.put(c,Integer.MAX_VALUE);
                    Q.add(c);

                }
            }
        }
        Case ref_start = cpy_graph[start.x][start.y];
        ref_start.g=0;
        map_distances.put(ref_start,0);
        Q.remove(ref_start);
        Q.add(ref_start);

        while (!Q.isEmpty()){
            Case current = Q.poll();

            map_distances.put(current,(canGo(current,ref_start,cpy_graph)));
          /*  List<Case> voisinage;
            if (current.equals(ref_start)){
                voisinage = voisin_start_dijstra(current,cpy_graph);
            }else{
                voisinage = voisin(current,cpy_graph);
            }


            for(Case v : voisinage){
                Double alternative = current.g + 1;


                if(map_distances.get(v) == null){
                    System.out.println(" dsf");
                }

                if( alternative < map_distances.get(v) ){
                    v.g = alternative;
                    map_distances.put(v,alternative);
                    v.parent = current;
                }
            }
*/
        }
        cleanGrid();

        return map_distances;

    }


    public static int canGo(Case start, Case end, Case[][] grid2){
        grid = grid2;

        moves_to_goal = 9999;

        open = new PriorityQueue<Case>(100,new Comparator<Case>() {
            @Override
            public int compare(Case a, Case b) {
                if(a.f < b.f) {
                    return -1;
                }
                if(a.f > b.f){
                    return 1;
                }
                return a.compareTo(b);
            }
        });
        open_map = new HashMap<Case, Case>();
        close = new HashSet<Case>();




        open.add(start);
        while (open.size() !=0){
            Case current = open.poll();

            if(end.equals(current)){
                if(start.equals(end)){
                    return 0;
                }

                end.parent = current.parent;

                moves_to_goal=0;
                Case path = end;
                while(path != null){
                    //moves_to_goal++;
                   path = path.parent;
                    moves_to_goal = (path!=null)? moves_to_goal+1 : moves_to_goal;
                }
       /*         if(moves_to_goal <=0){
                    System.out.println("df");
                }*/

                cleanGrid();
                return moves_to_goal;
            }

            open_map.remove(current);
            close.add(current);

          //  printCurrentPath(close);

            calculate_voisinage(grid, current, end);
        }



        cleanGrid();
        return moves_to_goal;

    }
    public static void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }


    private static void calculate_voisinage(Case[][] grid, Case current,Case to ){


        List<Case> voisins = voisin(current,grid );
        for(Case v : voisins){

            if ( !close.contains(v)){

                double newG = current.g +1;
                Case ref_voisin = open_map.get(v);


                if(ref_voisin==null || newG < v.g  ){

                    if(ref_voisin !=null){
                        open.remove(ref_voisin);
                        v= ref_voisin;
                    }

                    v.parent = current;
                    v.h = distance(v,to);
                    v.g = newG;
                    v.f = v.h + v.g;


                    if(ref_voisin ==null){
                        open_map.put(v,v);
                    }
                    open.add(v);
                }
            }
        }
    }


    private static List<Case> voisin_start_dijstra(Case current, Case[][] grid){
        Case c = (Case) current;
        List<Case> voisins = new ArrayList<Case>();
        if(in_grid(c.x -1,c.y)  ){
            voisins.add(grid[c.x-1][c.y]);
        }

        if(in_grid(c.x+1,c.y)     ){

            voisins.add(grid[c.x+1][c.y]);
        }
        if(in_grid(c.x,c.y+1)   ){
            voisins.add(grid[c.x][c.y+1]);
        }
        if(in_grid(c.x,c.y-1)    ){
            voisins.add(grid[c.x][c.y-1]);
        }

        return voisins;
    }

    private static List<Case> voisin(Case current, Case[][] grid){
        Case c = (Case) current;
        List<Case> voisins = new ArrayList<Case>();
        if(in_grid(c.x -1,c.y) && in_grid(c.x+1,c.y) ){
            voisins.add(grid[c.x-1][c.y]);
        }

        if(in_grid(c.x+1,c.y)  && in_grid(c.x-1,c.y)   ){

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

        if(!((x >=0 && x < grid.length) && (y >= 0 && y < grid[0].length))){
            return false;
        }

        if(grid[x][y] == null){
            System.out.println(" sd");
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

        private static void printGrid(Case[][] grid){

        for(int i =0; i< grid.length; i++){
            for (Case c : grid[i]){
                if(c != null)
                    System.out.print(c.symbole + "");
            }
            System.out.println();
        }

    }

}
