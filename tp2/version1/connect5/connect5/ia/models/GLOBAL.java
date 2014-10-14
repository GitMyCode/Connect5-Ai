package connect5.ia.models;

/**
 * Created by MB on 10/9/2014.
 */
public abstract class GLOBAL {

    public static int NBCOL;
    public static int NBLIGNE;

    public static int FULL_NBCOL;
    public static int FULL_NBLIGNE;



    public static final int WIN = Integer.MAX_VALUE- 200;

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




}
