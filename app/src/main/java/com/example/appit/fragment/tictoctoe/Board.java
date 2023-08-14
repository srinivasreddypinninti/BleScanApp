package com.example.appit.fragment.tictoctoe;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Board {

    int size;
    PlayingPiece[][] board;

    public Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
    }


    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                PlayingPiece piece = board[i][j];
                if (piece != null) {
                    System.out.print(piece.pieceType.name() + " ");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }

    }

    public boolean addPiece(int row, int column, PlayingPiece piece) {
        if (board[row][column] != null) {
            return false;
        }
        board[row][column] = piece;
        return true;
    }

    public List<Pair<Integer, Integer>> getFreeCells() {
        List<Pair<Integer, Integer>> freeCells = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    Pair<Integer, Integer> rowCol = new Pair<>(i, j);
                    freeCells.add(rowCol);
                }
            }
        }
        return freeCells;
     }
}
