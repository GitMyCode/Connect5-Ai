package connect5.ia.models;

import java.util.*;

/**
 * Created by MB on 10/9/2014.
 */
public enum Dir {


    DOWN{
        @Override public Dir opp() { return TOP; }
        @Override public int v() { return GLOBAL.NBCOL; }
        @Override public boolean boundaries (int index, int limit) { return checkBounderies(EnumSet.of(Cardinal.SUD),index, limit); }
    },
    TOP{
        @Override public Dir opp() { return DOWN; }
        @Override public int v() { return 0-DOWN.v(); }
        @Override public boolean boundaries (int index, int limit) { return checkBounderies(EnumSet.of(Cardinal.NORD),index,limit); }
    },

    LEFT{
        @Override public Dir opp() { return RIGHT; }  @Override public int v() { return -1; }
        @Override public boolean boundaries (int index,int limit) { return checkBounderies(EnumSet.of(Cardinal.OUEST),index,limit); }
    },
    RIGHT(){
        @Override public Dir opp() { return LEFT; }
        @Override public int v() { return 1; }
        @Override public boolean boundaries (int index,int limit) { return checkBounderies(EnumSet.of(Cardinal.EST),index,limit); }
    },

    DOWNLEFT(){
        @Override public Dir opp() { return TOPRIGHT; }
        @Override public int v() { return DOWN.v() + LEFT.v(); }
        @Override public boolean boundaries (int index, int limit) { return DOWN.boundaries(index,limit) && LEFT.boundaries(index, limit); }
    },
    TOPLEFT(Cardinal.OUEST){
        @Override public Dir opp() { return DOWNRIGHT; }
        @Override public int v() { return TOP.v() + LEFT.v(); }
        @Override public boolean boundaries (int index, int limit) { return TOP.boundaries(index, limit)&& LEFT.boundaries(index, limit); }
    },
    DOWNRIGHT(Cardinal.EST){
        @Override public Dir opp() { return TOPLEFT; }
        @Override public int v() { return DOWN.v() + RIGHT.v(); }
        @Override public boolean boundaries (int index, int limit) { return DOWN.boundaries(index,limit)&&RIGHT.boundaries(index,limit); }
    },
    TOPRIGHT(Cardinal.EST){
        @Override public Dir opp() { return DOWNLEFT; }
        @Override public int v() { return TOP.v() + RIGHT.v(); }
        @Override public boolean boundaries (int index, int limit) { return TOP.boundaries(index,limit)&& RIGHT.boundaries(index, limit); }
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
    abstract public boolean boundaries (int index,int limit);

    public int v(int step){
        return this.v() * step;
    }
    public boolean checkPossibleConnect(byte[] array,int i,int player){
        int res = array[i]|array[i+ this.v(1)]|array[i+ this.v(2)]|array[i+ this.v(3)]|array[i+ this.v(4)];
        if(res > 2){
            return false;
        }
        if(res != player){
            return false;
        }

        int test = Integer.bitCount(res);

        return true;
    }

    private static boolean checkBounderies(Set<Cardinal> cardinaux,int index, int limit){
        for(Cardinal c: cardinaux){
            if(! c.validLimit(index, limit)){
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
            @Override boolean validLimit (int index, int limit) {
                if(index < 0 || index >= GLOBAL.NBCOL*GLOBAL.NBCOL)
                    return false;

                return  (index + TOP.v()* (limit-1)) >=0;
            }
        },
        SUD{
            @Override boolean validLimit (int index, int limit) {
                int length = GLOBAL.NBCOL * GLOBAL.NBLIGNE;
                if(index < 0 || index >= length)
                    return false;
                return (index+ DOWN.v()* (limit-1)) < length;
            }
        },
         OUEST{
            @Override
            boolean validLimit (int index, int limit) {
                if(index < 0 || index >= GLOBAL.NBCOL*GLOBAL.NBCOL)
                    return false;
                if(!(((index%GLOBAL.NBCOL)+1)  >= limit)){ return false; }
                return true;
            }
        },
        EST{
            @Override
            boolean validLimit (int index, int limit) {
                if(index < 0 || index >= GLOBAL.NBCOL*GLOBAL.NBCOL)
                    return false;
                if(!((GLOBAL.NBCOL - (index%GLOBAL.NBCOL))  >= limit)){ return false; }
                return true;
            }
        };

        /*Check if we can get a sequence of 'limit' from the point of the index */
        abstract boolean validLimit (int index, int limit);

    }

    public enum Axes{

        VERTICAL(TOP,DOWN,0),
        HORIZONTAL(RIGHT,LEFT,1),
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
      //  public static final Map<Axes,Map<Integer,Integer>> mapAxesPointToStartPoint = new HashMap<Axes, Map<Integer, Integer>>();
      //  public static final Map<Axes,Set<Integer>> mapAxesStartPointSet = new HashMap<Axes, Set<Integer>>();

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

 /*           for(Axes a : Axes.values()){
                mapAxesPointToStartPoint.put(a, new HashMap<Integer, Integer>());
                mapAxesStartPointSet.put(a, new HashSet<Integer>());
            }

            for(int i=0; i < GLOBAL.NBCOL* GLOBAL.NBLIGNE; i++){
                int x = i/GLOBAL.NBCOL;
                int y = i%GLOBAL.NBCOL;

                int startPoint =0;
                for(Axes a : Axes.values()){
                    switch (a) {
                        case HORIZONTAL:
                            startPoint = x*GLOBAL.NBCOL;
                            break;
                        case VERTICAL:
                            startPoint = y;
                            break;
                        case DIAGR:
                            startPoint = (x+y < GLOBAL.NBLIGNE)? ( (x+y)*GLOBAL.NBCOL) : ((GLOBAL.NBLIGNE-1)*GLOBAL.NBCOL) + (x+y+1) - GLOBAL.NBCOL;
                            break;
                        case DIAGL:
                            startPoint = (x >y) ? (x-y) * GLOBAL.NBCOL : y-x;
                            break;
                    }
                    mapAxesStartPointSet.get(a).add(startPoint);
                    mapAxesPointToStartPoint.get(a).put(i, startPoint);
                }
          }
*/





        }
        public static Axes getA(Dir d){
            return lookup.get(d);
        }

        public boolean sameAxes(Dir a, Dir b){
            return lookup.get(a) == lookup.get(b);
        }


    }




}
