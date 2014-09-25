package sokoban;

import java.util.Comparator;

/**
 * Created by MB on 9/10/2014.
 */
public class Case extends Noeud implements Comparable<Case>,Cloneable{


    protected int x;
    protected int y;
    protected char symbole;



    public Case(){

    }

    public Case( int x, int y, char symbole){
        this.x = x;
        this.y = y;
        this.symbole = symbole;
    }

    public Case(Case c){
        this.x = c.x;
        this.y = c.y;
        this.symbole = c.symbole;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)return true;

        Case c = (Case) o;
        if(this.x != c.x){
            return false;
        }

        if( this.y != c.y){
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Case aCase) {

        if(this == aCase)return 0;

        if(x < aCase.x ) return -1;
        if(x > aCase.x ) return 1;

        if(y < aCase.y) return -1;
        if(y > aCase.y) return 1;


        return 0;


   }

    public void applyDeplacement(String action){
        if(action=="W")
            this.y -= 1;
        if(action=="E")
            this.y += 1;
        if(action=="N")
            this.x -= 1;
        if(action=="S")
            this.x += 1;
    }


    public int getX() {
        return x;
    }

    @Override
    public int hashCode() {
        int hash = 31;
        hash = 31  * hash + (this.x+1);
        hash = 37 * hash + (this.y+1);
        return hash;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    protected Object clone() {
        Case result = null;
        try{
            result = (Case) super.clone();
            result.x = x;
            result.y = y;
            result.symbole = symbole;

        }catch(CloneNotSupportedException e){

        }

        return result;
    }
}
