# 🎮 Epic Brawlers: Idle RPG

A simple Java Swing click-to-fight game demonstrating object-oriented design and event-driven GUI programming.

---

## 🧩 Overview

**Epic Brawlers: Idle RPG** is a lightweight, idle-style boss-battler built entirely with **Java Swing**.  
The player fights through an endless series of bosses, earns gold, upgrades their weapon, and advances through stages of increasing difficulty.

The project was created for a CISC 191 Object-Oriented Programming course to showcase:

- Encapsulation and aggregation between classes
- GUI programming with event listeners
- MVC design principles
- Exception handling and graceful restarts

---

## ⚔️ Gameplay

1. **Attack** — Each click starts a combat round.
   - The player deals damage to the boss.
   - The boss automatically attacks back.
   - Both sides lose HP each round.
2. **Upgrade Weapon** — Spend gold to permanently increase your attack power.
   - Cost = `10 × current level`.
3. **Stage Progression** — When a boss is defeated, a tougher one appears automatically with higher HP, attack, and reward.
4. **Death & Reset** — If the player’s HP reaches 0, they’re defeated and return to **Stage 1** fully healed, keeping their gold and upgrades.

---

## 🖥️ GUI

Built using **Java Swing**:

- `JFrame` for the main window
- `JButton` for Attack / Upgrade controls
- `JLabel` for displaying stats and messages
- `JProgressBar` for player and boss HP bars

Future versions will use **CardLayout** to switch between the main battle screen and upgrade or settings menus.

---

## 🧱 Classes

| Class      | Role                                                                                      |
| ---------- | ----------------------------------------------------------------------------------------- |
| **Player** | Tracks gold, level, damage, and HP; handles upgrades and taking damage.                   |
| **Boss**   | Stores HP, attack power, stage, and reward; handles taking damage and scaling difficulty. |
| **GameUI** | Manages the GUI, handles button events, updates HP bars, and controls stage logic.        |
| **App**    | Launches the game (`main()` method).                                                      |

---

## 🧠 Object-Oriented Design

**Aggregation:**  
`GameUI` _has-a_ `Player` and a `Boss`, keeping logic modular and independent.

**Encapsulation:**  
Each class maintains its own state and exposes only the necessary methods (e.g., `takeDamage()`, `tryUpgrade()`).

**Polymorphism (planned):**  
Future updates will include subclasses like `SlimeBoss` and `DragonBoss` for varied behaviors and difficulty curves.

**MVC Structure:**

- **Model:** `Player`, `Boss`
- **View + Controller:** `GameUI` (buttons trigger event listeners that update the model and refresh the view)

---

## 🧰 Installation & Run

### Requirements

- Java JDK 17 or newer
- Any IDE (Eclipse, IntelliJ, VS Code) or command line

### Compile & Run

```bash
javac *.java
java App
```
