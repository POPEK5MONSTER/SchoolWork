package psm7;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        double[][] board = new double[41][41];

        Arrays.fill(board[0], 200);
        Arrays.fill(board[board.length - 1], 150);

        for (int i = 1; i < board.length - 1; i++) {
            board[i][0] = 100;
            board[i][board[i].length - 1] = 50;
        }

        for (int k = 0; k < 1000; k++) {
            for (int i = 1; i < board.length - 1; i++) {
                for (int j = 1; j < board[i].length - 1; j++) {
                    board[i][j] = (board[i - 1][j] + board[i + 1][j] + board[i][j - 1] + board[i][j + 1]) / 4;
                }
            }
        }

        for (double[] row : board) {
            for (double value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

    }
}