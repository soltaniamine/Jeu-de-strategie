package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Système de notifications visuelles pour le jeu
 */
public class NotificationPanel extends JPanel {
    private List<Notification> notifications = new ArrayList<>();
    private Timer animationTimer;
    
    public NotificationPanel() {
        setLayout(null); // Positionnement absolu
        setOpaque(false);
        setPreferredSize(new Dimension(400, 600));
        
        // Timer pour animer et supprimer les notifications
        animationTimer = new Timer(50, e -> {
            boolean needsRepaint = false;
            List<Notification> toRemove = new ArrayList<>();
            
            for (Notification notif : notifications) {
                notif.update();
                if (notif.shouldRemove()) {
                    toRemove.add(notif);
                    needsRepaint = true;
                } else if (notif.isAnimating()) {
                    needsRepaint = true;
                }
            }
            
            notifications.removeAll(toRemove);
            
            if (needsRepaint) {
                repaint();
            }
        });
        animationTimer.start();
    }
    
    /**
     * Ajoute une notification d'information
     */
    public void addInfo(String message) {
        addNotification(message, NotificationType.INFO);
    }
    
    /**
     * Ajoute une notification de succès
     */
    public void addSuccess(String message) {
        addNotification(message, NotificationType.SUCCESS);
    }
    
    /**
     * Ajoute une notification d'avertissement
     */
    public void addWarning(String message) {
        addNotification(message, NotificationType.WARNING);
    }
    
    /**
     * Ajoute une notification de combat
     */
    public void addCombat(String message) {
        addNotification(message, NotificationType.COMBAT);
    }
    
    /**
     * Ajoute une notification de mort
     */
    public void addDeath(String message) {
        addNotification(message, NotificationType.DEATH);
    }
    
    private void addNotification(String message, NotificationType type) {
        Notification notif = new Notification(message, type);
        notifications.add(notif);
        
        // Repositionner toutes les notifications
        repositionNotifications();
        repaint();
    }
    
    private void repositionNotifications() {
        int y = 10;
        for (int i = notifications.size() - 1; i >= 0; i--) {
            notifications.get(i).setTargetY(y);
            y += 70;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (Notification notif : notifications) {
            notif.draw(g2d, getWidth());
        }
    }
    
    /**
     * Type de notification
     */
    enum NotificationType {
        INFO(new Color(33, 150, 243), "INFO"),
        SUCCESS(new Color(76, 175, 80), "OK"),
        WARNING(new Color(255, 152, 0), "ATTENTION"),
        COMBAT(new Color(244, 67, 54), "COMBAT"),
        DEATH(new Color(139, 0, 0), "MORT");
        
        final Color color;
        final String prefix;
        
        NotificationType(Color color, String prefix) {
            this.color = color;
            this.prefix = prefix;
        }
    }
    
    /**
     * Classe interne représentant une notification
     */
    private class Notification {
        private String message;
        private NotificationType type;
        private float alpha = 0f;
        private float targetAlpha = 1f;
        private int currentY = -60;
        private int targetY = 10;
        private long creationTime;
        private static final long DISPLAY_DURATION = 4000; // 4 secondes
        private static final long FADE_DURATION = 500; // 0.5 seconde
        
        Notification(String message, NotificationType type) {
            this.message = message;
            this.type = type;
            this.creationTime = System.currentTimeMillis();
        }
        
        void update() {
            // Animation d'apparition (slide down + fade in)
            if (currentY < targetY) {
                currentY += 2;
            }
            
            if (alpha < targetAlpha) {
                alpha += 0.05f;
                if (alpha > targetAlpha) alpha = targetAlpha;
            }
            
            // Début du fade out après la durée d'affichage
            long elapsed = System.currentTimeMillis() - creationTime;
            if (elapsed > DISPLAY_DURATION) {
                targetAlpha = 0f;
                if (alpha > 0) {
                    alpha -= 0.05f;
                    if (alpha < 0) alpha = 0;
                }
            }
        }
        
        void setTargetY(int y) {
            this.targetY = y;
        }
        
        boolean shouldRemove() {
            long elapsed = System.currentTimeMillis() - creationTime;
            return elapsed > DISPLAY_DURATION + FADE_DURATION && alpha <= 0;
        }
        
        boolean isAnimating() {
            return currentY != targetY || alpha != targetAlpha;
        }
        
        void draw(Graphics2D g2d, int panelWidth) {
            if (alpha <= 0) return;
            
            int width = 350;
            int height = 60;
            int x = panelWidth - width - 20;
            
            // Sauvegarde du composite
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            // Ombre
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(x + 3, currentY + 3, width, height, 10, 10);
            
            // Fond avec dégradé
            GradientPaint gradient = new GradientPaint(
                x, currentY, new Color(40, 40, 50),
                x, currentY + height, new Color(30, 30, 40)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(x, currentY, width, height, 10, 10);
            
            // Bordure colorée selon le type
            g2d.setColor(type.color);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x, currentY, width, height, 10, 10);
            
            // Barre latérale colorée
            g2d.fillRoundRect(x + 5, currentY + 5, 5, height - 10, 3, 3);
            
            // Icône/Préfixe
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.drawString(type.prefix, x + 20, currentY + 25);
            
            // Message
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            // Découper le message si trop long
            String displayMessage = message;
            FontMetrics fm = g2d.getFontMetrics();
            int maxWidth = width - 90;
            
            if (fm.stringWidth(displayMessage) > maxWidth) {
                while (fm.stringWidth(displayMessage + "...") > maxWidth && displayMessage.length() > 0) {
                    displayMessage = displayMessage.substring(0, displayMessage.length() - 1);
                }
                displayMessage += "...";
            }
            
            g2d.drawString(displayMessage, x + 20, currentY + 42);
            
            // Restaurer le composite
            g2d.setComposite(oldComposite);
        }
    }
}