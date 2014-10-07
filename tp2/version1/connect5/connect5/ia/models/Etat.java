package connect5.ia.models;

import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Position;

import java.util.*;

/**
 * Created by MB on 10/5/2014.
 */
public class Etat {

    public Grille grille;
    public int nbcol;
    public int nbligne;


              //   sud   est  sud/est    nord/est
    int dx[] = {1,    0,    1,       -1};
    int dy[] = {0,    1,    1,        1};
    final int AXES = 4;
    final int SEQ  = 5;

    byte[] one_dim;

    private final int  WIN = 100;

    public static GrilleVerificateur checker;

    public Etat(Grille grille){
        this.grille = grille;
        nbcol = grille.getData()[0].length;
        nbligne = grille.getData().length;

if(nbligne == 5 && nbcol ==7){
    int stop=0;
}

        one_dim= oneDimentionalArray(grille.getData());
    }



    public int evaluate(int player_color){

        byte[][] data = grille.getData();


        int evaluation =0;
        boolean has_win= false;
  /*      for(int i=0; i<nbligne; i++){
            for(int j=0; j< nbcol; j++){

                for(int d=0; d< 4 ; d++){

                    has_win = true;
                    for(int seq=0; seq < 5; seq++){
                        int xrow = i+ dx[d]*seq;
                        int ycol = j+ dy[d]*seq;

                        if(  !(in_grid(xrow,ycol) && data[xrow][ycol] == player_color )){
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
*/
        HashMap vector_map = new HashMap<Integer, HashSet<Integer>>();
        int opponent = (player_color == 1)? 2 : 1;
        boolean valid_vector = false;
         for(int i=0; i<nbligne; i++){
            for(int j=0; j< nbcol; j++){

                for(int d=0; d< AXES ; d++){

                    LinkedHashSet<Integer> vector = new LinkedHashSet<Integer>();

                    valid_vector = true;
                    boolean is_sequence = true;

                    for(int seq=0; seq < SEQ; seq++){
                        int xrow = i+ dx[d]*seq;
                        int ycol = j+ dy[d]*seq;

                        if( in_grid(xrow,ycol) && data[xrow][ycol] != opponent){

                            if(data[xrow][ycol] == player_color && is_sequence){
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
                        vector_map.put(xy_to_value(i,j),vector.size());
                        // sauver le vecteur




                    }

                }





            }
        }




        return evaluation;
    }




    public void play(int move,int player){

        grille.set(new Position(move / nbcol, move % nbcol), player);

        one_dim[move] = (byte)player;

    }

    public List<Integer> getNextMoves(){
        List<Integer> moves = new ArrayList<Integer>();

        for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    moves.add(l*nbcol+c);

        return moves;
    }

    public Grille getGrille(){
        return grille;
    }

    public int checkWinner(){

        int r = checker.determineGagnant(grille);
        int test = winner();

        if(r != test){
            System.out.println("nope pas pret   anwawer: "+r+"  mine: "+test);
            System.out.println(toStringOneDim(one_dim));
            System.out.println("--------------------");
            System.out.println(grille.toString());
        }

        return test;
    }

    public boolean isTerminal(){
        return grille.nbLibre() == 0;
    }

    @Override
    public Etat clone() {

        Grille new_grille = grille.clone();

        Etat cloned = new Etat(new_grille);
        cloned.setChecker(checker);

        return cloned;
    }



    public void setChecker(GrilleVerificateur checker){
        this.checker = checker;
    }


    public int xy_to_value(int x, int y){
        return (x*nbcol+y);
    }

    private boolean in_grid(int x,int y){
        if( !((x < nbligne && x >= 0) &&  ( y < nbcol && y >= 0))  ){
            return false;
        }

        return true;
    }


    private byte[] oneDimentionalArray(byte[][] grille){
        byte[] one_dim = new byte[nbcol * nbligne];
        //System.out.println("nb col:" +nbcol + "  nbligne: "+ nbligne);
        for(int l=0 ; l < nbligne ; l++){
            System.arraycopy(grille[l],0,one_dim,l*(nbcol),nbcol);
        }

        return one_dim;

    }

    public String toStringOneDim(byte[] data){
        char[] table = {'0', 'N', 'B' };
        String result = "" + nbligne + " " + nbcol+ "\n";

        int i=1;
        for(byte b : data){
            char c = (char)b;
            result += table[b];
            if(i % nbcol ==0){
                result += '\n';
            }
            i++;
        }

        /*for(byte[] b : data){
            char[] c = new char[b.length];
            for(int i=0;i<b.length;i++)
                c[i] = table[b[i]];
            result += new String(c);
            result += '\n';
        }*/
        return result;
    }

    public byte[] testByte(){

        String to_convert = "BBBNNNN00000" +
                "0B00N0000000" +
                "00B0N0000000" +
                "000BN0000000" +
                "0000B0000000" +
                "000000000000" +
                "000000000000" +
                "000000000000" +
                "000000000000" +
                "000000000000" +
                "000000000000" +
                "000000000000";


        Map<Character,Byte> convert = new HashMap<Character,Byte>();
        convert.put('0',(byte)0);
        convert.put('N',(byte)1);
        convert.put('B',(byte)2);


        char[] table = {'0', 'N', 'B' };

        byte[] to_test = new byte[nbcol * nbligne];

        for(int i=0; i< to_test.length; i++){
            to_test[i] = convert.get(to_convert.charAt(i));
        }
        return to_test;
    }


    public  int winner(){

        int winner =0;
        int last = 0;
        int DOWN = nbcol;
        int LEFT = 1;
        int DOWNLEFT = DOWN + LEFT;
        int TOPLEFT   = LEFT - DOWN;

        int[] direction = new int[]{DOWN,LEFT,DOWNLEFT,TOPLEFT};


        for(int i =0; i< one_dim.length; i++){
            for(int d=0; d< AXES; d++ ){
                boolean win = true;
                last = one_dim[i];
                if(d >1){
                    if(!(i%nbcol  <= SEQ-1 )){
                        break;
                    }
                }
                if(last !=0 ){
                    for(int seq = 0 ; seq < SEQ; seq++){
                        int next = i + direction[d]*seq;

                        if(next >= one_dim.length || next < 0 ){
                            win = false;
                            break;
                        }

                        if(one_dim[next] != last ){
                            win = false;
                            break;
                        }

                    }
                    if(win){
                        return last;
                    }

                }



            }

        }



        return 0;
    }


    public static void main(String[] args) {



    }



}
