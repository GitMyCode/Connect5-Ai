/* INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * Automne 2014 / TP1 - Algorithme A*
 * http://ericbeaudry.ca/INF4230/tp1/
 */
package sokoban;

import astar.Etat;
import sokoban.hungarian.Hungarian;

import java.util.*;

/**
 * Représente un but.
 */
public class But implements astar.But, astar.Heuristique {

    // À compléter.
    // Indice : les destinations des blocs.
    List<Case> les_buts;
    Map<Case,Map<Case,Integer>> map_distance;

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

    protected List<Integer> list_distances;

    protected Map<Integer,Map<Integer,Integer>> map_of_prority ;

    boolean passe = true;

    public But(List<Case> les_buts){
        this.les_buts = les_buts;
        matrix_distance = new int[les_buts.size()][les_buts.size()];


        map_of_prority = new HashMap<Integer, Map<Integer, Integer>>();
        for(int i=0; i< les_buts.size(); i++){
            map_of_prority.put(i,new HashMap<Integer, Integer>());
            for(int j=0; j< les_buts.size(); j++){
                map_of_prority.get(i).put(j,0);
            }
        }
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
        int best_distance_player = Integer.MAX_VALUE;
        int chosen_block =0;
        boolean changed_target = false;

        boolean goal_atteint = false;
        Case cible = null;
        for(int i=0; i< etat.blocks.size(); i++){
            Case block = etat.blocks.get(i);

            if(les_buts.contains(block)) {

                goal_atteint =true;
            }

            distance_player = distance(etat.bonhomme,block);
            // distance_player = distance_player_block(etat.bonhomme,block);
            if(distance_player < best_distance_player){
                chosen_block = i;
                cible = block;
                best_distance_player = distance_player;
            }



        }
        if(cible == null){
            return 0;
        }
        if(( etat.cible != chosen_block)){
            etat.cible = chosen_block;
            changed_target =true;
        }else{

        }
        best_distance_player--;


        if(changed_target || etat.last_action_move_block || etat.last_min == -1 ){
            double[][] cpy = new double[les_buts.size()][les_buts.size()];
            for(int i=0 ; i< cpy.length;i++){
                for(int j=0; j< cpy.length; j++){
                    cpy[i][j] = (double) map_distance.get(les_buts.get(i)).get(etat.blocks.get(j)) +
                                map_of_prority.get(i).get(j);
                }
            }
            //cpy[0][0] += 9999;

            int[][] matrix_distance = new int[cpy.length][cpy.length];
            for(int i=0 ; i< cpy.length;i++){
                for(int j=0; j< cpy.length; j++){
                    matrix_distance[i][j] =  (int)cpy[i][j];
                }
            }

            int[][] best_matrix_combinaison = HungarianAlgorithm.computeAssignments(matrix_distance);


            /*
            * Minimum distance entre les blocks et les buts
            * */
            min_distance=0;
            setGridWithSymbole(grid,etat.blocks,'$');
            chosen_block =0;
            best_distance_player =Integer.MAX_VALUE;

            goal_atteint = false; //pas propre pas propre
            for(int i =0 ; i< les_buts.size(); i++){

                int no_goal =  best_matrix_combinaison[i][0];
                int no_block = best_matrix_combinaison[i][1];

                min_distance += matrix_distance[no_goal][no_block];
                Case block = etat.blocks.get(i);
                grid[block.x][block.y].symbole = ' ';
                cleanGrid();
                grid[block.x][block.y].symbole = '$';

                distance_player = distance(etat.bonhomme,etat.blocks.get(no_block)) -1;
                if(distance_player< best_distance_player){
                    if(matrix_distance[no_goal][no_block] !=0){
                        chosen_block = no_block;
                        best_distance_player = distance_player;
                    }else{
                        //ATTENTION PAS BEAU
                        /*if(passe){
                            if(!puzzle_still_solvable(no_block,no_goal)){
                                int test = 9999;
                                map_of_prority.get(no_goal).put(no_block,test);
                            }

                        }
*/

                        goal_atteint = true;
                    }

                }


            }

            setGridWithSymbole(grid,etat.blocks,' ');


/*
distance player for each block
*/
            int distance1 = distance_player_block(etat.bonhomme,etat.blocks.get(0)); //distance(etat.bonhomme,etat.blocks.get(0)) -1; //
            int distance2 = distance_player_block(etat.bonhomme,etat.blocks.get(1)); // distance(etat.bonhomme,etat.blocks.get(1)) -1;

/*
 back parcour for each block after reach his goal
*/
            int goal1 = getAssociatedGoal(best_matrix_combinaison,0);
            int goal2 = getAssociatedGoal(best_matrix_combinaison,1);

            int ret_parcours1 =0;
            int cible1= 0;
            if(matrix_distance[goal1][0] ==0 ){
                distance1 = distance2;
                cible1 = 1;


            }else{

                ret_parcours1 = matrix_distance[goal1][1];
                ret_parcours1= (ret_parcours1 ==9999)? distance_player_block(les_buts.get(goal1),etat.blocks.get(1)) : ret_parcours1;
            }

            int ret_parcours2 =0;
            int cible2 =1;
            if(matrix_distance[goal2][1] == 0  ){
                cible2 = 0;
                distance2 = distance1;
            }else {

                ret_parcours2 = matrix_distance[goal2][0];
                ret_parcours2= (ret_parcours2 ==9999)? distance_player_block(les_buts.get(goal2),etat.blocks.get(0)) : ret_parcours2;
            }

            int choice1 = distance1 + ret_parcours1;
            int choice2 = distance2 + ret_parcours2;

/*
 on devrait choisir le block en fonction du meilleur parcourt
*/



        /*    if(ret_parcours1 > 5000 || ret_parcours2 > 5000){
                System.out.println("problem");
            }*/
            if(best_distance_player > 1000){
                System.out.print("");
            }


            int other_block = (chosen_block ==0) ? 1 : 0;
            int other_goal = getAssociatedGoal(best_matrix_combinaison,other_block);
            int chosen_goal = getAssociatedGoal(best_matrix_combinaison,chosen_block);


            int parcout = 0;
            if(!goal_atteint){


          parcout = matrix_distance[best_matrix_combinaison[other_goal][0]][other_block];
                if(parcout == 9999){
                    parcout = distance(les_buts.get(chosen_goal),etat.blocks.get(other_block));
                }

            }else{
                int ds=23;
            }

            int stop =0;

            if(choice1 < choice2){
                parcout = ret_parcours1;
                best_distance_player = distance1;
                etat.cible = cible1;
            }else {
                parcout = ret_parcours2;
                best_distance_player = distance2;
                etat.cible = cible2;
            }

            if(min_distance < 30){
                stop=2;
            }



/*
            * Mininum parcourt que le personnage devra faire
            *
  */
         /*   int moves_length = matrix_distance.length-1;

            int[][] matrix_deplacement = new int[moves_length][moves_length];
            for(int i=0; i< matrix_distance.length; i++){
                for(int j=0; j< matrix_distance.length; j++){



                    if(j > chosen_block){

                        if(best_matrix_combinaison[i][0] == i && best_matrix_combinaison[i][0] == j){

                        }
                        matrix_deplacement[i][j-1] = matrix_distance[i][j];




                    }else if (j < chosen_block){
                        matrix_deplacement[i][j] = matrix_distance[i][j];
                    }
                }
            }*/

     /*       for(int i=0; i< matrix_distance.length; i++){
                int t = best_matrix_combinaison[i][0];
                for(int j=0; j< matrix_distance.length; j++){




                }
            }
*/









/*


            List<Integer> done = new ArrayList<Integer>();
            done.add(chosen_block);
            int parcout_distance =0;




            for(int i=0; i< les_buts.size() && done.size() != les_buts.size(); i++){
                int associate_goal = getAssociatedGoal(best_matrix_combinaison,chosen_block);

                int smallest_here =Integer.MAX_VALUE;
                int next_chosen_block =0;
                for(int j=0; j< etat.blocks.size(); j++){
                    if(!done.contains(j)) {
                        if(les_buts.contains(etat.blocks.get(j))){
                            smallest_here =0;
                            done.add(j);
                        }else{
                            int distance_goal_block = matrix_distance[associate_goal][j];
                            if(distance_goal_block == 9999){
                                //distance_goal_block = distance_player_block(les_buts.get(associate_goal),etat.blocks.get(j)) ;
                                distance_goal_block = distance(les_buts.get(associate_goal),etat.blocks.get(j));

                                if(distance_goal_block == 9999){
                                    System.out.println("");
                                }
                            }
                            if (smallest_here > distance_goal_block) {
                                smallest_here = distance_goal_block -2;
                                next_chosen_block = j;
                            }
                        }

                    }
                }
                if(smallest_here > 500){
                    System.out.println("ouin");
                }
                done.add(chosen_block);
                chosen_block = next_chosen_block;
                parcout_distance+=  smallest_here;
            }


     if(parcout_distance > min_distance){
                System.out.println("d");
            }
*/

            if(parcout > 5000){
                System.out.println("prob");
            }


            etat.last_min = min_distance + parcout;
            min_distance = min_distance + parcout;
            etat.last_min = min_distance ;
        }else {
            min_distance = etat.last_min;
        }




        if(best_distance_player > 1000){
            System.out.print("");
        }

        double h =  min_distance + best_distance_player;//((min_distance + (min_distance/les_buts.size()-1))) + best_distance_player;

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

    private boolean puzzle_still_solvable(int index_block,int index_last_goal_reached){
        Case block = current_state.blocks.get(index_block);
        Case goal = les_buts.get(index_last_goal_reached);


        setGridWithSymbole(grid,current_state.blocks,'$');
        cleanGrid();
        for(int i=0; i< les_buts.size();i++){
            if(i != index_last_goal_reached){
                boolean blocked = true;
                for(int j=0; j< current_state.blocks.size(); j++){
                    if( j != index_block){
                        int res = AstarOutils.distance_moves_for_a_block(current_state.blocks.get(j),les_buts.get(i),grid);
                        cleanGrid();

                        if(res != 9999){
                            blocked = false;
                        }
                    }
                }

                if(blocked){
                    return false;
                }


            }

        }
        return true;

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




        while (!open.isEmpty()){

            Case current = open.poll();


            if(current.equals(to)){
                total_move =0;
                Case path = current;
                while (path!= null){
                    path = path.parent;
                    total_move = (path!=null)? total_move+1 : total_move ;
                }

                               /* if(total_move <= 0){
                    System.out.println(" ");
                }*/
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

        return  grid[x][y].symbole == ' ' || grid[x][y].symbole == '.' || grid[x][y].symbole == '$' ;
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

        char no= '0';
        for(Case c : e.blocks){
            print.get(c.x).set(c.y,no);
            no++;
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
