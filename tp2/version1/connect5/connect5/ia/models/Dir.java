package connect5.ia.models;

import java.util.*;

/**
 * Created by MB on 10/9/2014.
 */
public enum Dir {


    DOWN{
        @Override public Dir opp() { return TOP; }
        @Override public int v() { return GLOBAL.NBCOL; }
    },
    LEFT(Cardinal.OUEST){
        @Override public Dir opp() { return RIGHT; }  @Override public int v() { return -1; }
    },
    RIGHT(Cardinal.EST){
        @Override public Dir opp() { return LEFT; }
        @Override public int v() { return -1; }
    },
    TOP{
        @Override public Dir opp() { return DOWN; }
        @Override public int v() { return 0-DOWN.v(); }
    },
    DOWNLEFT(Cardinal.OUEST){
        @Override public Dir opp() { return TOPRIGHT; }
        @Override public int v() { return DOWN.v() + LEFT.v(); }
    },
    TOPLEFT(Cardinal.OUEST){
        @Override public Dir opp() { return DOWNRIGHT; }
        @Override public int v() { return TOP.v() + LEFT.v(); }
    },
    DOWNRIGHT(Cardinal.EST){
        @Override public Dir opp() { return TOPLEFT; }
        @Override public int v() { return DOWN.v() + RIGHT.v(); }
    },
    TOPRIGHT(Cardinal.EST){
        @Override public Dir opp() { return DOWNLEFT; }
        @Override public int v() { return TOP.v() + RIGHT.v(); }
    };


    public int nbcol;
    public int v ;
    public Axes axe;
    public Cardinal cardinal;

    public static final Set<Dir> direction4 = new HashSet<Dir>();
    public static final Set<Dir> direction8 = new HashSet<Dir>();
    static {
        direction4.add(DOWN);
        direction4.add(LEFT);
        direction4.add(DOWNLEFT);
        direction4.add(TOPLEFT);

        direction8.addAll(EnumSet.allOf(Dir.class));
    }

    Dir(){

    }
    Dir(Cardinal side){
        this.cardinal = side;
    }

    Dir(Axes a){
        this.axe = a;
    }

    abstract public int v();
    abstract public Dir opp();

    boolean check(int index){
        if(cardinal == null){
            int length = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
            if(index >= length || index < 0 ){ return false; }
        }

        return cardinal.valid(index);
    }



    public enum Cardinal{
        /*
        * Use Dir.DOWN value to get nbcol
        * because i use a byte[] array as a representation of a byte[][] grid
        * */
        OUEST{
            @Override
            boolean valid(int index) {
                int length = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
                if(index >= length || index < 0 ){ return false; }
                if(!(((index%Dir.DOWN.v())+1)  >=5)){ return false; }
                return true;
            }
        },
        EST{
            @Override
            boolean valid(int index) {
                int length = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
                if(index >= length || index < 0 ){ return false; }
                if(!((Dir.DOWN.v() - (index%Dir.DOWN.v()))  >=5)){ return false; }
                return true;
            }
        };

        abstract boolean valid(int index);

    }

    public enum Axes{

        VERTICAL(LEFT,RIGHT,0),
        HORIZONTAL(TOP,DOWN,1),
        DIAGR(DOWNLEFT,TOPRIGHT,2),
        DIAGL(TOPLEFT,DOWNRIGHT,3);


        public Dir dl;
        public Dir dr;
        public int i;



        Axes(Dir dir, Dir dir2,int i){
            dl = dir;
            dr = dir2;
            this.i = i;
        }


        private static final Map<Dir,Axes> lookup = new EnumMap<Dir, Axes>(Dir.class);

        static {
            for(Dir dir : Dir.values()){
                if(VERTICAL.dl == dir || VERTICAL.dr == dir)
                    lookup.put(dir,VERTICAL);
                else if(HORIZONTAL.dl == dir || HORIZONTAL.dr == dir){
                    lookup.put(dir,HORIZONTAL);
                }else if(DIAGR.dl == dir || DIAGR.dr == dir){
                    lookup.put(dir,DIAGR);
                }else if(DIAGL.dl == dir || DIAGL.dr == dir){
                    lookup.put(dir,DIAGL);
                }
            }
        }

        public boolean sameAxes(Dir a, Dir b){
            return lookup.get(a) == lookup.get(b);
        }

        public int getAxe(){
            return 0;
        }


    }




}
