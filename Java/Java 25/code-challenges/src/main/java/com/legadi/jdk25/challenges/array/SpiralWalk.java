package com.legadi.jdk25.challenges.array;

public class SpiralWalk {

    /*
     * Given a two-dimensional array:
     * 0,0   0,1   0,2   0,3
     * 1,0   1,1   1,2   1,3
     * 2,0   2,1   2,2   2,3
     * 3,0   3,1   3,2   3,3
     * 
     * An spiral has the following shape:
     *   (horizontal) hlength = array[0].length
     *   (vertical) vlength = array.length
     * 
     * 0,0 --+
     * 0,1   | top side (hlength)
     * 0,2   |
     * 0,3 --+
     * 1,3 --+
     * 2,3   | rigth side (vlength - 1)
     * 3,3 --+
     * 3,2 --+
     * 3,1   | bottom side (hlength - 1)
     * 3,0 --+
     * 2,0 --\ left side (vlength - 2)
     * 1,0 --/
     * 1,1 --\ top side (reassign: hlength = hlength - 2, vlength = vlength - 2) (hlength)
     * 1,2 --/
     * 2,2 --> right side (vlength - 1)
     * 2,1 --> bottom side (hlength - 1)
     */
    public int[] walk(int[][] field) {
        int size = field.length * field[0].length;
        int[] walk = new int[size];

        int hlength = field[0].length;
        int vlength = field.length;
        int currentSide = 0, sideWalk = 0;
        int path = 0, i = 0, j = 0;

        while(path < size) {
            walk[path] = field[i][j];

            sideWalk++;
            path++;

            switch(currentSide) {
                case 0:
                    j++;
                    if(sideWalk == hlength) {
                        currentSide++;
                        sideWalk = 0;
                        i++;
                        j--;
                    }
                    break;
                case 1:
                    i++;
                    if(sideWalk == vlength - 1) {
                        currentSide++;
                        sideWalk = 0;
                        i--;
                        j--;
                    }
                    break;
                case 2:
                    j--;
                    if(sideWalk == hlength - 1) {
                        currentSide++;
                        sideWalk = 0;
                        i--;
                        j++;
                    }
                    break;
                case 3:
                    i--;
                    if(sideWalk == vlength - 2) {
                        currentSide = 0;
                        sideWalk = 0;
                        hlength -= 2;
                        vlength -= 2;
                        i++;
                        j++;
                    }
                    break;
            }
        }

        return walk;
    }
}
