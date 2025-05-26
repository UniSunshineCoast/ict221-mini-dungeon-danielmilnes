import org.junit.jupiter.api.Test;
import dungeon.engine.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {
    @Test
    void testParentTile() {
        Tile tile = new Tile("tile_parent_class", "!", "");
        assertEquals("tile_parent_class", tile.getType());
        assertEquals("!", tile.getContent());
        assertEquals("", tile.getFXStyle());
    }

    @Test
    void testPlayerTile() {
        Tile playerTile = new PlayerTile();
        assertEquals("player", playerTile.getType());
        assertEquals("@", playerTile.getContent());
    }

    @Test
    void testOtherTiles() {
        Tile entry = new EntryTile();
        assertEquals("entry", entry.getType());
        Tile floor = new FloorTile();
        assertEquals("floor", floor.getType());
        Tile gold = new GoldTile();
        assertEquals("gold", gold.getType());
        Tile potion = new HealthPotionTile();
        assertEquals("healthPotion", potion.getType());
        Tile ladder = new LadderTile();
        assertEquals("ladder", ladder.getType());
        Tile melee = new MeleeMutantTile();
        assertEquals("meleeMutant", melee.getType());
        Tile ranged = new RangedMutantTile();
        assertEquals("rangedMutant", ranged.getType());
        Tile trap = new TrapTile();
        assertEquals("trap", trap.getType());
        Tile wall = new WallTile();
        assertEquals("wall", wall.getType());
    }
}