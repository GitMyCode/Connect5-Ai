package connect5.ia.models;

/**
 * Created by MB on 10/23/2014.
 */
class Vector5 implements Comparable {
        public boolean isCorded = false;
        public boolean moreThan5 = false;
        public int suite=0;
        public int value;
        public int valueBirdirection;
        public boolean bidirectionnel = false;
        public Dir Direction;
        public int[] tab_seq = new int[5];
        public boolean isMAXvector = false;
        public int binarySeq=0;


        public Vector5(int binarySeq ){
            this.binarySeq = binarySeq;
            value = Integer.bitCount(binarySeq);
            valueBirdirection = value;
            if ((5 - (Integer.numberOfLeadingZeros(binarySeq) - 27)) -
                    Integer.numberOfTrailingZeros(binarySeq) == value) {
                isCorded = true;
            }
        }

        public void setIfDirectionnel(boolean bidirectionnel){
            this.bidirectionnel = bidirectionnel;
            if(bidirectionnel){
                valueBirdirection = value+1;
            }else {
                valueBirdirection = value;
            }

        }

/*
        @Override
        public boolean equals (Object obj) {

        }*/

        @Override
        public int compareTo (Object o) {



            return 0;
        }

        public int vectorHeuristic(){
            int eval=0;
            if(suite>0){
                eval += Math.pow(valueBirdirection,4);
            }
            return eval;
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
            n.Direction = Direction;

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