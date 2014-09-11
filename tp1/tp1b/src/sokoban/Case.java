package sokoban;

import java.util.Comparator;

/**
 * Created by MB on 9/10/2014.
 */
public class Case implements Comparable<Case>,Cloneable{


    protected int x;
    protected int y;
    protected char symbole;


    public Case( int x, int y, char symbole){
        this.x = x;
        this.y = y;
        this.symbole = symbole;
    }

    @Override
    public int compareTo(Case aCase) {

        if(x < aCase.x ) return -1;
        if(x > aCase.x ) return +1;

        if(y < aCase.y) return -1;
        if(y > aCase.y) return +1;


        return 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Case result = (Case) super.clone();
        result.x = x;
        result.y = y;
        result.symbole = symbole;

        return result;
    }
}
