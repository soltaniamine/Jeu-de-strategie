package game.main;

import game.map.GameMap;
import game.map.Tile;
import game.player.Player;
import game.unit.*;
import game.building.*;
import game.combat.CombatSystem;
import game.resource.ResourceType;

import java.util.Scanner;

/**
 * Point d'entrée principal du jeu
 * Gère le menu, l'initialisation et la boucle de jeu
 */
public class GameLauncher {
    private GameMap map;
    private Player player;
    private Player enemy;
    private CombatSystem combatSystem;
    private Scanner scanner;
    private int currentTurn;
    private boolean gameRunning;

    public GameLauncher() {
        this.scanner = new Scanner(System.in);
        this.currentTurn = 1;
        this.gameRunning = true;
    }

    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        GameLauncher launcher = new GameLauncher();
        launcher.showMainMenu();
    }

    /**
     * Affiche le menu principal
     */
    private void showMainMenu() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║     JEU DE STRATÉGIE - JAVA ISIL      ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("\n1. Nouvelle Partie");
        System.out.println("2. Règles du Jeu");
        System.out.println("3. Quitter");
        System.out.print("\nVotre choix: ");

        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                startNewGame();
                break;
            case 2:
                displayRules();
                showMainMenu();
                break;
            case 3:
                System.out.println("\nMerci d'avoir joué ! Au revoir.");
                System.exit(0);
                break;
            default:
                System.out.println("\nChoix invalide !");
                showMainMenu();
        }
    }

    /**
     * Affiche les règles du jeu
     */
    private void displayRules() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║            RÈGLES DU JEU              ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("\n Objectif:");
        System.out.println("   Détruire le Centre de Commandement ennemi");
        System.out.println("\n  Bâtiments:");
        System.out.println("   - Centre de Commandement: +10 Or/tour");
        System.out.println("   - Ferme: +15 Nourriture/tour");
        System.out.println("   - Mine: +10 Pierre, +5 Or/tour");
        System.out.println("   - Scierie: +12 Bois/tour");
        System.out.println("   - Camp d'Entraînement: Recrute des unités");
        System.out.println("\n  Unités:");
        System.out.println("   - Soldat: Corps à corps, résistant");
        System.out.println("   - Archer: Distance, fragile");
        System.out.println("   - Cavalier: Rapide, mobile");
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    /**
     * Démarre une nouvelle partie
     */
    private void startNewGame() {
        System.out.print("\nEntrez votre nom: ");
        String playerName = scanner.nextLine();

        // Initialisation
        this.map = new GameMap(15, 15); // Carte 15×15 au lieu de 25×25
        this.player = new Player(playerName);
        this.enemy = new Player("IA Ennemi");
        this.combatSystem = new CombatSystem(map);

        // Placement initial des joueurs
        initializePlayerBase(player, 2, 2);
        initializePlayerBase(enemy, 22, 22);

        System.out.println("\n Partie initialisée !");
        System.out.println("Vous êtes en position (2, 2)");
        System.out.println("L'ennemi est en position (12, 12)");
        
        // Lancement de la boucle de jeu
        gameLoop();
    }

    /**
     * Initialise la base d'un joueur avec un centre de commandement et quelques unités
     */
    private void initializePlayerBase(Player player, int x, int y) {
        // Création du centre de commandement (déjà construit)
        CommandCenter cc = new CommandCenter(player, x, y);
        cc.advanceConstruction(); // Termine instantanément la construction pour le début
        cc.advanceConstruction();
        cc.advanceConstruction();
        cc.advanceConstruction();
        cc.advanceConstruction();
        
        player.addBuilding(cc);
        map.getTile(x, y).setBuilding(cc);

        // Ajout d'unités de départ
        Soldier soldier = new Soldier(player, x + 1, y);
        Archer archer = new Archer(player, x, y + 1);
        
        player.addUnit(soldier);
        player.addUnit(archer);
        
        map.getTile(x + 1, y).setUnit(soldier);
        map.getTile(x, y + 1).setUnit(archer);
    }

    /**
     * Boucle principale du jeu
     */
    private void gameLoop() {
        while (gameRunning) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🎮 TOUR " + currentTurn);
            System.out.println("=".repeat(50));

            // Phase du joueur
            playerTurn();

            // Vérification de victoire
            if (checkVictory()) {
                break;
            }

            // Phase de l'ennemi (IA simple)
            enemyTurn();

            // Vérification de défaite
            if (checkVictory()) {
                break;
            }

            // Production de ressources
            produceResources();

            // Nouveau tour
            currentTurn++;
            resetUnits();
        }

        // Fin de partie
        showMainMenu();
    }

    /**
     * Tour du joueur
     */
    private void playerTurn() {
        boolean turnEnded = false;

        while (!turnEnded) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Afficher l'état du joueur");
            System.out.println("2. Voir la carte (zone locale)");
            System.out.println("3. Construire un bâtiment");
            System.out.println("4. Gérer les unités");
            System.out.println("5. Terminer le tour");
            System.out.print("\nVotre choix: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    player.displayStatus();
                    break;
                case 2:
                    map.displayArea(2, 2, 5);
                    break;
                case 3:
                    buildingMenu();
                    break;
                case 4:
                    unitMenu();
                    break;
                case 5:
                    turnEnded = true;
                    System.out.println("\n Tour terminé !");
                    break;
                default:
                    System.out.println("\nChoix invalide !");
            }
        }
    }

    /**
     * Menu de construction
     */
    private void buildingMenu() {
        System.out.println("\n--- Menu Construction ---");
        System.out.println("1. Ferme (30 Or, 20 Bois)");
        System.out.println("2. Mine (50 Or, 30 Bois)");
        System.out.println("3. Scierie (40 Or, 15 Pierre)");
        System.out.println("4. Camp d'Entraînement (80 Or, 40 Bois, 30 Pierre)");
        System.out.println("5. Retour");
        System.out.print("\nVotre choix: ");

        int choice = getIntInput();
        
        System.out.print("Position X: ");
        int x = getIntInput();
        System.out.print("Position Y: ");
        int y = getIntInput();

        Building building = null;

        switch (choice) {
            case 1:
                building = new Farm(player, x, y);
                break;
            case 2:
                building = new Mine(player, x, y);
                break;
            case 3:
                building = new Sawmill(player, x, y);
                break;
            case 4:
                building = new TrainingCamp(player, x, y);
                break;
            case 5:
                return;
        }

        if (building != null && player.payResources(building.getCost())) {
            player.addBuilding(building);
            map.getTile(x, y).setBuilding(building);
            System.out.println(" Construction démarrée ");
        } else {
            System.out.println(" Ressources insuffisantes ou position invalide ");
        }
    }

    /**
     * Menu de gestion des unités
     */
    private void unitMenu() {
        if (player.getUnits().isEmpty()) {
            System.out.println("\nVous n'avez aucune unité !");
            return;
        }

        System.out.println("\n--- Vos Unités ---");
        int i = 1;
        for (Unit unit : player.getUnits()) {
            System.out.println(i + ". " + unit + " à (" + unit.getX() + ", " + unit.getY() + ")");
            i++;
        }

        System.out.print("\nChoisir une unité (0 pour annuler): ");
        int choice = getIntInput();

        if (choice <= 0 || choice > player.getUnits().size()) {
            return;
        }

        Unit selectedUnit = player.getUnits().get(choice - 1);
        manageUnit(selectedUnit);
    }

    /**
     * Gère une unité spécifique
     */
    private void manageUnit(Unit unit) {
        System.out.println("\n--- Gérer " + unit.getName() + " ---");
        System.out.println("1. Déplacer");
        System.out.println("2. Attaquer");
        System.out.println("3. Info unité");
        System.out.println("4. Retour");
        System.out.print("\nVotre choix: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.print("Nouvelle position X: ");
                int x = getIntInput();
                System.out.print("Nouvelle position Y: ");
                int y = getIntInput();
                combatSystem.moveUnit(unit, x, y);
                break;
            case 2:
                attackWithUnit(unit);
                break;
            case 3:
                unit.displayInfo();
                break;
        }
    }

    /**
     * Attaque avec une unité
     */
    private void attackWithUnit(Unit attacker) {
        System.out.println("\n--- Unités ennemies ---");
        int i = 1;
        for (Unit enemy : enemy.getUnits()) {
            System.out.println(i + ". " + enemy + " à (" + enemy.getX() + ", " + enemy.getY() + ")");
            i++;
        }

        System.out.print("\nChoisir une cible (0 pour annuler): ");
        int choice = getIntInput();

        if (choice <= 0 || choice > enemy.getUnits().size()) {
            return;
        }

        Unit target = enemy.getUnits().get(choice - 1);
        combatSystem.performAttack(attacker, target);
    }

    /**
     * Tour de l'ennemi (IA basique)
     */
    private void enemyTurn() {
        System.out.println("\n Tour de l'IA...");
        
        // IA simple: attaque si possible
        for (Unit enemyUnit : enemy.getUnits()) {
            if (!player.getUnits().isEmpty()) {
                Unit target = player.getUnits().get(0);
                if (combatSystem.canAttack(enemyUnit, target)) {
                    combatSystem.performAttack(enemyUnit, target);
                }
            }
        }
    }

    /**
     * Produit les ressources de tous les bâtiments
     */
    private void produceResources() {
        System.out.println("\n💰 Production de ressources...");
        
        for (Building building : player.getBuildings()) {
            building.advanceConstruction();
            building.performAction();
        }

        for (Building building : enemy.getBuildings()) {
            building.advanceConstruction();
            building.performAction();
        }
    }

    /**
     * Réinitialise toutes les unités pour le nouveau tour
     */
    private void resetUnits() {
        for (Unit unit : player.getUnits()) {
            unit.resetTurn();
        }
        for (Unit unit : enemy.getUnits()) {
            unit.resetTurn();
        }
    }

    /**
     * Vérifie les conditions de victoire
     */
    private boolean checkVictory() {
        boolean playerHasCC = false;
        boolean enemyHasCC = false;

        for (Building b : player.getBuildings()) {
            if (b instanceof CommandCenter && !b.isDestroyed()) {
                playerHasCC = true;
            }
        }

        for (Building b : enemy.getBuildings()) {
            if (b instanceof CommandCenter && !b.isDestroyed()) {
                enemyHasCC = true;
            }
        }

        if (!enemyHasCC) {
            System.out.println("\n VICTOIRE ! Vous avez détruit le Centre de Commandement ennemi !");
            gameRunning = false;
            return true;
        }

        if (!playerHasCC) {
            System.out.println("\n DÉFAITE ! Votre Centre de Commandement a été détruit !");
            gameRunning = false;
            return true;
        }

        return false;
    }

    /**
     * Lit un entier depuis l'entrée utilisateur
     */
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}