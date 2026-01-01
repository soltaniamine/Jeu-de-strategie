package game.unit;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Archer - Unité à distance
 * Attaque puissante à longue portée mais fragile en défense
 */
public class Archer extends Unit {

    public Archer(Player owner, int x, int y) {
        super("Archer", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 70;
        this.currentHealth = 70;
        this.attack = 20;
        this.defense = 5;
        this.range = 3;              // Attaque à distance
        this.movementPoints = 2;
        
        // Coût de recrutement
        this.cost.put(ResourceType.GOLD, 40);
        this.cost.put(ResourceType.WOOD, 15);
        this.cost.put(ResourceType.FOOD, 15);
    }

    /**
     * Capacité spéciale : Tir Précis
     * L'archer fait un tir qui ignore la moitié de la défense de la cible
     */
    public int precisionShot(Unit target) {
        if (this.hasActed) {
            System.out.println(this.name + " a déjà agi ce tour !");
            return 0;
        }

        if (!canAttack(target)) {
            System.out.println("Impossible d'attaquer cette cible !");
            return 0;
        }

        // Ignore 50% de la défense
        int effectiveDefense = target.getDefense() / 2;
        int damage = Math.max(1, this.attack - effectiveDefense);
        
        target.takeDamage(damage);
        this.hasActed = true;

        System.out.println(this.name + " utilise Tir Précis sur " + target.getName() + 
                         " et inflige " + damage + " dégâts !");

        return damage;
    }

    @Override
    public String toString() {
        return " " + super.toString();
    }
}