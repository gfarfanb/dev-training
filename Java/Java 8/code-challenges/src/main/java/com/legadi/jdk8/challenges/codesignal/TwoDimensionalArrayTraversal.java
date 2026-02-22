package com.legadi.jdk8.challenges.codesignal;

public class TwoDimensionalArrayTraversal {

    public int choosePosition(int[][] field, int[][] figure) {
        int positionLength = field[0].length - figure[0].length + 1;

        for(int position = 0; position < positionLength; position++) {
            int levelLength = field.length - figure.length + 1;

            for(int level = 0; level < levelLength; level++) {
                if(hasFilledRows(position, level, field, figure)) {
                    return position;
                }
            }
        }

        return -1;
    }

    private boolean hasFilledRows(int position, int level, int[][] field, int[][] figure) {
        boolean filledFound = false;

        for(int i = level; i < figure.length + level; i++) {
            int rowFilledCount = 0;

            for(int j = 0; j < field[i].length; j++) {
                int figureLevel = i - level;
                int figurePosition = j - position;
                int cellCount = 0;

                rowFilledCount += field[i][j];
                cellCount += field[i][j];

                if(figurePosition >= 0 && figurePosition < figure[figureLevel].length) {
                    rowFilledCount += figure[figureLevel][figurePosition];
                    cellCount += figure[figureLevel][figurePosition];
                }

                if(cellCount > 1) {
                    return false;
                }
            }

            if(rowFilledCount == field[i].length) {
                filledFound = true;
            }
        }

        return filledFound;
    }
}
