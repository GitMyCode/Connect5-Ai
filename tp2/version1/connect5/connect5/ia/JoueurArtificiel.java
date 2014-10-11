package connect5.ia;


import connect5.*;

import connect5.Grille;
import connect5.GrilleVerificateur;
import connect5.Joueur;
import connect5.Position;
import connect5.ia.models.*;
import connect5.ia.strategy.MinMax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;


public class JoueurArtificiel implements Joueur, Runnable {

    private final Random random = new Random();
    private static final GrilleVerificateur checker = new GrilleVerificateur();

    private int nbcol=0;
    private int nbligne=0;


    private int buffer_x;
    private int buffer_y;
    private int lowest_x;
    private int lowest_y;



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

        if(casesvides.size() == (nbcol*nbligne)){
            System.out.println("first hit");
            return new Position(nbcol/2,nbligne/2);
        }


        int player = ( ((nbligne*nbcol) - casesvides.size()) % 2  == 0)? 1:2;
        int opponent = (player==1)? 2:1;





        byte[][] t_cut = cutGrid(grille);
        byte[] t_cut1 = oneDimentionalArray(t_cut);
        System.out.println(toStringOneDimWithCol(t_cut1,t_cut[0].length,t_cut.length));
        GLOBAL.NBCOL = t_cut[0].length;
        GLOBAL.NBLIGNE = t_cut.length;



        /*INIT ETAT*/
        Etat init = new Etat(t_cut1,player,opponent);
        init.setChecker(checker);
        Direction.init_map(nbcol);


        /*TODO pour test*/
        //int testeval = init.evaluate();


        Move MAXcheckWinMove = init.getNextMoves(player).poll();
        if(MAXcheckWinMove.score == GLOBAL.WIN){
            int pos_x = ((MAXcheckWinMove.move/GLOBAL.NBCOL) ) + Math.abs(buffer_x - lowest_x);
            int pos_y =  ((MAXcheckWinMove.move%GLOBAL.NBCOL) ) + Math.abs(buffer_y - lowest_y);

            return new Position(pos_x,pos_y);
        }
        Move MINcheckWinMove = init.getNextMoves(opponent).poll();
        if(MINcheckWinMove.score == (0-GLOBAL.WIN)){
            int pos_x = ((MINcheckWinMove.move/GLOBAL.NBCOL) ) + Math.abs(buffer_x - lowest_x);
            int pos_y =  ((MINcheckWinMove.move%GLOBAL.NBCOL) ) + Math.abs(buffer_y - lowest_y);

            return new Position(pos_x,pos_y);
        }





        System.out.println("AI : JOUEUR "+player);


        int choix =  MinMax.getMove(init,player,MINcheckWinMove.score,MAXcheckWinMove.score); //random.nextInt(casesvides.size());
        //choix = casesvides.get(choix);
        int pos_x =  ((choix/GLOBAL.NBCOL) ) + Math.abs(buffer_x - lowest_x);
        int pos_y = ((choix%GLOBAL.NBCOL) ) + Math.abs(buffer_y - lowest_y);
        return new Position(pos_x,pos_y);
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


    public byte[][] cutGrid(Grille grille){
        int lenght_x = grille.getData().length;
        int length_y = nbcol;

        int[] hx = new int[2];
        int[] lx = new int[2];
        int[] hy = new int[2];
        int[] ly = new int[2];

        boolean bhx = false;
        boolean blx = false;
        boolean bhy = false;
        boolean bly = false;

        byte [][] data = grille.getData();
        for(int i=0; i< lenght_x && !blx  ; i++){
            for(int j=0 ; j< length_y; j++ ){
               if(data[i][j] != 0){
                    lx[0] = i; lx[1]= j;
                   blx = true;
                   break;
               }
            }
        }

        for(int i=0 ; i< lenght_x  ; i++){
            for(int j = length_y-1; j >= 0 ; j--){
               if(data[i][j] != 0 && j > hy[1]){
                    hy[0] = i; hy[1]= j;
                   break;
               }
            }
        }

        for(int i=lenght_x-1; i >= 0 && !bhx; i--){
            for(int j=0 ; j< length_y; j++ ){
               if(data[i][j] != 0){
                    hx[0] = i; hx[1]= j;
                   bhx =true;
                   break;
               }
            }
        }

        ly[1] = Integer.MAX_VALUE;
        for(int i=0 ; i < lenght_x; i++){
            for(int j =0; j < length_y; j++){
               if(data[i][j] != 0 && j < ly[1]){
                    ly[0] = i; ly[1]= j;
                   break;
               }
            }
        }




        int extend_bufferx= 3;
        int extend_buffery= 3;

        buffer_x = extend_bufferx;
        buffer_y = extend_buffery;
        lowest_x = lx[0];
        lowest_y = ly[1];

        int smallx = (hx[0] - lx[0] +1) + (extend_bufferx*2);
        int smally = (hy[1] - ly[1] +1) + (extend_buffery*2);

        if(smallx >= nbligne){
            smallx = nbligne;
            extend_bufferx = 0;
            lx[0] = 0;
            buffer_x= 0;
            lowest_x =0;
        }
        if(smally >= nbcol){
            smally=  nbcol;
            extend_buffery=0;
            ly[1] = 0;
            buffer_y =0;
            lowest_y = 0;
        }




        byte[][] cpy = new byte[smallx][smally];
        for(int i=0; i < smallx-extend_bufferx; i++){
            for(int j= 0 ; j < smally-extend_buffery; j++ ){
               cpy[i+extend_bufferx][j+extend_buffery] = data[lx[0] +i][ly[1] + j];
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

        /*for(byte[] b : data){
            char[] c = new char[b.length];
            for(int i=0;i<b.length;i++)
                c[i] = table[b[i]];
            result += new String(c);
            result += '\n';
        }*/
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

        /*for(byte[] b : data){
            char[] c = new char[b.length];
            for(int i=0;i<b.length;i++)
                c[i] = table[b[i]];
            result += new String(c);
            result += '\n';
        }*/
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
        init.setChecker(checker);
        Direction.init_map(nbcol);

        return init.evaluate();

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




