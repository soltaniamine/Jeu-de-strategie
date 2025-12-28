package game.resource;

/**
 * Énumération des différents types de ressources du jeu
 * Utilisé pour la gestion économique et la construction
 */
public enum ResourceType {
    GOLD("Or", "💰"),
    WOOD("Bois", "🪵"),
    STONE("Pierre", "🪨"),
    FOOD("Nourriture", "🌾");

    private final String name;
    private final String icon;

    /**
     * Constructeur de ResourceType
     * @param name Nom de la ressource
     * @param icon Icône pour l'affichage
     */
    ResourceType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }
}