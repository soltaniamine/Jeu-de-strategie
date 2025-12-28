package game.building;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Mine - Bâtiment de production
 * Produit de la pierre nécessaire pour construire des bâtiments avancés
 */
public class Mine extends Building {

    public Mine(Player owner, int x, int y) {
        super("Mine", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 250;
        this.currentHealth = 250;
        this.constructionTime = 3;
        
        // Coût de construction
        this.cost.put(ResourceType.GOLD, 50);
        this.cost.put(ResourceType.WOOD, 30);
        
        // Production par tour
        this.production.put(ResourceType.STONE, 10);
        this.production.put(ResourceType.GOLD, 5);
    }

    @Override
    public void performAction() {
        if (!isBuilt) {
            return;
        }
        
        // La mine produit automatiquement pierre et or
        produceResources();
    }

    @Override
    public String toString() {
        return "⛏️ " + super.toString();
    }
}