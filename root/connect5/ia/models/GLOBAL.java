package connect5.ia.models;

/**
 * Created by MB on 10/9/2014.
 * Diverses variables utilisees a travers le projet
 */
public abstract class GLOBAL {

    /* Joueur MIN et MAX */
    public static int MAX;
    public static int MIN;

    /* Nombre de lignes / colonnes dans grille reduite */
    public static int NBCOL;
    public static int NBLIGNE;

    /* Nombre lignes / colonnes dans grille complete */
    public static int FULL_NBCOL;
    public static int FULL_NBLIGNE;

    /* La derniere profondeur atteinte par MinMax */
    public static int LAST_DEPTH;

    /* Le "buffer" (espace supplementaire) pour la grille reduite */
    public static int bufferX;
    public static int bufferY;


    public static int lowestX;
    public static int lowestY;

    public static final int ALMOST_WIN = 5000000; // 50 million
    public static final int WIN = 100000000; // 100 million
    public static final int CONNECT4_SCORE = 100000; // 1 million

    public static final int LIMITE = 80;

    /* Variables pour le timer */
    public static long timer;
    public static long remain;
    public static boolean END = false;

    /**
     * Demarre le timer
     * @param delai Entier du temps maximum, en milliseconde
     */
    public static void startTimer(int delai){
        END = false;
        timer = System.currentTimeMillis();
        remain = delai;
    }

    public static String showTimeRemain(){
        return ("Time: " + (remain - (System.currentTimeMillis() - timer)) + " ms");
    }

    /**
     * @return Retourne le temps restant
     */
    public static long timeRemaining(){
        long passed = (System.currentTimeMillis() - timer);
        return remain - passed;
    }

    /***
     * Indique si le temps est écoulé
     * @return true si temps écoulé
     */
    public static boolean timeUp(){
        if(END){
            return true;
        }

        if(timeRemaining() < LIMITE){
            END = true;
            return true;
        }

        return false;
    }

    private int getMoveCutedGridToFullGrid(int move){
        int pos_x,pos_y;
        pos_x =  ((move/GLOBAL.NBCOL) ) + (lowestX - bufferX);
        pos_y = ((move%GLOBAL.NBCOL) ) + (lowestY - bufferY);

        return  pos_x * GLOBAL.FULL_NBCOL + pos_y;
    }

    public String toStringOneDim(byte[] data, int nbcol){
        char[] table = {'-', 'N', 'B' };
        String result =  nbcol+ "\n";

        int i = 1;
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
}
