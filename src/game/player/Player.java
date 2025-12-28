package game.player;

import game.resource.ResourceType;
import game.unit.Unit;
import game.building.Building;

import java.util.*;

/**
 * Représente un joueur dans le jeu
 * Gère ses ressources, unités et bâtiments
 */
public class Player {
    private final String name;
    private final Map<ResourceType, Integer> resources;
    private final List<Unit> units;
    private final List<Building> buildings;
    private boolean isAlive;

    /**
     * Constructeur du joueur
     * @param name Nom du joueur
     */
    public Player(String name) {
        this.name = name;
        this.resources = new HashMap<>();
        this.units = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.isAlive = true;
        
        // Initialisation des ressources de départ
        initializeResources();
    }

    /**
     * Initialise les ressources de départ du joueur
     */
    private void initializeResources() {
        resources.put(ResourceType.GOLD, 100);
        resources.put(ResourceType.WOOD, 50);
        resources.put(ResourceType.STONE, 50);
        resources.put(ResourceType.FOOD, 100);
    }

    /**
     * Ajoute une quantité de ressource
     * @param type Type de ressource
     * @param amount Quantité à ajouter
     */
    public void addResource(ResourceType type, int amount) {
        int current = resources.getOrDefault(type, 0);
        resources.put(type, current + amount);
    }

    /**
     * Retire une quantité de ressource
     * @param type Type de ressource
     * @param amount Quantité à retirer
     * @return true si la ressource a été retirée, false si insuffisante
     */
    public boolean removeResource(ResourceType type, int amount) {
        int current = resources.getOrDefault(type, 0);
        if (current >= amount) {
            resources.put(type, current - amount);
            return true;
        }
        return false;
    }

    /**
     * Vérifie si le joueur a suffisamment de ressources
     * @param costs Map des coûts à vérifier
     * @return true si le joueur peut payer
     */
    public boolean hasResources(Map<ResourceType, Integer> costs) {
        for (Map.Entry<ResourceType, Integer> entry : costs.entrySet()) {
            int available = resources.getOrDefault(entry.getKey(), 0);
            if (available < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Paie un coût en ressources
     * @param costs Map des coûts à payer
     * @return true si le paiement a réussi
     */
    public boolean payResources(Map<ResourceType, Integer> costs) {
        if (!hasResources(costs)) {
            return false;
        }
        
        for (Map.Entry<ResourceType, Integer> entry : costs.entrySet()) {
            removeResource(entry.getKey(), entry.getValue());
        }
        return true;
    }

    /**
     * Ajoute une unité à la liste du joueur
     */
    public void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Retire une unité de la liste du joueur
     */
    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    /**
     * Ajoute un bâtiment à la liste du joueur
     */
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    /**
     * Retire un bâtiment de la liste du joueur
     */
    public void removeBuilding(Building building) {
        buildings.remove(building);
    }

    /**
     * Affiche l'état du joueur (ressources, unités, bâtiments)
     */
    public void displayStatus() {
        System.out.println("\n=== État de " + name + " ===");
        
        System.out.println("\n📦 Ressources:");
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            System.out.println("  " + entry.getKey().getIcon() + " " + 
                             entry.getKey().getName() + ": " + entry.getValue());
        }
        
        System.out.println("\n⚔️  Unités: " + units.size());
        System.out.println("🏗️  Bâtiments: " + buildings.size());
    }

    /**
     * Vérifie si le joueur a perdu (plus de bâtiments et d'unités)
     */
    public boolean hasLost() {
        return buildings.isEmpty() && units.isEmpty();
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getResource(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    public Map<ResourceType, Integer> getResources() {
        return new HashMap<>(resources); // Retourne une copie pour l'encapsulation
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(units); // Retourne une copie
    }

    public List<Building> getBuildings() {
        return new ArrayList<>(buildings); // Retourne une copie
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}