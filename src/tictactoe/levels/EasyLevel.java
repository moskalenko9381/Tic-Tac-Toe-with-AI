package src.tictactoe.levels;

import src.tictactoe.Main;

import java.util.Random;

public class EasyLevel implements ILevel{
    public static void randomPos(char[][]table, char sign){
        Random random = new Random();
        int x = random.nextInt(3);
        int y = random.nextInt(3);
        if (table[x][y] != '_') {
            randomPos(table, sign);
            return;
        }
        table[x][y] = sign;
    }

    public static void makeStep(char[][] table, char ai, char human){
        System.out.println("Making move level \"easy\"");
        randomPos(table, ai);
        Main.printTable(table);
    }
}
