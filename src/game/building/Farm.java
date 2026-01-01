package game.building;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Ferme - Bâtiment de production
 * Produit de la nourriture nécessaire pour recruter et entretenir des unités
 */
public class Farm extends Building {

    public Farm(Player owner, int x, int y) {
        super("Ferme", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 200;
        this.currentHealth = 200;
        this.constructionTime = 2;
        
        // Coût de construction
        this.cost.put(ResourceType.GOLD, 30);
        this.cost.put(ResourceType.WOOD, 20);
        
        // Production par tour
        this.production.put(ResourceType.FOOD, 15);
    }

    @Override
    public void performAction() {
        if (!isBuilt) {
            return;
        }
        
        // La ferme produit automatiquement de la nourriture
        produceResources();
    }

    /**
     * Capacité spéciale : Récolte abondante
     * Double la production de nourriture pour ce tour
     */
    public void bountifulHarvest() {
        if (!isBuilt) {
            System.out.println("Le bâtiment n'est pas encore construit !");
            return;
        }

        int bonus = production.get(ResourceType.FOOD);
        owner.addResource(ResourceType.FOOD, bonus);
        System.out.println(" Récolte abondante ! +" + bonus + " Nourriture supplémentaire");
    }

    @Override
    public String toString() {
        return " " + super.toString();
    }
}