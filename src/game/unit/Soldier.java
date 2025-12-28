package game.unit;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Soldat - Unité de base corps à corps
 * Équilibré entre attaque et défense, déplacement moyen
 */
public class Soldier extends Unit {

    public Soldier(Player owner, int x, int y) {
        super("Soldat", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 100;
        this.currentHealth = 100;
        this.attack = 15;
        this.defense = 10;
        this.range = 1;              // Corps à corps
        this.movementPoints = 3;
        
        // Coût de recrutement
        this.cost.put(ResourceType.GOLD, 30);
        this.cost.put(ResourceType.FOOD, 20);
    }

    /**
     * Capacité spéciale : Bouclier
     * Le soldat peut augmenter temporairement sa défense
     */
    public void shieldWall() {
        if (!this.hasActed) {
            this.defense += 5;
            this.hasActed = true;
            System.out.println(this.name + " active Mur de Boucliers ! Défense +5");
        } else {
            System.out.println(this.name + " a déjà agi ce tour !");
        }
    }

    @Override
    public String toString() {
        return "🛡️ " + super.toString();
    }
}