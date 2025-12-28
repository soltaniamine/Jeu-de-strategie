package game.ui;

import game.map.GameMap;
import game.map.Tile;
import game.map.TileType;
import game.unit.Unit;
import game.building.Building;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panneau de jeu moderne avec effets visuels avancés
 */
public class ModernGamePanel extends JPanel {
    private static final int TILE_SIZE = 48;
    private GameMap map;
    private Tile selectedTile;
    private Tile hoveredTile;
    private ModernGameFrame gameFrame;
    
    // Effets visuels
    private List<ParticleEffect> particles = new ArrayList<>();
    private float animationProgress = 0f;
    
    // Couleurs modernes avec dégradés
    private static final Color GRASS_START = new Color(106, 190, 48);
    private static final Color GRASS_END = new Color(85, 170, 40);
    private static final Color FOREST_START = new Color(34, 139, 34);
    private static final Color FOREST_END = new Color(25, 100, 25);
    private static final Color MOUNTAIN_START = new Color(158, 158, 158);
    private static final Color MOUNTAIN_END = new Color(105, 105, 105);
    private static final Color WATER_START = new Color(30, 144, 255);
    private static final Color WATER_END = new Color(0, 105, 180);
    private static final Color DESERT_START = new Color(237, 201, 175);
    private static final Color DESERT_END = new Color(210, 180, 140);
    private static final Color SELECTION_GLOW = new Color(255, 215, 0, 150);
    private static final Color HOVER_GLOW = new Color(255, 255, 255, 80);
    
    public ModernGamePanel(GameMap map, ModernGameFrame gameFrame) {
        this.map = map;
        this.gameFrame = gameFrame;
        this.selectedTile = null;
        this.hoveredTile = null;
        
        setPreferredSize(new Dimension(
            map.getWidth() * TILE_SIZE,
            map.getHeight() * TILE_SIZE
        ));
        
        setBackground(new Color(20, 20, 30));
        
        // Gestion des événements souris
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                handleHover(e.getX(), e.getY());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredTile = null;
                repaint();
            }
        };
        
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        // Animation timer
        Timer animationTimer = new Timer(50, e -> {
            animationProgress += 0.05f;
            if (animationProgress > 1.0f) animationProgress = 0f;
            updateParticles();
            repaint();
        });
        animationTimer.start();
    }
    
    private void handleClick(int mouseX, int mouseY) {
        int tileX = mouseX / TILE_SIZE;
        int tileY = mouseY / TILE_SIZE;
        
        if (map.isValidPosition(tileX, tileY)) {
            selectedTile = map.getTile(tileX, tileY);
            gameFrame.onTileSelected(selectedTile);
            
            // Effet de particules lors du clic
            addClickEffect(mouseX, mouseY);
            repaint();
        }
    }
    
    private void handleHover(int mouseX, int mouseY) {
        int tileX = mouseX / TILE_SIZE;
        int tileY = mouseY / TILE_SIZE;
        
        if (map.isValidPosition(tileX, tileY)) {
            hoveredTile = map.getTile(tileX, tileY);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            hoveredTile = null;
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Anti-aliasing et qualité maximale
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Dessiner les tuiles avec effets
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile tile = map.getTile(x, y);
                if (tile != null) {
                    drawModernTile(g2d, tile, x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }
        
        // Dessiner les particules
        for (ParticleEffect particle : particles) {
            particle.draw(g2d);
        }
        
        // Grille subtile
        drawModernGrid(g2d);
    }
    
    private void drawModernTile(Graphics2D g2d, Tile tile, int x, int y) {
        // Dégradé de terrain
        GradientPaint gradient = getTerrainGradient(tile.getType(), x, y);
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        
        // Effet hover
        if (tile == hoveredTile) {
            g2d.setColor(HOVER_GLOW);
            g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        }
        
        // Effet sélection avec glow animé
        if (tile == selectedTile) {
            float pulse = (float) (0.5 + 0.5 * Math.sin(animationProgress * Math.PI * 4));
            g2d.setColor(new Color(255, 215, 0, (int)(100 + 50 * pulse)));
            g2d.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            
            // Bordure brillante
            g2d.setColor(new Color(255, 215, 0, (int)(200 + 55 * pulse)));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        }
        
        // Ombre intérieure pour profondeur
        drawInnerShadow(g2d, x, y);
        
        // Dessiner le contenu
        if (tile.hasBuilding()) {
            drawModernBuilding(g2d, tile.getBuilding(), x, y);
        }
        
        if (tile.hasUnit()) {
            drawModernUnit(g2d, tile.getUnit(), x, y);
        }
    }
    
    private void drawInnerShadow(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRect(x, y, TILE_SIZE, 2);
        g2d.fillRect(x, y, 2, TILE_SIZE);
    }
    
    private void drawModernBuilding(Graphics2D g2d, Building building, int x, int y) {
        boolean isPlayer = building.getOwner() != null && 
                          !building.getOwner().getName().equals("IA Ennemi");
        
        // Base du bâtiment avec dégradé
        GradientPaint buildingGradient = new GradientPaint(
            x, y, new Color(101, 67, 33),
            x, y + TILE_SIZE, new Color(75, 50, 25)
        );
        g2d.setPaint(buildingGradient);
        
        int padding = 6;
        g2d.fillRoundRect(x + padding, y + padding, 
                         TILE_SIZE - padding * 2, TILE_SIZE - padding * 2, 8, 8);
        
        // Bordure avec couleur du joueur
        Color borderColor = isPlayer ? 
            new Color(33, 150, 243) : new Color(244, 67, 54);
        
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(x + padding, y + padding, 
                         TILE_SIZE - padding * 2, TILE_SIZE - padding * 2, 8, 8);
        
        // Barre de santé
        drawHealthBar(g2d, building.getCurrentHealth(), building.getMaxHealth(), 
                     x + padding, y + TILE_SIZE - 8, TILE_SIZE - padding * 2);
        
        // Icône avec ombre
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String icon = getBuildingIcon(building);
        FontMetrics fm = g2d.getFontMetrics();
        int iconX = x + (TILE_SIZE - fm.stringWidth(icon)) / 2 + 1;
        int iconY = y + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent() + 1;
        g2d.drawString(icon, iconX, iconY);
        
        // Icône principale
        g2d.setColor(Color.WHITE);
        g2d.drawString(icon, iconX - 1, iconY - 1);
        
        // Indicateur de construction
        if (!building.isBuilt()) {
            drawConstructionIndicator(g2d, building, x, y);
        }
    }
    
    private void drawModernUnit(Graphics2D g2d, Unit unit, int x, int y) {
        boolean isPlayer = unit.getOwner() != null && 
                          !unit.getOwner().getName().equals("IA Ennemi");
        
        Color unitColor = isPlayer ? 
            new Color(33, 150, 243) : new Color(244, 67, 54);
        
        int centerX = x + TILE_SIZE / 2;
        int centerY = y + TILE_SIZE / 2;
        int radius = 16;
        
        // Ombre
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(centerX - radius + 2, centerY - radius + 2, radius * 2, radius * 2);
        
        // Cercle avec dégradé
        GradientPaint unitGradient = new GradientPaint(
            centerX, centerY - radius, unitColor.brighter(),
            centerX, centerY + radius, unitColor.darker()
        );
        g2d.setPaint(unitGradient);
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Bordure brillante
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        
        // Barre de santé
        drawHealthBar(g2d, unit.getCurrentHealth(), unit.getMaxHealth(), 
                     x + 6, y + TILE_SIZE - 6, TILE_SIZE - 12);
        
        // Lettre de l'unité
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String symbol = getUnitSymbol(unit);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(symbol, 
                      centerX - fm.stringWidth(symbol) / 2,
                      centerY + fm.getAscent() / 2 - 1);
        
        // Indicateur "a déjà agi"
        if (unit.hasActed()) {
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("✓", x + TILE_SIZE - 14, y + 12);
        }
    }
    
    private void drawHealthBar(Graphics2D g2d, int current, int max, int x, int y, int width) {
        int height = 4;
        float healthPercent = (float) current / max;
        
        // Fond
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRoundRect(x, y, width, height, 2, 2);
        
        // Barre de santé avec couleur selon le pourcentage
        Color healthColor;
        if (healthPercent > 0.6f) {
            healthColor = new Color(76, 175, 80);
        } else if (healthPercent > 0.3f) {
            healthColor = new Color(255, 152, 0);
        } else {
            healthColor = new Color(244, 67, 54);
        }
        
        g2d.setColor(healthColor);
        g2d.fillRoundRect(x, y, (int)(width * healthPercent), height, 2, 2);
        
        // Bordure
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(x, y, width, height, 2, 2);
    }
    
    private void drawConstructionIndicator(Graphics2D g2d, Building building, int x, int y) {
        float progress = 1.0f - ((float) building.getRemainingTime() / building.getConstructionTime());
        
        g2d.setColor(new Color(255, 152, 0, 200));
        g2d.setStroke(new BasicStroke(3));
        
        int size = TILE_SIZE - 12;
        int startAngle = 90;
        int arcAngle = (int) (360 * progress);
        
        g2d.drawArc(x + 6, y + 6, size, size, startAngle, -arcAngle);
    }
    
    private void drawModernGrid(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 20));
        g2d.setStroke(new BasicStroke(0.5f));
        
        for (int x = 0; x <= map.getWidth(); x++) {
            g2d.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, map.getHeight() * TILE_SIZE);
        }
        
        for (int y = 0; y <= map.getHeight(); y++) {
            g2d.drawLine(0, y * TILE_SIZE, map.getWidth() * TILE_SIZE, y * TILE_SIZE);
        }
    }
    
    private GradientPaint getTerrainGradient(TileType type, int x, int y) {
        Color start, end;
        switch (type) {
            case GRASS:
                start = GRASS_START;
                end = GRASS_END;
                break;
            case FOREST:
                start = FOREST_START;
                end = FOREST_END;
                break;
            case MOUNTAIN:
                start = MOUNTAIN_START;
                end = MOUNTAIN_END;
                break;
            case WATER:
                start = WATER_START;
                end = WATER_END;
                break;
            case DESERT:
                start = DESERT_START;
                end = DESERT_END;
                break;
            default:
                start = Color.GRAY;
                end = Color.DARK_GRAY;
        }
        
        return new GradientPaint(x, y, start, x, y + TILE_SIZE, end);
    }
    
    private String getBuildingIcon(Building building) {
        String name = building.getClass().getSimpleName();
        switch (name) {
            case "CommandCenter": return "CC";
            case "Farm": return "F";
            case "Mine": return "M";
            case "Sawmill": return "S";
            case "TrainingCamp": return "T";
            default: return "B";
        }
    }
    
    private String getUnitSymbol(Unit unit) {
        String name = unit.getClass().getSimpleName();
        switch (name) {
            case "Soldier": return "S";
            case "Archer": return "A";
            case "Cavalry": return "C";
            default: return "U";
        }
    }
    
    private void addClickEffect(int x, int y) {
        for (int i = 0; i < 8; i++) {
            particles.add(new ParticleEffect(x, y));
        }
    }
    
    private void updateParticles() {
        particles.removeIf(p -> !p.update());
    }
    
    // Classe interne pour les effets de particules
    private class ParticleEffect {
        float x, y, vx, vy;
        float life = 1.0f;
        Color color;
        
        ParticleEffect(int startX, int startY) {
            this.x = startX;
            this.y = startY;
            double angle = Math.random() * Math.PI * 2;
            float speed = 2 + (float) Math.random() * 3;
            this.vx = (float) Math.cos(angle) * speed;
            this.vy = (float) Math.sin(angle) * speed;
            this.color = new Color(255, 215, 0);
        }
        
        boolean update() {
            x += vx;
            y += vy;
            vy += 0.2f; // Gravité
            life -= 0.05f;
            return life > 0;
        }
        
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                                  (int) (255 * life)));
            int size = (int) (4 * life);
            g2d.fillOval((int) x - size / 2, (int) y - size / 2, size, size);
        }
    }
    
    public Tile getSelectedTile() {
        return selectedTile;
    }
    
    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
        repaint();
    }
}