package strategy;

import java.util.List;

import Models.*;

/**
 * Created by MB on 3/30/14.
 */


public class MinMax {



    static boolean pruned = false;
    static int current_player;
    static int opponent;
    static int MAX_DEPTH = 3;


    static int test_come_from = -1;
    public static int getMove(Grid grid, int player_color){

        /* Si le joueur est rouge alors son opposent est Jaune*/
        opponent = (player_color == 1)? 2 : 1;
        current_player = player_color;

        int[] play = minmax(grid,0, current_player,Integer.MIN_VALUE,Integer.MAX_VALUE,0);
        System.out.println("score :"+ play[1] + " pruned? :"+pruned);
        return play[0];
    }

    public static int[] minmax(Grid grid, int depth,int player,int alpha,int beta,int last_move) {//



        int score= (evaluer(grid, player));
        if(score != 0 || grid.isFull() || depth == MAX_DEPTH){
            // System.out.print("-------------" +
            //       "\n" + score);
            // grid1.show();
            int test =0;
            int winner = (player == current_player) ? depth : 0-depth;
            if( score == 0)
                winner =0;
            return new int[] {-1,score + winner } ;
        }


        List<Integer> nextMoves = grid.getPossibleMove();
        int bestScore;
        int currentScore;
        int bestMove = (player == current_player)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        /*if(nextMoves.isEmpty() || depth == 0){
            bestScore = evaluer(grid1);
            return  new int[] {bestMove,bestScore};
        }*/
        for(int move : nextMoves){
            Grid newGrid = (Grid) grid.clone();
            newGrid.play(move, player);
            if(player == current_player ){
                currentScore = minmax(newGrid,depth+1, opponent, alpha,beta,move)[1];
                if(bestMove == -1){
                    bestMove = move;
                    alpha = currentScore;
                }

                if(currentScore > alpha){
                    alpha = currentScore;
                    bestMove = move;
                }
            /*    if(alpha >= beta){

                    pruned = true;
                    test_come_from= 1;
                    return new int[] {-1, alpha};
                }*/


            }else {
                currentScore = minmax(newGrid,depth+1,current_player,alpha,beta,move)[1];
                if(bestMove == -1){
                    bestMove = move;
                    beta = currentScore;
                }
                if(currentScore < beta){
                    beta = currentScore;
                    bestMove = move;

                }
               /* if(beta <= alpha){
                    pruned =true;
                    test_come_from = 2;
                    return new int[] {-1, beta};
                }*/


            }
            // reset la case?
        }


        pruned = false;
        return new int[] {bestMove, ((player == current_player)? alpha: beta)};

    }


    private static int evaluer(Grid grid,int turn){

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

    }
}