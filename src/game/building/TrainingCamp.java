package game.building;

import game.player.Player;
import game.resource.ResourceType;
import game.unit.*;

/**
 * Camp d'Entraînement - Bâtiment militaire
 * Permet de recruter des unités (Soldats, Archers, Cavaliers)
 */
public class TrainingCamp extends Building {

    public TrainingCamp(Player owner, int x, int y) {
        super("Camp d'Entraînement", owner, x, y);
    }

    @Override
    protected void initializeStats() {
        this.maxHealth = 300;
        this.currentHealth = 300;
        this.constructionTime = 3;
        
        // Coût de construction
        this.cost.put(ResourceType.GOLD, 80);
        this.cost.put(ResourceType.WOOD, 40);
        this.cost.put(ResourceType.STONE, 30);
    }

    @Override
    public void performAction() {
        // Le camp d'entraînement n'a pas de production automatique
        // Son action principale est de recruter des unités
    }

    /**
     * Recrute un soldat
     * @return Le soldat créé ou null si impossible
     */
    public Soldier recruitSoldier() {
        if (!isBuilt) {
            System.out.println("Le bâtiment n'est pas encore construit !");
            return null;
        }

        Soldier soldier = new Soldier(owner, x, y);
        
        if (owner.payResources(soldier.getCost())) {
            owner.addUnit(soldier);
            System.out.println("Soldat recruté à la position (" + x + ", " + y + ")");
            return soldier;
        } else {
            System.out.println("Ressources insuffisantes pour recruter un soldat !");
            return null;
        }
    }

    /**
     * Recrute un archer
     * @return L'archer créé ou null si impossible
     */
    public Archer recruitArcher() {
        if (!isBuilt) {
            System.out.println("Le bâtiment n'est pas encore construit !");
            return null;
        }

        Archer archer = new Archer(owner, x, y);
        
        if (owner.payResources(archer.getCost())) {
            owner.addUnit(archer);
            System.out.println("Archer recruté à la position (" + x + ", " + y + ")");
            return archer;
        } else {
            System.out.println("Ressources insuffisantes pour recruter un archer !");
            return null;
        }
    }

    /**
     * Recrute un cavalier
     * @return Le cavalier créé ou null si impossible
     */
    public Cavalry recruitCavalry() {
        if (!isBuilt) {
            System.out.println("Le bâtiment n'est pas encore construit !");
            return null;
        }

        Cavalry cavalry = new Cavalry(owner, x, y);
        
        if (owner.payResources(cavalry.getCost())) {
            owner.addUnit(cavalry);
            System.out.println("Cavalier recruté à la position (" + x + ", " + y + ")");
            return cavalry;
        } else {
            System.out.println("Ressources insuffisantes pour recruter un cavalier !");
            return null;
        }
    }

    /**
     * Affiche le menu de recrutement
     */
    public void displayRecruitmentMenu() {
        System.out.println("\n=== Menu de Recrutement ===");
        System.out.println("1. Soldat - 30 Or, 20 Nourriture");
        System.out.println("2. Archer - 40 Or, 15 Bois, 15 Nourriture");
        System.out.println("3. Cavalier - 50 Or, 30 Nourriture");
    }

    @Override
    public String toString() {
        return " " + super.toString();
    }
}