/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import astar.Etat;

import java.util.*;

/**
 * Représente un but.
 */
public class But implements astar.But, astar.Heuristique {

    // À compléter.
    // Indice : les destinations des blocs.
    List<Case> les_buts;

    List<Case> mures;

    Case[][] grid;

    private int min_distance;
    static int[][] matrix_distance;

    Map<Case,Integer> g_count;
    Map<Case,Integer> h_count;
    Map<Case,Integer> f_count;

    protected int test =0;
    protected double ratio_space_wall;

    protected List<Integer> list_distances;

    public But(List<Case> les_buts){
        this.les_buts = les_buts;
        matrix_distance = new int[les_buts.size()][les_buts.size()];
    }

    @Override
    public boolean butSatisfait(astar.Etat e) {
        EtatSokoban etat = (EtatSokoban) e;

        for(Case c : les_buts){



            if(!etat.blocks.contains(c)){
                return false;
            }
        }

        return true;
    }

    @Override
    public double estimerCoutRestant(astar.Etat e, astar.But b) {

       EtatSokoban etat = (EtatSokoban) e;

        test =0;

        double distance_total =0;
        matrix_distance = etat.matrix_distance_goal;




        int[][] cpy = new int[matrix_distance.length][matrix_distance.length];
        for(int i=0 ; i< matrix_distance.length;i++){
            for(int j=0; j< matrix_distance.length; j++){
                cpy[i][j] = matrix_distance[i][j];
            }
        }


        int distance_player =0;
        for(int i=0; i<etat.blocks.size(); i++){



            for(int j=0; j< les_buts.size(); j++){
                cpy[i][j] = matrix_distance[i][j] + distance_player;
            }
        }


 /*       int[][] testM = new int[etat.blocks.size()][etat.blocks.size()];
        for(int i=0; i< etat.blocks.size(); i++){
            for(int j=0; j< les_buts.size(); j++){
                Case block = etat.blocks.get(i);
                Case but   = les_buts.get(j);
                matrix_distance[i][j] = distance(block,but); //can_go(block,but,etat);

            }
        }*/


        int distance_player_box = Integer.MAX_VALUE;
        for(Case c : etat.blocks){
                int dis = distance(etat.bonhomme, c);
                if( distance_player_box > dis ){
                    distance_player_box = dis;

                }

        }


        int[][] temp = new int[cpy.length][cpy.length];
        for(int i=0 ; i< matrix_distance.length;i++){
            for(int j=0; j< matrix_distance.length; j++){
                temp[i][j] = cpy[i][j];
            }
        }
        int[][] best_matrix_combinaison = HungarianAlgorithm.computeAssignments(cpy);


        min_distance=0;
        int best_distance_player = Integer.MAX_VALUE;
        setGridWithSymbole(grid,etat.blocks,'$');
        for(int i =0 ; i< best_matrix_combinaison.length; i++){
            min_distance += temp[best_matrix_combinaison[i][0]][best_matrix_combinaison[i][1]];


            Case block = etat.blocks.get(i);
                grid[block.x][block.y].symbole = ' ';
                distance_player = distance_player_block(etat.bonhomme,block);
                if(distance_player < best_distance_player){
                    best_distance_player = distance_player;
                }

                cleanGrid();
                grid[block.x][block.y].symbole = '$';



        }
        setGridWithSymbole(grid,etat.blocks,' ');

        double h =  min_distance + best_distance_player;

        return h;
/*
       *//* int block_to_choose = Integer.MAX_VALUE;
        int distance_choosens_block =0;
        for(int i=0; i< matrix_distance.length ; i++){
            for(Integer min : list_distances ){
                if(block_to_choose > matrix_distance[i][min]){
                    if(!les_buts.contains(etat.blocks.get(i))){
                        block_to_choose = matrix_distance[i][min];
                        distance_choosens_block = distance(etat.bonhomme,etat.blocks.get(i));
                    }

                }
            }
        }
*//*


        int distance_player_box = Integer.MAX_VALUE;
        for(Case c : etat.blocks){
            int dis = distance(etat.bonhomme, c);
            if( distance_player_box > dis ){
                distance_player_box = dis;

            }
        }

        return Math.pow(Double.valueOf(min_distance + (distance_player_box/1.5)),2)/7 ;//+ distance_player_box * 10; *//*//**//* block_to_choose;*/
    }


    private void printMatrix(int[][] matrix){

        for(int i =0; i< matrix.length; i++){
            for(int j=0; j < matrix.length; j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }

        System.out.println("----");
    }


    private void min_matrix(ArrayList<Integer> used,int i,int so_far){

        int max=0;

        if(i >= matrix_distance.length){
            if( so_far < min_distance){

                min_distance = so_far;
                list_distances = used;
            }

            test++;
            return;
        }

        for(int j=0; j< matrix_distance.length; j++){
            if(!used.contains(j)){

                int n = matrix_distance[i][j] + so_far;
                ArrayList<Integer> n_used = (ArrayList<Integer>) used.clone();
                n_used.add(j);
                min_matrix(n_used,i+1,n);
            }
        }



    }


    private int distance(Case start, Case to){

        int D =0;
        D = Math.abs(start.x - to.x) + Math.abs(start.y - to.y);

        return D;
    }


    private int distance_player_block(Case player, Case to){

        PriorityQueue<Case> open = new PriorityQueue<Case>(100, new Comparator<Case>() {
            @Override
            public int compare(Case a, Case b) {

                if(a.f < b.f){
                    return -1;
                }
                if(a.f > b.f ){
                    return 1;
                }
                return a.compareTo(b);
            }
        });
        Map<Case,Case> hash_open = new HashMap<Case, Case>();
        HashSet<Case> close = new HashSet<Case>();

        open.add(player);
        hash_open.put(player,player);
        int total_move = 9999;




        while (!open.isEmpty()){

            Case current = open.poll();


            if(current.equals(to)){
                total_move =0;
                Case path = current;
                while (path!= null){
                    path = path.parent;
                    total_move = (path!=null)? total_move+1 : total_move ;
                }
                return total_move -1;
            }


            voisinage(current,to,hash_open,open,close);

            hash_open.remove(current);
            close.add(current);

        }




        return total_move;
    }


    private void voisinage(Case current,Case to, Map<Case,Case> map_open,PriorityQueue<Case> open, HashSet<Case> close){

        List<Case> voisins = getVoisin(current);

        for(Case v : voisins){

            if(!close.contains(v)){

                double new_g = current.g + 1;
                Case ref_voisin = map_open.get(v);
                if(ref_voisin == null || new_g < ref_voisin.g){

                    if(ref_voisin != null){
                        open.remove(ref_voisin);
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





    private int can_go(Case start, Case to, EtatSokoban etat){

        int moves_to_goal =999;

        Noeud s = (Noeud)  start;
        Case end = to;

        List<Case> open = new ArrayList<Case>();
        List<Case> close = new ArrayList<Case>();


        open.add(start);
        prepareGrid(grid, etat, start);

        while (open.size() !=0){


            Case current = best_one(open);

            if(end.equals(current)){
                end.parent = current.parent;

                moves_to_goal=0;
                Case path = end;
                while(path != null){
                    moves_to_goal++;
                   path = path.parent;
                }
                cleanGrid(etat);
                return moves_to_goal;
            }

            open.remove(current);
            close.add(current);

            calculate_voisinage(open, close, current, start, to , etat.bonhomme);
        }

        cleanGrid(etat);

        return moves_to_goal;
    }




    private void calculate_voisinage(List<Case> open, List<Case> close, Case current,Case start, Case to, Case player ){


        List<Case> voisins = getVoisin(current);
        for(Case v : voisins){

            if ( !close.contains(v)){

                double newG = current.g +1;

                if(v ==null){
                    System.out.println("sdaf");
                }

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



    private List<Case> getVoisin(Case current){
        Case c =  current;

        List<Case> voisins = new ArrayList<Case>();

        if(in_grid(c.x-1,c.y) ) {
            voisins.add(grid[c.x - 1][c.y]);
        }
        if(in_grid(c.x+1,c.y)){
            voisins.add(grid[c.x+1][c.y]);
        }
        if(in_grid(c.x,c.y+1)){
            voisins.add(grid[c.x][c.y+1]);
        }
        if(in_grid(c.x,c.y-1)){
            voisins.add(grid[c.x][c.y-1]);
        }

        return voisins;
    }

    private boolean in_grid(int x,int y){

        if(!(x >=0 && x < grid.length) && (y >= 0 && y < grid[0].length)){
            return false;
        }
        if(grid[x][y] == null){
            return false;
        }

        return  grid[x][y].symbole == ' ' || grid[x][y].symbole == '.' || grid[x][y].symbole == '$';
    }


    private Case[][] copyGrid(Case[][] grid_to_cpy, Case start){
        Case[][] cpy_grid = new Case[grid_to_cpy.length][grid_to_cpy[0].length];

        for(int i=0; i< grid_to_cpy.length; i++){
            for(Case c: grid_to_cpy[i]){
                if( c != null)
                    cpy_grid[i][c.y] = new Case(c);
            }
        }
        cpy_grid[start.x][start.y].symbole = '$';


        return cpy_grid;

    }




    private  void prepareGrid(Case[][] grid, EtatSokoban etat,Case start){
        for (Case c : etat.blocks){
            grid[c.x][c.y].symbole = c.symbole;
        }
        grid[start.x][start.y].symbole = ' ';
    }
    private Case best_one(List<Case> open){
        Case best =  open.get(0);

        for(Case c : open){
            if(  best.f > c.f){
                best = c;
            }
        }

        return best;
    }

    private void cleanGrid(EtatSokoban etat){

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

    private void printGrid(EtatSokoban e){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid[i]){
                if( c!= null)
                    print.get(i).add(c.symbole);
            }
        }

        for(Case c: les_buts){
            print.get(c.x).set(c.y,'.');
        }

        for(Case c : e.blocks){
            print.get(c.x).set(c.y,c.symbole);
        }

        print.get(e.bonhomme.x).set(e.bonhomme.y,'X');

        for(int i=0; i< print.size(); i++){
            for(Character c : print.get(i)){
                System.out.print(c );
            }
            System.out.println();
        }


    }

    private void printPath(Case end){

        List<List<Character>> print = new ArrayList<List<Character>>();
        for(int i=0 ; i < grid.length; i++){
            print.add(new ArrayList<Character>());
            for(Case c: grid[i]){
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
                System.out.print(c + " ");
            }
            System.out.println();
        }

    }


    private  void cleanGrid(){

        for(int i=0; i< grid.length; i++){
            for(Case c : grid[i]){
                if( c != null){
                    c.g = 0.0;
                    c.f = 0.0;
                    c.h = 0.0;
                    c.parent = null;
                }

            }
        }
        /*for(Case c : etat.blocks){
            grid[c.x][c.y].symbole = ' ';
        }*/
    }

   private void setGridWithSymbole(Case[][] clean_grid, List<Case> caseToSet, Character sym){

        for(Case c : caseToSet){
            clean_grid[c.x][c.y].symbole = sym;
        }
    }

}
