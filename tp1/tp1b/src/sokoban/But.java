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
    Map<Case,Map<Case,Integer>> map_distance;
    Map<Case,Map<Case,Integer>> map_distance_no_block;

    List<Case> mures;

    Case[][] grid;

    private int min_distance;
    static int[][] matrix_distance;

    Map<Case,Integer> g_count;
    Map<Case,Integer> h_count;
    Map<Case,Integer> f_count;

    private EtatSokoban current_state;

    protected int test =0;
    protected double ratio_space_wall;

    protected final int REMOVE = -4;
    protected Set<Integer> blocks_restant;


    int best_parcours_player =0;
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
        current_state = etat;



        int distance_player =0;
        int chosen_block=0;
        boolean changed_target = false;

        int best_distance_player = Integer.MAX_VALUE;
        for(int i=0; i< etat.blocks.size(); i++){
            Case block = etat.blocks.get(i);
            distance_player = distance(etat.bonhomme,block) -1;//distance_player_block(etat.bonhomme,block);
            if(distance_player < best_distance_player){
                best_distance_player = distance_player;
                chosen_block = i;
            }

        }



        if( etat.cible != chosen_block || etat.last_action_move_block || etat.last_min_blocks_distance == -1 ){

/*Get distances froms map*/

            double[][] cpy = new double[les_buts.size()][les_buts.size()];
            int[][] parcours_skip_block = new int[les_buts.size()][les_buts.size()];
            for(int i=0 ; i< cpy.length;i++){
                for(int j=0; j< cpy.length; j++){
                    int sdf= (i==0 && j==0)? 9999: 0;

                    cpy[i][j] = (double) map_distance.get(les_buts.get(i)).get(etat.blocks.get(j)) ;//+ sdf;
                    parcours_skip_block[i][j] = (int) map_distance_no_block.get(les_buts.get(i)).get(etat.blocks.get(j));
                }
            }


/*
            * Evaluate min distance between block and goal
            * */

            int[][] best_matrix_combinaison = new int[les_buts.size()][les_buts.size()];


            int[][] temp = new int[cpy.length][cpy.length];
            for(int i=0 ; i< cpy.length;i++){
                for(int j=0; j< cpy.length; j++){
                    temp[i][j] =  (int)cpy[i][j];
                }
            }
            best_matrix_combinaison = HungarianAlgorithm.computeAssignments(temp);
            min_distance=0;
            for(int i =0 ; i< les_buts.size(); i++){
                min_distance += temp[best_matrix_combinaison[i][0]][best_matrix_combinaison[i][1]];
                Case block = etat.blocks.get(i);
            }






/*
            * Get min distance player
            * */


            best_parcours_player = Integer.MAX_VALUE;
            if(les_buts.size() ==1 ){
                best_parcours_player =0;
            }else {

                blocks_restant = new HashSet<Integer>();
                for(int i=0; i<etat.blocks.size(); i++){

                    int associated_goal = getAssociatedGoal(best_matrix_combinaison,i);
                    if(temp[associated_goal][i] !=0){
                        blocks_restant.add(i);
                    }
                }


                Case block = etat.blocks.get(chosen_block);

                //for (Integer block : blocks_restant) {

                    int[][] parcours_matrix = getParcoursMatrix(parcours_skip_block, best_matrix_combinaison, chosen_block);
                    if(parcours_matrix.length <= 0){
                        System.out.println("");
                    }

                    int[][] parcours_matrix_match = HungarianAlgorithm.computeAssignments(parcours_matrix);
                    int parcours = 0;
                    for (int j = 0; j < parcours_matrix.length; j++) {
                        best_parcours_player += parcours_matrix[parcours_matrix_match[j][0]][parcours_matrix_match[j][1]];
                    }

                  /*  if (best_parcours_player > parcours) {
                        best_parcours_player = parcours;
                        chosen_block = block;
                    }*/



                //}
            }
            if(best_parcours_player == Integer.MAX_VALUE){
                best_parcours_player =0;
            }


            etat.cible = chosen_block;
            Case chosen = etat.blocks.get(chosen_block);

            best_distance_player = distance(etat.bonhomme, chosen);
            /*if(etat.last_action_move_block){
                best_distance_player =  distance(etat.bonhomme,chosen);
            }else{
                cleanGrid();
                setGridWithSymbole(grid,etat.blocks,'$');
                grid[chosen.x][chosen.y] = chosen;
                grid[chosen.x][chosen.y].symbole = ' ';

                best_distance_player =  AstarHangar.distance_player_block(etat.bonhomme,chosen,grid);
                //best_distance_player =  distance(etat.bonhomme,chosen);
                if(best_distance_player > 1000){
                    // System.out.println("dfdf");
                    //paranoid check
                    best_distance_player =  distance(etat.bonhomme,chosen);
                }
            }
*/
            cleanGrid();



            setGridWithSymbole(grid,etat.blocks,' ');
            etat.last_min_blocks_distance = min_distance;
            etat.last_min_parcourt = best_parcours_player;


            int a=0;
            for(Case block : etat.blocks){
                if(les_buts.contains(block)){
                    a++;
                }
            }
            if(a > 2){
                int t=0;
            }


        }else {
            min_distance = etat.last_min_blocks_distance ;
            best_parcours_player = etat.last_min_parcourt;
        }



        double h =  min_distance +best_parcours_player + best_distance_player;

        return h;
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


    private int[][] getParcoursMatrix(int[][] min_matrix, int[][]  matches_matrix,int chosen_block){


        if(blocks_restant.size() <1){
            return new int[1][1];
        }

        int[][] parcours_mat = new int[min_matrix.length][min_matrix.length];



        for( int j=0; j< min_matrix.length;j++){

            int associate_goal = getAssociatedGoal(matches_matrix,j);

            int i2=0;
            for(int i= 0;  i < min_matrix.length; i++ ){
                i2 = (i > associate_goal)? i-1 : i;

                parcours_mat[i2][j] = min_matrix[i][j];
            }
        }

        int grid_size = blocks_restant.size() ;
        int[][] parcours_restant = new int[grid_size][grid_size];
        int block_index=0;
        for(Integer block : blocks_restant){
            if(block != chosen_block){
                for(int j=0; j< grid_size; j++){
                    parcours_restant[j][block_index] = parcours_mat[j][block];
                }

                block_index++;
            }


        }


        if(parcours_restant.length ==0){
           return new int[1][1];

        }

        return parcours_restant;

    }

    private int getAssociatedGoal(int[][] matrix, int block){
        int associated_goal =0;
        for(int i=0; i< matrix.length; i++){
            if(matrix[i][1] == block){
                return matrix[i][0];
            }
        }
        return associated_goal;
    }


    private int distance(Case start, Case to){

        int D =0;
        D = Math.abs(start.x - to.x) + Math.abs(start.y - to.y);

        return D;
    }

/*

    private int distance_player_block(Case player, Case to){

        PriorityQueue<Case> open = new PriorityQueue<Case>(350, new Comparator<Case>() {
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


        Case current;

        while (!open.isEmpty()){

            current = open.poll();


            if(current.equals(to)){
                total_move =0;
                Case path = current;
                while (path!= null){
                    path = path.parent;
                    total_move = (path!=null)? total_move+1 : total_move ;
                }

                return total_move;
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

        return  grid[x][y].symbole == ' ' || grid[x][y].symbole == '.'; //|| grid[x][y].symbole == '$';
    }
*/


    private Case[][] copyGrid(Case[][] grid_to_cpy, Case start){
        Case[][] cpy_grid = new Case[grid_to_cpy.length][grid_to_cpy[0].length];

        for(int i=0; i< grid_to_cpy.length; i++){
            for(Case c: grid_to_cpy[i]){
                if( c != null)
                    cpy_grid[c.x][c.y] = new Case(c);
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
                System.out.print(c);
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
            if(c !=null){
                if(clean_grid[c.x][c.y] == null){
                    System.out.println("ds");
                }
                clean_grid[c.x][c.y].symbole = sym;
            }
        }
    }

}
