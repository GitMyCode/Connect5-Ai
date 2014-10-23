package connect5.ia;


import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Joueur;
import connect5.Position;
import connect5.ia.Utilitaires.Util;
import connect5.ia.models.*;
import connect5.ia.strategy.MinMax;
import connect5.ia.strategy.NegaScout;

import java.awt.image.LookupTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class JoueurArtificiel implements Joueur, Runnable {

    private final Random random = new Random();
    private static final GrilleVerificateur checker = new GrilleVerificateur();

    private int nbcol=0;
    private int nbligne=0;


    private int bufferX;
    private int bufferY;
    private int lowestX;
    private int lowestY;



    public JoueurArtificiel(){

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
    public Position getProchainCoup(Grille grille, int delais) {
        GLOBAL.startTimer(delais);
        GLOBAL.FULL_NBCOL = grille.getData()[0].length;
        GLOBAL.FULL_NBLIGNE = grille.getData().length;

        Etat init = getInitStat(grille);


        if(init.getNblibre() == (GLOBAL.FULL_NBCOL * GLOBAL.FULL_NBLIGNE)){
            System.out.println("first hit");
            return new Position(GLOBAL.FULL_NBLIGNE/2,GLOBAL.FULL_NBCOL/2);
        }

        PriorityQueue<Move>  pq = init.getNextMoves(GLOBAL.MAX);

        Move MAXcheckWinMove =  pq.poll();
        if(MAXcheckWinMove.score == GLOBAL.WIN){

            System.out.println("Try WIN: ("+(MAXcheckWinMove.move/GLOBAL.NBCOL)+","+(MAXcheckWinMove.move%GLOBAL.NBCOL)+")");
            int choix_converted = getMoveCutedGridToFullGrid(MAXcheckWinMove.move);

            return new Position(choix_converted/GLOBAL.FULL_NBCOL,choix_converted%GLOBAL.FULL_NBCOL);
        }
        /*Move MINcheckWinMove = init.getNextMoves(opponent).poll();
        if(MINcheckWinMove.score == (0-GLOBAL.WIN)){
            System.out.println("Try to save: ("+(MINcheckWinMove.move/GLOBAL.NBCOL)+","+(MINcheckWinMove.move%GLOBAL.NBCOL)+")");
            int choix_converted = getMoveCutedGridToFullGrid(MINcheckWinMove.move);

            return new Position(choix_converted/GLOBAL.FULL_NBCOL,choix_converted%GLOBAL.FULL_NBCOL);
        }*/


        System.out.println("AI : JOUEUR "+GLOBAL.MAX);
        int deep = 2;
        List<Integer> moves = new ArrayList<Integer>();
        moves.add(MAXcheckWinMove.move);
        System.out.println(GLOBAL.showTimeRemain());
        System.out.println(" TRY TO MAX :" + GLOBAL.MAX + " AND MIN :" + GLOBAL.MAX );

        int[] res = null;
        boolean stoped = false;
        while (!stoped ){
            try {
                System.out.println("try deep:   " + deep);
                res = MinMax.getMove(init, GLOBAL.MAX, deep);
                deep += (deep >= 4)? 1 : 2;
                if (res !=null)
                    moves.add(res[0]);

                if(res[1] >= GLOBAL.WIN - 4000){
                    stoped= true;
                }

            }catch (TimeOver e){
                System.out.println(e);
                stoped=true;
            }
        }
        MinMax.closelist.clear();
        int choix = moves.get(moves.size()-1);
        if(res != null){
            System.out.println("----------------------------- LAST DEPTH : "+GLOBAL.LAST_DEPTH);
            System.out.println("score :" + res[1] + " play :(" + res[0] / GLOBAL.NBCOL + "," + res[0] % GLOBAL.NBCOL + ")");
            init.play(res[0], 3);
            System.out.println(Util.toStringOneDim(init.one_dim));
            init.unplay(res[0]);
        }else {
            System.out.println("res est  null----------------------");
            System.out.println("score :" + MAXcheckWinMove.score + " play :(" + choix / GLOBAL.NBCOL + "," + choix% GLOBAL.NBCOL + ")");
        }

        int choix_converted = getMoveCutedGridToFullGrid(choix);
        return new Position(choix_converted/GLOBAL.FULL_NBCOL,choix_converted%GLOBAL.FULL_NBCOL);
    }

    public Etat getInitStat(Grille grille){

        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        for(int l=0;l<GLOBAL.FULL_NBLIGNE;l++)
            for(int c=0;c<GLOBAL.FULL_NBCOL;c++)
                if(grille.getData()[l][c]==0)
                    casesvides.add(l*GLOBAL.FULL_NBCOL+c);

        GLOBAL.MAX = ( ((GLOBAL.FULL_NBCOL*GLOBAL.FULL_NBLIGNE) - casesvides.size()) % 2  == 0)? 1:2;
        GLOBAL.MIN = (GLOBAL.MAX==1)? 2:1;

        byte[] myGrid;
        if(grille.getSize() > 81){
            byte[][] firstCut = cutGrid(grille);
            byte[] finalCut = oneDimentionalArray(firstCut);
            System.out.println(toStringOneDimWithCol(finalCut,firstCut[0].length,firstCut.length));
            GLOBAL.NBCOL = firstCut[0].length;
            GLOBAL.NBLIGNE = firstCut.length;
            myGrid = finalCut;
        }else {
            myGrid = oneDimentionalArray(grille.getData());
            GLOBAL.NBCOL = GLOBAL.FULL_NBCOL;
            GLOBAL.NBLIGNE = GLOBAL.FULL_NBLIGNE;
        }


        /*INIT ETAT*/
        GLOBAL.bufferX= bufferX; GLOBAL.bufferY = bufferY;
        GLOBAL.lowestX = lowestX;GLOBAL.lowestY = lowestY;
        Etat init = new Etat(myGrid,GLOBAL.MAX,GLOBAL.MIN);
        init.initMemo();
        Direction.init_map(nbcol);
        return init;

    }

    private byte[] oneDimentionalArray(byte[][] grille){
        int nbligne = grille.length;
        int nbcol = grille[0].length;

        byte[] one_dim = new byte[grille.length * grille[0].length];
        //System.out.println("nb col:" +nbcol + "  nbligne: "+ nbligne);
        for(int l=0 ; l < nbligne ; l++){
            System.arraycopy(grille[l],0,one_dim,l*(nbcol),nbcol);
        }

        return one_dim;

    }

    private int getMoveCutedGridToFullGrid(int move){
        int pos_x,pos_y;
        pos_x =  ((move/GLOBAL.NBCOL) ) + (lowestX - bufferX);
        pos_y = ((move%GLOBAL.NBCOL) ) +  (lowestY - bufferY);

        return  pos_x * GLOBAL.FULL_NBCOL + pos_y;
    }


    public byte[][] cutGrid(Grille grille){
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

        byte [][] data = grille.getData();
        for(int i=0; i< lenghtX && !blx  ; i++){
            for(int j=0 ; j< lengthY; j++ ){
               if(data[i][j] != 0){
                    lx[0] = i; lx[1]= j;
                   blx = true;
                   break;
               }
            }
        }

        for(int i=0 ; i< lenghtX  ; i++){
            for(int j = lengthY-1; j >= 0 ; j--){
               if(data[i][j] != 0 && j > hy[1]){
                    hy[0] = i; hy[1]= j;
                   break;
               }
            }
        }

        for(int i=lenghtX-1; i >= 0 && !bhx; i--){
            for(int j=0 ; j< lengthY; j++ ){
               if(data[i][j] != 0){
                    hx[0] = i; hx[1]= j;
                   bhx =true;
                   break;
               }
            }
        }

        ly[1] = Integer.MAX_VALUE;
        for(int i=0 ; i < lenghtX; i++){
            for(int j =0; j < lengthY; j++){
               if(data[i][j] != 0 && j < ly[1]){
                    ly[0] = i; ly[1]= j;
                   break;
               }
            }
        }




        lowestX = lx[0];
        lowestY = ly[1];
        int highestX= hx[0];
        int highestY = hy[1];



        int cutedNoBuffX = (hx[0] - lx[0] +1);
        int cutedNoBuffY = (hy[1] - ly[1] +1);


        int extendBufferX= (cutedNoBuffX > 5)? 3:3;
        int extendBufferY= (cutedNoBuffY > 5)? 3:3;





        int extendedX = (cutedNoBuffX) + (extendBufferX*2);
        int extendedY = (cutedNoBuffY) + (extendBufferY*2);

        int leftBuffer  = (lowestY - extendBufferY > 0)? extendBufferY : lowestY;
        int rightBuffer = ((extendBufferY ) > GLOBAL.FULL_NBCOL - (highestY+1))? GLOBAL.FULL_NBCOL-(highestY+1) : (extendBufferY );
        int topBuffer  =  (lowestX - extendBufferX > 0)? extendBufferX : lowestX;
        int downBuffer =  (  extendBufferX >(GLOBAL.FULL_NBLIGNE - (highestX+1))  )? GLOBAL.FULL_NBLIGNE- (highestX+1) : extendBufferX ;


        bufferY = leftBuffer;
        bufferX = topBuffer;

/*
        if(smallx >= GLOBAL.FULL_NBLIGNE){
            smallx = GLOBAL.FULL_NBLIGNE;
            extendBufferX = 0;
            lx[0] = 0;
            bufferX = 0;
            lowestX =0;
        }
        if(smally >= GLOBAL.FULL_NBCOL){
            smally=  GLOBAL.FULL_NBCOL;
            extendBufferY=0;
            ly[1] = 0;
            bufferY =0;
            lowestY = 0;
        }*/



        int smallSizeX = downBuffer + topBuffer + (hx[0] - lx[0] +1);
        int smallSizeY = leftBuffer + rightBuffer + (hy[1] - ly[1] +1);

        /*TODO: remplacer par System.arrayCopy()*/
      /*  int x =  (((smallx - extendBufferX) +lx[0] ) > GLOBAL.FULL_NBLIGNE)? 0: lx[0];
        int y =  (((smally - extendBufferY) +ly[1] ) > GLOBAL.FULL_NBLIGNE)? 0: ly[1];*/
        System.out.println("smallSizeY: "+smallSizeY+"      smallSizeX: "+smallSizeX);
        System.out.println("left_buffer: "+leftBuffer+"       rightBuffer: "+ rightBuffer);
        System.out.println("topBuffer: "+topBuffer+"     downBuffer: "+downBuffer);
        byte[][] cpy = new byte[smallSizeX][smallSizeY];
        for(int i=0; i < cutedNoBuffX; i++){
            for(int j= 0 ; j < cutedNoBuffY; j++ ){
                int t = data[lowestX +i][lowestY + j];
                cpy[i+topBuffer][j+leftBuffer] = (byte)t;
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

    public String toStringOneDim(byte[] data){
        char[] table = {'-', 'N', 'B','n','s','e','o' };
        String result = "" + nbligne + " " + nbcol+ "\n";

        int i=1;
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

    public String toStringOneDimWithCol(byte[] data,int nbcol,int nbligne){
        char[] table = {'-', 'N', 'B','n','s','e','o' };
        String result = "" + nbligne + " " + nbcol+ "\n";

        int i=1;
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


    public int evaluateThis(Grille grille){
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        nbcol = grille.getData()[0].length;
        for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    casesvides.add(l*nbcol+c);

        GLOBAL.NBCOL = nbcol;
        GLOBAL.NBLIGNE = grille.getData().length;

        nbligne = grille.getData().length;


        byte[] test = oneDimentionalArray(grille.getData());



        int player = ( ((nbligne*nbcol) - casesvides.size()) % 2  == 0)? 1:2;
        int opponent = (player==1)? 2:1;

        /*INIT ETAT*/
        Etat init = new Etat(grille,player,opponent);
        Direction.init_map(nbcol);

        return 0;//init.evaluate(player);

    }

    @Override
    public String getAuteurs() {
        return "Martin Bouchard (BOUM15078700)";
    }

    PrintStream out;
    BufferedReader reader;

    public JoueurArtificiel(Socket socket) throws IOException {
        out = new PrintStream(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }



    @Override
    public void run() {
        try {
            out.println("Eric Beaudry (00 000 000)");
            out.println("READY");
            out.flush();

            Random random = new Random();

            while (true) {
                int nbligne, nbcol;
                String ligne = reader.readLine();
                if (ligne.equalsIgnoreCase("shutdown")) {
                    System.exit(0);
                    return;
                }
                StringTokenizer tokens = new StringTokenizer(ligne);
                nbligne = Integer.parseInt(tokens.nextToken());
                if (nbligne == 0)
                    return;
                nbcol = Integer.parseInt(tokens.nextToken());

                int grille[][] = new int[nbligne][nbcol];

                for (int l = 0; l < nbligne; l++) {
                    ligne = reader.readLine();
                    for (int c = 0; c < nbcol; c++) {
                        switch (ligne.charAt(c)) {
                            case '0':
                                grille[l][c] = 0;
                                break;
                            case 'N':
                                grille[l][c] = 1;
                                break;
                            case 'B':
                                grille[l][c] = 2;
                                break;
                        }
                    }
                }

                int delai = Integer.parseInt(reader.readLine());
                Vector<Integer> vides = new Vector<Integer>();
                for (int l = 0; l < nbligne; l++)
                    for (int c = 0; c < nbcol; c++)
                        if (grille[l][c] == 0)
                            vides.add(l * nbcol + c);

                int choix = random.nextInt(vides.size());
                choix = vides.get(choix);
                out.println("" + (choix / nbcol) + " " + (choix % nbcol));
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

            public static void main(String[] args) throws Exception {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : 1199;

            System.out.println("Ouverture du port " + port);
            ServerSocket serversocket = new ServerSocket(port);
            System.out.println("Attente de connection...");

            while (true) {
                Socket socket = serversocket.accept();
                System.out.println("Client: " + socket.toString());
                JoueurArtificiel jr = new JoueurArtificiel(socket);
                Thread t = new Thread(jr);
                t.start();
            }
        }

    }

/*

    public static class JoueurRunner implements Runnable {

        PrintStream out;
        BufferedReader reader;

        public JoueurRunner(Socket socket) throws IOException {
            out = new PrintStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                out.println("Eric Beaudry (00 000 000)");
                out.println("READY");
                out.flush();

                Random random = new Random();

                while (true) {
                    int nbligne, nbcol;
                    String ligne = reader.readLine();
                    if (ligne.equalsIgnoreCase("shutdown")) {
                        System.exit(0);
                        return;
                    }
                    StringTokenizer tokens = new StringTokenizer(ligne);
                    nbligne = Integer.parseInt(tokens.nextToken());
                    if (nbligne == 0)
                        return;
                    nbcol = Integer.parseInt(tokens.nextToken());

                    int grille[][] = new int[nbligne][nbcol];

                    for (int l = 0; l < nbligne; l++) {
                        ligne = reader.readLine();
                        for (int c = 0; c < nbcol; c++) {
                            switch (ligne.charAt(c)) {
                                case '0':
                                    grille[l][c] = 0;
                                    break;
                                case 'N':
                                    grille[l][c] = 1;
                                    break;
                                case 'B':
                                    grille[l][c] = 2;
                                    break;
                            }
                        }
                    }

                    int delai = Integer.parseInt(reader.readLine());
                    Vector<Integer> vides = new Vector<Integer>();
                    for (int l = 0; l < nbligne; l++)
                        for (int c = 0; c < nbcol; c++)
                            if (grille[l][c] == 0)
                                vides.add(l * nbcol + c);

                    int choix = random.nextInt(vides.size());
                    choix = vides.get(choix);
                    out.println("" + (choix / nbcol) + " " + (choix % nbcol));
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] args) throws Exception {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : 1199;

            System.out.println("Ouverture du port " + port);
            ServerSocket serversocket = new ServerSocket(port);
            System.out.println("Attente de connection...");

            while (true) {
                Socket socket = serversocket.accept();
                System.out.println("Client: " + socket.toString());
                JoueurRunner jr = new JoueurRunner(socket);
                Thread t = new Thread(jr);
                t.start();
            }
        }

    }

*/




