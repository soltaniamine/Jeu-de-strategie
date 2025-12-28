package game.unit;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Cavalier - Unité rapide et mobile
 * Déplacement élevé, bonne attaque, défense moyenne
 */
public class Cavalry extends Unit {

    public Cavalry(Player owner, int x, int y) {
        super("Cavalier", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 90;
        this.currentHealth = 90;
        this.attack = 18;
        this.defense = 7;
        this.range = 1;              // Corps à corps
        this.movementPoints = 5;     // Très mobile
        
        // Coût de recrutement
        this.cost.put(ResourceType.GOLD, 50);
        this.cost.put(ResourceType.FOOD, 30);
    }

    /**
     * Capacité spéciale : Charge
     * Le cavalier inflige des dégâts bonus si il se déplace avant d'attaquer
     */
    public int charge(Unit target) {
        if (this.hasActed) {
            System.out.println(this.name + " a déjà agi ce tour !");
            return 0;
        }

        if (!canAttack(target)) {
            System.out.println("Impossible d'attaquer cette cible !");
            return 0;
        }

        // Dégâts de charge : +10 bonus
        int damage = Math.max(1, (this.attack + 10) - target.getDefense());
        
        target.takeDamage(damage);
        this.hasActed = true;

        System.out.println(this.name + " charge " + target.getName() + 
                         " et inflige " + damage + " dégâts !");

        return damage;
    }

    @Override
    public String toString() {
        return "🐎 " + super.toString();
    }
}