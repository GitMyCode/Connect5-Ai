package connect5.ia.models;

/**
 * Created by MB on 10/8/2014.
 */
public class Move {


    public int move;
    public int score;

    public Move(int place){
        this.move = place;
    }

    public Move(int place, int score){
        this.move = place;
        this.score = score;
    }


}
