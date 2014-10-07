/*
 * ServeurJoueurAleatoire.java
 *
 * Created on 15 janvier 2009
 *
 */
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Eric Beaudry
 */
public class ServeurJoueurAleatoire {


    public static class JoueurRunner implements Runnable{

        PrintStream    out;
        BufferedReader reader;

        public JoueurRunner(Socket socket) throws IOException{
            out = new PrintStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try{
                out.println("Eric Beaudry (00 000 000)");
                out.println("READY");
                out.flush();

                Random random = new Random();

                while(true){
                    int nbligne, nbcol;
                    String ligne = reader.readLine();
                    if(ligne.equalsIgnoreCase("shutdown")){
                        System.exit(0);
                        return;
                    }
                    StringTokenizer tokens = new StringTokenizer(ligne);
                    nbligne = Integer.parseInt(tokens.nextToken());
                    if(nbligne==0)
                        return;
                    nbcol = Integer.parseInt(tokens.nextToken());

                    int grille[][] = new int[nbligne][nbcol];

                    for(int l=0;l<nbligne;l++){
                        ligne = reader.readLine();
                        for(int c=0;c<nbcol;c++){
                            switch(ligne.charAt(c)){
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
                    for(int l=0;l<nbligne;l++)
                        for(int c=0;c<nbcol;c++)
                            if(grille[l][c]==0)
                                vides.add(l*nbcol+c);

                    int choix = random.nextInt(vides.size());
                    choix = vides.get(choix);
                    out.println("" + (choix/nbcol) + " " + (choix%nbcol));
                    out.flush();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception{

        int port = args.length>0 ? Integer.parseInt(args[0]) : 1199;

        System.out.println("Ouverture du port " + port);
        ServerSocket serversocket = new ServerSocket(port);
        System.out.println("Attente de connection...");

        while(true){
            Socket socket = serversocket.accept();
            System.out.println("Client: " + socket.toString());
            JoueurRunner jr = new JoueurRunner(socket);
            Thread t = new Thread(jr);
            t.start();
        }
    }
}