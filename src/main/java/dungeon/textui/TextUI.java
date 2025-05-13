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
        switch (input) {
            case "up":
                // tell engine to move up
            case "down":
                // tell engine to move down
            case "left":
                // tell engine to move left
            case "right":
                // tell engine to move right
            default:
                System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) {
        TextUI ui = new TextUI();
        ui.drawUI();
        Scanner scn = new Scanner(System.in);

        while (ui.engine.getGameState().equals("running")) {
            System.out.print("Input: ");
            String input = scn.next();
            ui.processInput(input);
        }

        scn.close();
    }
}