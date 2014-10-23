package connect5.ia.Utilitaires;

import connect5.ia.*;
import connect5.ia.models.*;
import connect5.ia.models.Vector5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by MB on 10/23/2014.
 */


public abstract class Util {

    static public void print_all_vector(List<Vector5> all,byte[] one_dim){

        int total=0;
        for(Vector5 v : all){
            if(v.value > 0){
                int value = (v.bidirectionnel)? v.value+1 : v.value;
                System.out.println("---- value: "+v.value+ "   bi: "+v.bidirectionnel+"  ---- score: "+ Math.pow(value,4));
                System.out.println(toStringVector(one_dim,v.tab_seq));
                total += Math.pow(value,4);
            }
        }
        System.out.println("Total theses vector: "+total);
    }
    static private void printAllDiffVector(List<Vector5> l1, List<Vector5> l2,byte[] one_dim){

        int total=0;

        for(Vector5 v : l1){
            if(v.value > 0){
                if(!l2.contains(l1)){
                    int value = (v.bidirectionnel)? v.value+1 : v.value;
                    System.out.println("---- value: "+v.value+ "   bi: "+v.bidirectionnel+"  ---- score: "+ Math.pow(value,4));
                    System.out.println(toStringVector(one_dim,v.tab_seq));
                    total += Math.pow(value,4);
                }


            }
        }
        System.out.println("Total theses vector: "+total);
    }

    public static boolean areAllEqual(int... values)
    {
        if (values.length == 0)
        {
            return true; // Alternative below
        }
        int checkValue = values[0];
        for (int i = 1; i < values.length; i++)
        {
            if (values[i] != checkValue)
            {
                return false;
            }
        }
        return true;
    }

    static public void printAllStartPoint(Map<Dir.Axes,Map<Integer,Integer>> mapAxesStartPointSet,byte[] one_dim){
        for(Dir d : Dir.direction4){
            Dir.Axes a = Dir.Axes.getA(d);
            int test[] = new int[mapAxesStartPointSet.get(a).size()];
            int i=0;
            System.out.println(a + " nbStartpoint:"+ test.length + " Axe: "+a);

            for( Integer entry : mapAxesStartPointSet.get(a).values()){
                int startPoint = entry;
                test[i] =startPoint;
                i++;
            }
            System.out.println("all the startPoint: "+ Arrays.toString(test));
            System.out.println(toStringVector(one_dim,test));
        }

    }

    static public String toStringOneDim(byte[] data){
        char[] table = {'-', 'N', 'B','X' };
        String result = "" + GLOBAL.NBLIGNE + " " + GLOBAL.NBCOL+ "\n";

        int i=1;
        for(byte b : data){
            char c = (char)b;
            result += table[b];
            if(i % GLOBAL.NBCOL ==0){
                result += '\n';
            }
            i++;
        }

        return result;
    }
    static public void pintTotalMapMemoAxeValue(Map<Dir.Axes,Map<Integer,Integer>> mapMemoAxesValue){
        int t=0;

        for(Map m : mapMemoAxesValue.values()){
            for(Object i : m.values()){
                t += (Integer) i;
            }

        }
        System.out.println(t);

    }




    static public String toStringVector(byte[] data_original, int[] vector){
        int nbcol = GLOBAL.NBCOL;
        int nbligne = GLOBAL.NBLIGNE;


        byte[] data = new byte[nbcol *nbligne];
        System.arraycopy(data_original,0,data,0,data_original.length);

        char[] table = {'_', 'N', 'B','X' };
        String result = "" + nbligne + " " + nbcol+ "\n";
        for(int j=0; j< vector.length; j++){
            data[vector[j]] = 3;
        }


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

}
