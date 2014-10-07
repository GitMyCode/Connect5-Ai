package Models;

/**
 * Created by MB on 9/30/2014.
 */
public enum CaseValue {


    EMPTY('0',0),
    NOIRE('N',1),
    BLANC('B',2);


    char sym;
    int int_sym;

    CaseValue(char sym, int int_sym){
        this.sym = sym;
        this.int_sym = int_sym;
    }


}
