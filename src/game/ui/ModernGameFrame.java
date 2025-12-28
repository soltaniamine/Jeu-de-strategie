package game.ui;

import game.map.GameMap;
import game.map.Tile;
import game.player.Player;
import game.combat.CombatSystem;
import game.building.*;
import game.unit.*;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de jeu moderne avec interface élégante
 */
public class ModernGameFrame extends JFrame {
    private GameMap map;
    private Player player;
    private Player enemy;
    private CombatSystem combatSystem;
    private int currentTurn;
    
    private ModernGamePanel gamePanel;
    private ModernInfoPanel infoPanel;
    private ModernActionPanel actionPanel;
    private NotificationPanel notificationPanel;
    private JPanel topPanel;
    private JLabel turnLabel;
    private JLabel phaseLabel;
    
    public ModernGameFrame(String playerName, int mapSize) {
        this.map = new GameMap(mapSize, mapSize);
        this.player = new Player(playerName);
        this.enemy = new Player("IA Ennemi");
        this.combatSystem = new CombatSystem(map);
        this.currentTurn = 1;
        
        int playerX = 2;
        int playerY = 2;
        int enemyX = mapSize - 3;
        int enemyY = mapSize - 3;
        
        initializePlayerBase(player, playerX, playerY);
        initializePlayerBase(enemy, enemyX, enemyY);
        
        setTitle("Jeu de Stratégie - Interface Moderne");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 20, 30));
        
        createMenuBar();
        createComponents();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 40, 50));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 100, 120)));
        
        JMenu gameMenu = createModernMenu("Jeu");
        JMenuItem newGameItem = createModernMenuItem("Nouvelle Partie");
        JMenuItem exitItem = createModernMenuItem("Quitter");
        
        newGameItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Commencer une nouvelle partie ?",
                "Nouvelle Partie",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new ModernMainMenuFrame());
            }
        });
        
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        JMenu helpMenu = createModernMenu("Aide");
        JMenuItem rulesItem = createModernMenuItem("Regles");
        JMenuItem aboutItem = createModernMenuItem("A propos");
        
        rulesItem.addActionListener(e -> showRules());
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(rulesItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenu createModernMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return menu;
    }
    
    private JMenuItem createModernMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(new Color(40, 40, 50));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return item;
    }
    
    private void createComponents() {
        // Panneau supérieur moderne
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 50));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 100, 120)));
        topPanel.setPreferredSize(new Dimension(0, 70));
        
        // Informations du tour
        JPanel turnPanel = new JPanel();
        turnPanel.setLayout(new BoxLayout(turnPanel, BoxLayout.Y_AXIS));
        turnPanel.setBackground(new Color(40, 40, 50));
        turnPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        turnLabel = new JLabel("TOUR " + currentTurn);
        turnLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        turnLabel.setForeground(new Color(76, 175, 80));
        
        phaseLabel = new JLabel("Phase du joueur: " + player.getName());
        phaseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phaseLabel.setForeground(new Color(200, 200, 220));
        
        turnPanel.add(turnLabel);
        turnPanel.add(phaseLabel);
        
        topPanel.add(turnPanel, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // SIDEBAR GAUCHE : Actions
        actionPanel = new ModernActionPanel(this, player, combatSystem);
        JScrollPane leftScrollPane = new JScrollPane(actionPanel);
        leftScrollPane.setPreferredSize(new Dimension(300, 800));
        leftScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(100, 100, 120)));
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftScrollPane.getViewport().setBackground(new Color(30, 30, 40));
        
        add(leftScrollPane, BorderLayout.WEST);
        
        // Panneau de la carte avec notifications par-dessus
        JLayeredPane layeredPane = new JLayeredPane();
        
        gamePanel = new ModernGamePanel(map, this);
        JScrollPane mapScrollPane = new JScrollPane(gamePanel);
        mapScrollPane.setBackground(new Color(20, 20, 30));
        mapScrollPane.setBorder(null);
        
        int panelWidth = Math.min(map.getWidth() * 48 + 20, 900);
        int panelHeight = Math.min(map.getHeight() * 48 + 20, 800);
        
        mapScrollPane.setBounds(0, 0, panelWidth, panelHeight);
        layeredPane.add(mapScrollPane, JLayeredPane.DEFAULT_LAYER);
        
        // Panneau de notifications par-dessus
        notificationPanel = new NotificationPanel();
        notificationPanel.setBounds(0, 0, panelWidth, panelHeight);
        layeredPane.add(notificationPanel, JLayeredPane.PALETTE_LAYER);
        
        layeredPane.setPreferredSize(new Dimension(panelWidth, panelHeight));
        add(layeredPane, BorderLayout.CENTER);
        
        // SIDEBAR DROITE : Informations
        infoPanel = new ModernInfoPanel(player);
        JScrollPane rightScrollPane = new JScrollPane(infoPanel);
        rightScrollPane.setPreferredSize(new Dimension(300, 800));
        rightScrollPane.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(100, 100, 120)));
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        rightScrollPane.getViewport().setBackground(new Color(30, 30, 40));
        
        add(rightScrollPane, BorderLayout.EAST);
    }
    
    private void initializePlayerBase(Player player, int x, int y) {
        CommandCenter cc = new CommandCenter(player, x, y);
        for (int i = 0; i < 5; i++) cc.advanceConstruction();
        player.addBuilding(cc);
        map.getTile(x, y).setBuilding(cc);
        
        Soldier soldier = new Soldier(player, x + 1, y);
        Archer archer = new Archer(player, x, y + 1);
        
        player.addUnit(soldier);
        player.addUnit(archer);
        
        map.getTile(x + 1, y).setUnit(soldier);
        map.getTile(x, y + 1).setUnit(archer);
    }
    
    public void onTileSelected(Tile tile) {
        infoPanel.displayTileInfo(tile);
        actionPanel.updateButtons(tile);
    }
    
    public void endTurn() {
        notificationPanel.addInfo("Production des ressources...");
        
        produceResources();
        
        notificationPanel.addWarning("Tour de l'IA...");
        performEnemyTurn();
        
        if (checkVictory()) return;
        
        resetUnits();
        currentTurn++;
        turnLabel.setText("TOUR " + currentTurn);
        
        refresh();
        
        notificationPanel.addSuccess("Tour " + currentTurn + " - A vous de jouer !");
    }
    
    private void produceResources() {
        for (Building building : player.getBuildings()) {
            boolean wasBuilding = !building.isBuilt();
            building.advanceConstruction();
            
            // Notifier quand un bâtiment est terminé
            if (wasBuilding && building.isBuilt()) {
                notificationPanel.addSuccess(
                    building.getName() + " termine ! Production active."
                );
            }
            
            building.performAction();
        }
        
        for (Building building : enemy.getBuildings()) {
            building.advanceConstruction();
            building.performAction();
        }
    }
    
    private void performEnemyTurn() {
        for (Unit enemyUnit : enemy.getUnits()) {
            if (!player.getUnits().isEmpty() && !enemyUnit.hasActed()) {
                Unit target = player.getUnits().get(0);
                if (combatSystem.canAttack(enemyUnit, target)) {
                    combatSystem.performAttack(enemyUnit, target);
                    notificationPanel.addCombat(
                        enemyUnit.getName() + " ennemi attaque votre " + target.getName() + " !"
                    );
                    
                    // Vérifier si l'unité est morte
                    if (!target.isAlive()) {
                        notificationPanel.addDeath(
                            "Votre " + target.getName() + " a ete elimine !"
                        );
                    }
                }
            }
        }
    }
    
    private void resetUnits() {
        for (Unit unit : player.getUnits()) unit.resetTurn();
        for (Unit unit : enemy.getUnits()) unit.resetTurn();
    }
    
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
            JOptionPane.showMessageDialog(this,
                "🎉 VICTOIRE !\nVous avez détruit le Centre de Commandement ennemi !",
                "Victoire !",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new ModernMainMenuFrame());
            return true;
        }
        
        if (!playerHasCC) {
            JOptionPane.showMessageDialog(this,
                "💀 DÉFAITE !\nVotre Centre de Commandement a été détruit !",
                "Défaite",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new ModernMainMenuFrame());
            return true;
        }
        
        return false;
    }
    
    /**
     * Vérifie la victoire immédiatement (appelé après destruction d'un bâtiment)
     */
    public void checkVictoryNow() {
        checkVictory();
    }
    
    public void refresh() {
        infoPanel.updatePlayerInfo();
        gamePanel.repaint();
        if (gamePanel.getSelectedTile() != null) {
            infoPanel.displayTileInfo(gamePanel.getSelectedTile());
        }
    }
    
    private void showRules() {
        String rules = """
            ============================================
                       REGLES DU JEU              
            ============================================
            
            OBJECTIF:
            Detruire le Centre de Commandement ennemi
            
            BATIMENTS:
            * Centre: +10 Or/tour
            * Ferme: +15 Nourriture/tour
            * Mine: +10 Pierre +5 Or/tour
            * Scierie: +12 Bois/tour
            * Camp: Recrute des unites
            
            UNITES:
            * Soldat: Tank defensif
            * Archer: DPS a distance
            * Cavalier: Mobilite
            
            CONTROLES:
            * Cliquer sur une case = Selectionner
            * Boutons d'action a droite
            * Terminer le tour = Production
            """;
        
        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(new Color(40, 40, 50));
        textArea.setForeground(Color.WHITE);
        
        JOptionPane.showMessageDialog(this, textArea, "Regles du Jeu",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Jeu de Stratégie - Interface Moderne\n" +
            "Projet ISIL 2025/2026\n\n" +
            "Développé avec Java Swing",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Tile getSelectedTile() {
        return gamePanel.getSelectedTile();
    }
    
    public GameMap getMap() {
        return map;
    }
    
    /**
     * Affiche une notification
     */
    public void showNotification(String message, String type) {
        switch (type.toLowerCase()) {
            case "info":
                notificationPanel.addInfo(message);
                break;
            case "success":
                notificationPanel.addSuccess(message);
                break;
            case "warning":
                notificationPanel.addWarning(message);
                break;
            case "combat":
                notificationPanel.addCombat(message);
                break;
            case "death":
                notificationPanel.addDeath(message);
                break;
            default:
                notificationPanel.addInfo(message);
        }
    }
}