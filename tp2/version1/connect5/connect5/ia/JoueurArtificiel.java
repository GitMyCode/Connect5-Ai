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

        /*INIT ETAT*/
        Etat init = new Etat(grille,player,opponent);
        init.setChecker(checker);
        Direction.init_map(nbcol);


        /*TODO pour test*/
        int testeval = init.evaluate();


        Move MAXcheckWinMove = init.getNextMoves(player).poll();
        if(MAXcheckWinMove.score == GLOBAL.WIN){
            return new Position(MAXcheckWinMove.move/nbcol,MAXcheckWinMove.move%nbcol);
        }
        Move MINcheckWinMove = init.getNextMoves(opponent).poll();
        if(MINcheckWinMove.score == (0-GLOBAL.WIN)){
            return new Position(MINcheckWinMove.move/nbcol,MINcheckWinMove.move%nbcol);
        }






        System.out.println("AI : JOUEUR "+player);


        int choix =  MinMax.getMove(init,player,MINcheckWinMove.score,MAXcheckWinMove.score); //random.nextInt(casesvides.size());
        //choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }


    private byte[] oneDimentionalArray(byte[][] grille){
        byte[] one_dim = new byte[nbcol * nbligne];
        //System.out.println("nb col:" +nbcol + "  nbligne: "+ nbligne);
        for(int l=0 ; l < nbligne ; l++){
            System.arraycopy(grille[l],0,one_dim,l*(nbcol),nbcol);
        }

        return one_dim;

    }

    public String toStringOneDim(byte[] data){
        char[] table = {'0', 'N', 'B' };
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




