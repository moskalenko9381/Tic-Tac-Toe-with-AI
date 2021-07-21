package tictactoe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        char[][] table = new char[3][3];
        makeTable(table);
        game(table);
    }

    static void makeTable(char[][] table) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                table[i][j] = '_';
    }

    static void game(char[][] table) {
        while (true) {
            ArrayList<String> result = new ArrayList<String>();
            inputCommand(result);         // получаем список, кто первый игрок, а кто второй
            printTable(table);
            char firstMove = 'X';
            char secondMove = 'O';

            if ((result.get(0).equals("easy") || result.get(0).equals("medium"))
                    && result.get(1).equals("user")) {
                firstMove = 'O';
                secondMove = 'X';
            }

            boolean win = false;
            String resultString = null;

            while (!win) {        // цикл игры
                if (result.get(0).equals("user"))
                    userMove(table, firstMove);
                if (result.get(0).equals("easy"))
                    makeStepEasy(table, firstMove);    // компьютер делает ход
                if (result.get(0).equals("medium"))
                    makeStepMedium(table, firstMove, secondMove);
                if (result.get(0).equals("hard"))
                    makeStepHard(table, firstMove, secondMove);

                resultString = checkWin(table);
                if (!(resultString.equals("Game not finished"))) {
                    break;
                }

                if (result.get(1).equals("user"))
                    userMove(table, secondMove);
                if (result.get(1).equals("easy"))
                    makeStepEasy(table, secondMove);    // компьютер делает ход
                if (result.get(1).equals("medium"))
                    makeStepMedium(table, secondMove, firstMove);
                if (result.get(1).equals("hard"))
                    makeStepHard(table, secondMove, firstMove);

                resultString = checkWin(table);
                if (!(resultString.equals("Game not finished")))
                    win = true;
            }

            System.out.println(resultString);
            result.clear();
            makeTable(table);
        }
    }

    static void makeStepHard(char[][] table, char ai, char human) {
        System.out.println("Making move level \"hard\"");
        Result next = miniMax(table, ai, human, true);
        table[next.move.x][next.move.y] = ai;
        printTable(table);
    }

    static Boolean selectSpace(char[][] table, Coordinates move, char sign) {
        if (move.x >= 0 && move.y >= 0)
            if (table[move.x][move.y] == '_') {
                table[move.x][move.y] = sign;
                return true;
            }
        return false;
    }

    // возвращает список индексов пустых клеток доски
    static ArrayList<Coordinates> emptyIndexes(char[][] table) {
        ArrayList<Coordinates> moves = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++)
                if (table[i][j] == '_')
                    moves.add(new Coordinates(i, j));
        }
    return moves;
    }

    static int evaluateTable(char[][] table, char ai, char human) {
        if (winning(table, ai))
            return 1;
        if (winning(table, human))
            return -1;
        return 0;
    }

    static Boolean winning(char[][] table, char player) {
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
        if (!checkWin(inputTable).equals("Game not finished"))
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


    static void userMove(char[][] table, char sign) {
        Coordinates res = checkCell(table); //ввод и проверка проверка координат
        table[res.x - 1][res.y - 1] = sign;  //записываем ход
        printTable(table);   // выводим таблицу
    }

    static void inputCommand(ArrayList<String> result) {
        System.out.print("Input command: ");
        Scanner console = new Scanner(System.in);
        String command;
        command = console.nextLine();

        String[] subStr = command.split(" ");

        if (subStr[0].equals("exit"))
            System.exit(0);

        if (!subStr[0].equals("start")) {
            System.out.println("Bad parameters for start!");
            inputCommand(result);

        } else {
            if (subStr.length < 3){
                System.out.println("Bad parameters!");
                inputCommand(result);
        }
            for (String s: subStr) {
                if (s.equals("start"))
                    continue;

                if (!s.equals("user") && !s.equals("easy") && !s.equals("medium") && !s.equals("hard")) {  // здесь 2 уровня
                    System.out.println("Bad parameters!");
                    inputCommand(result);
                }
                else result.add(s);
            }
        }
    }

    static void randomPos(char[][]table, char sign){
        Random random = new Random();
        int x = random.nextInt(3);
        int y = random.nextInt(3);
        if (table[x][y] != '_') {
            randomPos(table, sign);
            return;
        }
        table[x][y] = sign;
    }

    static void makeStepEasy(char[][] table, char sign){
        System.out.println("Making move level \"easy\"");
        randomPos(table, sign);
        printTable(table);
    }

    static void makeStepMedium(char[][] table, char mySign, char signEnemy){
        System.out.println("Making move level \"medium\"");
        if (stepCheckMyLastStep(table, mySign)) {
            System.out.println("my step");
            printTable(table);
            return;
        }
        else if (stepCheckEnemyLastStep(table, mySign, signEnemy)) {
            System.out.println("enemy step");
            printTable(table);
            return;
        }
        System.out.println("random");
        randomPos(table, mySign);
        printTable(table);
    }

    static Boolean stepCheckMyLastStep(char[][] table, char sign) {
       for (int x = 0; x < 3; x++) {   // horizontal
           if (table[x][0] == sign && table[x][1] == sign
                   && table[x][2] == '_') {
               table[x][2] = sign;
               return true;
           }
           if (table[x][1] == sign && table[x][2] == sign
                   && table[x][0] == '_') {
               table[x][0] = sign;
               return true;
           }
           if (table[x][0] == sign && table[x][2] == sign
                   && table[x][1] == '_') {
               table[x][1] = sign;
               return true;
           }
       }

       for (int y = 0; y < 3; y++) {    // vertical
           if (table[0][y] == sign && table[1][y] == sign
                   && table[2][y] == '_') {
               table[2][y] = sign;
               return true;
           }
           if (table[1][y] == sign && table[2][y] == sign
                   && table[0][y] == '_') {
               table[0][y] = sign;
               return true;
           }
           if (table[0][y] == sign && table[2][y] == sign
                   && table[1][y] == '_') {
               table[1][y] = sign;
               return true;
           }
       }
        // diagonal
       if (table[0][0] == sign && table[1][1] == sign
       && table[2][2] == '_') {
           table[2][2] = sign;
           return true;
       }
        if (table[0][0] == sign && table[2][2] == sign
                && table[1][1] == '_') {
            table[1][1] = sign;
            return true;
        }
        if (table[2][2] == sign && table[1][1] == sign
                && table[0][0] == '_') {
            table[0][0] = sign;
            return true;
        }

        if (table[0][2] == sign && table[1][1] == sign
                && table[2][0] == '_') {
            table[2][0] = sign;
            return true;
        }
        if (table[0][2] == sign && table[2][0] == sign
                && table[1][1] == '_') {
            table[1][1] = sign;
            return true;
        }
        if (table[1][1] == sign && table[2][0] == sign
                && table[0][2] == '_') {
            table[0][2] = sign;
            return true;
        }
        return false;
    }

    static Boolean stepCheckEnemyLastStep(char[][] table,  char mySign, char signEnemy) {
        for (int x = 0; x < 3; x++) {   // horizontal
            if (table[x][0] == signEnemy && table[x][1] == signEnemy
                    && table[x][2] == '_') {
                table[x][2] = mySign;
                return true;
            }
            if (table[x][1] == signEnemy && table[x][2] == signEnemy
                    && table[x][0] == '_') {
                table[x][0] = mySign;
                return true;
            }
            if (table[x][0] == signEnemy && table[x][2] == signEnemy
                    && table[x][1] == '_') {
                table[x][1] = mySign;
                return true;
            }
        }

        for (int y = 0; y < 3; y++) {    // vertical
            if (table[0][y] == signEnemy && table[1][y] == signEnemy
                    && table[2][y] == '_') {
                table[2][y] = mySign;
                return true;
            }
            if (table[1][y] == signEnemy && table[2][y] == signEnemy
                    && table[0][y] == '_') {
                table[0][y] = mySign;
                return true;
            }
            if (table[0][y] == signEnemy && table[2][y] == signEnemy
                    && table[1][y] == '_') {
                table[1][y] = mySign;
                return true;
            }
        }
        // diagonal
        if (table[0][0] == signEnemy && table[1][1] == signEnemy
                && table[2][2] == '_') {
            table[2][2] = mySign;
            return true;
        }
        if (table[0][0] == signEnemy && table[2][2] == signEnemy
                && table[1][1] == '_') {
            table[1][1] = mySign;
            return true;
        }
        if (table[2][2] == signEnemy && table[1][1] == signEnemy
                && table[0][0] == '_') {
            table[0][0] = mySign;
            return true;
        }

        if (table[0][2] == signEnemy && table[1][1] == signEnemy
                && table[2][0] == '_') {
            table[2][0] = mySign;
            return true;
        }

        if (table[0][2] == signEnemy && table[2][0] == signEnemy
                && table[1][1] == '_') {
            table[1][1] = mySign;
            return true;
        }
        if (table[1][1] == signEnemy && table[2][0] == signEnemy
                && table[0][2] == '_') {
            table[0][2] = mySign;
            return true;
        }
        return false;
    }

    static void printTable(char[][] table) {
        System.out.println("---------");

        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                if (table[i][j] == '_')
                    System.out.print("  ");
                else
                    System.out.print(table[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }


    static Coordinates checkCell(char[][] table) {
        int x, y;
        Scanner console = new Scanner(System.in);
        System.out.println("Enter the coordinates:");
        if (console.hasNextInt()) { // возвращает истину, если с потока ввода можно считать целое число
            x = console.nextInt();
            if (console.hasNextInt()) {
                y = console.nextInt();
            } else {
                System.out.println("You should enter numbers!");
                return checkCell(table);
            }
        } else {
            System.out.println("You should enter numbers!");
            return checkCell(table);
        }

        if (x > 3 || y > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return checkCell(table);
        }

        if (table[x - 1][y - 1] != '_') {
            System.out.println("This cell is occupied! Choose another one!");
            return checkCell(table);
        }
        return new Coordinates(x, y);
    }

    static String checkWin(char[][] table) {
        // row
        for (char[] chars : table) {
            int count = 0;
            for (char aChar : chars) {
                if (aChar == chars[1] && chars[1] != '_')
                    count++;
            }
            if (count == 3) {
                return chars[0] + " wins";
            }
        }
        // diagonal
        if (table[0][0] == table[1][1] && table[1][1] == table[2][2] && table[1][1] != '_' ||
                table[0][2] == table[1][1] && table[1][1] == table[2][0] && table[1][1] != '_')
            return table[1][1] + " wins";

        // up and down
        if (table[0][0] == table[1][0] && table[1][0] == table[2][0] && table[0][0] != '_')
            return table[0][0] + " wins";
        if (table[0][1] == table[1][1] && table[1][1] == table[2][1] && table[1][1] != '_')
            return table[1][1] + " wins";
        if (table[0][2] == table[1][2] && table[1][2] == table[2][2] && table[2][2] != '_')
            return table[2][2] + " wins";


        for (char[] chars : table) {
            for (char aChar : chars) {
                if (aChar == '_')
                    return "Game not finished";
            }
        }
        return "Draw";
    }
}
