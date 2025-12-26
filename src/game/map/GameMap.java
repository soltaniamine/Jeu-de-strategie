package game.map;

public class GameMap {

    private Tile[][] map;

    public GameMap(int width, int height) {
        map = new Tile[width][height];
        generateMap();
    }

    private void generateMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Tile(TileType.GRASS, true);
            }
        }
    }
}
