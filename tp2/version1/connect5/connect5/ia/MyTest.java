package connect5.ia;

import connect5.Grille;
import connect5.Joueur;
import connect5.Position;
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
              "" +
                      "-------B----" +
                      "--B----N_N--" +
                      "---N-N-BBBN-" +
                      "----N-NBB---" +
                      "---NBNBBBNB-" +
                      "----NBNN----" +
                      "---BNBNB----" +
                      "--N---------" +
                      "------------" +
                      "------------" +
                      "------------" +
                      "------------";

        //"132.208.137.66";
      String f=
              "-----------" +
                      "-----------" +
                      "--------B--" +
                      "-------N---" +
                      "---BNNN----" +
                      "----BNBN---" +
                      "----NNBN---" +
                      "---BNBBB---" +
                      "----NB-----" +
                      "----B-N----" +
                      "---B-------" +
                      "--N--------" +
                      "-----------" +
                      "-----------";

      String f2= "-----------" +
              "-----------" +
              "----N-N----" +
              "--NN-BB-BB-" +
              "---B-NNBN--" +
              "----BNNNNB-" +
              "-----BNNN--" +
              "-----NBNNB-" +
              "-N--BNBNBB-" +
              "----N-BB---" +
              "----BB-----" +
              "------N----" +
              "-----------" +
              "-----------";

        String f3=
                "----------" +
                "--N-------" +
                "--BBNB----" +
                "---NB--B--" +
                "---NNBNN--" +
                "----NNB---" +
                "----NBNN--" +
                "---NN--B--" +
                "--B-B-----" +
                "----------" +
                "----------";

        /*7  10*/
        String f4=
                                "-------" +
                                "-------" +
                                "--N----" +
                                "-------" +
                                "---B---" +
                                "-------" +
                                "-------" +
                                "-------" ;
        String f5=
                "----------" +
                        "----------" +
                        "----------" +
                        "---NNNBN--" +
                        "----BBN---" +
                        "---B-N----" +
                        "---BN-----" +
                        "---B------" +
                        "---B------" +
                        "----------" +
                        "----------";
        String f6 =
             "----------" +
                     "----------" +
                     "----------" +
                     "---N-N----" +
                     "---NNNB---" +
                     "---B-N----" +
                     "---BN-B---" +
                     "---BB-----" +
                     "----------" +
                     "----------" +
                     "----------";
        String p1 = // 15 15  score :171 play :(12,7)   mais c'est MAUVAIS! a corriger
         "---------------" +
                 "---------------" +
                 "---------------" +
                 "---B-----------" +
                 "----N---B-B----" +
                 "----BNNBNN-----" +
                 "-----NNBNNNB---" +
                 "------BNBB-N---" +
                 "------NBBN-----" +
                 "----NBBBBN-----" +
                 "-----NB-BN-----" +
                 "----BN-BN------" +
                 "---------------" +
                 "---------------" +
                 "---------------" ;

        String t1 = //12 11
          "-----------" +
                  "-----------" +
                  "--B--------" +
                  "---BN--B---" +
                  "----BNB----" +
                  "----NB-----" +
                  "---NNBN----" +
                  "--N-NN-----" +
                  "----B------" +
                  "-----------" +
                  "-----------" +
                  "-----------";

        String t2 = //14 13

                "-------------" +
                        "-------------" +
                        "-------------" +
                        "------B------" +
                        "-----B-NBB---" +
                        "---BNBNNN----" +
                        "----NBNNNN---" +
                        "----BNBBN----" +
                        "----NBNB-----" +
                        "---BNBN------" +
                        "----B--------" +
                        "-------------" +
                        "-------------" +
                        "-------------";

        String t3 =//score :22 play :(6,3)
                //16 16
                        "----------------" +
                        "----------------" +
                        "----------------" +
                        "----------N-B---" +
                        "----------BN----" +
                        "-------NNNB-----" +
                        "---N-N-BNBB-----" +
                        "----BBBNB-N-----" +
                        "-----BNNBNB-----" +
                        "----BBBN---N----" +
                        "---N---BN-------" +
                        "--------NN------" +
                        "----------B-----" +
                        "----------------" +
                        "----------------" +
                        "----------------";



      String testMoreThan5=
                "-------------\n" +
                "-------------\n" +
                "-------------\n" +
                "-----N-B-----\n" +
                "-----N-B-----\n" +
                "-----N-B-----\n" +
                "-------------\n" +
                "-----N-B-----\n" +
                "-----N-B-----\n" +
                "-----N-------\n" +
                "-------------";
        Map<Character,Byte> convert = new HashMap<Character,Byte>();
        convert.put('0',(byte)0);
        convert.put('_',(byte)0);
        convert.put('-',(byte)0);
        convert.put('N',(byte)1);
        convert.put('B',(byte)2);


        char[] table = {'_', 'N', 'B' };

        int[] to_test = new int[nbcol * nbligne];

        for(int i=0; i< to_test.length; i++){
            to_test[i] = convert.get(f4.charAt(i)) -1;
        }
        return to_test;
    }



    public static void main(String args[]){

        Joueur joueur = new JoueurArtificiel();

        // Test B1
        System.out.println("Test #1");
        nbligne = 8; nbcol = 7;
        Grille g = new Grille(nbligne,nbcol,testByte());
        Position coup = joueur.getProchainCoup(g, 4000);
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
