package connect5.ia.models;

/**
 * Created by MB on 10/24/2014.
 */
public class Treat {

    public Vector5 treat1;
    public Vector5 treat2;

    public int point;
    public int player;
    public boolean isMax =false;

    Treat(Vector5 v1, Vector5 v2){
        treat1 = v1;
        treat2 = v2;
    }

}
