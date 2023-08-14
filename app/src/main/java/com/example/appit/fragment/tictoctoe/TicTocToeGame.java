package com.example.appit.fragment.tictoctoe;

import android.util.Pair;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TicTocToeGame {

    Deque<Player> players;
    Board gameBoard;

    public TicTocToeGame() {
        init();
    }

    private void init() {
        players = new LinkedList<>();
        gameBoard = new Board(3);

        Player p1 = new Player("Player1", new PlayingPieceX());
        Player p2 = new Player("Player2", new PlayingPieceO());

        players.add(p1);
        players.add(p2);
    }

    public String startGame() {
        boolean noWinner = true;
        while (noWinner) {
            Player playerTurn = players.removeFirst();
            gameBoard.printBoard();
            List<Pair<Integer, Integer>> freeCells = gameBoard.getFreeCells();
            if (freeCells.isEmpty()) {
                noWinner = false;
                continue;
            }

            // read input from user
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            String[] values = s.split(",");
            int row = Integer.parseInt(values[0]);
            int col = Integer.parseInt(values[1]);

           boolean result = gameBoard.addPiece(row, col, playerTurn.piece);
           if (!result) {
               System.out.println("Incorredt possition chosen, try again");
               players.addFirst(playerTurn);
               continue;
           }
            players.addLast(playerTurn);

           boolean winner = isWinner(row, col, playerTurn.piece.pieceType);
           if (winner) {
               return playerTurn.name;
           }

        }
        return "tie";
    }

    private boolean isWinner(int row, int col, PieceType piece) {
        boolean rowMatched = true;
        boolean colMatched = true;
        boolean diagonalMatched = true;
        boolean antiDiagonalMatched = true;

        //row
        for (int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[row][i] != null && gameBoard.board[row][i].pieceType != piece) {
                rowMatched = false;
            }
        }

        //column
        for (int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[i][col] != null && gameBoard.board[i][col].pieceType != piece) {
                colMatched = false;
            }
        }

        //Diagonal
        for (int i = 0, j=0; i < gameBoard.size; i++, j++) {
            if (gameBoard.board[i][j] != null && gameBoard.board[i][j].pieceType != piece) {
                diagonalMatched = false;
            }
        }

        //AntiDiagonal
        for (int i = 0, j=gameBoard.size-1; j >=0 ; i++, j--) {
            if (gameBoard.board[i][j] != null && gameBoard.board[i][j].pieceType != piece) {
                antiDiagonalMatched = false;
            }
        }

        return rowMatched || colMatched || diagonalMatched || antiDiagonalMatched;
    }
}
