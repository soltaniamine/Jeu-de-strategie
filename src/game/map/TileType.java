package game.map;

/**
 * Énumération des différents types de cases du terrain
 * Chaque type a des propriétés qui influencent le gameplay
 */
public enum TileType {
    GRASS("Herbe", true, 1.0, 0, '▓'),
    FOREST("Forêt", true, 0.75, 1, '♣'),
    MOUNTAIN("Montagne", true, 0.5, 2, '▲'),
    WATER("Eau", false, 0.0, 0, '≈'),
    DESERT("Désert", true, 1.2, -1, '░');

    private final String name;
    private final boolean walkable;
    private final double movementCost;  // Coût de déplacement (1.0 = normal)
    private final int defenseBonus;     // Bonus de défense pour les unités
    private final char symbol;          // Symbole pour affichage console

    /**
     * Constructeur de TileType
     * @param name Nom du terrain
     * @param walkable Si les unités peuvent marcher dessus
     * @param movementCost Coût de déplacement (1.0 = normal, >1 = plus lent)
     * @param defenseBonus Bonus de défense accordé aux unités
     * @param symbol Caractère pour l'affichage
     */
    TileType(String name, boolean walkable, double movementCost, int defenseBonus, char symbol) {
        this.name = name;
        this.walkable = walkable;
        this.movementCost = movementCost;
        this.defenseBonus = defenseBonus;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public double getMovementCost() {
        return movementCost;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return name;
    }
}