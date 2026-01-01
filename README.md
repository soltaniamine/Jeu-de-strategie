# 🎮 Jeu de Stratégie en Java - ISIL 2025/2026

## 📋 Description

Jeu de stratégie au tour par tour développé en Java avec une interface graphique moderne utilisant Swing. Le joueur gère des ressources, construit des bâtiments, recrute des unités et combat pour détruire le Centre de Commandement ennemi.

## 🎯 Objectif du Jeu

Détruire le Centre de Commandement ennemi tout en protégeant le vôtre. Pour y parvenir, vous devez d'abord éliminer toutes les unités ennemies.

## ✨ Fonctionnalités

### 🗺️ Système de Carte
- Carte procédurale avec 5 types de terrain (Herbe, Forêt, Montagne, Eau, Désert)
- Taille configurable (10×10, 15×15, 20×20)
- Bonus/malus de terrain (défense, coût de mouvement)
- Système de cases avec gestion des obstacles

### 💰 Système de Ressources
- 4 ressources : Or, Bois, Pierre, Nourriture
- Production automatique par bâtiments
- Affichage en temps réel avec barres de progression

### 🏗️ Bâtiments (5 types)
- **Centre de Commandement** : +10 Or/tour (bâtiment principal)
- **Ferme** : +15 Nourriture/tour
- **Mine** : +10 Pierre + 5 Or/tour
- **Scierie** : +12 Bois/tour
- **Camp d'Entraînement** : Recrute des unités

### ⚔️ Unités (3 types)
- **Soldat** : 100 PV, 15 ATK, 10 DEF, Portée 1, Mvt 3
- **Archer** : 70 PV, 20 ATK, 5 DEF, Portée 3, Mvt 2
- **Cavalier** : 90 PV, 18 ATK, 7 DEF, Portée 1, Mvt 5

### 💥 Système de Combat
- Attaques avec calcul de dégâts (ATK - DEF + aléatoire ±20%)
- Contre-attaques automatiques (50% dégâts)
- Coups critiques (10% chance, ×1.5 dégâts)
- Bonus de terrain (Montagne +2 DEF)
- Possibilité d'attaquer les bâtiments

### 🎨 Interface Graphique Moderne
- Design sombre élégant avec effets visuels
- Deux sidebars (Actions à gauche, Informations à droite)
- Système de notifications en temps réel
- Affichage clair des ressources et statistiques
- Animations fluides et effets de particules

## 🛠️ Technologies Utilisées

- **Langage** : Java 21
- **Interface** : Java Swing
- **Collections** : ArrayList, HashMap, List, Map
- **Architecture** : POO (héritage, polymorphisme, encapsulation, classes abstraites)

## 📁 Structure du Projet

```
src/game/
├── main/
│   └── GameLauncher.java          # Version console
├── ui/
│   ├── ModernMainMenuFrame.java   # Menu principal GUI
│   ├── ModernGameFrame.java       # Fenêtre de jeu
│   ├── ModernGamePanel.java       # Vue de la carte
│   ├── ModernInfoPanel.java       # Panneau d'informations
│   ├── ModernActionPanel.java     # Panneau d'actions
│   └── NotificationPanel.java     # Système de notifications
├── map/
│   ├── TileType.java              # Types de terrain (enum)
│   ├── Tile.java                  # Case individuelle
│   └── GameMap.java               # Carte complète
├── player/
│   └── Player.java                # Gestion du joueur
├── resource/
│   └── ResourceType.java          # Types de ressources (enum)
├── unit/
│   ├── Unit.java                  # Classe abstraite
│   ├── Soldier.java               # Soldat
│   ├── Archer.java                # Archer
│   └── Cavalry.java               # Cavalier
├── building/
│   ├── Building.java              # Classe abstraite
│   ├── CommandCenter.java         # Centre de commandement
│   ├── Farm.java                  # Ferme
│   ├── Mine.java                  # Mine
│   ├── Sawmill.java               # Scierie
│   └── TrainingCamp.java          # Camp d'entraînement
└── combat/
    └── CombatSystem.java          # Système de combat
```

## 🚀 Installation et Exécution

### Prérequis
- Java JDK 11 ou supérieur
- IDE Java (IntelliJ IDEA, Eclipse) ou terminal

### Compilation et Exécution

#### Avec la ligne de commande :
```bash
# Naviguer vers le dossier src
cd game/src

# Compiler le projet
javac game/ui/ModernMainMenuFrame.java

# Lancer le jeu (Interface Graphique)
java game.ui.ModernMainMenuFrame

# OU lancer la version console
javac game/main/GameLauncher.java
java game.main.GameLauncher
```

#### Avec un IDE :
1. Importer le projet dans votre IDE
2. Définir `src` comme dossier source
3. Exécuter `ModernMainMenuFrame.main()`

## 🏆 Concepts POO Implémentés

### Héritage
- `Unit` → `Soldier`, `Archer`, `Cavalry`
- `Building` → `CommandCenter`, `Farm`, `Mine`, `Sawmill`, `TrainingCamp`

### Polymorphisme
- Méthodes abstraites `initializeStats()`, `performAction()`
- Méthodes redéfinies dans chaque sous-classe

### Encapsulation
- Attributs privés avec getters/setters
- Protection des données avec collections immuables

### Classes Abstraites
- `Unit` : Définit le comportement commun des unités
- `Building` : Définit le comportement commun des bâtiments

### Collections Java
- `HashMap<ResourceType, Integer>` : Gestion des ressources
- `ArrayList<Unit>` : Liste des unités
- `ArrayList<Building>` : Liste des bâtiments
- `List<Notification>` : File de notifications

---

**Version** : 1.0.0  
**Date** : Janvier 2026  
**Statut** : Complet et fonctionnel