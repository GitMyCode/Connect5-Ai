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

    int MAX_player;
    int MIN_player;


    //   sud   est  sud/est    nord/est
    int dx[] = {1,    0,    1,       -1};
    int dy[] = {0,    1,    1,        1};
    final int AXES = 4;
    final int SEQ  = 5;

    final int DOWN = nbcol;
    final int TOP  = 0-nbcol;
    final int LEFT = -1;
    final int RIGHT = 1;

    final int DOWNLEFT = DOWN + LEFT;
    final int TOPLEFT   = TOP + LEFT;
    final int DOWNRIGHT = DOWN + RIGHT;
    final int TOPRIGHT = TOP + RIGHT;

    final int HORIZONTAL = 0;
    final int VERTICAL = 1;
    final int DIAGL    = 2;
    final int DIAGR    = 3;

    public int nb_libre;


    final int[] direction4 = new int[]{DOWN,LEFT,DOWNLEFT,TOPLEFT};
    final int[] direction8 = new int[]{DOWN,LEFT,TOP,RIGHT,DOWNLEFT,TOPLEFT,TOPRIGHT, DOWNRIGHT};




    public byte[] one_dim;
    public int score;


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
        Etat etat_b = (Etat) obj;
        for(int i=0;i< one_dim.length; i++){
            if(one_dim[i] != etat_b.one_dim[i]){
                return false;
            }
        }

        return true;
    }

    public int evaluate23(int player_color){

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

//        grille.set(new Position(move / nbcol, move % nbcol), player);

        one_dim[move] = (byte)player;
    }
    public void unplay(int move){

        //  grille.set(new Position(move / nbcol, move % nbcol), 0);

        one_dim[move] = 0;
    }




    public PriorityQueue<Move> getNextMoves(int player_to_max){
        List<Integer> moves = new ArrayList<Integer>();
/*

        for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    moves.add(l*nbcol+c);

*/

        int nblibre =0;
        for(byte b : one_dim){
            if(b == 0){
                nblibre++;
            }
        }



        PriorityQueue<Move> ordered_move;
        if(player_to_max == MIN_player) {
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMIN());
        }else{
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMAX() );
        }

        for(int i =0; i< one_dim.length; i++){
            if(one_dim[i] ==0){
                play(i,player_to_max);
                Move a_move =  new Move(i,evaluate());
                ordered_move.add(a_move);
                unplay(i);


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

    public int evaluate(){


/*
        int max_res = evaluate4(MAX_player);
        int min_res = evaluate4(MIN_player);
        if(max_res == GLOBAL.WIN){
            return GLOBAL.WIN;
        }else if(min_res == GLOBAL.WIN){
            return 0-GLOBAL.WIN;
        }
*/

        return evaluate5();
/*        //int r = checker.determineGagnant(grille);
        result = checker.determineGagnant(grille); // winner();
*//*
        if(r != result){
            System.out.println("nope pas pret   anwawer: "+r+"  mine: "+result);
            System.out.println(toStringOneDim(one_dim));
            System.out.println("--------------------");
            System.out.println(grille.toString());
        }*//*

        if(result ==0){
            result = evaluate2(player);
        }else{
            result = WIN;
        }
        return result;*/
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

        /*for(byte[] b : data){
            char[] c = new char[b.length];
            for(int i=0;i<b.length;i++)
                c[i] = table[b[i]];
            result += new String(c);
            result += '\n';
        }*/
        return result;
    }

    public void print_all_vector(List<Vector5> all){

        for(Vector5 v : all){
            if(v.value > 0){
                System.out.println("---- value: "+v.value+ "   bi: "+v.bidirectionnel+"         ----");
                System.out.println(toStringVector(one_dim,v.tab_seq));
            }

        }

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

    public int evaluate5(){

        int evaluation =0;
        int last = 0;



        Vector5[][] memo = new Vector5[AXES][one_dim.length];


        ArrayList<Vector5> allVectorMax = new ArrayList<Vector5>();
        ArrayList<Vector5> allVectorOpponent = new ArrayList<Vector5>();
        for(int i =0; i< one_dim.length; i++){
            for(Dir D : Dir.direction4){

                if(!D.boundaries5(i)){
                    continue;
                }

                //START loop
                int res = one_dim[i]|one_dim[i+ D.v(1)]|one_dim[i+ D.v(2)]|one_dim[i+ D.v(3)]|one_dim[i+ D.v(4)];



                int test = Integer.bitCount(res);



                if(res== 1 || res == 2){ //


                    Dir.Axes axe = Dir.Axes.getA(D);

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
                        return (res == MAX_player)? GLOBAL.WIN : 0- GLOBAL.WIN;
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

                    new_vector.tab_seq[0] = i + D.v(0);
                    new_vector.tab_seq[1] = i + D.v(1);
                    new_vector.tab_seq[2] = i + D.v(2);
                    new_vector.tab_seq[3] = i + D.v(3);
                    new_vector.tab_seq[4] = i + D.v(4);


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

        for(Vector5 s: allVectorMax){
            if(false){ // special case for win
                return GLOBAL.WIN;
                //evaluation += WIN;
            }else {
                if(s.bidirectionnel){
                    evaluation += Math.pow(s.value+1,5);
                }else {
                    evaluation += Math.pow(s.value,5);
                }

            }
        }

        for(Vector5 s: allVectorOpponent){
            if(false){ // special case for win
                return GLOBAL.WIN;
                //evaluation += WIN;
            }else {
                if(s.bidirectionnel){
                    evaluation -= Math.pow(s.value+1,5);
                }else {
                    evaluation -= Math.pow(s.value,5);
                }

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

                if(!D.boundaries5(i)){
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

                    new_vector.tab_seq[0] = i + D.v(0);
                    new_vector.tab_seq[1] = i + D.v(1);
                    new_vector.tab_seq[2] = i + D.v(2);
                    new_vector.tab_seq[3] = i + D.v(3);
                    new_vector.tab_seq[4] = i + D.v(4);


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
            if ( !D.opp().boundaries5(step) || one_dim[step] != 0) {
                return false;
            }
        }


        //Check step foward
        if (one_dim[last_index] != 0){

            /*To check if there is space for one more*/
            if(!D.boundaries5(index+D.v()))
                return false;

            step = last_index + D.v();

            if (one_dim[step] != 0) {
                return false;
            }
        }
        return true;
    }


    public int evaluate3(int player){

        int opponent = (player == 1)? 2:1;
        int evaluation =0;
        int last = 0;



        Vector5[][] memo = new Vector5[AXES][one_dim.length];


        ArrayList<Vector5> all_vector = new ArrayList<Vector5>();
        for(int i =0; i< one_dim.length; i++){
            for(Integer D : Direction.direction4){
                boolean sequence = true;
                last = one_dim[i];


                if(Direction.side_map.get(D) !=null && Direction.side_map.get(D) == Direction.OUEST){
                    if(!(((i%nbcol)+1)  >=5)){
                        continue;
                    }
                }else
                if(Direction.side_map.get(D) != null && Direction.side_map.get(D) == Direction.EST){
                    if(!((nbcol - (i%nbcol))  >=5)){
                        continue;
                    }
                }




                //START loop
                int ref_last_axes =-1;
                if(last == player){ //

                    int nb_seq = 0;


                    int axe = Direction.axes_map.get(D);
                    //loop to find suite
                    Vector5 vector = new Vector5();

                    int nb_overlap =0;
                    Vector5 ref = null;
                    for(int s = 0 ; s < SEQ; s++){

                        int next = i + D*s;


                        /*TODO
                        * Ce check va empecher de compter un cas come  *0****
                        * */
                        /*Check if time to leave this for*/
                        if(next >= one_dim.length || next < 0 ){
                            sequence = false;
                            break;
                        }
                        if(one_dim[next] == opponent){

                            sequence = false;
                            break;
                        }


                        vector.tab_seq[s] = next;

                        /*Okey we got one valid token */
                        if(one_dim[next] == player ){

                            vector.value++;
                            ref_last_axes = next;

                            int[][] test = new int[5][5];
                            for(Dir d : Dir.values()){

                            }


                            if(memo[axe][next] == null) {
                                memo[axe][next] = vector;
                            }else {
                                ref = memo[axe][next];
                                nb_overlap++;

                            }
                        }

                    }
                    //END VECTOR

                    //there is space for a 5 sequence
                    if(!sequence){
                        vector.value = 0;
                    }else{


                        if(ref != null){
                            if(ref.value <= vector.value){ // we already passed here but from another direction
                                ref.value -= nb_overlap;
                                all_vector.add(vector);



                                int other_dir = D *-1;
                                int r_next = i+other_dir;

                                //Check if left side ok
                                if(check_space(other_dir,i) &&  one_dim[r_next] ==0){
                                    //Check if left side ok
                                    r_next = ref_last_axes + D;
                                    if(check_space(D,ref_last_axes) &&  one_dim[r_next] ==0){
                                        vector.bidirectionnel = true;
                                    }
                                }


                            }


                        }else {
                            all_vector.add(vector);
                        }
                    }



                }
            }
        }

        for(Vector5 s: all_vector){
            if(s.value == 5){ // special case for win
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

    public boolean check_space(int D, int index){

        int next = index + D;
        if(next >= one_dim.length || next < 0 ){
            return false;
        }

        if(Direction.side_map.get(D) !=null && Direction.side_map.get(D) == Direction.OUEST){
            if(!(((index%nbcol)+1)  >=5)){
                return false;
            }
        }else
        if(Direction.side_map.get(D) != null && Direction.side_map.get(D) == Direction.EST){
            if(!((nbcol - (index%nbcol))  >=5)){
                return false;
            }
        }
        return true;
    }

    public int evaluate2(int player){

        int opponent = (player == 1)? 2:1;
        int evaluation =0;
        int last = 0;



        Vector5[][] memo = new Vector5[AXES][one_dim.length];


        ArrayList<Vector5> all_vector = new ArrayList<Vector5>();
        for(int i =0; i< one_dim.length; i++){
            for(Integer D : Direction.direction8){
                boolean sequence = true;
                last = one_dim[i];


                boolean check = true;
                if(Direction.side_map.get(D) !=null && Direction.side_map.get(D) == Direction.EST){
                    if(!(((i%nbcol)+1)  >=5)){
                        continue;
                    }
                }else
                if(Direction.side_map.get(D) != null && Direction.side_map.get(D) == Direction.OUEST){
                    if(!((nbcol - (i%nbcol))  >=5)){
                        continue;
                    }
                }

                if (!check){
                    break;
                }

                if(true){ // last == player

                    int nb_seq = 0;

                    int axe = Direction.axes_map.get(D);
                    //loop to find suite
                    Vector5 vector = new Vector5();

                    int nb_overlap =0;
                    Vector5 ref = null;
                    for(int s = 0 ; s < SEQ; s++){

                        int next = i + D*s;


                        /*Check if time to leave this for*/
                        if(next >= one_dim.length || next < 0 ){
                            sequence = false;
                            break;
                        }
                        if(one_dim[next] == opponent){

                            sequence = false;
                            break;
                        }

                        vector.tab_seq[s] = next;

                        /*Okey we got one valid token */
                        if(one_dim[next] == player ){

                            vector.value++;
                            if(memo[axe][next] == null) {
                                memo[axe][next] = vector;
                            }else {
                                ref = memo[axe][next];
                                nb_overlap++;

                            }
                        }

                    }

                    //there is space for a 5 sequence
                    if(!sequence){
                        vector.value = 0;
                    }else{
                        if(vector.value == 2){
                            int test2=0;
                        }

                        if(ref != null){
                            if(ref.value <= vector.value){
                                ref.value -= nb_overlap;
                                all_vector.add(vector);
                            }
                        }else {
                            all_vector.add(vector);
                        }
                    }



                }
            }
        }

        for(Vector5 s: all_vector){
            evaluation += Math.pow(s.value,3);
        }

        return evaluation;

    }




    public boolean enought_space(int D,int index){

        if(D == LEFT || D == DOWNLEFT || D == TOPLEFT){
            return ((index%nbcol)+1)  >=5 ;
        }else
        if(D == RIGHT || D == DOWNRIGHT || D == TOPRIGHT){
            return (nbcol - (index%nbcol))  >=5 ;
        }


        return false;

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
                        return WIN;
                    }

                }



            }

        }



        return 0;
    }

    public  int winner2(){

        int winner =0;
        int last = 0;


        for(int i =0; i< one_dim.length; i++){

            for(int s =0; s < SEQ ; s++){




            }


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
                        int next = i + direction4[d]*seq;

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
        public int value;
        public int valueBirdirection;
        public boolean bidirectionnel = false;
        public Dir Direction;
        public int[] tab_seq = new int[5];

    }


    public static void main(String[] args) {



    }



}
