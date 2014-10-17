package connect5.ia.strategy;

import connect5.ia.models.Etat;
import connect5.ia.models.GLOBAL;
import connect5.ia.models.Move;
import connect5.ia.models.TimeOver;

import java.awt.*;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by MB on 10/15/2014.
 */
public class NegaScout {
    static int currentPlayer;
    static int opponent;
    static int MAX_DEPTH = 0;
    static int nbMAX,nbMIN;

    static int alpha = -1000000;
    static int beta  = 1000000;

    public static Integer getMove (Etat etatInitial, int playerColor, int deep) throws Exception {
        MAX_DEPTH = deep;
        opponent = (playerColor == 1) ? 2 : 1;
        currentPlayer = playerColor;
        nbMAX =0; nbMIN =0;
        int score =0;

        long time = System.currentTimeMillis();

        //System.out.println(" TRY TO MAX :" + currentPlayer + " AND MIN :" + opponent + "    DEEP: "+ deep);



        Move bestMove = null;
        int best = Integer.MIN_VALUE;
        PriorityQueue<Move> allMoves = etatInitial.getNextMoves(playerColor);
        /*List<Integer> allMove = etatInitial.getEmptyMoves(2);*/
        Move move = allMoves.poll();
        Etat nextState = etatInitial.clone();
        nextState.play(move.move,playerColor);

        score = negaScout2(nextState,alpha,beta,1,opponent, move)[1];
        alpha = score;
        best = score;
        bestMove  = move;


        while (!allMoves.isEmpty()){
            move = allMoves.poll();
            nextState = etatInitial.clone();
            nextState.play(move.move,playerColor);

            if( bestMove == null){
                bestMove = move;
            }
            score =  negaScout2(nextState,alpha,-alpha,1,opponent, move)[1];
            if( score > best){
                bestMove = move;
                best = score;
                alpha = score;
            }
        }


        //int negaScout2(etatInitial,alpha,beta,0,currentPlayer,0);


        int moveInteger = bestMove.move;
        System.out.println("score :" + score + " play :(" + moveInteger/ GLOBAL.NBCOL + "," + moveInteger% GLOBAL.NBCOL
                + ") ---- nbMAX: " + nbMAX + " - nbMIN: " + nbMIN);
        System.out.println("TIME: " + (System.currentTimeMillis() - time) + " ms");
        //System.out.println("Closelist size: " + closelist.size() + "  nbSave: " + nbSaveInCloseList);
        etatInitial.play(moveInteger, currentPlayer);
        System.out.println(etatInitial.toStringOneDim(etatInitial.one_dim));
        etatInitial.unplay(moveInteger);
        return moveInteger;

    }


    private static int[] negaScout2 (Etat etat, int alpha, int beta, int deep, int player, Move last_score)throws Exception{

        if (GLOBAL.timeRemaining() < 100) {
            System.out.println("GOOOO BACK!!");
            throw new TimeOver("Time Over");
        }




        int a, b;
        if (deep == MAX_DEPTH) {
            return new int[] {last_score.move,last_score.score};
        }

        a= alpha;
        b = beta;
        int bestMove=-1;

        PriorityQueue<Move> nextMoves = etat.getNextMoves(player);
        boolean first = true;
        while (!nextMoves.isEmpty()){


            Move moveM = nextMoves.poll();
            Integer move = moveM.move;
            if(opponent == player){
               // moveM.score = -moveM.score;
            }


            Etat nextEtat = etat.clone();
            nextEtat.play(move,player);
            int otherPlayer = (player ==1)? 2:1;

            int score;
            if(!first){
                score = negaScout2(nextEtat,-alpha -1,-alpha,deep+1,otherPlayer,moveM)[1];
                if( score > alpha && score < beta){
                    score = negaScout2(nextEtat,-beta,-score,deep+1, otherPlayer,moveM)[1];
                }
            }else{
                score = negaScout2(nextEtat,-beta,-alpha,deep+1,otherPlayer,moveM)[1];
            }
            alpha = Math.max(alpha,score);

            if(alpha >= beta){

                return  new int[]{bestMove, alpha};
            }


        }
        return  new int[]{bestMove, alpha};
    }






    private static int negaScout (Etat etat, int alpha, int beta, int deep, int player, int last_score)throws Exception{

        if (GLOBAL.timeRemaining() < 100) {
            System.out.println("GOOOO BACK!!");
            throw new TimeOver("Time Over");
        }


        int a, b;
        if (deep == MAX_DEPTH) {
            return last_score;
        }

        a= alpha;
        b = beta;

        PriorityQueue<Move> nextMoves = etat.getNextMoves(player);
        boolean first = true;
        while (!nextMoves.isEmpty()){


            Move moveM = nextMoves.poll();
            if(player==opponent){
                moveM.score = -moveM.score;
            }
            Integer move = moveM.move;


            Etat nextEtat = etat.clone();

            nextEtat.play(move,player);

            int otherPlayer = (player ==1)? 2:1;

                //Prendre plus Grand a

                int res = -negaScout(nextEtat,-b,-a, deep+1,otherPlayer,moveM.score);


                if(  (res > a) && (res < b) && (!first) && ( deep < MAX_DEPTH ) ){
                    a =  -negaScout(nextEtat, -beta,-res, deep +1, opponent,moveM.score);
                }
                a = Math.max(a,res);
                if( a >= beta){
                    return a;

                }
                b = a +1;
                first =false;




        }
        return  a;
    }


}
/*
* public class NegaScout {
        public Board brd;
        public int INF = Integer.MAX_VALUE;
        public int depth;
        public byte Player;
        public int alpha;
        public int beta;

        public NegaScout(Board b, byte Player, int depth){
                brd = b;
                alpha = -INF;
                beta = INF;
                this.depth = depth;
                this.Player = Player;
        }

        private int negaScout( Board brd, int alpha, int beta, int d,byte Player ){
                int a,b;
                if (d == this.depth)
                        return brd.evaluateBoard3(brd,Player);
                a = alpha;
            b = beta;
            Vector<Move> v = brd.getAllMoves(Player);
            for (int i = 0; i< v.size(); i++){
                Board bd = brd.getCopy();
                Move m = v.get(i);
                bd.updateBoard(m.getP1(), m.getP2(), (byte)bd.W_QUEEN);
                int t = - negaScout(bd,-b,-a,d+1,(byte)(1-Player));
                if ( (t > a) && (t < beta) && (i > 0) && (d < this.depth -1))
                        a = -negaScout(bd,-beta,-t,d+1,(byte)(1-Player));
                a = Math.max(a,t);
                if (a >= beta)
                        return a;
                b = a + 1;
            }
            return a;
        }

        public Move returnBestMove(){
                Move bestMove = null;
                int best = -INF;
            Vector<Move> v = brd.getAllMoves(Player);
            for (Move m : v){
                Board b = brd.getCopy();
                b.updateBoard(m.getP1(), m.getP2(), (byte)b.W_QUEEN);
                if (bestMove == null)
                        bestMove = m;
                int score = -negaScout(b,alpha,beta,0,(byte)(1-Player));
                if (score > best){
                        bestMove = m;
                        best = score;
                }
            }
          return bestMove;
        }

}
*
* */