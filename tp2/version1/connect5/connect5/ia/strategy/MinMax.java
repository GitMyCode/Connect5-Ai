package connect5.ia.strategy;

import connect5.Grille;
import connect5.ia.models.Dir;
import connect5.ia.models.Etat;
import connect5.ia.models.GLOBAL;
import connect5.ia.models.Move;
import org.omg.CORBA.INTERNAL;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

/**
 * Created by MB on 3/30/14.
 */


public class MinMax {



    static boolean pruned = false;
    static int current_player;
    static int opponent;
    static int MAX_DEPTH = 2;


    static int nbcol;


    static int nbMAX;
    static int nbMIN;


    static Map<Etat,int[]> closelist = new HashMap<Etat, int[]>();

    public static int getMove(Etat etatInitial, int player_color,int alpha, int beta){
        nbcol = etatInitial.nbcol;
        /* Si le joueur est rouge alors son opposent est Jaune*/
        opponent = (player_color == 1)? 2 : 1;
        current_player = player_color;

        long time = System.currentTimeMillis();

        System.out.println(" TRY TO MAX :" +current_player + " AND MIN :" +opponent);
        int[] play = minmax(etatInitial,0, current_player,Integer.MIN_VALUE, Integer.MAX_VALUE,0);
        System.out.println("score :"+ play[1] + " play :("+play[0]/nbcol+","+play[0]%nbcol+") ---- nbMAX: "+ nbMAX+" - nbMIN: " + nbMIN );
        System.out.println("TIME: "+(System.currentTimeMillis() - time)+" ms");
        System.out.println("Closelist size: "+closelist.size());
        System.out.println(etatInitial.toStringOneDim(etatInitial.one_dim));

        return play[0];
    }

    public static int[] minmax(Etat etat, int depth,int player,int alpha,int beta,int last_score) {

        int last_player = (player ==1)? 2:1;

        int winner = etat.checkWinner();
        if( winner!= 0 || etat.isTerminal() || depth == MAX_DEPTH){

            int deep_penality = (player != current_player) ? depth : 0-depth;
            if(winner!=0){
                int t = get_score_winner(winner,player,depth);
               // System.out.println("WINNER :"+winner+"  result:"+t+"  player:"+player);

              //  closelist.put(etat,t);

                return new int[] {-1, t};
            }

     /*       int score_opponent = etat.evaluate3(opponent);
            int score_player = etat.evaluate3(current_player);*/
            int score= last_score; //score_player - score_opponent;
           // System.out.println(" score :"+score);
            /*if(last_player == opponent){
                score = 0 -score;
            }*/

            //closelist.put(etat,last_score);

            return new int[] {-1,score +deep_penality} ;
        }


        PriorityQueue<Move> nextMoves = etat.getNextMoves(player);
        int bestScore;
        int currentScore;
        int bestMove = (player == current_player)? Integer.MIN_VALUE : Integer.MAX_VALUE;

        /*if(nextMoves.isEmpty() || depth == 0){
            bestScore = evaluer(grid1);
            return  new int[] {bestMove,bestScore};
        }*/
        int test = 0;
        while (!nextMoves.isEmpty()){

            Move move = nextMoves.poll();

/*

            if(test == 30){
                break;
            }
            test++;
*/


            Etat next_step = etat.clone();
            next_step.play(move.move, player);

            /*if(closelist.containsKey(next_step)){
                System.out.println("ici");
                return closelist.get(next_step);
            }*/
            next_step.score = move.score;


            if(player == current_player ){
                nbMAX++;
                currentScore = minmax(next_step,depth+1, opponent, alpha,beta,move.score)[1];

                if(bestMove == Integer.MIN_VALUE){
                    bestMove = move.move;
                    alpha = currentScore;
                }

                if(currentScore > alpha){
                    alpha = currentScore;
                    bestMove = move.move;
                }
                if(alpha >= beta){
                    closelist.put(next_step,new int[]{bestMove,alpha});
                    return new int[] {bestMove, alpha};
                }


            }else {
                nbMIN++;
                currentScore = minmax(next_step,depth+1,current_player,alpha,beta,move.score)[1];
                if(bestMove == Integer.MAX_VALUE){
                    bestMove = move.move;
                    beta = currentScore;
                }
                if(currentScore < beta){
                    beta = currentScore;
                    bestMove = move.move;

                }
                if(beta <= alpha){

                    closelist.put(next_step,new int[]{bestMove,beta});
                    return new int[] {bestMove, beta};
                }


            }
            // reset la case?
        }

        return new int[] {bestMove, ((player == current_player)? alpha: beta)};

    }



    private static int evaluer(Etat e,int turn){


        int winner = e.evaluate();

        if(winner !=0){
            if(turn == current_player){
                return winner;
            }else if(turn != current_player){
                return 0-winner;
            }
            
        }
        return 0;

/*
        int eval = grid.evaluate(turn);
        if(eval != 0){
            if(turn == opponent) {
                return 0 - eval;
            }else {
                return eval;
            }
        }else{
            int opposed = (turn == current_player)? opponent : current_player;
            eval = grid.evaluate(opposed);
            if( opposed== opponent) {
                return 0- eval;
            }else {
                return eval;
            }
        }
*/

    }

    private static int get_score_winner(int winner, int turn, int depth){

        int opponnent_turn = (turn==1)? 2:1;

        if(winner == opponent){
            return (0-GLOBAL.WIN) + depth;
        }else {
            return GLOBAL.WIN - depth;
        }


    }

    private static List<Integer> getPossibleMove(Grille g){
        List<Integer> moves = new ArrayList<Integer>();

        for(int l=0;l<g.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(g.getData()[l][c]==0)
                    moves.add(l*nbcol+c);


        return moves;
    }

}
