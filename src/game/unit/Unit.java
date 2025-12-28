package game.unit;

import game.player.Player;
import game.resource.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite représentant une unité militaire
 * Toutes les unités héritent de cette classe
 */
public abstract class Unit {
    protected String name;
    protected Player owner;
    protected int maxHealth;
    protected int currentHealth;
    protected int attack;
    protected int defense;
    protected int range;           // Portée d'attaque (1 = corps à corps)
    protected int movementPoints;  // Points de déplacement par tour
    protected int x;               // Position X sur la carte
    protected int y;               // Position Y sur la carte
    protected boolean hasActed;    // Si l'unité a déjà agi ce tour
    protected Map<ResourceType, Integer> cost;

    /**
     * Constructeur de l'unité
     */
    public Unit(String name, Player owner, int x, int y) {
        this.name = name;
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.hasActed = false;
        this.cost = new HashMap<>();
        initializeStats();
    }

    /**
     * Méthode abstraite pour initialiser les statistiques de l'unité
     * Chaque type d'unité définit ses propres stats
     */
    protected abstract void initializeStats();

    /**
     * Attaque une unité ennemie
     * @param target Unité cible
     * @return Dégâts infligés
     */
    public int attack(Unit target) {
        if (this.hasActed) {
            System.out.println(this.name + " a déjà agi ce tour !");
            return 0;
        }

        // Calcul des dégâts : attaque de l'attaquant - défense du défenseur
        int damage = Math.max(1, this.attack - target.defense);
        
        target.takeDamage(damage);
        this.hasActed = true;

        System.out.println(this.name + " attaque " + target.getName() + 
                         " et inflige " + damage + " dégâts !");

        return damage;
    }

    /**
     * L'unité reçoit des dégâts
     * @param damage Dégâts reçus
     */
    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            die();
        }
    }

    /**
     * Soigne l'unité
     * @param amount Quantité de santé à restaurer
     */
    public void heal(int amount) {
        this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
        System.out.println(this.name + " récupère " + amount + " PV !");
    }

    /**
     * Déplace l'unité vers une nouvelle position
     * @param newX Nouvelle position X
     * @param newY Nouvelle position Y
     */
    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.hasActed = true;
        System.out.println(this.name + " se déplace vers (" + newX + ", " + newY + ")");
    }

    /**
     * Calcule la distance jusqu'à une position
     */
    public int distanceTo(int targetX, int targetY) {
        return Math.abs(this.x - targetX) + Math.abs(this.y - targetY);
    }

    /**
     * Vérifie si l'unité peut attaquer une cible
     */
    public boolean canAttack(Unit target) {
        int distance = distanceTo(target.getX(), target.getY());
        return !this.hasActed && 
               this.isAlive() && 
               target.isAlive() && 
               distance <= this.range &&
               this.owner != target.owner;
    }

    /**
     * L'unité meurt
     */
    protected void die() {
        System.out.println(this.name + " est mort !");
        if (owner != null) {
            owner.removeUnit(this);
        }
    }

    /**
     * Vérifie si l'unité est en vie
     */
    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    /**
     * Réinitialise l'état de l'unité pour un nouveau tour
     */
    public void resetTurn() {
        this.hasActed = false;
    }

    /**
     * Affiche les informations de l'unité
     */
    public void displayInfo() {
        System.out.println("\n=== " + name + " ===");
        System.out.println("Propriétaire: " + (owner != null ? owner.getName() : "Aucun"));
        System.out.println("Position: (" + x + ", " + y + ")");
        System.out.println("Santé: " + currentHealth + "/" + maxHealth);
        System.out.println("Attaque: " + attack);
        System.out.println("Défense: " + defense);
        System.out.println("Portée: " + range);
        System.out.println("Déplacement: " + movementPoints);
        System.out.println("Statut: " + (hasActed ? "A agi" : "Prêt"));
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getRange() {
        return range;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasActed() {
        return hasActed;
    }

    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }

    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(cost);
    }

    @Override
    public String toString() {
        return name + " (" + currentHealth + "/" + maxHealth + " PV)";
    }
}