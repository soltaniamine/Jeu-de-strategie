package game.combat;

import game.unit.Unit;
import game.map.GameMap;
import game.map.Tile;

import java.util.Random;

/**
 * Système de combat du jeu
 * Gère les attaques, calculs de dégâts et résolution des combats
 */
public class CombatSystem {
    private final Random random;
    private final GameMap map;

    public CombatSystem(GameMap map) {
        this.map = map;
        this.random = new Random();
    }

    /**
     * Effectue une attaque entre deux unités
     * @param attacker Unité attaquante
     * @param defender Unité défenseur
     * @return true si l'attaque a réussi
     */
    public boolean performAttack(Unit attacker, Unit defender) {
        // Vérifications préalables
        if (!canAttack(attacker, defender)) {
            return false;
        }

        System.out.println("\n⚔️ === COMBAT ===");
        System.out.println(attacker.getName() + " (" + attacker.getCurrentHealth() + " PV) " +
                         "attaque " + defender.getName() + " (" + defender.getCurrentHealth() + " PV)");

        // Calcul des dégâts de base
        int baseDamage = calculateDamage(attacker, defender);
        
        // Application du bonus de terrain
        int terrainBonus = getTerrainDefenseBonus(defender);
        int finalDamage = Math.max(1, baseDamage - terrainBonus);

        // Chance de coup critique (10%)
        if (random.nextInt(100) < 10) {
            finalDamage = (int)(finalDamage * 1.5);
            System.out.println("💥 COUP CRITIQUE !");
        }

        // Application des dégâts
        defender.takeDamage(finalDamage);
        attacker.setHasActed(true);

        System.out.println("➡️  Dégâts infligés: " + finalDamage);
        System.out.println("❤️  " + defender.getName() + " PV restants: " + 
                         defender.getCurrentHealth() + "/" + defender.getMaxHealth());

        // Contre-attaque si le défenseur survit et est à portée
        if (defender.isAlive() && canCounterAttack(defender, attacker)) {
            performCounterAttack(defender, attacker);
        }

        System.out.println("=================\n");
        return true;
    }

    /**
     * Calcule les dégâts de base d'une attaque
     * Formule: Attaque - Défense + Aléatoire (-20% à +20%)
     */
    private int calculateDamage(Unit attacker, Unit defender) {
        int baseDamage = attacker.getAttack() - defender.getDefense();
        
        // Ajout d'un facteur aléatoire (-20% à +20%)
        double randomFactor = 0.8 + (random.nextDouble() * 0.4);
        int damage = (int)(baseDamage * randomFactor);
        
        // Dégâts minimum de 1
        return Math.max(1, damage);
    }

    /**
     * Vérifie si une unité peut attaquer une autre
     */
    public boolean canAttack(Unit attacker, Unit defender) {
        if (attacker == null || defender == null) {
            System.out.println("❌ Unité invalide !");
            return false;
        }

        if (!attacker.isAlive()) {
            System.out.println("❌ L'attaquant est mort !");
            return false;
        }

        if (!defender.isAlive()) {
            System.out.println("❌ Le défenseur est mort !");
            return false;
        }

        if (attacker.hasActed()) {
            System.out.println("❌ " + attacker.getName() + " a déjà agi ce tour !");
            return false;
        }

        if (attacker.getOwner() == defender.getOwner()) {
            System.out.println("❌ Vous ne pouvez pas attaquer vos propres unités !");
            return false;
        }

        // Vérification de la portée
        int distance = attacker.distanceTo(defender.getX(), defender.getY());
        if (distance > attacker.getRange()) {
            System.out.println("❌ Cible hors de portée ! (Distance: " + distance + 
                             ", Portée: " + attacker.getRange() + ")");
            return false;
        }

        return true;
    }

    /**
     * Vérifie si une unité peut contre-attaquer
     */
    private boolean canCounterAttack(Unit defender, Unit attacker) {
        int distance = defender.distanceTo(attacker.getX(), attacker.getY());
        return distance <= defender.getRange() && !defender.hasActed();
    }

    /**
     * Effectue une contre-attaque
     */
    private void performCounterAttack(Unit defender, Unit attacker) {
        System.out.println("\n🔄 CONTRE-ATTAQUE !");
        
        int counterDamage = calculateDamage(defender, attacker);
        counterDamage = counterDamage / 2; // La contre-attaque fait 50% des dégâts
        
        attacker.takeDamage(counterDamage);
        
        System.out.println("➡️  " + defender.getName() + " contre-attaque pour " + 
                         counterDamage + " dégâts !");
        System.out.println("❤️  " + attacker.getName() + " PV restants: " + 
                         attacker.getCurrentHealth() + "/" + attacker.getMaxHealth());
    }

    /**
     * Récupère le bonus de défense du terrain
     */
    private int getTerrainDefenseBonus(Unit unit) {
        Tile tile = map.getTile(unit.getX(), unit.getY());
        if (tile != null) {
            int bonus = tile.getType().getDefenseBonus();
            if (bonus > 0) {
                System.out.println("🛡️  Bonus de terrain: +" + bonus + " défense");
            }
            return bonus;
        }
        return 0;
    }

    /**
     * Calcule le résultat d'un combat complet jusqu'à la mort
     * Utile pour les simulations d'IA
     * @return L'unité gagnante
     */
    public Unit simulateCombat(Unit unit1, Unit unit2) {
        // Créer des copies pour ne pas modifier les originaux
        int health1 = unit1.getCurrentHealth();
        int health2 = unit2.getCurrentHealth();
        
        while (health1 > 0 && health2 > 0) {
            // Unit1 attaque
            int damage1 = calculateDamage(unit1, unit2);
            health2 -= damage1;
            
            if (health2 <= 0) {
                return unit1;
            }
            
            // Unit2 contre-attaque
            int damage2 = calculateDamage(unit2, unit1);
            health1 -= damage2;
        }
        
        return health1 > 0 ? unit1 : unit2;
    }

    /**
     * Affiche les chances de victoire d'une unité contre une autre
     */
    public void displayCombatOdds(Unit attacker, Unit defender) {
        System.out.println("\n📊 Analyse de Combat:");
        System.out.println("Attaquant: " + attacker);
        System.out.println("Défenseur: " + defender);
        
        int attackerWins = 0;
        int simulations = 1000;
        
        // Simulation de combat
        for (int i = 0; i < simulations; i++) {
            Unit winner = simulateCombat(attacker, defender);
            if (winner == attacker) {
                attackerWins++;
            }
        }
        
        double winRate = (attackerWins * 100.0) / simulations;
        System.out.println("Chances de victoire de l'attaquant: " + 
                         String.format("%.1f", winRate) + "%");
    }

    /**
     * Déplace une unité et met à jour la carte
     */
    public boolean moveUnit(Unit unit, int targetX, int targetY) {
        if (!map.isValidPosition(targetX, targetY)) {
            System.out.println("❌ Position invalide !");
            return false;
        }

        Tile currentTile = map.getTile(unit.getX(), unit.getY());
        Tile targetTile = map.getTile(targetX, targetY);

        if (targetTile == null || !targetTile.isAccessible()) {
            System.out.println("❌ Case inaccessible !");
            return false;
        }

        int distance = map.getDistance(unit.getX(), unit.getY(), targetX, targetY);
        if (distance > unit.getMovementPoints()) {
            System.out.println("❌ Trop loin ! Distance: " + distance + 
                             ", Déplacement: " + unit.getMovementPoints());
            return false;
        }

        if (unit.hasActed()) {
            System.out.println("❌ Cette unité a déjà agi ce tour !");
            return false;
        }

        // Déplacement
        if (currentTile != null) {
            currentTile.setUnit(null);
        }
        
        targetTile.setUnit(unit);
        unit.moveTo(targetX, targetY);

        return true;
    }
}