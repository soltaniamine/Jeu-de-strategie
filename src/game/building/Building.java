package game.building;

import game.player.Player;
import game.resource.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite représentant un bâtiment
 * Tous les bâtiments héritent de cette classe
 */
public abstract class Building {
    protected String name;
    protected Player owner;
    protected int x;                    // Position X sur la carte
    protected int y;                    // Position Y sur la carte
    protected int maxHealth;
    protected int currentHealth;
    protected int constructionTime;     // Temps de construction (en tours)
    protected int remainingTime;        // Temps restant avant fin de construction
    protected boolean isBuilt;          // Si le bâtiment est terminé
    protected Map<ResourceType, Integer> cost;
    protected Map<ResourceType, Integer> production; // Production par tour

    /**
     * Constructeur du bâtiment
     */
    public Building(String name, Player owner, int x, int y) {
        this.name = name;
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.isBuilt = false;
        this.cost = new HashMap<>();
        this.production = new HashMap<>();
        initializeStats();
        this.remainingTime = this.constructionTime;
    }

    /**
     * Méthode abstraite pour initialiser les statistiques du bâtiment
     * Chaque type de bâtiment définit ses propres stats
     */
    protected abstract void initializeStats();

    /**
     * Méthode abstraite pour l'action principale du bâtiment
     * Ex: produire des ressources, entraîner des unités, etc.
     */
    public abstract void performAction();

    /**
     * Avance la construction d'un tour
     * @return true si la construction est terminée
     */
    public boolean advanceConstruction() {
        if (isBuilt) {
            return true;
        }

        remainingTime--;
        if (remainingTime <= 0) {
            isBuilt = true;
            System.out.println("✅ " + name + " est maintenant construit !");
            return true;
        }

        System.out.println("🏗️ " + name + " en construction... (" + 
                         remainingTime + " tours restants)");
        return false;
    }

    /**
     * Produit les ressources du bâtiment (si applicable)
     */
    public void produceResources() {
        if (!isBuilt || production.isEmpty()) {
            return;
        }

        System.out.println("📦 " + name + " produit des ressources:");
        for (Map.Entry<ResourceType, Integer> entry : production.entrySet()) {
            owner.addResource(entry.getKey(), entry.getValue());
            System.out.println("  +" + entry.getValue() + " " + 
                             entry.getKey().getIcon() + " " + entry.getKey().getName());
        }
    }

    /**
     * Le bâtiment reçoit des dégâts
     */
    public void takeDamage(int damage) {
        this.currentHealth -= damage;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            destroy();
        }
    }

    /**
     * Répare le bâtiment
     */
    public void repair(int amount) {
        if (!isBuilt) {
            System.out.println("Impossible de réparer un bâtiment en construction !");
            return;
        }

        this.currentHealth = Math.min(this.currentHealth + amount, this.maxHealth);
        System.out.println(this.name + " réparé de " + amount + " PV !");
    }

    /**
     * Détruit le bâtiment
     */
    protected void destroy() {
        System.out.println("💥 " + name + " a été détruit !");
        if (owner != null) {
            owner.removeBuilding(this);
        }
        
        // Retirer le bâtiment de la carte
        // Note: la carte doit être mise à jour par le code appelant
    }

    /**
     * Vérifie si le bâtiment est détruit
     */
    public boolean isDestroyed() {
        return this.currentHealth <= 0;
    }

    /**
     * Affiche les informations du bâtiment
     */
    public void displayInfo() {
        System.out.println("\n=== " + name + " ===");
        System.out.println("Propriétaire: " + (owner != null ? owner.getName() : "Aucun"));
        System.out.println("Position: (" + x + ", " + y + ")");
        System.out.println("Santé: " + currentHealth + "/" + maxHealth);
        System.out.println("Statut: " + (isBuilt ? "✅ Opérationnel" : 
                         "🏗️ En construction (" + remainingTime + " tours)"));
        
        if (!production.isEmpty()) {
            System.out.println("\nProduction par tour:");
            for (Map.Entry<ResourceType, Integer> entry : production.entrySet()) {
                System.out.println("  " + entry.getKey().getIcon() + " " + 
                                 entry.getKey().getName() + ": +" + entry.getValue());
            }
        }
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getConstructionTime() {
        return constructionTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(cost);
    }

    public Map<ResourceType, Integer> getProduction() {
        return new HashMap<>(production);
    }

    @Override
    public String toString() {
        return name + " (" + currentHealth + "/" + maxHealth + " PV)" + 
               (isBuilt ? "" : " [En construction]");
    }
}