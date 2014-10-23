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
    public int depth = 0;
    public int maxDepth =0;


    public LinkedList<Vector5> vector5MAX = new LinkedList<Vector5>();
    public LinkedList<Vector5> vector5MIN = new LinkedList<Vector5>();

    public LinkedList<Vector5> vector5MAX_2 = new LinkedList<Vector5>();
    public LinkedList<Vector5> vector5MIN_2 = new LinkedList<Vector5>();



    public Vector5[][] memo2;


    public Map<Dir.Axes,Map<Integer,Integer>> mapMemoAxesValue;
    Map<Dir.Axes,Map<Integer,Integer>> mapAxesPointToStartPoint ;
    Map<Dir.Axes,Set<Integer>> mapAxesStartPointSet ;
    Map<Dir.Axes,Map<Integer,Integer>> mapAngleDangerMAX;
    Map<Dir.Axes,Map<Integer,Integer>> mapAngleDangerMIN;



    public Map<Dir.Axes,Map<Integer,Map<Integer,Vector5>>> wtfMap = new HashMap<Dir.Axes, Map<Integer, Map<Integer, Vector5>>>();

    public int evaluationHere;

    public static GrilleVerificateur checker;

    /*pour fonciton*/
    boolean playe1Won =false;
    boolean player2Won =false;
    int higthestMAX =0;
    int higthestMIN =0;
    Angle higthestMAXAngle = null;
    Angle hightestMINAngle=null;

    public Etat(byte[] one_dim, int max,int min){
        this.grille = grille;
        nbcol = GLOBAL.NBCOL;
        nbligne = GLOBAL.NBLIGNE;

        MAX_player = max;
        MIN_player = min;

        this.one_dim = one_dim;
       // initMemo();
    }
    public Etat(Grille grille, int max,int min){
        this.grille = grille;
        nbcol = GLOBAL.NBCOL;
        nbligne = GLOBAL.NBLIGNE;
        MAX_player = max;
        MIN_player = min;
        one_dim= oneDimentionalArray(grille.getData());
       // initMemo();

    }
    @Override
    public Etat clone() {
        //Grille new_grille = grille.clone();
        byte[] cloned_array = cloneByteArray(this.one_dim);
        Etat cloned = new Etat(cloned_array,MAX_player,MIN_player);
        // cloned.setChecker(checker);

        //  Vector5[][] clonedMemo = cloneMemoArray(memo2);
        Map<Dir.Axes,Map<Integer,Integer>> clonedMapValue = cloneMap(mapMemoAxesValue);
        cloned.mapMemoAxesValue = clonedMapValue;
        //cloned.memo2 = clonedMemo;
        cloned.mapAngleDangerMAX=  cloneMap(mapAngleDangerMAX);
        cloned.mapAngleDangerMIN = cloneMap(mapAngleDangerMIN);

        cloned.evaluationHere = this.evaluationHere;
        cloned.mapAxesPointToStartPoint = mapAxesPointToStartPoint;
        cloned.mapAxesStartPointSet = mapAxesStartPointSet;


      /*
        cloned.hightestMINAngle = (hightestMINAngle!= null)? hightestMINAngle.clone() : null;
        cloned.higthestMAXAngle = (higthestMAXAngle !=null)? higthestMAXAngle.clone() : null;*/
     /*   cloned.higthestMAX = higthestMAX;
        cloned.higthestMIN = higthestMIN;

*/


        return cloned;
    }


    private  Map<Dir.Axes,Map<Integer,Integer>> cloneMap (Map<Dir.Axes, Map<Integer, Integer>> toClone){
        Map<Dir.Axes,Map<Integer,Integer>> c = new HashMap<Dir.Axes, Map<Integer, Integer>>();
        for(Dir.Axes ax : Dir.Axes.values()){
            c.put(ax, new HashMap<Integer, Integer>());
            for(Map.Entry<Integer,Integer> it : toClone.get(ax).entrySet()){
                c.get(ax).put(it.getKey(),it.getValue());
            }
        }
        return c;

    }

    private Vector5[][] cloneMemoArray(Vector5[][] memoToClone){
        int lenght = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
        Vector5[][] cloned = new Vector5[AXES][lenght];
        for(int i=0; i< AXES; i++){


            Vector5 precedentRef = null;
            Vector5 refClone=null;
            Vector5 refOld = null;
            for(int j=0; j < lenght; j++ ){

                refOld = memoToClone[i][j];
                if(refOld !=null && precedentRef != refOld){
                    precedentRef = refOld;
                    refClone = memoToClone[i][j].clone();
                    cloned[i][j] = refClone;
                }else if(refOld!= null && precedentRef == refOld){
                    cloned[i][j] = refClone;
                }

            }
        }
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
        if(etat_b.depth != depth){
            return false;
        }



        for(int i=0;i< one_dim.length; i++){
            if(one_dim[i] != etat_b.one_dim[i]){
                return false;
            }
        }

        return true;
    }

    public void playAndUpdate(int move,int player){
        one_dim[move] = (byte)player;

        int oldScore =0;
        int thisScore=0;
        int stepInAdvance =0;
        boolean stepAheadWasBlocked = false;
        for(Dir D : Dir.direction4 ){
            Dir.Axes axe = Dir.Axes.getA(D);
            Integer startPoint = mapAxesPointToStartPoint.get(axe).get(move);
            oldScore += mapMemoAxesValue.get(axe).get(startPoint);
            int thisAngleScore = axeAngleValue(startPoint, D,player);

            thisScore+= thisAngleScore;
            updateAngleMaps(axe,startPoint);

            mapMemoAxesValue.get(axe).put(startPoint,thisAngleScore);

        }
        stepInAdvance = getScoreStepAhead();

       /* evaluate(5);
        int mx = getMaxSeq();
        if(mx != higthestMAX && higthestMAX > 3){
            System.out.println("ici");
        }
        int mi = getMinSeq();
        if(mi != higthestMIN && higthestMIN > 3){
            System.out.println("ici min");
        }


        int test = (evaluationHere - oldScore) + thisScore ;*/
        this.score = (evaluationHere - oldScore) + thisScore + stepInAdvance;
/*
        if(!areAllEqual(this.score,evaluate(player),evaluate(2))){
            evalPoint(move,player);
            System.out.println("Update failed");
        }
*/


        evaluationHere = (evaluationHere - oldScore) + thisScore ;

    }
    public void play(int move,int player){
        one_dim[move] = (byte)player;
    }

    public void unplay(int move){
        one_dim[move] = 0;
    }

    public PriorityQueue<Move> getNextMoves(int player_to_max){

        int lowestX = Integer.MAX_VALUE ;
        int lowestY = Integer.MAX_VALUE;
        int highestX = Integer.MIN_VALUE;
        int highestY = Integer.MIN_VALUE;


        int nblibre =0;


        /* This is to cut the area to explore */
        int a=0;
        for(byte carreau : one_dim){


            int pos_x =  ((a/GLOBAL.NBCOL) ) ;
            int pos_y = ((a%GLOBAL.NBCOL) ) ;


            if(carreau != 0 ){
                lowestX = (lowestX > pos_x)? pos_x : lowestX;
                lowestY = (lowestY > pos_y)? pos_y : lowestY;
                highestX= (highestX < pos_x)? pos_x: highestX;
                highestY= (highestY < pos_y)? pos_y : highestY;

            }
            if(carreau == 0){
                nblibre++;
            }
            a++;
        }
        /***************************************/


        PriorityQueue<Move> ordered_move;
        if(player_to_max == MIN_player) {
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMIN());
        }else{
            ordered_move = new PriorityQueue<Move>(nblibre,new CompareMAX() );
        }


        int buffer = 2;

        for(int i =0; i< one_dim.length; i++){

            int pos_x =  ((i/GLOBAL.NBCOL) ) ;
            int pos_y = ((i%GLOBAL.NBCOL) );

            if( (pos_x >= lowestX-buffer) && (pos_x <= highestX+buffer ) && (pos_y  >= lowestY -buffer) && (pos_y <= highestY +buffer  ) ){
                if(one_dim[i] ==0){
                    play(i,player_to_max);
                    //int evaluation = evaluate(player_to_max);
                    int evaluation= evalPoint(i,player_to_max);
/*
                    if(!areAllEqual(evaluation,evaluation2)){
                        if(true){///evaluation  < GLOBAL.ALMOST_WIN-6000 && evaluation > -GLOBAL.ALMOST_WIN+6000 ){
                            System.out.println("prob: TotalScan: "+ evaluation2
                                    +" thisPoint:" +evaluation);
                            evaluate(2);
                            calculateAllAngle(2);
                            evalPoint(i,player_to_max);
                            System.out.println(toStringOneDim(one_dim));
                        }

                    }*/
                    Move aMove =  new Move(i,evaluation);
                    ordered_move.add(aMove);

                    unplay(i);
                }
            }
        }


        return ordered_move;
    }

    public Grille getGrille(){
        return grille;
    }

    /*OLD FUNCTION
    * */
    public int evaluate(int player){

        return evaluate5(player);

    }

    public boolean isTerminal(){
        return getNblibre() == 0;
    }





    int MAXhigthestSeqThisAngle;
    int MINhigthestSeqThisAngle;

    public int axeAngleValue(int point, Dir D,int player){
        MAXhigthestSeqThisAngle =0;
        MINhigthestSeqThisAngle =0;
        HashSet<Vector5> allV = new HashSet<Vector5>();
        Map<Integer,Vector5> tempMemo = new HashMap<Integer, Vector5>();

        playe1Won =false;
        player2Won = false;

        Dir.Axes axe = Dir.Axes.getA(D);
        for (int i= point; D.boundaries(i,5);i= i+ D.step(1)){
            if(true) {
                int res = one_dim[i] | one_dim[i + D.step(1)] | one_dim[i + D.step(2)] | one_dim[i + D.step(3)] | one_dim[i + D.step(4)];
                if (res == 1 || res == 2) { //

/*We must not count vector that have more than 5*/

                    int[] tab_s = new int[5];

                    tab_s[0] = i + D.step(0);
                    tab_s[1] = i + D.step(1);
                    tab_s[2] = i + D.step(2);
                    tab_s[3] = i + D.step(3);
                    tab_s[4] = i + D.step(4);
                    if (tempMemo.containsKey(i)&& tempMemo.get(i).moreThan5) {
                        continue;
                    }


                    int nb_seqt = 0;
                    if (one_dim[i + D.step(0)] == res) {
                        nb_seqt += 1 ;
                    }
                    if (one_dim[i + D.step(1)] == res) {
                        nb_seqt += 1 << 1;
                    }
                    if (one_dim[i + D.step(2)] == res) {
                        nb_seqt += 1 << 2;
                    }
                    if (one_dim[i + D.step(3)] == res) {
                        nb_seqt += 1 << 3;
                    }
                    if (one_dim[i + D.step(4)] == res) {
                        nb_seqt += 1 << 4;
                    }

                    Vector5 new_vector = new Vector5(nb_seqt,D);
                    new_vector.setIfDirectionnel(isBidirectionnel(i,i+D.step(4),D));

                    if (new_vector.suite == 5) {
/*TODO there is a chance where we return before checking if the other player won*/

                        if (D.boundaries(i, 6) && one_dim[i + D.step(5)] == res) {
                            new_vector.moreThan5 = true;
                            Vector5 old_ref;
                            for (int v = 0; v < 6; v++) {
                                if ((nb_seqt & (power2[v])) != 0) {
                                    old_ref = tempMemo.get(i+D.step(v));
                                    if (old_ref != null && !old_ref.moreThan5) {
                                        old_ref.value--;
                                    }
                                    tempMemo.put(i+D.step(v),new_vector);
                                }
                            }
                            continue;
                        } else {
                            if (res == 1) {
                                playe1Won = true;
                            } else {
                                player2Won = true;
                            }
                            //return (res == MAX_player)? GLOBAL.WIN : 0- GLOBAL.WIN;
                        }
                    }

                    Vector5 old_ref;
                    new_vector.tab_seq = tab_s;

/*Update the memo[][] to avoid count two vector in the same place same direction*/

                    for (int v = 0; v < 5; v++) {
                        if ((nb_seqt & (power2[v])) != 0) {

                            int steped = i+D.step(v);
                            if(tempMemo.containsKey(steped)){
                                old_ref = tempMemo.get(steped);
                            }else {
                                old_ref = null;
                            }
                            if (old_ref == null || old_ref.valueBirdirection <= new_vector.valueBirdirection) {
                                if (old_ref != null){
                                    old_ref.value--;
                                    old_ref.valueBirdirection--;
                                }
                                tempMemo.put(i+D.step(v),new_vector);
                            } else {
                                new_vector.valueBirdirection--;
                                new_vector.value--;
                            }
                        }
                    }

                    if(new_vector.value > 0){
                        if(res == MAX_player){
                            new_vector.isMAXvector = true;
                        }
                        allV.add(new_vector);
                    }
                }
            }
        }
        int thisEvaluation =0;

        for(Vector5 s : allV){
            int value = s.valueBirdirection; //(s.bidirectionnel && s.value>0)? s.value+1 : s.value;
            //Dir.Axes a = Dir.Axes.getA(s.Direction);
            //int previous = mapMemoAxesValue.get(a).get(Dir.Axes.mapAxesPointToStartPoint.get(s.tab_seq[0]));
            if(s.value >0){
                if(s.isMAXvector){
                    vector5MAX_2.add(s);
                    thisEvaluation += Math.pow(value,4);
                    if(s.getThreatValue()> MAXhigthestSeqThisAngle ){
                        MAXhigthestSeqThisAngle = s.getThreatValue();
                    }

                }else {
                    vector5MIN_2.add(s);
                    thisEvaluation -= Math.pow(value,4);
                    if(s.getThreatValue() > MINhigthestSeqThisAngle ){
                        MINhigthestSeqThisAngle = s.getThreatValue();
                    }
                }
            }

        }


        if(MAXhigthestSeqThisAngle >3 && MAXhigthestSeqThisAngle > higthestMAX){
            higthestMAXAngle = new Angle(axe,point, MAXhigthestSeqThisAngle);
            higthestMAX = MAXhigthestSeqThisAngle;
        }
        if(MINhigthestSeqThisAngle > 3 && MINhigthestSeqThisAngle > higthestMIN){
            hightestMINAngle = new Angle(axe,point,MINhigthestSeqThisAngle);
            higthestMIN = MINhigthestSeqThisAngle;
        }


       return thisEvaluation;
    }

    public int evalPoint(int move,int player){

        int oldScore =0;
        int thisScore=0;
        int oldStepScore = getScoreStepAhead();
        int stepInAdvance=oldStepScore;
        boolean stepAheadWasBlocked = false;
        boolean checkP1win = false;
        boolean checkP2win = false;
        for(Dir D : Dir.direction4 ){
            Dir.Axes axe = Dir.Axes.getA(D);
            Integer startPoint = mapAxesPointToStartPoint.get(axe).get(move);
            oldScore += mapMemoAxesValue.get(axe).get(startPoint);
            thisScore+= axeAngleValue(startPoint, D,player);
            checkP1win = (playe1Won)? true : checkP1win;
            checkP2win = (player2Won)? true : checkP2win;

            int stepWithoutThisAngle = getScoreStepAheadExcept(axe, startPoint);
            if(stepWithoutThisAngle != oldStepScore){
                stepInAdvance = stepWithoutThisAngle;
            }

        }

        if(checkP1win && getLastPlayedPlayer()==1 && !checkP2win){
            stepInAdvance =(MAX_player == 1)? GLOBAL.WIN: -GLOBAL.WIN;
        }

        if(checkP2win && getLastPlayedPlayer()==2 && !checkP2win){
            stepInAdvance = (MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN;
        }




        //stepInAdvance = (stepAheadWasBlocked)? 0 : stepInAdvance;
        return (evaluationHere-oldScore) + (thisScore + stepInAdvance);
    }

    public void initMemo(){

        mapAxesPointToStartPoint = new HashMap<Dir.Axes, Map<Integer, Integer>>();
        mapAxesStartPointSet = new HashMap<Dir.Axes, Set<Integer>>();
        mapMemoAxesValue = new HashMap<Dir.Axes, Map<Integer, Integer>>();
        for(Dir.Axes a : Dir.Axes.values()){
            mapAxesPointToStartPoint.put(a, new HashMap<Integer, Integer>());
            mapAxesStartPointSet.put(a, new HashSet<Integer>());
        }


        for(Dir.Axes a : Dir.Axes.values()){
            for(int i=0; i < GLOBAL.NBCOL* GLOBAL.NBLIGNE; i++) {
                int x = i / GLOBAL.NBCOL;
                int y = i % GLOBAL.NBCOL;

                int startPoint = 0;
                switch (a) {
                    case HORIZONTAL:
                        startPoint = x * GLOBAL.NBCOL;
                        break;
                    case VERTICAL:
                        startPoint = y;
                        break;
                    case DIAGR:
                        startPoint = (x + y < GLOBAL.NBLIGNE) ? ((x + y) * GLOBAL.NBCOL) : ((GLOBAL.NBLIGNE - 1) * GLOBAL.NBCOL) + (x + y + 1) - GLOBAL.NBLIGNE;
                        break;
                    case DIAGL:
                        startPoint = (x > y) ? (x - y) * GLOBAL.NBCOL : y - x;
                        break;
                    default:
                        break;
                }
                mapAxesStartPointSet.get(a).add(startPoint);
                mapAxesPointToStartPoint.get(a).put(i, startPoint);

            }
        }


        mapAngleDangerMAX = new HashMap<Dir.Axes, Map<Integer, Integer>>();
        mapAngleDangerMIN = new HashMap<Dir.Axes, Map<Integer, Integer>>();


        for(Dir.Axes a : Dir.Axes.values()){
            mapMemoAxesValue.put(a, new HashMap<Integer, Integer>());
            mapAngleDangerMAX.put(a, new HashMap<Integer, Integer>());
            mapAngleDangerMIN.put(a, new HashMap<Integer, Integer>());
            wtfMap.put(a, new HashMap<Integer, Map<Integer, Vector5>>());
        }
        for(Dir d : Dir.direction4){
            Dir.Axes a = Dir.Axes.getA(d);
            int test[] = new int[mapAxesStartPointSet.get(a).size()];
            int i=0;
            //   System.out.println(a + " nbStartpoint:"+ test.length );
            for(Iterator<Integer> it = mapAxesStartPointSet.get(a).iterator(); it.hasNext(); ){
                int startPoint = it.next();
                test[i] =startPoint;
                int eval = axeAngleValue(startPoint,d,MAX_player);
                evaluationHere += eval;
                mapMemoAxesValue.get(a).put(startPoint,eval);
                updateAngleMaps(a,startPoint);

                i++;
            }
        }
/*        int t=0;
        for(Map m : mapMemoAxesValue.values()){
            for(Object i : m.values()){
                t += (Integer) i;
            }
        }
        if(evaluationHere != t){
            System.out.println("peut etre ici!");
        }*/

    }

    public void updateAngleMaps(Dir.Axes axe, int starPoint){

        if(MINhigthestSeqThisAngle > 3){
            mapAngleDangerMIN.get(axe).put(starPoint,MINhigthestSeqThisAngle);
        }else{
            if(mapAngleDangerMIN.get(axe).containsKey(starPoint)){
                mapAngleDangerMIN.get(axe).remove(starPoint);
            }
        }

        if(MAXhigthestSeqThisAngle > 3){
            mapAngleDangerMAX.get(axe).put(starPoint,MAXhigthestSeqThisAngle);
        }else{
            if(mapAngleDangerMAX.get(axe).containsKey(starPoint)){
                mapAngleDangerMAX.get(axe).remove(starPoint);
            }
        }

    }

    public int getScoreStepAheadExcept(Dir.Axes axe, Integer startPoint){
        {
            int maxAhead =0;
            int minAhead =0;

            if(playe1Won && getLastPlayedPlayer()==1 && !player2Won){
                return (MAX_player == 1)? GLOBAL.WIN: -GLOBAL.WIN;
            }

            if(player2Won && getLastPlayedPlayer()==2 && !playe1Won){
                return (MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN;
            }


            if(mapAngleDangerMAX.get(axe).entrySet().size() ==0){
                maxAhead = MAXhigthestSeqThisAngle;
            }
            if(mapAngleDangerMIN.get(axe).entrySet().size() ==0){
                minAhead = MINhigthestSeqThisAngle;
            }

            for(Dir.Axes axes : Dir.Axes.values()){
                for(Map.Entry<Integer,Integer> max :  mapAngleDangerMAX.get(axes).entrySet()){
                    if( !(axes == axe && max.getKey().equals(startPoint))){
                        if(max.getValue() > maxAhead){
                            maxAhead = max.getValue();
                        }
                    }else if(MAXhigthestSeqThisAngle > maxAhead){
                        maxAhead = MAXhigthestSeqThisAngle;
                    }
                }
                for(Map.Entry<Integer,Integer> min : mapAngleDangerMIN.get(axes).entrySet()){
                    if( !(axes == axe && min.getKey().equals(startPoint))){
                        if(min.getValue() > minAhead){
                            minAhead = min.getValue();
                        }
                    }else if(MINhigthestSeqThisAngle > minAhead){
                        minAhead = MINhigthestSeqThisAngle;
                    }
                }
            }

            /*TODO ca ne derais jamais etre la c,est a cause d<un bug que je comprends pas*/
            if(getMaxSeq() ==0 && getMinSeq() ==0){
                minAhead = higthestMIN;
                maxAhead = higthestMAX;
            }



            if(getLastPlayedPlayer() == MAX_player){

                if(maxAhead > 4 && maxAhead > minAhead){
                    return GLOBAL.ALMOST_WIN ;
                }

                if(minAhead > 3 && minAhead >= maxAhead){
                    return -GLOBAL.ALMOST_WIN;
                }

            }else if(getLastPlayedPlayer() == MIN_player){

                if(minAhead > 4 && minAhead >maxAhead){
                    return -GLOBAL.ALMOST_WIN ;
                }
                if(maxAhead > 3 && maxAhead >= minAhead){
                    return GLOBAL.ALMOST_WIN ;
                }

            }
            return 0;
        }


    }

    public int getScoreStepAhead(){
        int maxAhead =0;
        int minAhead =0;
/*
        if(playe1Won && getLastPlayedPlayer()==1 && !player2Won){
            return (MAX_player == 1)? GLOBAL.WIN: -GLOBAL.WIN;
        }

        if(player2Won && getLastPlayedPlayer()==2 && !playe1Won){
            return (MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN;
        }*/


        for(Map.Entry<Dir.Axes,Map<Integer,Integer>> entry : mapAngleDangerMAX.entrySet()){
            for(Integer i : entry.getValue().values()){
                if(i > maxAhead){
                    maxAhead = i;
                }
            }
        }
        for(Map.Entry<Dir.Axes,Map<Integer,Integer>> entry : mapAngleDangerMIN.entrySet()){
            for(Integer i : entry.getValue().values()){
                if(i > minAhead){
                    minAhead = i;
                }
            }
        }



        if(getLastPlayedPlayer() == MAX_player){

            if(maxAhead > 4 && maxAhead > minAhead){
                return GLOBAL.ALMOST_WIN ;
            }

            if(minAhead > 3 && minAhead >= maxAhead){
                return -GLOBAL.ALMOST_WIN;
            }

        }else if(getLastPlayedPlayer() == MIN_player){

            if(minAhead > 4 && minAhead >maxAhead){
                return -GLOBAL.ALMOST_WIN ;
            }
            if(maxAhead > 3 && maxAhead >= minAhead){
                return GLOBAL.ALMOST_WIN ;
            }

        }
        return 0;
    }






    public int evaluate5(int player){

        int evaluation =0;
        Vector5[][] memo = new Vector5[AXES][one_dim.length];
        //memo2 = memo;
        LinkedList<Vector5> allVectorMax = new LinkedList<Vector5>();
        LinkedList<Vector5> allVectorOpponent = new LinkedList<Vector5>();
        vector5MAX = allVectorMax;
        vector5MIN = allVectorOpponent;
        boolean playe1Won =false;
        boolean player2Won =false;

        for(int i =0; i< one_dim.length; i++){
            for(Dir D : Dir.direction4){

                if(!D.boundaries(i,5)){
                    continue;
                }

                //START loop
                int res = one_dim[i]|one_dim[i+ D.step(1)]|one_dim[i+ D.step(2)]|one_dim[i+ D.step(3)]|one_dim[i+ D.step(4)];

                if(res== 1 || res == 2){ //


                    Dir.Axes axe = Dir.Axes.getA(D);
                    /*We must not count vector that have more than 5*/
                    if(memo[axe.i][i]!=null && memo[axe.i][i].moreThan5){
                        continue;
                    }


                    Vector5 new_vector = new Vector5();
                    new_vector.Direction = D;

                    int nb_seqt=0;
                    if(one_dim[i + D.step(0)] == res){
                        nb_seqt += 1<<0;
                    }
                    if(one_dim[i + D.step(1)] == res){
                        nb_seqt += 1<<1;
                    }
                    if(one_dim[i + D.step(2)] == res){
                        nb_seqt += 1<<2;
                    }
                    if(one_dim[i + D.step(3)] == res){
                        nb_seqt += 1<<3;
                    }
                    if(one_dim[i + D.step(4)] == res){
                        nb_seqt += 1<<4;
                    }



                    int vecteur_value = Integer.bitCount(nb_seqt);
                    if(vecteur_value == 5){

                        /*TODO there is a chance where we return before checking if the other player won*/
                        if(D.boundaries(i,6) && one_dim[i+ D.step(5)] == res){
                            new_vector.moreThan5= true;
                            Vector5 old_ref;
                            for(int v=0;v<6;v++){
                                if( (nb_seqt&(power2[v])) != 0){
                                    old_ref = memo[axe.i][i + D.step(v)];
                                    if(old_ref != null && !old_ref.moreThan5){
                                        old_ref.value--;
                                    }
                                    memo[axe.i][i + D.step(v)] = new_vector;
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
                    if(isBidirectionnel(i,i+D.step(4),D)){
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
                            old_ref = memo[axe.i][i + D.step(v)];
                            if(old_ref ==null || old_ref.value < new_vector.valueBirdirection ) {
                                if (old_ref != null){
                                    old_ref.value--;
                                    old_ref.valueBirdirection--;
                                }
                                memo[axe.i][i + D.step(v)] = new_vector;
                            }else {
                                new_vector.valueBirdirection--;
                                new_vector.value--;
                            }
                        }
                    }

                    /*Just for printing when debugging*/

                    new_vector.tab_seq[0] = i + D.step(0);
                    new_vector.tab_seq[1] = i + D.step(1);
                    new_vector.tab_seq[2] = i + D.step(2);
                    new_vector.tab_seq[3] = i + D.step(3);
                    new_vector.tab_seq[4] = i + D.step(4);



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

        higthestMAX=0; higthestMIN=0;
        for(int v1=0; v1 < allVectorMax.size(); v1++){
            Vector5 s = allVectorMax.get(v1);
            if(s.value >0){
                int value = (s.bidirectionnel)? s.value+1 : s.value;
                evaluation += Math.pow(value,4);
                higthestMAX = (value > higthestMAX && s.isCorded)? value : higthestMAX;

            }

        }
        //int higthestMIN =0;
        for(int v2=0; v2 < allVectorOpponent.size(); v2++){
            Vector5 s = allVectorOpponent.get(v2);
            if(s.value > 0){
                int value = (s.bidirectionnel)? s.value+1 : s.value;
                evaluation -= Math.pow(value,4);
                higthestMIN = (value > higthestMIN && s.isCorded)? value : higthestMIN;
            }

        }



        if(playe1Won && getLastPlayedPlayer()==1 && !player2Won){
            return ((MAX_player == 1)? GLOBAL.WIN: -GLOBAL.WIN ) + evaluation;
        }

        if(player2Won && getLastPlayedPlayer()==2 && !playe1Won){
            return ((MAX_player == 2)? GLOBAL.WIN: -GLOBAL.WIN ) + evaluation;
        }


        if(getLastPlayedPlayer() == MAX_player){

            if(higthestMAX > 4 && higthestMAX > higthestMIN){
                return GLOBAL.ALMOST_WIN + evaluation;
            }

            if(higthestMIN > 3 && higthestMIN >= higthestMAX){
                return -GLOBAL.ALMOST_WIN +evaluation;
            }

        }else if(getLastPlayedPlayer() == MIN_player){

            if(higthestMIN > 4 && higthestMIN >higthestMAX){
                return -GLOBAL.ALMOST_WIN + evaluation;
            }
            if(higthestMAX > 3 && higthestMAX >= higthestMIN){
                return GLOBAL.ALMOST_WIN + evaluation;
            }

        }


        return evaluation;

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

    class Angle {
        int SeqScore =0;
        int startPoint;
        Dir.Axes axes;

        public Angle(){

        }
        public Angle(Dir.Axes a , int s){
            startPoint = s;
            axes = a;
        }
        public Angle(Dir.Axes a , int s, int score){
            startPoint = s;
            axes = a;
            SeqScore = score;
        }

        public boolean isThisAngle(Dir.Axes a, int s){
            return a==axes && s == startPoint;
        }
        public Angle clone(){
            Angle cloned = new Angle(axes,startPoint,SeqScore);
            return cloned;
        }

    }







    public boolean isBidirectionnel(int index,int last_index, Dir D) {
        int step;
        if (one_dim[index] != 0) {

            step = index + D.opp().v();
            if(!D.opp().boundaries(index,2)){
                return false;
            }

            if ( !D.opp().boundaries(index,2) && !D.opp().boundaries(step,5) || one_dim[step] != 0) {
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

        if(one_dim[index] != 0 && !D.opp().boundaries(index,2)){
            int t =4;
        }


        return true;
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

        private byte[] oneDimentionalArray(byte[][] grille){
        byte[] one_dim = new byte[nbcol * nbligne];
        //System.out.println("nb col:" +nbcol + "  nbligne: "+ nbligne);
        for(int l=0 ; l < nbligne ; l++){
            System.arraycopy(grille[l],0,one_dim,l*(nbcol),nbcol);
        }
        return one_dim;
    }




    public int getLastPlayedPlayer (){
        if(((one_dim.length-1) - getNblibre()) % 2 ==0){
            return 1;
        }else{
            return 2;
        }
    }

    public int getNextPlayerToPlay (){
        if(((one_dim.length) - getNblibre()) % 2 ==0){
            return 1;
        }else{
            return 2;
        }
    }



    public int getMaxSeq(){
        int maxAhead =0;
        for(Map.Entry<Dir.Axes,Map<Integer,Integer>> entry : mapAngleDangerMAX.entrySet()){
            for(Integer i : entry.getValue().values()){
                if(i > maxAhead){
                    maxAhead = i;
                }
            }
        }
        return maxAhead;

    }
    public int getMinSeq(){
        int minAhead =0;
        for(Map.Entry<Dir.Axes,Map<Integer,Integer>> entry : mapAngleDangerMIN.entrySet()){
            for(Integer i : entry.getValue().values()){
                if(i > minAhead){
                    minAhead = i;
                }
            }
        }
        return minAhead;
    }



    public int calculateAllAngle(int player){
        int total=0;

        vector5MAX_2 = new LinkedList<Vector5>();
        vector5MIN_2 = new LinkedList<Vector5>();

        for(Dir d : Dir.direction4){
            Dir.Axes a = Dir.Axes.getA(d);
            //int test[] = new int[mapAxesStartPointSet.get(a).size()];
            int i=0;
            //   System.out.println(a + " nbStartpoint:"+ test.length );
            for(Iterator<Integer> it = mapAxesStartPointSet.get(a).iterator(); it.hasNext(); ){
                int startPoint = it.next();
             //   test[i] =startPoint;
                int t = axeAngleValue(startPoint,d,player);
                total += t;
              //  mapMemoAxesValue.get(a).put(startPoint,t);
                if(higthestMIN> 3){
                    int df=0;
                }
                i++;
            }

/*   System.out.println("all the startPoint: "+ Arrays.toString(test));
            System.out.println(toStringVector(one_dim,test));*/


        }
        return total;
    }

}
