package game.map;

public class Tile {

    private TileType type;
    private boolean accessible;

    public Tile(TileType type, boolean accessible) {
        this.type = type;
        this.accessible = accessible;
    }

    public TileType getType() {
        return type;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
