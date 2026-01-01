package game.ui;

import game.player.Player;
import game.resource.ResourceType;
import game.map.Tile;
import game.unit.Unit;
import game.building.Building;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Panneau d'informations moderne avec design élégant
 */
public class ModernInfoPanel extends JPanel {
    private Player player;
    private JPanel resourcePanel;
    private JTextArea selectionInfo;

    // Labels de ressources avec barres
    private ResourceDisplay goldDisplay;
    private ResourceDisplay woodDisplay;
    private ResourceDisplay stoneDisplay;
    private ResourceDisplay foodDisplay;

    public ModernInfoPanel(Player player) {
        this.player = player;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 800));
        setMinimumSize(new Dimension(280, 600));
        setBackground(new Color(30, 30, 40));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        createPlayerInfoPanel();
        createSelectionPanel();

        updatePlayerInfo();
    }

    private void createPlayerInfoPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBackground(new Color(40, 40, 50));
        playerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 100, 120), 2, true),
                new EmptyBorder(15, 15, 15, 15)));
        playerPanel.setMaximumSize(new Dimension(290, 350));

        // Titre du joueur
        JLabel titleLabel = new JLabel("Joueur: " + player.getName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerPanel.add(titleLabel);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Ligne de séparation
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(100, 100, 120));
        sep.setMaximumSize(new Dimension(260, 2));
        playerPanel.add(sep);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Ressources avec affichage moderne
        JLabel resLabel = new JLabel("RESSOURCES");
        resLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resLabel.setForeground(new Color(200, 200, 220));
        resLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerPanel.add(resLabel);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        goldDisplay = new ResourceDisplay("Or", new Color(255, 215, 0));
        woodDisplay = new ResourceDisplay("Bois", new Color(139, 69, 19));
        stoneDisplay = new ResourceDisplay("Pierre", new Color(128, 128, 128));
        foodDisplay = new ResourceDisplay("Nourriture", new Color(255, 140, 0));

        playerPanel.add(goldDisplay);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        playerPanel.add(woodDisplay);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        playerPanel.add(stoneDisplay);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        playerPanel.add(foodDisplay);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stats
        JLabel statsLabel = new JLabel("STATISTIQUES");
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsLabel.setForeground(new Color(200, 200, 220));
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerPanel.add(statsLabel);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel statsGrid = new JPanel(new GridLayout(1, 2, 10, 8));
        statsGrid.setBackground(new Color(40, 40, 50));
        statsGrid.setMaximumSize(new Dimension(260, 60));

        statsGrid.add(createStatBox("", "Unites", "0", new Color(33, 150, 243)));
        statsGrid.add(createStatBox("", "Batiments", "0", new Color(76, 175, 80)));

        playerPanel.add(statsGrid);

        add(playerPanel);
        add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JPanel createStatBox(String icon, String label, String value, Color color) {
        JPanel box = new JPanel(new BorderLayout(5, 5));
        box.setBackground(new Color(50, 50, 60));
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color, 2, true),
                new EmptyBorder(3, 1, 3, 1)));
        box.setPreferredSize(new Dimension(110, 100));
        box.setMinimumSize(new Dimension(110,100));

        // Panneau central avec texte
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(50, 50, 60));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(new Color(180, 180, 200));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setName("value");

        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(valueLabel);

        box.add(centerPanel, BorderLayout.CENTER);

        return box;
    }

    private void createSelectionPanel() {
        JPanel selectionPanel = new JPanel(new BorderLayout());
        selectionPanel.setBackground(new Color(40, 40, 50));
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 100, 120), 2, true),
                new EmptyBorder(15, 15, 15, 15)));
        selectionPanel.setMaximumSize(new Dimension(270, 400));
        selectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("SELECTION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(200, 200, 220));
        selectionPanel.add(titleLabel, BorderLayout.NORTH);

        selectionInfo = new JTextArea();
        selectionInfo.setEditable(false);
        selectionInfo.setLineWrap(true);
        selectionInfo.setWrapStyleWord(true);
        selectionInfo.setFont(new Font("Consolas", Font.PLAIN, 11));
        selectionInfo.setBackground(new Color(30, 30, 40));
        selectionInfo.setForeground(new Color(220, 220, 240));
        selectionInfo.setCaretColor(Color.WHITE);
        selectionInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        selectionInfo.setText("Aucune selection");

        JScrollPane scrollPane = new JScrollPane(selectionInfo);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(230, 320));
        scrollPane.getViewport().setBackground(new Color(30, 30, 40));

        selectionPanel.add(scrollPane, BorderLayout.CENTER);

        add(selectionPanel);
        add(Box.createVerticalGlue());
    }

    public void updatePlayerInfo() {
        goldDisplay.setValue(player.getResource(ResourceType.GOLD));
        woodDisplay.setValue(player.getResource(ResourceType.WOOD));
        stoneDisplay.setValue(player.getResource(ResourceType.STONE));
        foodDisplay.setValue(player.getResource(ResourceType.FOOD));

        // Mettre à jour les stats
        updateStatsValues(player.getUnits().size(), player.getBuildings().size());
    }

    private void updateStatsValues(int unitsCount, int buildingsCount) {
        // Parcourir les composants pour trouver et mettre à jour les stats
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                updateStatsInPanel((JPanel) comp, unitsCount, buildingsCount);
            }
        }
    }

    private void updateStatsInPanel(JPanel panel, int unitsCount, int buildingsCount) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel innerPanel = (JPanel) comp;
                if (innerPanel.getLayout() instanceof GridLayout) {
                    // C'est le panneau de stats
                    Component[] gridComps = innerPanel.getComponents();
                    if (gridComps.length >= 2) {
                        updateStatBoxValue(gridComps[0], unitsCount);
                        updateStatBoxValue(gridComps[1], buildingsCount);
                    }
                }
                // Récursion pour les sous-panneaux
                updateStatsInPanel(innerPanel, unitsCount, buildingsCount);
            }
        }
    }

    private void updateStatBoxValue(Component comp, int value) {
        if (comp instanceof JPanel) {
            for (Component c : ((JPanel) comp).getComponents()) {
                if (c instanceof JPanel) {
                    for (Component cc : ((JPanel) c).getComponents()) {
                        if (cc instanceof JLabel && "value".equals(cc.getName())) {
                            ((JLabel) cc).setText(String.valueOf(value));
                        }
                    }
                }
            }
        }
    }

    public void displayTileInfo(Tile tile) {
        if (tile == null) {
            selectionInfo.setText("Aucune sélection");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== CASE (").append(tile.getX()).append(", ").append(tile.getY()).append(") =====\n\n");

        sb.append("TERRAIN\n");
        sb.append("  ").append(tile.getType().getName()).append("\n");
        sb.append("  Defense: +").append(tile.getType().getDefenseBonus()).append("\n");
        sb.append("  Cout mvt: x").append(tile.getType().getMovementCost()).append("\n\n");

        if (tile.hasBuilding()) {
            Building b = tile.getBuilding();
            sb.append("BATIMENT\n");
            sb.append("  ").append(b.getName()).append("\n");
            sb.append("  Joueur: ").append(b.getOwner().getName()).append("\n");
            sb.append("  Sante: ").append(b.getCurrentHealth()).append("/").append(b.getMaxHealth()).append("\n");

            float healthPercent = (float) b.getCurrentHealth() / b.getMaxHealth();
            sb.append("  [");
            for (int i = 0; i < 10; i++) {
                sb.append(i < healthPercent * 10 ? "#" : "-");
            }
            sb.append("]\n");

            sb.append("  ").append(b.isBuilt() ? "OK Operationnel" : "En Construction").append("\n\n");
        }

        if (tile.hasUnit()) {
            Unit u = tile.getUnit();
            sb.append("UNITE\n");
            sb.append("  ").append(u.getName()).append("\n");
            sb.append("  Joueur: ").append(u.getOwner().getName()).append("\n");
            sb.append("  Sante: ").append(u.getCurrentHealth()).append("/").append(u.getMaxHealth()).append(" PV\n");

            float healthPercent = (float) u.getCurrentHealth() / u.getMaxHealth();
            sb.append("  [");
            for (int i = 0; i < 10; i++) {
                sb.append(i < healthPercent * 10 ? "#" : "-");
            }
            sb.append("]\n");

            sb.append("  ATK: ").append(u.getAttack()).append(" | DEF: ").append(u.getDefense()).append("\n");
            sb.append("  Portee: ").append(u.getRange()).append(" | Mvt: ").append(u.getMovementPoints()).append("\n");
            sb.append("  ").append(u.hasActed() ? "X A agi" : "OK Pret").append("\n");
        }

        if (!tile.hasBuilding() && !tile.hasUnit()) {
            sb.append("Case vide\n");
        }

        selectionInfo.setText(sb.toString());
        selectionInfo.setCaretPosition(0);
    }

    // Classe interne pour l'affichage des ressources
    private class ResourceDisplay extends JPanel {
        private JLabel valueLabel;
        private JProgressBar bar;
        private int maxValue = 500;

        ResourceDisplay(String name, Color color) {
            setLayout(new BorderLayout(5, 5));
            setBackground(new Color(40, 40, 50));
            setMaximumSize(new Dimension(240, 48));

            // Panneau supérieur : Nom + Valeur
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(new Color(40, 40, 50));

            // Nom de la ressource
            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            nameLabel.setForeground(new Color(200, 200, 220));

            // Valeur
            valueLabel = new JLabel("0");
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            valueLabel.setForeground(Color.WHITE);

            topPanel.add(nameLabel, BorderLayout.WEST);
            topPanel.add(valueLabel, BorderLayout.EAST);

            // Barre de progression plus épaisse
            bar = new JProgressBar(0, maxValue);
            bar.setValue(0);
            bar.setPreferredSize(new Dimension(240, 10));
            bar.setBackground(new Color(50, 50, 60));
            bar.setForeground(color);
            bar.setBorderPainted(false);

            add(topPanel, BorderLayout.NORTH);
            add(Box.createRigidArea(new Dimension(0, 3)), BorderLayout.CENTER);
            add(bar, BorderLayout.SOUTH);
        }

        void setValue(int value) {
            valueLabel.setText(String.valueOf(value));
            bar.setValue(Math.min(value, maxValue));
        }
    }
}