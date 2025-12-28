package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Menu principal moderne avec animations et effets visuels
 */
public class ModernMainMenuFrame extends JFrame {
    private float animationProgress = 0f;
    private AnimatedPanel mainPanel;
    
    public ModernMainMenuFrame() {
        setTitle("Jeu de Stratégie - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setUndecorated(true);
        setLayout(new BorderLayout());
        
        createComponents();
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Animation
        Timer animationTimer = new Timer(30, e -> {
            animationProgress += 0.01f;
            if (animationProgress > 1.0f) animationProgress = 0f;
            mainPanel.repaint();
        });
        animationTimer.start();
    }
    
    private void createComponents() {
        mainPanel = new AnimatedPanel();
        mainPanel.setLayout(null); // Absolute positioning pour un contrôle total
        
        // Titre principal avec effet
        JLabel titleLabel = new JLabel("JEU DE STRATÉGIE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 56));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(200, 100, 600, 70);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("ISIL 2025/2026");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(200, 200, 220));
        subtitleLabel.setBounds(200, 170, 600, 40);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Boutons stylisés
        int buttonWidth = 350;
        int buttonHeight = 60;
        int centerX = (900 - buttonWidth) / 2;
        int startY = 280;
        int gap = 20;
        
        ModernMenuButton newGameButton = new ModernMenuButton("NOUVELLE PARTIE",
                                                               new Color(76, 175, 80));
        newGameButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        newGameButton.addActionListener(e -> startNewGame());
        
        ModernMenuButton rulesButton = new ModernMenuButton("REGLES DU JEU",
                                                             new Color(33, 150, 243));
        rulesButton.setBounds(centerX, startY + buttonHeight + gap, buttonWidth, buttonHeight);
        rulesButton.addActionListener(e -> showRules());
        
        ModernMenuButton exitButton = new ModernMenuButton("QUITTER",
                                                            new Color(244, 67, 54));
        exitButton.setBounds(centerX, startY + (buttonHeight + gap) * 2, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));
        
        // Footer
        JLabel footerLabel = new JLabel("Développé avec Java Swing • Cliquez pour commencer");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(150, 150, 170));
        footerLabel.setBounds(200, 570, 600, 30);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Bouton fermer
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(244, 67, 54));
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setBounds(850, 10, 40, 40);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));
        
        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(newGameButton);
        mainPanel.add(rulesButton);
        mainPanel.add(exitButton);
        mainPanel.add(footerLabel);
        mainPanel.add(closeButton);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void startNewGame() {
        String[] sizeOptions = {"Petite (10×10)", "Moyenne (15×15)", "Grande (20×20)"};
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(new Color(40, 40, 50));
        
        JLabel label = new JLabel("Choisissez la taille de la carte:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JComboBox<String> sizeCombo = new JComboBox<>(sizeOptions);
        sizeCombo.setSelectedIndex(1);
        sizeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panel.add(label);
        panel.add(sizeCombo);
        
        int result = JOptionPane.showConfirmDialog(this, panel,
                                                   "Configuration",
                                                   JOptionPane.OK_CANCEL_OPTION,
                                                   JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) return;
        
        String playerName = JOptionPane.showInputDialog(this,
            "Entrez votre nom:",
            "Nom du joueur",
            JOptionPane.QUESTION_MESSAGE);
        
        if (playerName != null && !playerName.trim().isEmpty()) {
            int mapSize = 15;
            String choice = (String) sizeCombo.getSelectedItem();
            
            if (choice.startsWith("Petite")) {
                mapSize = 10;
            } else if (choice.startsWith("Grande")) {
                mapSize = 20;
            }
            
            dispose();
            final int finalSize = mapSize;
            SwingUtilities.invokeLater(() -> new ModernGameFrame(playerName, finalSize));
        }
    }
    
    private void showRules() {
        String rules = """
            ================================================
                          REGLES DU JEU                    
            ================================================
            
            OBJECTIF:
            Detruire le Centre de Commandement ennemi !
            
            BATIMENTS:
            * Centre de Commandement: +10 Or/tour
               Batiment principal. Si detruit = defaite !
            
            * Ferme: +15 Nourriture/tour
               Necessaire pour recruter des unites
            
            * Mine: +10 Pierre, +5 Or/tour
               Pierre pour construire, Or pour recruter
            
            * Scierie: +12 Bois/tour
               Bois pour constructions et archers
            
            * Camp d'Entrainement: Recrute des unites
               Permet de creer votre armee
            
            UNITES:
            * Soldat: 100 PV, 15 ATK, 10 DEF, Portee 1
               Tank defensif, bon en melee
            
            * Archer: 70 PV, 20 ATK, 5 DEF, Portee 3
               Attaque a distance, fragile
            
            * Cavalier: 90 PV, 18 ATK, 7 DEF, Portee 1
               Tres mobile, charge puissante
            
            CONTROLES:
            * Cliquer sur une case pour la selectionner
            * Utiliser les boutons d'action a droite
            * Construire > Produire > Recruter > Attaquer !
            * Terminer le tour pour produire des ressources
            
            CONSEILS STRATEGIQUES:
            * Construisez des fermes des le debut
            * Protegez votre Centre de Commandement
            * Utilisez le terrain a votre avantage
            * Les montagnes donnent +2 defense
            * Equilibrez economie et armee
            
            Bonne chance, Commandant !
            """;
        
        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        textArea.setBackground(new Color(40, 40, 50));
        textArea.setForeground(new Color(220, 220, 240));
        textArea.setCaretColor(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Règles du Jeu",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Panel avec fond animé
    private class AnimatedPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dégradé de fond animé
            Color color1 = new Color(20, 20, 40);
            Color color2 = new Color(40, 20, 60);
            GradientPaint gradient = new GradientPaint(
                0, 0, color1,
                getWidth(), getHeight(), color2
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Effet de particules flottantes
            g2d.setColor(new Color(255, 255, 255, 30));
            for (int i = 0; i < 50; i++) {
                float offset = (animationProgress + i * 0.02f) % 1.0f;
                int x = (int) ((i * 37 + offset * 50) % getWidth());
                int y = (int) ((i * 53 + offset * getHeight()) % getHeight());
                int size = 2 + (i % 3);
                g2d.fillOval(x, y, size, size);
            }
        }
    }
    
    // Bouton de menu moderne
    private class ModernMenuButton extends JButton {
        private Color baseColor;
        private boolean hovered = false;
        private float hoverProgress = 0f;
        
        ModernMenuButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            
            setFont(new Font("Segoe UI", Font.BOLD, 20));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    animateHover();
                }
                
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                }
            });
        }
        
        private void animateHover() {
            Timer timer = new Timer(20, null);
            timer.addActionListener(e -> {
                if (hovered && hoverProgress < 1.0f) {
                    hoverProgress += 0.1f;
                } else if (!hovered && hoverProgress > 0f) {
                    hoverProgress -= 0.1f;
                } else {
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Ombre
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 15, 15);
            
            // Bouton avec effet hover
            Color currentColor = hovered ?
                baseColor.brighter() : baseColor;
            
            GradientPaint gradient = new GradientPaint(
                0, 0, currentColor,
                0, getHeight(), currentColor.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 15, 15);
            
            // Bordure lumineuse si hover
            if (hoverProgress > 0) {
                g2d.setColor(new Color(255, 255, 255, (int) (150 * hoverProgress)));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 15, 15);
            }
            
            super.paintComponent(g);
        }
    }
    
    /**
     * Point d'entrée pour lancer le jeu moderne
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new ModernMainMenuFrame());
    }
}