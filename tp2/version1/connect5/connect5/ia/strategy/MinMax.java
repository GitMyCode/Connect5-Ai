package connect5.ia.strategy;

import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Position;
import connect5.ia.models.Etat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MB on 3/30/14.
 */


public class MinMax {



    static boolean pruned = false;
    static int current_player;
    static int opponent;
    static int MAX_DEPTH = 5;


    static int nbcol;

    static int test_come_from = -1;
    public static int getMove(Etat etatInitial, int player_color){
        nbcol = etatInitial.nbcol;
        /* Si le joueur est rouge alors son opposent est Jaune*/
        opponent = (player_color == 1)? 2 : 1;
        current_player = player_color;

        int[] play = minmax(etatInitial,0, current_player,Integer.MIN_VALUE,Integer.MAX_VALUE,0);
        System.out.println("score :"+ play[1] + " pruned? :"+pruned);
        return play[0];
    }

    public static int[] minmax(Etat etat, int depth,int player,int alpha,int beta,int last_move) {//



        int score= (evaluer(etat, player));
        if(score != 0 || etat.isTerminal() || depth == MAX_DEPTH){

            int winner = (player == current_player) ? depth : 0-depth;
            if( score == 0)
                winner =0;
            return new int[] {-1,score + winner } ;
        }


        List<Integer> nextMoves = etat.getNextMoves();
        int bestScore;
        int currentScore;
        int bestMove = (player == current_player)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        /*if(nextMoves.isEmpty() || depth == 0){
            bestScore = evaluer(grid1);
            return  new int[] {bestMove,bestScore};
        }*/
        for(int move : nextMoves){
            Etat next_step = etat.clone();
            next_step.play( move, player);
            if(player == current_player ){
                currentScore = minmax(next_step,depth+1, opponent, alpha,beta,move)[1];
                if(bestMove == -1){
                    bestMove = move;
                    alpha = currentScore;
                }

                if(currentScore > alpha){
                    alpha = currentScore;
                    bestMove = move;
                }
                if(alpha >= beta){

                    return new int[] {-1, alpha};
                }


            }else {
                currentScore = minmax(next_step,depth+1,current_player,alpha,beta,move)[1];
                if(bestMove == -1){
                    bestMove = move;
                    beta = currentScore;
                }
                if(currentScore < beta){
                    beta = currentScore;
                    bestMove = move;

                }
                if(beta <= alpha){
                    return new int[] {-1, beta};
                }


            }
            // reset la case?
        }


        pruned = false;
        return new int[] {bestMove, ((player == current_player)? alpha: beta)};

    }



    private static int evaluer(Etat e,int turn){


        int winner = e.checkWinner();

        if(winner !=0){
            if(winner == current_player){
                return 100;
            }else if(winner != current_player){
                return -100;
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

    private static List<Integer> getPossibleMove(Grille g){
        List<Integer> moves = new ArrayList<Integer>();

        for(int l=0;l<g.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(g.getData()[l][c]==0)
                    moves.add(l*nbcol+c);


        return moves;
    }

}
