package dungeon.textui;

import dungeon.engine.GameEngine;
import dungeon.engine.Tile;

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

    public static void main(String[] args) {
        TextUI ui = new TextUI();
        ui.drawUI();
    }
}