package connect5.ia.models;

/**
 * Created by MB on 10/23/2014.
 */
public class Vector5 implements Comparable {
        public boolean isCorded = false;
        public boolean moreThan5 = false;
        public int suite=0;
        public int value;
        public int valueBirdirection;
        public boolean bidirectionnel = false;
        public Dir D;
        public int[] tab_seq = new int[5];
        public boolean isMAXvector = false;
        public int binarySeq=0;

        public int beginPoint;
        public int endPoint;


        public Vector5(){}

        public Vector5(int binarySeq,Dir D ){
            this.D = D;
            this.binarySeq = binarySeq;
            value = Integer.bitCount(binarySeq);
            valueBirdirection = value;
            if (isSuite(binarySeq) ){
                isCorded = true;
                suite = value;
            }
        }

        public Vector5( int binarySeq, Dir D, int beginPoint ){
            this.D = D;
            this.binarySeq = binarySeq;
            value = Integer.bitCount(binarySeq);
            valueBirdirection = value;
            if (isSuite(binarySeq) ){
                isCorded = true;
                suite = value;
            }
            this.beginPoint = beginPoint;
            this.endPoint = beginPoint +D.step(lastIndexBitOfSequence(binarySeq));

        }

        public boolean isNearPoint(int point){
            Dir.Axes axe = Dir.Axes.getA(D);
            int vectorAxesStartPoint = Etat.mapAxesPointToStartPoint.get(axe).get(beginPoint);
            if(Etat.mapAxesPointToStartPoint.get(axe).get(point) != vectorAxesStartPoint){
                return false;
            }

            int from = (D.opp().boundaries(beginPoint,2))? beginPoint + D.opp().step(1) : beginPoint ;
            int to = (D.boundaries(endPoint,2))? endPoint + D.step(1) : endPoint ;

            if( (from <= point && to >= point) || (from >= point && to <= point) ){
                return true;
            }

            return false;

        }


        /*Check if they are next to each other  ->   01110 :Yes  01011 : No*/
        public boolean isSuite(int binarySeq){
            return (distanceStartToEnd(binarySeq)== value);
        }
        public int distanceStartToEnd(int binarySeq){
            return ((5 - (Integer.numberOfLeadingZeros(binarySeq) - 27)) -
                    Integer.numberOfTrailingZeros(binarySeq));
        }

        public int lastIndexBitOfSequence (int binarySeq){
            return (5- (Integer.numberOfLeadingZeros(binarySeq)-27));
        }

        public int getHeuristicVal(){
            int eval =0;
            if(value ==0)return 0;
            eval += (isCorded)? Math.pow(valueBirdirection,4.1): Math.pow(valueBirdirection,4);
            return eval;
        }
/*Check if they there is free space on the two side. If yes we can assume that we could put a least one more
                    * before being blocked. So we do + 1   */

        public void setIfDirectionnel(boolean bidirectionnel){
            this.bidirectionnel = bidirectionnel;
            if(bidirectionnel){
                valueBirdirection = value+1;
            }else {
                valueBirdirection = value;
            }

        }

        public boolean samePoint(Vector5 v2){
            if(endPoint == v2.endPoint && beginPoint == v2.beginPoint){
                return true;
            }
            if(endPoint == v2.beginPoint && beginPoint == v2.endPoint){
                return true;
            }
            return false;
        }


        @Override
        public int compareTo (Object o) {
            Vector5 otherV = (Vector5) o;
            if(valueBirdirection < otherV.valueBirdirection ){
                return -1;
            }
            if(value < otherV.value){
                return -1;
            }
            if(value == otherV.value){
                if(isCorded){
                    return 1;
                }else {
                    return -1;
                }
            }

            return 0;
        }

        public int getThreatValue(){

            if(isCorded){
                return valueBirdirection;
            }else if( valueBirdirection >4) {
                return valueBirdirection;
            }else if( (distanceStartToEnd(binarySeq)-value) <=1 ){
                return valueBirdirection;
            }
            return value;
        }


        public void reduceVector(){
            suite--;
            value--;
            valueBirdirection--;
        }



        public Vector5 clone() {
            Vector5 n  = new Vector5();
            n.isCorded = this.isCorded;
            n.moreThan5 = moreThan5;
            n.value = value;
            n.valueBirdirection = valueBirdirection;
            n.D = D;

            return n;
        }

        @Override
        public boolean equals (Object obj) {
            Vector5 v = (Vector5) obj;
            for(int i =0; i<tab_seq.length; i++){
                if(tab_seq[i] != v.tab_seq[i]){
                    return false;
                }
            }
            return true;

        }

    }
