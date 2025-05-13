package dungeon.engine;

public class Tile {
    private String type = "tile_parent_class";
    private String content = "!";
    private String fxStyle = "";

    public Tile(String type, String content, String fxStyle) {
        this.type = type;
        this.content = content;
        this.fxStyle = fxStyle;
    }

    public String getType() { return this.type; }

    public String getContent() { return this.content; }

    public String getFXStyle() { return this.fxStyle; }
}