package dungeon.textui;

import dungeon.engine.GameEngine;
import dungeon.engine.Tile;
import java.util.ArrayList;
import java.util.Scanner;

public class TextUI {

    GameEngine engine = new GameEngine(10);

    public void drawUI() {
        System.out.println("HP: " + engine.getHP());
        System.out.println("Score: " + engine.getScore());
        System.out.println("Moves remaining: " + engine.getMovesLeft());
        Tile[][] grid = engine.getGrid();
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                System.out.print(tile.getContent() + " ");
            }
            System.out.println();
        }
        // Update message log
        String messages = "\n";
        ArrayList<String> messageLog = engine.getMessageLog();
        for (String s : messageLog) {
            messages = messages.concat(s);
            messages = messages.concat("\n");
        }
        System.out.println(messages);
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