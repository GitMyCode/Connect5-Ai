
import connect5.Grille;
import connect5.Joueur;
import connect5.Position;
import connect5.ia.JoueurArtificiel;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JoueurArtificielTest {

    int nbcol=0;
    int nbligne=0;

    public int[] createByteGrid(String s,int nbligne,int nbcol){
        Map<Character,Byte> convert = new HashMap<Character,Byte>();
        convert.put('0',(byte)0);
        convert.put('_',(byte)0);
        convert.put('-',(byte)0);
        convert.put('N',(byte)1);
        convert.put('B',(byte)2);

        char[] table = {'_', 'N', 'B' };

        //String[] fractionned = s.split("\n");

        // nbligne = fractionned.length;
         //bcol = fractionned[0].length();
        int[] to_test = new int[nbligne*nbcol];

        for(int i=0; i< to_test.length; i++){
            to_test[i] = convert.get(s.charAt(i)) -1;
        }
       /*
        for(int i=0; i< nbligne; i++){
            for(int j=0; j< nbcol; j++){
                to_test[j*(i+1)] = convert.get(fractionned[i].charAt(j))-1 ;
            }
        }*/
        return to_test;
    }

    @org.junit.Before
    public void setUp () throws Exception {

    }

    @org.junit.After
    public void tearDown () throws Exception {

    }

    @org.junit.Test
    public void testGetProchainCoup () throws Exception {
        Joueur j  = new JoueurArtificiel();
              String testMoreThan5=
                "-------B---" +
                "-----------" +
                "-----------" +
                "---N-B-----" +
                "---N-B-----" +
                "---N-B-----" +
                "-----------" +
                "---N-B-----" +
                "---N-B-----" +
                "---N-------" +
                "-----------";
        int[] grid = createByteGrid(testMoreThan5,11,11);
        Grille grille = new Grille(11,11,grid);
        System.out.println(grille.toString());
        Position coup = j.getProchainCoup(grille,2000);
        assertTrue(coup.ligne == 6 && coup.colonne == 6);


    }

        @org.junit.Test
    public void testGetProchainCoup2 () throws Exception {
        Joueur j  = new JoueurArtificiel();
              String t = "----------" +
            "----------" +
            "----N-----" +
            "----B-----" +
            "----BB----" +
            "----BNB---" +
            "---NBN----" +
            "---NNN----" +
            "---N-N----" +
            "-----BB---" +
            "----------" +
            "----------";

        int[] grid = createByteGrid(t,12,10);
        Grille grille = new Grille(12,10,grid);
        System.out.println(grille.toString());
        Position coup = j.getProchainCoup(grille,Integer.MAX_VALUE);
        assertTrue(coup.ligne == 6 && coup.colonne == 6);


    }



}