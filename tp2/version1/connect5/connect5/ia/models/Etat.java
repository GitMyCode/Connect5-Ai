package connect5.ia.models;

import connect5.Grille;
import connect5.GrilleVerificateur;

import java.util.*;

/**
 * Created by MB on 10/5/2014.
 */
public class Etat {

    public Grille grille;
    public int nbcol;
    public int nbligne;

    int MAX_player;
    int MIN_player;

    final int  power2[] = {1,2,4,8,16,32};
    final int AXES = 4;
    final int SEQ  = 5;


    public int nb_libre;






    public byte[] one_dim;
    public int score;
    public int bestMove;
    public Integer lowerBound = null;
    public Integer upperBound = null;

    public LinkedList<Vector5> vector5MAX = new LinkedList<Vector5>();
    public LinkedList<Vector5> vector5MIN = new LinkedList<Vector5>();

    public final int  WIN = 10000;

    public static GrilleVerificateur checker;

    public Etat(byte[] one_dim, int max,int min){
        this.grille = grille;
        nbcol = GLOBAL.NBCOL;
        nbligne = GLOBAL.NBLIGNE;

        MAX_player = max;
        MIN_player = min;

        this.one_dim = one_dim;
    }
    public Etat(Grille grille, int max,int min){
        this.grille = grille;
        nbcol = GLOBAL.NBCOL;
        nbligne = GLOBAL.NBLIGNE;

        MAX_player = max;
        MIN_player = min;

        this.one_dim = one_dim;



        one_dim= oneDimentionalArray(grille.getData());
    }




    @Override
    public Etat clone() {

        //Grille new_grille = grille.clone();
        byte[] cloned_array = cloneByteArray(this.one_dim);
        Etat cloned = new Etat(cloned_array,MAX_player,MIN_player);
        cloned.setChecker(checker);

        return cloned;
    }

    private byte[] cloneByteArray(byte[] array){
        byte[] one_dim = new byte[array.length];
        //System.out.println("nb col:" +nbcol + "  nbligne: "+ nbligne);
        System.arraycopy(array,0,one_dim,0,array.length);
        return one_dim;
    }


    @Override
    public int hashCode() {

        int result =3;
        for(int i=0;i<one_dim.length;i++){
            result = result * 7 + one_dim[i];
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this ==(Etat) obj){
            return true;
        }

        Etat etat_b = (Etat) obj;
        if(one_dim.length != etat_b.one_dim.length ){
            return false;
        }

        for(int i=0;i< one_dim.length; i++){
            if(one_dim[i] != etat_b.one_dim[i]){
                return false;
            }
        }

        return true;
    }

    public void play(int move,int player){

//        grille.set(new Position(move / nbcol, move % nbcol), player);

        one_dim[move] = (byte)player;
    }
    public void unplay(int move){

        //  grille.set(new Position(move / nbcol, move % nbcol), 0);

        one_dim[move] = 0;
    }

    public PriorityQueue<Move> getNextMoves(int player_to_max){

        int lowestX = Integer.MAX_VALUE ;
        int lowestY = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;


        int nblibre =0;

        int a=0;
        for(byte b : one_dim){


            int pos_x =  ((a/GLOBAL.NBCOL) ) ;
            int pos_y = ((a%GLOBAL.NBCOL) ) ;


            if(b != 0 ){
                lowestX = (lowestX > pos_x)? pos_x : lowestX;
                lowestY = (lowestY > pos_y)? pos_y : lowestY;
                highestX= (highestX < pos_x)? pos_x: highestX;
                highestY= (highestY < pos_y)? pos_y : highestY;

            }
            if(b == 0){
                nblibre++;
            }
            a++;
        }


        PriorityQueue<Move> ordered_move;
        if(player_to_max == MIN_player) {
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMIN());
        }else{
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMAX() );
        }


        int buffer = (one_dim.length > 90)? 1: 3;

        for(int i =0; i< one_dim.length; i++){

            int pos_x =  ((i/GLOBAL.NBCOL) ) ;
            int pos_y = ((i%GLOBAL.NBCOL) );

            if( (pos_x >= lowestX-buffer) && (pos_x <= highestX+buffer ) && (pos_y  >= lowestY -buffer) && (pos_y <= highestY +buffer  ) ){
                if(one_dim[i] ==0){
                    play(i,player_to_max);
                    Move a_move =  new Move(i,evaluate(player_to_max));
                    ordered_move.add(a_move);
                    unplay(i);
                }
            }
        }


        return ordered_move;
    }



    public class CompareMAX implements Comparator<Move>{
        @Override
        public int compare(Move o1, Move o2) {

            if(o1.score > o2.score){
                return -1;
            }
            if(o1.score < o2.score){
                return 1;
            }
            return 0;
        }
    }

    public class CompareMIN implements Comparator<Move>{
        @Override
        public int compare(Move o1, Move o2) {

            if(o1.score < o2.score){
                return -1;
            }
            if(o1.score > o2.score){
                return 1;
            }
            return 0;
        }
    }




    public Grille getGrille(){
        return grille;
    }

    public int evaluate(int player){

        return evaluate5(player);

    }

    public int checkWinner(){
        return checker.determineGagnant(grille);
    }

    public boolean isTerminal(){



        return getNblibre() == 0;
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
        char[] table = {'-', 'N', 'B' };
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

        return result;
    }

    public void print_all_vector(List<Vector5> all){

        for(Vector5 v : all){
            if(v.value > 0){
                System.out.println("---- value: "+v.value+ "   bi: "+v.bidirectionnel+"         ----");
              //  System.out.println(toStringVector(one_dim,v.tab_seq));
            }

        }

    }

    public int evaluate5(int player){

        int evaluation =0;
        Vector5[][] memo = new Vector5[AXES][one_dim.length];
        ArrayList<Vector5> allVectorMax = new ArrayList<Vector5>();
        ArrayList<Vector5> allVectorOpponent = new ArrayList<Vector5>();

        boolean playe1Won =false;
        boolean player2Won =false;

        for(int i =0; i< one_dim.length; i++){
            for(Dir D : Dir.direction4){

                if(!D.boundaries(i,5)){
                    continue;
                }

                //START loop
                int res = one_dim[i]|one_dim[i+ D.v(1)]|one_dim[i+ D.v(2)]|one_dim[i+ D.v(3)]|one_dim[i+ D.v(4)];

                if(res== 1 || res == 2){ //


                    Dir.Axes axe = Dir.Axes.getA(D);
                    /*We must not count vector that have more than 5*/
                    if(memo[axe.i][i]!=null && memo[axe.i][i].moreThan5){
                        continue;
                    }


                    Vector5 new_vector = new Vector5();
                    new_vector.Direction = D;

                    int nb_seqt=0;
                    if(one_dim[i + D.v(0)] == res){
                        nb_seqt += 1<<0;
                    }
                    if(one_dim[i + D.v(1)] == res){
                        nb_seqt += 1<<1;
                    }
                    if(one_dim[i + D.v(2)] == res){
                        nb_seqt += 1<<2;
                    }
                    if(one_dim[i + D.v(3)] == res){
                        nb_seqt += 1<<3;
                    }
                    if(one_dim[i + D.v(4)] == res){
                        nb_seqt += 1<<4;
                    }



                    int vecteur_value = Integer.bitCount(nb_seqt);
                    if(vecteur_value == 5){

                        /*TODO there is a chance where we return before checking if the other player won*/
                        if(D.boundaries(i,6) && one_dim[i+ D.v(5)] == res){
                            new_vector.moreThan5= true;
                            Vector5 old_ref;
                            for(int v=0;v<6;v++){
                                if( (nb_seqt&(power2[v])) != 0){
                                    old_ref = memo[axe.i][i + D.v(v)];
                                    if(old_ref != null && !old_ref.moreThan5){
                                        old_ref.value--;
                                    }
                                    memo[axe.i][i + D.v(v)] = new_vector;
                                }
                            }
                            continue;
                        }else{
                            if(res == 1){
                                playe1Won = true;
                            }else {
                                player2Won = true;
                            }
                            //return (res == MAX_player)? GLOBAL.WIN : 0- GLOBAL.WIN;
                        }


                    }

                    /*Check if they are next to each other  ->   01110 :Yes  01011 : No*/
                    if((5-  (Integer.numberOfLeadingZeros(nb_seqt)-27)) - Integer.numberOfTrailingZeros(nb_seqt) == vecteur_value  ){
                       new_vector.isCorded = true;
                    }
                    /*Check if they there is free space on the two side. If yes we can assume that we could put a least one more
                    * before being blocked. So we do + 1   */
                    if(isBidirectionnel(i,i+D.v(4),D)){
                        new_vector.bidirectionnel = true;
                        new_vector.valueBirdirection = vecteur_value+1;
                    }else {
                        new_vector.valueBirdirection = vecteur_value;
                    }


                    Vector5 old_ref;
                    new_vector.value = vecteur_value;

                    /*Update the memo[][] to avoid count two vector in the same place same direction*/
                    for(int v=0;v<5;v++){
                        if( (nb_seqt&(power2[v])) != 0){
                            old_ref = memo[axe.i][i + D.v(v)];
                            if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {
                                if (old_ref != null) old_ref.value--;
                                memo[axe.i][i + D.v(v)] = new_vector;
                            }else {
                                new_vector.valueBirdirection--;
                                new_vector.value--;
                            }
                        }
                    }

                    /*Just for printing when debugging*/
/*
                    new_vector.tab_seq[0] = i + D.v(0);
                    new_vector.tab_seq[1] = i + D.v(1);
                    new_vector.tab_seq[2] = i + D.v(2);
                    new_vector.tab_seq[3] = i + D.v(3);
                    new_vector.tab_seq[4] = i + D.v(4);
*/


                    if(new_vector.value >0){
                        if(res == MAX_player){
                            allVectorMax.add(new_vector);
                        }else{
                            allVectorOpponent.add(new_vector);
                        }
                    }
                }
            }
        }



        if(playe1Won && player==1 && !player2Won){
            return (MAX_player == 1)? GLOBAL.WIN: -GLOBAL.WIN;
        }

        if(player2Won && player==2 && !playe1Won){
            return (MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN;
        }
        int higthestMAX =0;

        for(Vector5 s: allVectorMax){
            if(false){ // special case for win
                return GLOBAL.WIN;
                //evaluation += WIN;
            }else {
                int value = (s.bidirectionnel)? s.value+1 : s.value;
                evaluation += Math.pow(value,4);
                higthestMAX = (value > higthestMAX && s.isCorded)? value : higthestMAX;

            }
        }
        int higthestMIN =0;
        for(Vector5 s: allVectorOpponent){
            if(false){ // special case for win
                return GLOBAL.WIN;
                //evaluation += WIN;
            }else {
                int value = (s.bidirectionnel)? s.value+1 : s.value;
                evaluation -= Math.pow(value,4);
                higthestMIN = (value > higthestMIN && s.isCorded)? value : higthestMIN;
            }
        }

        /* This check is for the situation when we are sure to win */
        if(((one_dim.length-1) - getNblibre()) % 2 ==0 && player ==1){ // Au tour du joueur 2 a jouer apres


            int maxScore = (MAX_player == 2)? higthestMAX : higthestMIN;
            int oppScore = (MAX_player == 2)? higthestMIN : higthestMAX;

            if(player2Won){
                return (MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN;
            }

            if(maxScore > 3 && maxScore >= oppScore){
                return (MAX_player == 2)? GLOBAL.ALMOST_WIN + evaluation  : -GLOBAL.ALMOST_WIN +evaluation;
            }

        }else if( ((one_dim.length-1)- getNblibre()) % 2 == 1 && player == 2 ){ // Si au tour du joueur 1 apres
            int maxScore = (MAX_player == 1)? higthestMAX : higthestMIN;
            int oppScore = (MAX_player == 1)? higthestMIN : higthestMAX;

            if(playe1Won){
                return (MAX_player ==1)? GLOBAL.WIN : -GLOBAL.WIN;
            }

            if(maxScore > 3 &&  maxScore >= oppScore){
                return (MAX_player == 1)? GLOBAL.ALMOST_WIN + evaluation : -GLOBAL.ALMOST_WIN + evaluation;
            }
        }


        return evaluation;

    }

    public int evaluate4(int player){

        int opponent = (player == 1)? 2:1;
        int evaluation =0;
        int last = 0;



        Vector5[][] memo = new Vector5[AXES][one_dim.length];


        ArrayList<Vector5> all_vector = new ArrayList<Vector5>();
        for(int i =0; i< one_dim.length; i++){
            for(Dir D : Dir.direction4){

                if(!D.boundaries(i,5)){
                    continue;
                }

                //START loop
                if(D.checkPossibleConnect(one_dim,i,player)){ //


                    Dir.Axes axe = Dir.Axes.getA(D);

                    Vector5 new_vector = new Vector5();
                    new_vector.Direction = D;

                    int nb_seqt=0;
                    if(one_dim[i + D.v(0)] == player){
                        nb_seqt += 1<<0;
                    }
                    if(one_dim[i + D.v(1)] == player){
                        nb_seqt += 1<<1;
                    }
                    if(one_dim[i + D.v(2)] == player){
                        nb_seqt += 1<<2;
                    }
                    if(one_dim[i + D.v(3)] == player){
                        nb_seqt += 1<<3;
                    }
                    if(one_dim[i + D.v(4)] == player){
                        nb_seqt += 1<<4;
                    }


                    int vecteur_value = Integer.bitCount(nb_seqt);
                    if(vecteur_value == 5){
                        return GLOBAL.WIN;
                    }


                    if(isBidirectionnel(i,i+D.v(4),D)){
                        new_vector.bidirectionnel = true;
                        new_vector.valueBirdirection = vecteur_value+1;
                    }else {
                        new_vector.valueBirdirection = vecteur_value;
                    }
                    Vector5 old_ref;
                    new_vector.value = vecteur_value;
                    if( (nb_seqt&1) != 0){
                        old_ref = memo[axe.i][i];
                        if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {

                            if (old_ref != null) old_ref.value--;
                            memo[axe.i][i] = new_vector;
                        }else {
                            new_vector.valueBirdirection--;
                            new_vector.value--;
                        }
                    }

                    if( (nb_seqt&2) != 0){
                        old_ref = memo[axe.i][i + D.v(1)];
                        if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {

                            if (old_ref != null) old_ref.value--;
                            memo[axe.i][i + D.v(1)] = new_vector;
                        }else {
                            new_vector.valueBirdirection--;
                            new_vector.value--;
                        }

                    }if( (nb_seqt&4) != 0){
                        old_ref = memo[axe.i][i + D.v(2)];
                        if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {

                            if (old_ref != null) old_ref.value--;
                            memo[axe.i][i + D.v(2)] = new_vector;
                        }else {
                            new_vector.valueBirdirection--;
                            new_vector.value--;
                        }

                    }if( (nb_seqt&8) != 0){
                        old_ref = memo[axe.i][i + D.v(3)];
                        if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {

                            if (old_ref != null) old_ref.value--;
                            memo[axe.i][i + D.v(3)] = new_vector;
                        }else {
                            new_vector.valueBirdirection--;
                            new_vector.value--;
                        }

                    }if( (nb_seqt&16) != 0){
                        old_ref = memo[axe.i][i + D.v(4)];
                        if(old_ref ==null || old_ref.valueBirdirection <= new_vector.valueBirdirection ) {

                            if (old_ref != null) old_ref.value--;
                            memo[axe.i][i + D.v(4)] = new_vector;
                        }else {
                            new_vector.valueBirdirection--;
                            new_vector.value--;
                        }


                    }

/*
                    new_vector.tab_seq[0] = i + D.v(0);
                    new_vector.tab_seq[1] = i + D.v(1);
                    new_vector.tab_seq[2] = i + D.v(2);
                    new_vector.tab_seq[3] = i + D.v(3);
                    new_vector.tab_seq[4] = i + D.v(4);
*/


                    if(new_vector.value >0){
                        all_vector.add(new_vector);
                    }
                }
            }
        }

        for(Vector5 s: all_vector){
            if(false){ // special case for win
                return GLOBAL.WIN;
                //evaluation += WIN;
            }else {
                if(s.bidirectionnel){
                    evaluation += Math.pow(s.value+1,4);
                }else {
                    evaluation += Math.pow(s.value,4);
                }

            }
        }

        return evaluation;

    }

    public boolean isBidirectionnel(int index,int last_index, Dir D) {
        int step;
        if (one_dim[index] != 0) {

            step = index + D.opp().v();
            if ( !D.opp().boundaries(step,5) || one_dim[step] != 0) {
                return false;
            }
        }


        //Check step foward
        if (one_dim[last_index] != 0){

            /*To check if there is space for one more*/
            if(!D.boundaries(index + D.v(),5))
                return false;

            step = last_index + D.v();

            if (one_dim[step] != 0) {
                return false;
            }
        }
        return true;
    }



    public int getNblibre(){
        int nblibre=0;
        for(byte b: one_dim){
            if(b ==0){
                nblibre++;
            }
        }
        return nblibre;
    }

    class Vector5 {
        public boolean isCorded = false;
        public boolean moreThan5 = false;
        public int value;
        public int valueBirdirection;
        public boolean bidirectionnel = false;
        public Dir Direction;
        //public int[] tab_seq = new int[5];

    }



    public String toStringVector(byte[] data_original, int[] vector){



        byte[] data = new byte[nbcol *nbligne];
        System.arraycopy(data_original,0,data,0,data_original.length);

        char[] table = {'_', 'N', 'B','X' };
        String result = "" + nbligne + " " + nbcol+ "\n";
        for(int j=0; j< vector.length; j++){
            data[vector[j]] = 3;
        }


        int i=1;
        for(byte b : data){
            char c = (char)b;
            result += table[b];
            if(i % nbcol ==0){
                result += '\n';
            }
            i++;
        }

        return result;

    }


    public static void main(String[] args) {



    }



}
