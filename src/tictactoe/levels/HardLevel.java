package src.tictactoe.levels;

import src.tictactoe.Coordinates;
import src.tictactoe.Main;
import src.tictactoe.Result;

import java.util.ArrayList;

public class HardLevel implements ILevel{
    public static void makeStep(char[][] table, char ai, char human) {
        System.out.println("Making move level \"hard\"");
        Result next = miniMax(table, ai, human, true);
        table[next.move.x][next.move.y] = ai;
        Main.printTable(table);
    }

    public static Boolean selectSpace(char[][] table, Coordinates move, char sign) {
        if (move.x >= 0 && move.y >= 0)
            if (table[move.x][move.y] == '_') {
                table[move.x][move.y] = sign;
                return true;
            }
        return false;
    }

    // возвращает список индексов пустых клеток доски
    public static ArrayList<Coordinates> emptyIndexes(char[][] table) {
        ArrayList<Coordinates> moves = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++)
                if (table[i][j] == '_')
                    moves.add(new Coordinates(i, j));
        }
        return moves;
    }

    public static int evaluateTable(char[][] table, char ai, char human) {
        if (winning(table, ai))
            return 1;
        if (winning(table, human))
            return -1;
        return 0;
    }

    public static Boolean winning(char[][] table, char player) {
        return ((table[0][0] == player && table[0][1] == player && table[0][2] == player) ||
                (table[1][0] == player && table[1][1] == player && table[1][2] == player) ||
                (table[2][0] == player && table[2][1] == player && table[2][2] == player) ||
                (table[0][0] == player && table[1][0] == player && table[2][0] == player) ||
                (table[0][1] == player && table[1][1] == player && table[2][1] == player) ||
                (table[0][2] == player && table[1][2] == player && table[2][2] == player) ||
                (table[0][0] == player && table[1][1] == player && table[2][2] == player) ||
                (table[0][2] == player && table[1][1] == player && table[2][0] == player));
    }


    static Result miniMax(char[][] inputTable, char aiPlayer, char huPlayer, Boolean isMaximizing) {
        ArrayList<Coordinates> availSpots = emptyIndexes(inputTable);
        if (!Main.checkWin(inputTable).equals("Game not finished"))
            return new Result(evaluateTable(inputTable, aiPlayer, huPlayer), new Coordinates(-1, -1));
        int bestScore = -2;
        char sign;
        Coordinates bestMove = new Coordinates(-1,-1);

        if (isMaximizing) {
            bestScore = -1000;
            sign = aiPlayer;
        } else {
            bestScore = 1000;
            sign = huPlayer;
        }

        for (Coordinates move : availSpots) {
            char[][] newTable = new char[3][3];
            for (int i = 0; i < 3; i++)
                System.arraycopy(inputTable[i], 0, newTable[i], 0, 3);

            selectSpace(newTable, move, sign);
            int mbValue = miniMax(newTable, aiPlayer, huPlayer, !isMaximizing).score;
            if (isMaximizing && mbValue > bestScore) {
                bestScore = mbValue;
                bestMove = move;
            }
            if (!isMaximizing && mbValue < bestScore) {
                bestScore = mbValue;
                bestMove = move;
            }
        }
        return new Result(bestScore, bestMove);
    }
}
