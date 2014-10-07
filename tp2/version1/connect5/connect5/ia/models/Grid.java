package ia.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by MB on 9/30/2014.
 */
public class Grid {


    //   sud   est  sud/est    nord/est
    int dx[] = {1,    0,    1,       -1};
    int dy[] = {0,    1,    1,        1};



    Map<Integer,HashSet<Integer>> vector_map;

    private final int WIN = 10;
    private final int AXES = 4;
    private final int SEQ  = 5;


    public int nb_ligne;
    public int nb_col;
    public int[][] grid;

    public Grid(int nb_col, int nb_ligne, int[][] ref_grid){

        this.nb_col = nb_col;
        this.nb_ligne = nb_ligne;
        this.grid = ref_grid;
    }



    public int getLenght(){
        return grid.length;
    }

    public int evaluate(int player_color){

        int evaluation =0;
        boolean has_win= false;
        for(int i=0; i<nb_ligne; i++){
            for(int j=0; j< nb_col; j++){

                for(int d=0; d< 4 ; d++){

                    has_win = true;
                    for(int seq=0; seq < 5; seq++){
                        int xrow = i+ dx[d]*seq;
                        int ycol = j+ dy[d]*seq;

                        if(  !(in_grid(xrow,ycol) && grid[xrow][ycol] == player_color )){
                            has_win = false;
                            break;
                        }

                    }

                    if (has_win){
                        return WIN;
                    }

                }





            }
        }

/*        vector_map = new HashMap<Integer, HashSet<Integer>>();
        int opponent = (player_color == 1)? 2 : 1;
        boolean valid_vector = false;
         for(int i=0; i<nb_ligne; i++){
            for(int j=0; j< nb_col; j++){

                for(int d=0; d< AXES ; d++){

                    LinkedHashSet<Integer> vector = new LinkedHashSet<Integer>();

                    valid_vector = true;
                    boolean is_sequence = true;

                    for(int seq=0; seq < SEQ; seq++){
                        int xrow = i+ dx[d]*seq;
                        int ycol = j+ dy[d]*seq;

                        if( in_grid(xrow,ycol) && grid[xrow][ycol] != opponent){

                            if(grid[xrow][ycol] == player_color && is_sequence){
                                vector.add(xy_to_value(xrow,ycol));
                            }else {
                                is_sequence = false;
                            }



                        }else{
                            valid_vector = false;
                            break;
                        }


                    }

                    if (valid_vector){
                        vector_map.put()
                        // sauver le vecteur




                    }

                }





            }
        }*/




        return evaluation;
    }


    private boolean is_vector_bidirectionel(int pos, int axe){


        return false;
    }


    public int xy_to_value(int x, int y){
        return (x*nb_col+y);
    }

    public List<Integer> getPossibleMove(){
        List<Integer> moves = new ArrayList<Integer>();

        int x=0;
        int y=0;
        for(int i=0; i< nb_ligne; i++ ){
            for(int j=0; j< nb_col; j++){
                if(grid[i][j] ==0){
                    moves.add(i*nb_col+j);
                }
            }
        }


        return moves;
    }

    public boolean isFull(){
        for(int i=0; i< nb_ligne; i++ ){
            for(int j=0; j< nb_col; j++){
                if(grid[i][j] == 0){
                    return false;
                }
            }
        }


        return true;
    }

    public void play(int move, int player){
        int x = move/nb_col;
        int y = move%nb_col;

        grid[x][y] = player;


    }



    @Override
    public Object clone() {
        Grid cloned = null;
        try {
            int[][] cpy_grid = new int[nb_ligne][nb_col];
            for(int i=0; i< nb_ligne; i++ ){
                for(int j=0; j< nb_col; j++){
                    cpy_grid[i][j] = grid[i][j];
                }
            }
            cloned = new Grid(nb_col,nb_ligne,cpy_grid);
            return cloned;
        }catch (Exception e){
        }
        return cloned;
    }


    private boolean in_grid(int x,int y){
        if( !((x < nb_ligne && x >= 0) &&  ( y < nb_col && y >= 0))  ){
            return false;
        }

        return true;
    }
}
