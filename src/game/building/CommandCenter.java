package game.building;

import game.player.Player;
import game.resource.ResourceType;

/**
 * Centre de Commandement - Bâtiment principal
 * Produit de l'or et permet de construire d'autres bâtiments
 * Si détruit, le joueur perd la partie
 */
public class CommandCenter extends Building {

    public CommandCenter(Player owner, int x, int y) {
        super("Centre de Commandement", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 500;
        this.currentHealth = 500;
        this.constructionTime = 5;
        
        // Coût de construction
        this.cost.put(ResourceType.GOLD, 200);
        this.cost.put(ResourceType.WOOD, 100);
        this.cost.put(ResourceType.STONE, 100);
        
        // Production par tour
        this.production.put(ResourceType.GOLD, 10);
    }

    @Override
    public void performAction() {
        if (!isBuilt) {
            return;
        }
        
        // Le centre de commandement produit automatiquement des ressources
        produceResources();
    }

    /**
     * Capacité spéciale : Recrutement d'urgence
     * Augmente temporairement la production d'or
     */
    public void emergencyFunds() {
        if (!isBuilt) {
            System.out.println("Le bâtiment n'est pas encore construit !");
            return;
        }

        int bonus = 20;
        owner.addResource(ResourceType.GOLD, bonus);
        System.out.println(" Fonds d'urgence activés ! +" + bonus + " Or");
    }

    @Override
    protected void destroy() {
        super.destroy();
        System.out.println(" Le Centre de Commandement a été détruit !");
        System.out.println(" " + owner.getName() + " est en danger critique !");
    }

    @Override
    public String toString() {
        return " " + super.toString();
    }
}