package dungeon.textui;

import dungeon.engine.GameEngine;
import dungeon.engine.Tile;
import java.util.Scanner;

public class TextUI {

    GameEngine engine = new GameEngine(10);

    public void drawUI() {
        Tile[][] grid = engine.getGrid();
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                System.out.print(tile.getContent() + " ");
            }
            System.out.println();
        }
    }

    public void processInput(String input) {
        engine.playerInput(input);
    }

    public static void main(String[] args) {
        TextUI ui = new TextUI();
        ui.drawUI();
        Scanner scn = new Scanner(System.in);

        while (ui.engine.getGameState().equals("running")) {
            System.out.print("Input: ");
            String input = scn.next();
            input = input.strip();
            ui.processInput(input);
            ui.drawUI();
        }

        scn.close();
    }
}