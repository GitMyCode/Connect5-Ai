package connect5.ia.strategy;

import java.util.*;

import connect5.ia.models.*;

/**
 * Created by MB on 3/30/14.
 */

public class MinMax {
    static int currentPlayer;
    static int opponent;
    static int MAX_DEPTH = 4;

    static int nbcol;
    static int nbMAX;
    static int nbMIN;
    static int nbSaveInCloseList;

    static boolean activateLookUp = false;

    public static HashMap<Etat,Etat> closelist = new HashMap<Etat, Etat>();

    public static int[] minmax (Etat etat, int depth, int player, int alpha, int beta, int lastScore) throws TimeOver {
        /* Arreter lorsque pas de temps pour continuer explo */
        if (GLOBAL.timeUp()) {
            throw new TimeOver("Time Over");
        }

        if(activateLookUp && depth != 0){
            if(closelist.containsKey(etat)){
                Etat ref = closelist.get(etat);
                nbSaveInCloseList++;

                if(ref.lowerBound != null && ref.lowerBound >= beta && ref.maxDepth == MAX_DEPTH){
                    return new int[] {-1, ref.lowerBound};
                }
                if(ref.upperBound != null && ref.upperBound <= alpha && ref.maxDepth == MAX_DEPTH){
                    return new int[] {-1, ref.upperBound};
                }
                if(ref.lowerBound != null){
                    alpha = Math.max(alpha, ref.lowerBound);
                }
                if(ref.upperBound != null){
                    beta = Math.min(beta, ref.upperBound);
                }
            }
        }

        int winner = 0;
        if (Math.abs(lastScore + 1000) > GLOBAL.ALMOST_WIN + 40000) {
            winner = (lastScore < 0) ? opponent : currentPlayer;
        }


        if (winner != 0 || etat.isTerminal() || depth == MAX_DEPTH) {
            int deepPenality = (player != currentPlayer) ? depth : 0 - depth;
            if (winner != 0) {
                int t = getScoreWinner(winner, player, depth);
                return new int[]{-1, t};
            }

            return new int[]{-1, lastScore + deepPenality};
        }

        Integer currentScore = null;
        int bestMove = (player == currentPlayer) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        TreeSet<Move> nextMoves = etat.getNextMoves(player);

        int a = alpha; // Pour garder alpha intact
        int b = beta; // Pour garder beta intact

        while (!nextMoves.isEmpty()) {

            Move move = nextMoves.pollFirst();

            Etat next_step = etat.clone();
            next_step.depth= depth+1;
            next_step.maxDepth = MAX_DEPTH;
            next_step.playAndUpdate(move.move, player);
            //next_step.play(move.move,player);
            next_step.score = move.score; // garder l'évaluation dans l'Etat

            if (player == currentPlayer) {
                nbMAX++;

                currentScore = minmax(next_step, depth + 1, opponent, a, beta, move.score)[1];
                if (bestMove == Integer.MIN_VALUE) {
                    bestMove = move.move;
                    a = currentScore;
                }

                if (currentScore > a) {
                    a = currentScore;
                    bestMove = move.move;
                }
                if (a >= beta) {
                    break;
                }
            } else {
                nbMIN++;
                currentScore = minmax(next_step, depth + 1, currentPlayer, alpha, b, move.score)[1];
                if (bestMove == Integer.MAX_VALUE) {
                    bestMove = move.move;
                    b = currentScore;
                }
                if (currentScore < b) {
                    b = currentScore;
                    bestMove = move.move;
                }
                if (alpha >= b) {
                    break;
                }
            }
        }

        if(activateLookUp){
            Etat eRef = null;
            eRef = ((eRef =closelist.get(etat)) ==null)? etat : eRef;
            eRef.bestMove = bestMove;
            int cur = (player == currentPlayer)? a: b;
            if(cur <= alpha){
                eRef.upperBound = b;
            }else{
                eRef.lowerBound = a;
            }
            eRef.depth= depth;
            closelist.put(eRef,eRef);
        }

        return new int[]{bestMove, ((player == currentPlayer)? a : b)};

    }

    public static int[] getMove (Etat etatInitial, int playerColor, int deep) throws TimeOver {
        MAX_DEPTH = deep;
        nbcol = GLOBAL.NBCOL;
        opponent = (playerColor == 1) ? 2 : 1;
        currentPlayer = playerColor;
        nbMAX = 0;
        nbMIN = 0;
        nbSaveInCloseList =0;
        closelist.clear();

        long time = System.currentTimeMillis();
        if(deep > 2){
            activateLookUp = true;
        }else{
            activateLookUp = false;
        }

        int[] play = minmax(etatInitial, 0, currentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        GLOBAL.LAST_DEPTH = deep;

        return play;
    }

    private static int getScoreWinner (int winner, int turn, int depth) {
        if (winner == opponent) {
            return (0 - GLOBAL.WIN) + depth;
        } else {
            return GLOBAL.WIN - depth;
        }
    }

}
