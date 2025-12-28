package game.ui;

import game.player.Player;
import game.map.Tile;
import game.unit.*;
import game.building.*;
import game.combat.CombatSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panneau d'actions moderne avec boutons stylisés
 */
public class ModernActionPanel extends JPanel {
    private ModernGameFrame gameFrame;
    private Player player;
    private CombatSystem combatSystem;
    
    private ModernButton buildButton;
    private ModernButton recruitButton;
    private ModernButton moveButton;
    private ModernButton attackButton;
    private ModernButton endTurnButton;
    
    public ModernActionPanel(ModernGameFrame gameFrame, Player player, CombatSystem combatSystem) {
        this.gameFrame = gameFrame;
        this.player = player;
        this.combatSystem = combatSystem;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 800));
        setMinimumSize(new Dimension(280, 600));
        setBackground(new Color(30, 30, 40));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        createActionButtons();
    }
    
    private void createActionButtons() {
        // Titre principal
        JLabel mainTitle = new JLabel("PANNEAU DE CONTROLE");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainTitle.setForeground(new Color(76, 175, 80));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mainTitle);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Section Actions
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(new Color(40, 40, 50));
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 120), 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        actionsPanel.setMaximumSize(new Dimension(270, 450));
        actionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Titre
        JLabel titleLabel = new JLabel("ACTIONS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(200, 200, 220));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsPanel.add(titleLabel);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Section Construction
        JLabel constructLabel = new JLabel("Construction");
        constructLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        constructLabel.setForeground(new Color(150, 150, 170));
        constructLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsPanel.add(constructLabel);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        buildButton = new ModernButton("Construire Batiment", new Color(33, 150, 243));
        buildButton.addActionListener(e -> openBuildMenu());
        actionsPanel.add(buildButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        recruitButton = new ModernButton("Recruter Unite", new Color(156, 39, 176));
        recruitButton.addActionListener(e -> openRecruitMenu());
        actionsPanel.add(recruitButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Séparateur
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(100, 100, 120));
        sep1.setMaximumSize(new Dimension(240, 2));
        actionsPanel.add(sep1);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Section Unités
        JLabel unitLabel = new JLabel("Gestion des Unites");
        unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        unitLabel.setForeground(new Color(150, 150, 170));
        unitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsPanel.add(unitLabel);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        moveButton = new ModernButton("Deplacer", new Color(255, 152, 0));
        moveButton.addActionListener(e -> openMoveDialog());
        moveButton.setEnabled(false);
        actionsPanel.add(moveButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        attackButton = new ModernButton("Attaquer", new Color(244, 67, 54));
        attackButton.addActionListener(e -> openAttackDialog());
        attackButton.setEnabled(false);
        actionsPanel.add(attackButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Séparateur
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(100, 100, 120));
        sep2.setMaximumSize(new Dimension(240, 2));
        actionsPanel.add(sep2);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Bouton terminer tour
        endTurnButton = new ModernButton(">>> TERMINER LE TOUR <<<", new Color(76, 175, 80));
        endTurnButton.addActionListener(e -> gameFrame.endTurn());
        endTurnButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        actionsPanel.add(endTurnButton);
        
        add(actionsPanel);
        add(Box.createVerticalGlue());
    }
    
    private void openBuildMenu() {
        Tile selected = gameFrame.getSelectedTile();
        if (selected == null) {
            showError("Sélectionnez d'abord une case !");
            return;
        }
        
        if (selected.hasBuilding() || selected.hasUnit()) {
            showError("Cette case est déjà occupée !");
            return;
        }
        
        ModernDialog dialog = new ModernDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                                "Construction");
        
        String[] options = {
            "Ferme - 30 Or, 20 Bois",
            "Mine - 50 Or, 30 Bois",
            "Scierie - 40 Or, 15 Pierre",
            "Camp - 80 Or, 40 Bois, 30 Pierre"
        };
        
        JList<String> list = new JList<>(options);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBackground(new Color(40, 40, 50));
        list.setForeground(Color.WHITE);
        list.setSelectedIndex(0);
        
        dialog.add(new JScrollPane(list));
        dialog.showDialog();
        
        if (dialog.isConfirmed()) {
            int choice = list.getSelectedIndex();
            Building building = null;
            
            switch (choice) {
                case 0: building = new Farm(player, selected.getX(), selected.getY()); break;
                case 1: building = new Mine(player, selected.getX(), selected.getY()); break;
                case 2: building = new Sawmill(player, selected.getX(), selected.getY()); break;
                case 3: building = new TrainingCamp(player, selected.getX(), selected.getY()); break;
            }
            
            if (building != null) {
                if (player.payResources(building.getCost())) {
                    player.addBuilding(building);
                    selected.setBuilding(building);
                    gameFrame.refresh();
                    gameFrame.showNotification("Construction de " + building.getName() + " demarree !", "success");
                } else {
                    showError("Ressources insuffisantes !");
                }
            }
        }
    }
    
    private void openRecruitMenu() {
        Tile selected = gameFrame.getSelectedTile();
        if (selected == null || !selected.hasBuilding()) {
            showError("Sélectionnez un Camp d'Entraînement !");
            return;
        }
        
        Building building = selected.getBuilding();
        if (!(building instanceof TrainingCamp)) {
            showError("Ce bâtiment ne recrute pas d'unités !");
            return;
        }
        
        if (!building.isBuilt()) {
            showError("Le bâtiment n'est pas construit !");
            return;
        }
        
        TrainingCamp camp = (TrainingCamp) building;
        
        ModernDialog dialog = new ModernDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                                "Recrutement");
        
        String[] options = {
            "Soldat - 30 Or, 20 Nourriture",
            "Archer - 40 Or, 15 Bois, 15 Nourriture",
            "Cavalier - 50 Or, 30 Nourriture"
        };
        
        JList<String> list = new JList<>(options);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBackground(new Color(40, 40, 50));
        list.setForeground(Color.WHITE);
        list.setSelectedIndex(0);
        
        dialog.add(new JScrollPane(list));
        dialog.showDialog();
        
        if (dialog.isConfirmed()) {
            int choice = list.getSelectedIndex();
            Unit unit = null;
            
            switch (choice) {
                case 0: unit = camp.recruitSoldier(); break;
                case 1: unit = camp.recruitArcher(); break;
                case 2: unit = camp.recruitCavalry(); break;
            }
            
            if (unit != null) {
                // CORRECTION : Placer l'unité sur la carte
                Tile campTile = gameFrame.getMap().getTile(selected.getX(), selected.getY());
                if (campTile != null) {
                    campTile.setUnit(unit);
                }
                
                gameFrame.refresh();
                gameFrame.showNotification(unit.getName() + " recrute avec succes !", "success");
            } else {
                showError("Recrutement échoué ! Ressources insuffisantes.");
            }
        }
    }
    
    private void openMoveDialog() {
        Tile selected = gameFrame.getSelectedTile();
        if (selected == null || !selected.hasUnit()) return;
        
        Unit unit = selected.getUnit();
        if (unit.getOwner() != player) {
            showError("Ce n'est pas votre unité !");
            return;
        }
        
        if (unit.hasActed()) {
            showError("Cette unité a déjà agi !");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this,
            "Position cible (X,Y)\nExemple: 5,10",
            "Déplacer l'unité",
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && input.contains(",")) {
            try {
                String[] parts = input.split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                
                if (combatSystem.moveUnit(unit, x, y)) {
                    gameFrame.refresh();
                    gameFrame.showNotification("Unite deplacee vers (" + x + ", " + y + ")", "info");
                } else {
                    showError("Déplacement impossible !");
                }
            } catch (Exception e) {
                showError("Format invalide !");
            }
        }
    }
    
    private void openAttackDialog() {
        Tile selected = gameFrame.getSelectedTile();
        if (selected == null || !selected.hasUnit()) return;
        
        Unit attacker = selected.getUnit();
        if (attacker.getOwner() != player) {
            showError("Ce n'est pas votre unité !");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this,
            "Position cible (X,Y)\nExemple: 5,10",
            "Attaquer",
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && input.contains(",")) {
            try {
                String[] parts = input.split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                
                Tile targetTile = gameFrame.getMap().getTile(x, y);
                if (targetTile != null) {
                    // Vérifier si c'est une unité
                    if (targetTile.hasUnit()) {
                        Unit target = targetTile.getUnit();
                        
                        if (combatSystem.performAttack(attacker, target)) {
                            gameFrame.refresh();
                            gameFrame.showNotification(
                                attacker.getName() + " attaque " + target.getName() + " !",
                                "combat"
                            );
                            
                            // Vérifier si la cible est morte
                            if (!target.isAlive()) {
                                gameFrame.showNotification(
                                    target.getName() + " ennemi elimine !",
                                    "death"
                                );
                            }
                        }
                    }
                    // Vérifier si c'est un bâtiment
                    else if (targetTile.hasBuilding()) {
                        Building target = targetTile.getBuilding();
                        
                        if (canAttackBuilding(attacker, target)) {
                            attackBuilding(attacker, target);
                            gameFrame.refresh();
                            gameFrame.showNotification(
                                attacker.getName() + " attaque " + target.getName() + " !",
                                "combat"
                            );
                            
                            // Vérifier si le bâtiment est détruit
                            if (target.isDestroyed()) {
                                gameFrame.showNotification(
                                    target.getName() + " ennemi detruit !",
                                    "death"
                                );
                                
                                // Vérifier la victoire immédiatement
                                gameFrame.checkVictoryNow();
                            }
                        } else {
                            showError("Impossible d'attaquer ce batiment !");
                        }
                    } else {
                        showError("Aucune cible a cette position !");
                    }
                } else {
                    showError("Position invalide !");
                }
            } catch (Exception e) {
                showError("Format invalide !");
            }
        }
    }
    
    /**
     * Vérifie si une unité peut attaquer un bâtiment
     */
    private boolean canAttackBuilding(Unit attacker, Building target) {
        if (attacker.hasActed()) {
            return false;
        }
        
        if (attacker.getOwner() == target.getOwner()) {
            return false;
        }
        
        int distance = attacker.distanceTo(target.getX(), target.getY());
        return distance <= attacker.getRange();
    }
    
    /**
     * Attaque un bâtiment
     */
    private void attackBuilding(Unit attacker, Building target) {
        if (!canAttackBuilding(attacker, target)) {
            return;
        }
        
        // Calcul des dégâts (les bâtiments n'ont pas de défense)
        int damage = attacker.getAttack();
        
        target.takeDamage(damage);
        attacker.setHasActed(true);
        
        System.out.println(attacker.getName() + " attaque " + target.getName() + 
                         " et inflige " + damage + " degats !");
        System.out.println(target.getName() + " : " + target.getCurrentHealth() + 
                         "/" + target.getMaxHealth() + " PV");
        
        // Si le bâtiment est détruit, le retirer de la carte
        if (target.isDestroyed()) {
            Tile tile = gameFrame.getMap().getTile(target.getX(), target.getY());
            if (tile != null) {
                tile.removeBuilding();
            }
        }
    }
    
    public void updateButtons(Tile selectedTile) {
        if (selectedTile != null && selectedTile.hasUnit()) {
            Unit unit = selectedTile.getUnit();
            boolean isPlayer = unit.getOwner() == player;
            moveButton.setEnabled(isPlayer && !unit.hasActed());
            attackButton.setEnabled(isPlayer && !unit.hasActed());
        } else {
            moveButton.setEnabled(false);
            attackButton.setEnabled(false);
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur",
                                     JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Classe pour boutons modernes
    private class ModernButton extends JButton {
        private Color baseColor;
        private boolean hovered = false;
        
        ModernButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setBackground(color);
            setFocusPainted(false);
            setBorderPainted(false);
            setMaximumSize(new Dimension(240, 42));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        hovered = true;
                        setBackground(baseColor.brighter());
                    }
                }
                
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    setBackground(baseColor);
                }
            });
        }
        
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            if (!enabled) {
                setBackground(new Color(80, 80, 90));
            } else {
                setBackground(baseColor);
            }
        }
    }
    
    // Dialogue moderne
    private class ModernDialog extends JDialog {
        private boolean confirmed = false;
        
        ModernDialog(Frame parent, String title) {
            super(parent, title, true);
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(new Color(40, 40, 50));
            setSize(350, 250);
            setLocationRelativeTo(parent);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(40, 40, 50));
            
            ModernButton okButton = new ModernButton("✓ Confirmer", new Color(76, 175, 80));
            okButton.addActionListener(e -> {
                confirmed = true;
                dispose();
            });
            
            ModernButton cancelButton = new ModernButton("✗ Annuler", new Color(244, 67, 54));
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        void showDialog() {
            setVisible(true);
        }
        
        boolean isConfirmed() {
            return confirmed;
        }
    }
}