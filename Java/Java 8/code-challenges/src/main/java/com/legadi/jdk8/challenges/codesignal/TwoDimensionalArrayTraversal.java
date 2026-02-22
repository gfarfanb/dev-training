package com.legadi.jdk8.challenges.codesignal;

public class TwoDimensionalArrayTraversal {

    public int choosePosition(int[][] field, int[][] figure) {
        int positionLength = field[0].length - figure[0].length + 1;

        for(int position = 0; position < positionLength; position++) {
            int levelLength = field.length - figure.length + 1;
            int lastFitLevel = -1;

            for(int level = 0; level < levelLength; level++) {
                if(canFit(position, level, field, figure)) {
                    lastFitLevel = level;
                } else {
                    break;
                }
            }

            if(lastFitLevel > -1 && filledRows(position, lastFitLevel, field, figure)) {
                return position;
            }
        }

        return -1;
    }

    private boolean filledRows(int position, int level, int[][] field, int[][] figure) {
        for(int i = level; i < figure.length + level; i++) {
            int columnCount = 0;

            for(int j = 0; j < field[i].length; j++) {
                int figureLevel = i - level;
                int figurePosition = j - position;

                columnCount += field[i][j];

                if(figurePosition >= 0 && figurePosition < figure[figureLevel].length) {
                    columnCount += figure[figureLevel][figurePosition];
                }
            }

            if(columnCount == field[i].length) {
                return true;
            }
        }

        return false;
    }

    private boolean canFit(int position, int level, int[][] field, int[][] figure) {
        for(int i = 0; i < figure.length; i++) {
            for(int j = 0; j < figure[i].length; j++) {
                int cell = figure[i][j] + field[i + level][j + position];

                if(cell > 1) {
                    return false;
                }
            }
        }

        return true;
    }

}
