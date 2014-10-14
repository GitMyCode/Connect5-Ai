package connect5.ia.strategy;

import connect5.Grille;
import connect5.ia.models.*;

import java.util.*;

/**
 * Created by MB on 3/30/14.
 */


public class MinMax {


    static boolean pruned = false;
    static int currentPlayer;
    static int opponent;
    static int MAX_DEPTH = 4;


    static int nbcol;


    static int nbMAX;
    static int nbMIN;


    static Map<Etat, int[]> closelist = new HashMap<Etat, int[]>();

    public static Integer getMove (Etat etatInitial, int playerColor, int deep) throws Exception {
        MAX_DEPTH = deep;
        nbcol = GLOBAL.NBCOL;
        opponent = (playerColor == 1) ? 2 : 1;
        currentPlayer = playerColor;
        nbMAX = 0;
        nbMIN = 0;


        long time = System.currentTimeMillis();

        System.out.println(" TRY TO MAX :" + currentPlayer + " AND MIN :" + opponent);
        int[] play = minmax(etatInitial, 0, currentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        if (play == null) {
            return null;
        }

        System.out.println("score :" + play[1] + " play :(" + play[0] / nbcol + "," + play[0] % nbcol + ") ---- nbMAX: " + nbMAX + " - nbMIN: " + nbMIN);
        System.out.println("TIME: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println("Closelist size: " + closelist.size());
        etatInitial.play(play[0], currentPlayer);
        System.out.println(etatInitial.toStringOneDim(etatInitial.one_dim));
        etatInitial.unplay(play[0]);
        return play[0];
    }

    public static int[] minmax (Etat etat, int depth, int player, int alpha, int beta, int lastScore) throws Exception {

        if (GLOBAL.timeRemaining() < 100) {
            System.out.println("GOOOO BACK!!");
            throw new TimeOver("Time Over");
        }


        int winner = 0;
        if (Math.abs(lastScore + 10000) > 50000) {
            winner = (lastScore < 0) ? opponent : currentPlayer;
        }

        if (winner != 0 || etat.isTerminal() || depth == MAX_DEPTH) {

            int deepPenality = (player != currentPlayer) ? depth : 0 - depth;
            if (winner != 0) {
                int t = getScoreWinner(winner, player, depth);
                //  closelist.put(etat,t);
                return new int[]{-1, t};
            }
            int score = lastScore;

            return new int[]{-1, score + deepPenality};
        }


        PriorityQueue<Move> nextMoves = etat.getNextMoves(player);
        int bestScore;
        Integer currentScore = null;
        int bestMove = (player == currentPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        int test = 0;
        while (!nextMoves.isEmpty()) {

            Move move = nextMoves.poll();




/*
            if(test == 40){
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


            if (player == currentPlayer) {
                nbMAX++;
                Object receive = minmax(next_step, depth + 1, opponent, alpha, beta, move.score)[1];
                if (receive == null) return null;
                currentScore = (Integer) receive;
                if (bestMove == Integer.MIN_VALUE) {
                    bestMove = move.move;
                    alpha = currentScore;
                }

                if (currentScore > alpha) {
                    alpha = currentScore;
                    bestMove = move.move;
                }
                if (alpha >= beta) {
                    closelist.put(next_step, new int[]{bestMove, alpha});
                    return new int[]{bestMove, alpha};
                }


            } else {
                nbMIN++;
                Object receive = minmax(next_step, depth + 1, currentPlayer, alpha, beta, move.score)[1];
                if (receive == null) return null;
                currentScore = (Integer) receive;
                if (bestMove == Integer.MAX_VALUE) {
                    bestMove = move.move;
                    beta = currentScore;
                }
                if (currentScore < beta) {
                    beta = currentScore;
                    bestMove = move.move;

                }
                if (beta <= alpha) {

                    closelist.put(next_step, new int[]{bestMove, beta});
                    return new int[]{bestMove, beta};
                }


            }
            // reset la case?
        }

        return new int[]{bestMove, ((player == currentPlayer) ? alpha : beta)};

    }


    private static int evaluer (Etat e, int turn) {


        int winner = e.evaluate();

        if (winner != 0) {
            if (turn == currentPlayer) {
                return winner;
            } else if (turn != currentPlayer) {
                return 0 - winner;
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
            int opposed = (turn == currentPlayer)? opponent : currentPlayer;
            eval = grid.evaluate(opposed);
            if( opposed== opponent) {
                return 0- eval;
            }else {
                return eval;
            }
        }
*/

    }

    private static int getScoreWinner (int winner, int turn, int depth) {


        if (winner == opponent) {
            return (0 - GLOBAL.WIN) + depth;
        } else {
            return GLOBAL.WIN - depth;
        }


    }

    private static List<Integer> getPossibleMove (Grille g) {
        List<Integer> moves = new ArrayList<Integer>();

        for (int l = 0; l < g.getData().length; l++)
            for (int c = 0; c < nbcol; c++)
                if (g.getData()[l][c] == 0)
                    moves.add(l * nbcol + c);


        return moves;
    }

}
