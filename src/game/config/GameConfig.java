package game.config;

/**
 * Configuration globale du jeu
 * Permet de modifier facilement les paramètres de la carte et de l'interface
 */
public class GameConfig {
    
    // Taille de la carte
    public static final int MAP_WIDTH = 15;
    public static final int MAP_HEIGHT = 15;
    
    // Taille des tuiles (en pixels)
    public static final int TILE_SIZE = 48;
    
    // Positions de départ des joueurs
    public static final int PLAYER_START_X = 2;
    public static final int PLAYER_START_Y = 2;
    public static final int ENEMY_START_X = 12;
    public static final int ENEMY_START_Y = 12;
    
    // Ressources de départ
    public static final int STARTING_GOLD = 100;
    public static final int STARTING_WOOD = 50;
    public static final int STARTING_STONE = 50;
    public static final int STARTING_FOOD = 100;
    
    // Couleurs de l'interface
    public static final java.awt.Color PLAYER_COLOR = java.awt.Color.BLUE;
    public static final java.awt.Color ENEMY_COLOR = java.awt.Color.RED;
    
    // Configurations prédéfinies
    public enum MapSize {
        SMALL(10, 10, 56),      // Petite carte : 10×10, tuiles 56px
        MEDIUM(15, 15, 48),     // Moyenne carte : 15×15, tuiles 48px (par défaut)
        LARGE(20, 20, 40);      // Grande carte : 20×20, tuiles 40px
        
        public final int width;
        public final int height;
        public final int tileSize;
        
        MapSize(int width, int height, int tileSize) {
            this.width = width;
            this.height = height;
            this.tileSize = tileSize;
        }
    }
    
    // Taille actuelle (modifiable)
    private static MapSize currentMapSize = MapSize.MEDIUM;
    
    public static MapSize getCurrentMapSize() {
        return currentMapSize;
    }
    
    public static void setMapSize(MapSize size) {
        currentMapSize = size;
    }
}