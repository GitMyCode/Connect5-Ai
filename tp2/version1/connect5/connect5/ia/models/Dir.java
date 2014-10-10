package connect5.ia.models;

import java.util.*;

/**
 * Created by MB on 10/9/2014.
 */
public enum Dir {


    DOWN{
        @Override public Dir opp() { return TOP; }
        @Override public int v() { return GLOBAL.NBCOL; }
        @Override public boolean boundaries5(int index) { return checkBounderies(EnumSet.of(Cardinal.SUD),index); }
    },
    TOP{
        @Override public Dir opp() { return DOWN; }
        @Override public int v() { return 0-DOWN.v(); }
        @Override public boolean boundaries5(int index) { return checkBounderies(EnumSet.of(Cardinal.NORD),index); }
    },

    LEFT{
        @Override public Dir opp() { return RIGHT; }  @Override public int v() { return -1; }
        @Override public boolean boundaries5(int index) { return checkBounderies(EnumSet.of(Cardinal.OUEST),index); }
    },
    RIGHT(){
        @Override public Dir opp() { return LEFT; }
        @Override public int v() { return 1; }
        @Override public boolean boundaries5(int index) { return checkBounderies(EnumSet.of(Cardinal.EST),index); }
    },

    DOWNLEFT(){
        @Override public Dir opp() { return TOPRIGHT; }
        @Override public int v() { return DOWN.v() + LEFT.v(); }
        @Override public boolean boundaries5(int index) { return DOWN.boundaries5(index) && LEFT.boundaries5(index); }
    },
    TOPLEFT(Cardinal.OUEST){
        @Override public Dir opp() { return DOWNRIGHT; }
        @Override public int v() { return TOP.v() + LEFT.v(); }
        @Override public boolean boundaries5(int index) { return TOP.boundaries5(index)&& LEFT.boundaries5(index); }
    },
    DOWNRIGHT(Cardinal.EST){
        @Override public Dir opp() { return TOPLEFT; }
        @Override public int v() { return DOWN.v() + RIGHT.v(); }
        @Override public boolean boundaries5(int index) { return DOWN.boundaries5(index)&&RIGHT.boundaries5(index); }
    },
    TOPRIGHT(Cardinal.EST){
        @Override public Dir opp() { return DOWNLEFT; }
        @Override public int v() { return TOP.v() + RIGHT.v(); }
        @Override public boolean boundaries5(int index) { return TOP.boundaries5(index)&& RIGHT.boundaries5(index); }
    };


    public int nbcol;
    public int v ;
    public Axes axe;
    public Cardinal cardinal = null;

    public static final Set<Dir> direction4 = new HashSet<Dir>();
    public static final Set<Dir> direction8 = new HashSet<Dir>();
    static {
        direction4.add(DOWN);
        direction4.add(RIGHT);
        direction4.add(DOWNRIGHT);
        direction4.add(TOPRIGHT);

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
    abstract public boolean boundaries5(int index);

    public int v(int step){
        return this.v() * step;
    }
    public boolean checkPossibleConnect(byte[] array,int i,int player){
        int res = array[i+ this.v()*0]|array[i+ this.v()*1]|array[i+ this.v()*2]|array[i+ this.v()*3]|array[i+ this.v()]*4;
        if(res > 2)
            return false;
        if(res != player)
            return false;

        int test = Integer.bitCount(res);

        return true;
    }

    private static boolean checkBounderies(Set<Cardinal> cardinaux,int index){
        for(Cardinal c: cardinaux){
            if(! c.valid(index)){
                return false;
            }
        }
        return true;
    }


    /**
     *
     */

    public enum Cardinal{
        NORD{
            @Override boolean valid(int index) {

                return  (index + TOP.v()*4) >=0;
            }
        },
        SUD{
            @Override boolean valid(int index) {
                int length = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
                return (index+ DOWN.v()*4) < length;
            }
        },
         OUEST{
            @Override
            boolean valid(int index) {
                if(!(((index%GLOBAL.NBCOL)+1)  >=5)){ return false; }
                return true;
            }
        },
        EST{
            @Override
            boolean valid(int index) {
                if(!((GLOBAL.NBCOL - (index%GLOBAL.NBCOL))  >=5)){ return false; }
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


        public static final Map<Dir,Axes> lookup = new EnumMap<Dir, Axes>(Dir.class);

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
        public static Axes getA(Dir d){
            return lookup.get(d);
        }

        public boolean sameAxes(Dir a, Dir b){
            return lookup.get(a) == lookup.get(b);
        }


    }




}
