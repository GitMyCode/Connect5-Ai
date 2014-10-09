package connect5.ia.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MB on 10/7/2014.
 */
public abstract class Direction {

    public static int nbcol;

    public static final  int AXES = 4;
    public static final int SEQ  = 5;

    public static int DOWN = nbcol;
    public static int TOP  = 0-nbcol;
    public static int LEFT = -1;
    public static int RIGHT = 1;

    public static int DOWNLEFT = DOWN + LEFT;
    public static int TOPLEFT   = TOP + LEFT;
    public static int DOWNRIGHT = DOWN + RIGHT;
    public static int TOPRIGHT = TOP + RIGHT;

    public static int HORIZONTAL = 0;
    public static int VERTICAL = 1;
    public static int DIAGL    = 2;
    public static int DIAGR    = 3;
    public static int EST     = 4;
    public static int OUEST     = 5;


    public static int[] direction4 = new int[]{DOWN,LEFT,DOWNLEFT,TOPLEFT};
    public static int[] direction8 = new int[]{DOWN,LEFT,TOP,RIGHT,DOWNLEFT,TOPLEFT,TOPRIGHT, DOWNRIGHT};

    public static Map<Integer,Integer> axes_map = new HashMap<Integer, Integer>();
    public static Map<Integer,Integer> side_map = new HashMap<Integer, Integer>();



    public static void init_map(int nbcol1){
        nbcol = nbcol1;

        DOWN = nbcol;
        TOP  = 0-nbcol;
        LEFT = -1;
        RIGHT = 1;
        DOWNLEFT = DOWN + LEFT;
        TOPLEFT   = TOP + LEFT;
        DOWNRIGHT = DOWN + RIGHT;
        TOPRIGHT = TOP + RIGHT;

        HORIZONTAL = 0;
        VERTICAL = 1;
        DIAGL    = 2;
        DIAGR    = 3;

        EST     = 4;
        OUEST     = 5;

        direction4 = new int[]{DOWN,LEFT,DOWNLEFT,TOPLEFT};
        direction8 = new int[]{DOWN,LEFT,TOP,RIGHT,DOWNLEFT,TOPLEFT,TOPRIGHT, DOWNRIGHT};


        axes_map.put(DOWN,VERTICAL);
        axes_map.put(TOP,VERTICAL);

        axes_map.put(RIGHT,HORIZONTAL);
        axes_map.put(LEFT,HORIZONTAL);

        axes_map.put(TOPRIGHT,DIAGL);
        axes_map.put(DOWNLEFT,DIAGL);

        axes_map.put(TOPLEFT,DIAGR);
        axes_map.put(DOWNRIGHT,DIAGR);


        side_map.put(LEFT,OUEST);
        side_map.put(DOWNLEFT,OUEST);
        side_map.put(TOPLEFT,OUEST);


        side_map.put(RIGHT,EST);
        side_map.put(DOWNRIGHT,EST);
        side_map.put(TOPRIGHT,EST);


    }

}
