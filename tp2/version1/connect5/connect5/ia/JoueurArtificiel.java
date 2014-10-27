package connect5.ia;

import java.util.*;

import connect5.Grille;
import connect5.Joueur;
import connect5.Position;
import connect5.ia.Utilitaires.Util;
import connect5.ia.models.*;
import connect5.ia.strategy.MinMax;


public class JoueurArtificiel implements Joueur{

    private final Random random = new Random();

    private int nbcol = 0;
    private int nbligne = 0;

    private int bufferX;
    private int bufferY;
    private int lowestX;
    private int lowestY;


    public JoueurArtificiel () {

    }

    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans JoueurArtificiel.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package connect5.ia.
     * Vous de pouvez pas modifier les fichiers directement dans connect., car ils seront écrasés.
     *
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup (Grille grille, int delais) {
        GLOBAL.startTimer(delais);
        GLOBAL.FULL_NBCOL = grille.getData()[0].length;
        GLOBAL.FULL_NBLIGNE = grille.getData().length;

        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        for (int l = 0; l < GLOBAL.FULL_NBLIGNE; l++)
            for (int c = 0; c < GLOBAL.FULL_NBCOL; c++)
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * GLOBAL.FULL_NBCOL + c);




        /*Trouve pour qui on jouer et contre qui*/
        GLOBAL.MAX = (((GLOBAL.FULL_NBCOL * GLOBAL.FULL_NBLIGNE) - casesvides.size()) % 2 == 0) ? 1 : 2;
        GLOBAL.MIN = (GLOBAL.MAX == 1) ? 2 : 1;

        /* Si la grille est vide, jouer au centre */
        if (casesvides.size() == (GLOBAL.FULL_NBCOL * GLOBAL.FULL_NBLIGNE)) {
            System.out.println("first hit");
            return new Position(GLOBAL.FULL_NBLIGNE / 2, GLOBAL.FULL_NBCOL / 2);
        }

        /* INITIALISATION du premier etat:  Reduction de la grille et l'ajouter a l'état */
        Etat init = getInitState(grille);

        /* Vérifier si possibilité de gagner immédiatement */
        TreeSet<Move> pq = init.getNextMoves(GLOBAL.MAX);
        Move MAXcheckWinMove = pq.pollFirst();
        if (MAXcheckWinMove.score == GLOBAL.WIN) {
            System.out.println("Try WIN: (" + (MAXcheckWinMove.move / GLOBAL.NBCOL) + "," + (MAXcheckWinMove.move % GLOBAL.NBCOL) + ")");
            int choix_converted = getMoveCutedGridToFullGrid(MAXcheckWinMove.move);

            return new Position(choix_converted / GLOBAL.FULL_NBCOL, choix_converted % GLOBAL.FULL_NBCOL);
        }

        System.out.println("AI : JOUEUR " + GLOBAL.MAX);
        int deep = 2;
        List<Integer> moves = new ArrayList<Integer>();
        moves.add(MAXcheckWinMove.move);
        System.out.println(GLOBAL.showTimeRemain());
        System.out.println(" TRY TO MAX :" + GLOBAL.MAX + " AND MIN :" + GLOBAL.MAX);


        /************************* BOUCLE MINMAX ********************************/
        int[] res = null;
        boolean stoped = false;
        while (!stoped) {
            try {
                System.out.println("try deep:   " + deep);
                res = MinMax.getMove(init, GLOBAL.MAX, deep);
                deep += (deep >= 4) ? 1 : 2;
                if (res != null)
                    moves.add(res[0]);

                if (res[1] >= GLOBAL.WIN - 4000) {
                    stoped = true;
                }

            } catch (TimeOver e) {
                System.out.println(e);
                stoped = true;
            }
        }
        MinMax.closelist.clear(); /* Clear  le memo des etats */
        /***************************- FIN BOUCLE MINMAX -******************************/


        int choix = moves.get(moves.size() - 1);
        if (res != null) {
            System.out.println("----------------------------- LAST DEPTH : " + GLOBAL.LAST_DEPTH);
            System.out.println("score :" + res[1] + " play :(" + res[0] / GLOBAL.NBCOL + "," + res[0] % GLOBAL.NBCOL + ")");
            init.play(res[0], 3);
            System.out.println(Util.toStringOneDim(init.one_dim));
            init.unplay(res[0]);
        } else {
            System.out.println("res est  null----------------------");
            System.out.println("score :" + MAXcheckWinMove.score + " play :(" + choix / GLOBAL.NBCOL + "," + choix % GLOBAL.NBCOL + ")");
        }

        int choix_converted = getMoveCutedGridToFullGrid(choix);
        return new Position(choix_converted / GLOBAL.FULL_NBCOL, choix_converted % GLOBAL.FULL_NBCOL);
    }

    /**
     * Obtention du premier Etat
     * */
    public Etat getInitState(Grille grille) {
        byte[] myGrid;
        if (grille.getSize() > 81) {
            byte[][] firstCut = cutGrid(grille);
            byte[] finalCut = oneDimentionalArray(firstCut);
            System.out.println(toStringOneDimWithCol(finalCut, firstCut[0].length, firstCut.length));
            GLOBAL.NBCOL = firstCut[0].length;
            GLOBAL.NBLIGNE = firstCut.length;
            myGrid = finalCut;
        } else {
            myGrid = oneDimentionalArray(grille.getData());
            GLOBAL.NBCOL = GLOBAL.FULL_NBCOL;
            GLOBAL.NBLIGNE = GLOBAL.FULL_NBLIGNE;
        }

        /*INIT ETAT*/
        GLOBAL.bufferX = bufferX;
        GLOBAL.bufferY = bufferY;
        GLOBAL.lowestX = lowestX;
        GLOBAL.lowestY = lowestY;
        Etat init = new Etat(myGrid, GLOBAL.MAX, GLOBAL.MIN);
        init.initMemo();
//        Direction.init_map(nbcol);
        return init;
    }

    private byte[] oneDimentionalArray (byte[][] grille) {
        int nbligne = grille.length;
        int nbcol = grille[0].length;

        byte[] one_dim = new byte[grille.length * grille[0].length];

        for (int l = 0; l < nbligne; l++) {
            System.arraycopy(grille[l], 0, one_dim, l * (nbcol), nbcol);
        }

        return one_dim;
    }

    /*
    * Fonction pour les tests
    * */
    public Position getProchainCoupTEST (Grille grille, int maxDepth) {
        GLOBAL.startTimer(Integer.MAX_VALUE);
        GLOBAL.FULL_NBCOL = grille.getData()[0].length;
        GLOBAL.FULL_NBLIGNE = grille.getData().length;

        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        for (int l = 0; l < GLOBAL.FULL_NBLIGNE; l++)
            for (int c = 0; c < GLOBAL.FULL_NBCOL; c++)
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * GLOBAL.FULL_NBCOL + c);

        GLOBAL.MAX = (((GLOBAL.FULL_NBCOL * GLOBAL.FULL_NBLIGNE) - casesvides.size()) % 2 == 0) ? 1 : 2;
        GLOBAL.MIN = (GLOBAL.MAX == 1) ? 2 : 1;

        if (casesvides.size() == (GLOBAL.FULL_NBCOL * GLOBAL.FULL_NBLIGNE)) {
            System.out.println("first hit");
            return new Position(GLOBAL.FULL_NBLIGNE / 2, GLOBAL.FULL_NBCOL / 2);
        }

        Etat init = getInitState(grille);

        System.out.println("AI : JOUEUR " + GLOBAL.MAX);
        int deep = 2;
        List<Integer> moves = new ArrayList<Integer>();
        moves.add(init.getNextMoves(GLOBAL.MAX).pollFirst().move);
        System.out.println(GLOBAL.showTimeRemain());
        System.out.println(" TRY TO MAX :" + GLOBAL.MAX + " AND MIN :" + GLOBAL.MIN);

        int[] res = null;
        boolean stoped = false;
        while (deep <= maxDepth && !stoped) {
            try {
                System.out.println("try deep:   " + deep);
                res = MinMax.getMove(init, GLOBAL.MAX, deep);
                deep += (deep >= 4) ? 1 : 2;
                if (res != null)
                    moves.add(res[0]);

                if (res[1] >= GLOBAL.WIN - 4000) {
                    stoped = true;
                }

            } catch (TimeOver e) {
                System.out.println(e);
                stoped = true;
            }
        }
        MinMax.closelist.clear();
        int choix = moves.get(moves.size() - 1);
        if (res != null) {
            System.out.println("----------------------------- LAST DEPTH : " + GLOBAL.LAST_DEPTH);
            System.out.println("score :" + res[1] + " play :(" + res[0] / GLOBAL.NBCOL + "," + res[0] % GLOBAL.NBCOL + ")");
            init.play(res[0], 3);
            System.out.println(Util.toStringOneDim(init.one_dim));
            init.unplay(res[0]);
        } else {
            System.out.println("res est  null----------------------");
            System.out.println("play :(" + choix / GLOBAL.NBCOL + "," + choix % GLOBAL.NBCOL + ")");
        }

        int choix_converted = getMoveCutedGridToFullGrid(choix);
        return new Position(choix_converted / GLOBAL.FULL_NBCOL, choix_converted % GLOBAL.FULL_NBCOL);
    }


    /* Cette methode convertit le point qui est sur la grille coupé vers un point sur la vrai grille*/
    private int getMoveCutedGridToFullGrid (int move) {
        int pos_x, pos_y;
        pos_x = ((move / GLOBAL.NBCOL)) + (lowestX - bufferX);
        pos_y = ((move % GLOBAL.NBCOL)) + (lowestY - bufferY);

        return pos_x * GLOBAL.FULL_NBCOL + pos_y;
    }


    /* Couper la grille*/
    public byte[][] cutGrid (Grille grille) {
        int lenghtX = GLOBAL.FULL_NBLIGNE;
        int lengthY = GLOBAL.FULL_NBCOL;

        int[] hx = new int[2];
        int[] lx = new int[2];
        int[] hy = new int[2];
        int[] ly = new int[2];

        boolean bhx = false;
        boolean blx = false;
        boolean bhy = false;
        boolean bly = false;

        byte[][] data = grille.getData();
        for (int i = 0; i < lenghtX && !blx; i++) {
            for (int j = 0; j < lengthY; j++) {
                if (data[i][j] != 0) {
                    lx[0] = i;
                    lx[1] = j;
                    blx = true;
                    break;
                }
            }
        }

        for (int i = 0; i < lenghtX; i++) {
            for (int j = lengthY - 1; j >= 0; j--) {
                if (data[i][j] != 0 && j > hy[1]) {
                    hy[0] = i;
                    hy[1] = j;
                    break;
                }
            }
        }

        for (int i = lenghtX - 1; i >= 0 && !bhx; i--) {
            for (int j = 0; j < lengthY; j++) {
                if (data[i][j] != 0) {
                    hx[0] = i;
                    hx[1] = j;
                    bhx = true;
                    break;
                }
            }
        }

        ly[1] = Integer.MAX_VALUE;
        for (int i = 0; i < lenghtX; i++) {
            for (int j = 0; j < lengthY; j++) {
                if (data[i][j] != 0 && j < ly[1]) {
                    ly[0] = i;
                    ly[1] = j;
                    break;
                }
            }
        }


        lowestX = lx[0];
        lowestY = ly[1];
        int highestX = hx[0];
        int highestY = hy[1];


        int cutedNoBuffX = (hx[0] - lx[0] + 1);
        int cutedNoBuffY = (hy[1] - ly[1] + 1);


        int extendBufferX = (cutedNoBuffX > 5) ? 3 : 3;
        int extendBufferY = (cutedNoBuffY > 5) ? 3 : 3;


        int extendedX = (cutedNoBuffX) + (extendBufferX * 2);
        int extendedY = (cutedNoBuffY) + (extendBufferY * 2);

        int leftBuffer = (lowestY - extendBufferY > 0) ? extendBufferY : lowestY;
        int rightBuffer = ((extendBufferY) > GLOBAL.FULL_NBCOL - (highestY + 1)) ? GLOBAL.FULL_NBCOL - (highestY + 1) : (extendBufferY);
        int topBuffer = (lowestX - extendBufferX > 0) ? extendBufferX : lowestX;
        int downBuffer = (extendBufferX > (GLOBAL.FULL_NBLIGNE - (highestX + 1))) ? GLOBAL.FULL_NBLIGNE - (highestX + 1) : extendBufferX;


        bufferY = leftBuffer;
        bufferX = topBuffer;

        int smallSizeX = downBuffer + topBuffer + (hx[0] - lx[0] + 1);
        int smallSizeY = leftBuffer + rightBuffer + (hy[1] - ly[1] + 1);

        /*TODO: remplacer par System.arrayCopy()*/
      /*  int x =  (((smallx - extendBufferX) +lx[0] ) > GLOBAL.FULL_NBLIGNE)? 0: lx[0];
        int y =  (((smally - extendBufferY) +ly[1] ) > GLOBAL.FULL_NBLIGNE)? 0: ly[1];*/
        System.out.println("smallSizeY: " + smallSizeY + "      smallSizeX: " + smallSizeX);
        System.out.println("left_buffer: " + leftBuffer + "       rightBuffer: " + rightBuffer);
        System.out.println("topBuffer: " + topBuffer + "     downBuffer: " + downBuffer);
        byte[][] cpy = new byte[smallSizeX][smallSizeY];
        for (int i = 0; i < cutedNoBuffX; i++) {
            for (int j = 0; j < cutedNoBuffY; j++) {
                int t = data[lowestX + i][lowestY + j];
                cpy[i + topBuffer][j + leftBuffer] = (byte) t;
            }
        }


       /*
        cpy[hx[0]][hx[1]] = 3;
        cpy[lx[0]][lx[1]] = 4;
        cpy[hy[0]][hy[1]] = 5;
        cpy[ly[0]][ly[1]] = 6;
*/

        return cpy;

    }

    public String toStringOneDim (byte[] data) {
        char[] table = {'-', 'N', 'B', 'n', 's', 'e', 'o'};
        String result = "" + nbligne + " " + nbcol + "\n";

        int i = 1;
        for (byte b : data) {
            char c = (char) b;
            result += table[b];
            if (i % nbcol == 0) {
                result += '\n';
            }
            i++;
        }
        return result;
    }

    public String toStringOneDimWithCol (byte[] data, int nbcol, int nbligne) {
        char[] table = {'-', 'N', 'B', 'n', 's', 'e', 'o'};
        String result = "" + nbligne + " " + nbcol + "\n";

        int i = 1;
        for (byte b : data) {
            char c = (char) b;
            result += table[b];
            if (i % nbcol == 0) {
                result += '\n';
            }
            i++;
        }

        return result;
    }



    @Override
    public String getAuteurs () {
        return "Martin Bouchard (BOUM15078700) et Nilovna Bascunan-Vasquez(BASN22518900)";
    }
}


