package connect5;

import connect5.ia.JoueurArtificiel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MB on 10/6/2014.
 */
public class MyTest {

    static int nbcol = 12;
    static int nbligne = 12;

    public static int[] testByte(){

        String to_convert =
        "000000000000" +
        "000000000000" +
        "0000N0000000" +
        "000000000000" +
        "000000B00000" +
        "00000BNB0000" +
        "0000NBN0B000" +
        "0000NN000B00" +
        "0000N0B00000" +
        "000000000000" +
        "000000000000" +
        "000000000000";

        String test2 =
                "000000000000" +
                        "000000000000" +
                        "000000000000" +
                        "000000000000" +
                        "0000B0N00000" +
                        "000000N00000" +
                        "000000N00000" +
                        "000000B00000" +
                        "000000000000" +
                        "000000000000" +
                        "000000000000" +
                        "000000000000";



        Map<Character,Byte> convert = new HashMap<Character,Byte>();
        convert.put('0',(byte)0);
        convert.put('N',(byte)1);
        convert.put('B',(byte)2);


        char[] table = {'_', 'N', 'B' };

        int[] to_test = new int[nbcol * nbligne];

        for(int i=0; i< to_test.length; i++){
            to_test[i] = convert.get(test2.charAt(i)) -1;
        }
        return to_test;
    }


    public static void main(String args[]){

        Joueur joueur = new JoueurArtificiel();

        // Test B1
        System.out.println("Test #1");
        Grille g = new Grille(nbligne,nbcol,testByte());
        Position coup = joueur.getProchainCoup(g, 20000);
        System.out.println((coup.ligne==4 && coup.colonne==0) ? "Réussi": "Échoué");


        // Test B2
        /*g = new Grille(5, 7);
        for(int i=0;i<4;i++){
            g.set(0, 2+i, 1);
            g.set(4, 2+i, 2);
        }*/
        coup = joueur.getProchainCoup(g, 20000);
        System.out.println("Test #1");
        System.out.println((coup.ligne==0 && (coup.colonne==1||coup.colonne==6)) ? "Réussi": "Échoué");

    }
}
