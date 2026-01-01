package game.building;

import game.player.Player;
import game.resource.ResourceType;

public class Sawmill extends Building {
    public Sawmill(Player owner, int x, int y) {
        super("Scierie", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 180;
        this.currentHealth = 180;
        this.constructionTime = 2;
        
        // Coût de construction
        this.cost.put(ResourceType.GOLD, 40);
        this.cost.put(ResourceType.STONE, 15);
        
        // Production par tour
        this.production.put(ResourceType.WOOD, 12);
    }

    @Override
    public void performAction() {
        if (!isBuilt) {
            return;
        }
        produceResources();
    }

    @Override
    public String toString() {
        return " " + super.toString();
    }
}