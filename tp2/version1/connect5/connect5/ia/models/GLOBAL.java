package connect5.ia.models;

/**
 * Created by MB on 10/9/2014.
 */
public abstract class GLOBAL {

    public static int NBCOL;
    public static int NBLIGNE;

    public static int FULL_NBCOL;
    public static int FULL_NBLIGNE;
    public static int LAST_DEPTH;



    public static final int ALMOST_WIN = Integer.MAX_VALUE/2;
    public static final int WIN = Integer.MAX_VALUE- 200;
    public static final int LIMITE =50;

    public static long timer;
    public static long remain;

    public static void startTimer(int delais){
        timer = System.currentTimeMillis();
        remain = delais;
    }
    public static String showTimer(){
        return ("Time: "+ (System.currentTimeMillis() - timer) + " ms" );
    }
    public static String showTimeRemain(){
        return ("Time: "+ (remain-(System.currentTimeMillis() - timer)) + " ms" );
    }


    public static long timeRemaining(){
        long passed = (System.currentTimeMillis() - timer);
        return remain-passed;
    }

    public static boolean timeUp(){
        if(timeRemaining() < LIMITE ){
            return true;
        }

        return false;
    }




    public String toStringOneDim(byte[] data,int nbcol){
        char[] table = {'-', 'N', 'B' };
        String result =  nbcol+ "\n";

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
