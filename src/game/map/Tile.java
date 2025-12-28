package game.map;

import game.unit.Unit;
import game.building.Building;

/**
 * Représente une case individuelle de la carte de jeu
 * Peut contenir un type de terrain, une unité, et un bâtiment
 */
public class Tile {
    private final int x;
    private final int y;
    private final TileType type;
    private Unit unit;              // Unité présente sur cette case (peut être null)
    private Building building;      // Bâtiment construit sur cette case (peut être null)
    private boolean explored;       // Si le joueur a exploré cette case (brouillard de guerre)

    /**
     * Constructeur d'une case
     * @param x Position X sur la carte
     * @param y Position Y sur la carte
     * @param type Type de terrain
     */
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.unit = null;
        this.building = null;
        this.explored = false;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public Unit getUnit() {
        return unit;
    }

    public Building getBuilding() {
        return building;
    }

    public boolean isExplored() {
        return explored;
    }

    // Setters
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    /**
     * Vérifie si la case est occupée par une unité
     */
    public boolean hasUnit() {
        return unit != null;
    }

    /**
     * Vérifie si la case a un bâtiment
     */
    public boolean hasBuilding() {
        return building != null;
    }

    /**
     * Vérifie si une unité peut se déplacer sur cette case
     */
    public boolean isAccessible() {
        return type.isWalkable() && !hasUnit();
    }

    /**
     * Retire l'unité de cette case
     */
    public void removeUnit() {
        this.unit = null;
    }

    /**
     * Retire le bâtiment de cette case
     */
    public void removeBuilding() {
        this.building = null;
    }

    @Override
    public String toString() {
        if (hasUnit()) {
            return "[U]"; // Unité présente
        } else if (hasBuilding()) {
            return "[B]"; // Bâtiment présent
        } else {
            return "[" + type.getSymbol() + "]";
        }
    }
}