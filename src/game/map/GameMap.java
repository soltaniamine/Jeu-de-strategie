package game.map;

import java.util.Random;

/**
 * Représente la carte de jeu complète
 * Gère la génération et l'accès aux cases
 */
public class GameMap {
    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private final Random random;

    /**
     * Constructeur avec taille par défaut (25x25)
     */
    public GameMap() {
        this(25, 25);
    }

    /**
     * Constructeur avec taille personnalisée
     * @param width Largeur de la carte
     * @param height Hauteur de la carte
     */
    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
        this.random = new Random();
        generateMap();
    }

    /**
     * Génère la carte de manière procédurale
     * Distribution aléatoire des terrains avec des règles simples
     */
    private void generateMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileType type = generateTileType(x, y);
                tiles[y][x] = new Tile(x, y, type);
            }
        }
    }

    /**
     * Détermine le type de terrain pour une position donnée
     * Utilise des probabilités pour une carte équilibrée
     */
    private TileType generateTileType(int x, int y) {
        int rand = random.nextInt(100);
        
        // 50% Herbe (terrain de base)
        if (rand < 50) {
            return TileType.GRASS;
        }
        // 20% Forêt
        else if (rand < 70) {
            return TileType.FOREST;
        }
        // 15% Montagne
        else if (rand < 85) {
            return TileType.MOUNTAIN;
        }
        // 10% Eau
        else if (rand < 95) {
            return TileType.WATER;
        }
        // 5% Désert
        else {
            return TileType.DESERT;
        }
    }

    /**
     * Récupère une case aux coordonnées données
     * @return La case ou null si hors limites
     */
    public Tile getTile(int x, int y) {
        if (isValidPosition(x, y)) {
            return tiles[y][x];
        }
        return null;
    }

    /**
     * Vérifie si une position est valide sur la carte
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Calcule la distance de Manhattan entre deux positions
     * Utile pour les déplacements et la portée des unités
     */
    public int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    /**
     * Vérifie si deux positions sont adjacentes (incluant diagonales)
     */
    public boolean areAdjacent(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        return (dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0);
    }

    /**
     * Affiche la carte dans la console (version simple)
     */
    public void display() {
        System.out.println("=== Carte de jeu ===");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(tiles[y][x].getType().getSymbol() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Affiche une partie de la carte autour d'une position
     * @param centerX Centre X
     * @param centerY Centre Y
     * @param radius Rayon d'affichage
     */
    public void displayArea(int centerX, int centerY, int radius) {
        System.out.println("=== Vue locale ===");
        for (int y = centerY - radius; y <= centerY + radius; y++) {
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                if (isValidPosition(x, y)) {
                    System.out.print(tiles[y][x] + " ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}