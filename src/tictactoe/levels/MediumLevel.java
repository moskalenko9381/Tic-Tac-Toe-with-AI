package src.tictactoe.levels;

import src.tictactoe.Main;

public class MediumLevel implements ILevel{
    public static void makeStep(char[][] table, char ai, char human){
        System.out.println("Making move level \"medium\"");
        if (Main.stepCheckMyLastStep(table, ai)) {
            System.out.println("my step");
            Main.printTable(table);
            return;
        }
        else if (Main.stepCheckEnemyLastStep(table, ai, human)) {
            System.out.println("enemy step");
            Main.printTable(table);
            return;
        }
        System.out.println("random");
        EasyLevel.randomPos(table, ai);
        Main.printTable(table);
    }
}
