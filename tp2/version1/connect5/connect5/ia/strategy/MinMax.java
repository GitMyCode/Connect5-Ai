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
    static int nbSaveInCloseList;

    static int alpha = Integer.MIN_VALUE;
    static int beta = Integer.MAX_VALUE;


    //static Map<Etat, int[]> closelist = new HashMap<Etat, int[]>();
    static HashMap<Etat,Integer> closelist = new HashMap<Etat, Integer>();

    public static Integer getMove (Etat etatInitial, int playerColor, int deep) throws Exception {
        MAX_DEPTH = deep;
        nbcol = GLOBAL.NBCOL;
        opponent = (playerColor == 1) ? 2 : 1;
        currentPlayer = playerColor;
        nbMAX = 0;
        nbMIN = 0;
        nbSaveInCloseList =0;
        closelist.clear();


        long time = System.currentTimeMillis();

        System.out.println(" TRY TO MAX :" + currentPlayer + " AND MIN :" + opponent + "    DEEP: "+ deep);
        int[] play = minmax(etatInitial, 0, currentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        System.out.println("score :" + play[1] + " play :(" + play[0] / nbcol + "," + play[0] % nbcol + ") ---- nbMAX: " + nbMAX + " - nbMIN: " + nbMIN);
        System.out.println("TIME: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println("Closelist size: " + closelist.size() + "  nbSave: "+nbSaveInCloseList);
        etatInitial.play(play[0], currentPlayer);
        System.out.println(etatInitial.toStringOneDim(etatInitial.one_dim));
        etatInitial.unplay(play[0]);
        return play[0];
    }

    public static int[] minmax (Etat etat, int depth, int player, int alpha, int beta, int lastScore) throws Exception {

        if (GLOBAL.timeUp()) {
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




            Etat next_step = etat.clone();
            next_step.play(move.move, player);


            next_step.score = move.score;


            if (player == currentPlayer) {
                nbMAX++;
                currentScore = minmax(next_step, depth + 1, opponent, alpha, beta, move.score)[1];
                if (bestMove == Integer.MIN_VALUE) {
                    bestMove = move.move;
                    alpha = currentScore;
                }

                if (currentScore > alpha) {
                    alpha = currentScore;
                    bestMove = move.move;
                }
                if (alpha >= beta) {
                    //closelist.put(next_step, new int[]{bestMove, alpha});
                    break;
                    //return new int[] {bestMove, alpha};
                    //return new int[]{-1, Integer.MAX_VALUE};
                }


            } else {
                nbMIN++;
                currentScore = minmax(next_step, depth + 1, currentPlayer, alpha, beta, move.score)[1];
                if (bestMove == Integer.MAX_VALUE) {
                    bestMove = move.move;
                    beta = currentScore;
                }
                if (currentScore < beta) {
                    beta = currentScore;
                    bestMove = move.move;

                }
                if (alpha >= beta) {

                    break;
                    //closelist.put(next_step, new int[]{bestMove, beta});
                    //return new int[] {bestMove, beta};
                    //return new int[]{-1, Integer.MIN_VALUE};

                }


            }
            // reset la case?
        }

        closelist.put(etat,0);

        return new int[]{bestMove, ((player == currentPlayer) ? alpha : beta)};

    }


    private class Key {
        Etat etat;
        boolean isLowerBound= false;
        int score;

        public Key(Etat e,Integer score){
            etat = e;
            this.score = score;
        }

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
