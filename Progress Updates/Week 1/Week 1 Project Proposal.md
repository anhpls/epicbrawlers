# Week 1: Project Proposal

Anh Huynh



### Project Pitch

**Game Title:** Epic Brawlers: Idle RPG

**Description:** 
*Epic Brawlers* is a click-to-fight boss game built with Java Swing. The player faces one enemy boss at a time. Clicking **attack** inflicts damage; defeating a boss awards gold, which the player can spend on **weapon upgrades** to increase damage per click. Selecting to move onto the **Next Boss** spawns a tougher opponent with higher health and larger rewards. 

All gameplay happens through GUI components — buttons, labels, and a progress bar. There's no player movement; instead, progression focuses on stages, upgrades, and incremental strength.

**Gameplay each round:**
When the player clicks Attack, the player deals damage to the boss, and immediately after, the boss attacks back. This ensures that both sides lose HP each round. When the boss's HP reaches 0, the player earns gold and has the option to upgrade their weapon (upgrades unavailable if funds are insufficient) or advance to the next stage, where the next boss has higher HP, greater attack damage, and better rewards.

**User actions:**

- Clicks **Attack** —> Boss HP decreases. If HP <= 0, the boss dies, the player earns gold, and a new boss appears.
- Clicks **Upgrade Weapon** —> if enough gold, the player's weapon level increases, raising it's attack damage
- Clicks **Next Boss** —> Generates the next-tier boss.
- Stage Progression —> defeating a boss allows to advance to the next stage
- Death/Reset —> if HP <= 0, the player restarts at Stage 1, fully healed.



### GUI Preview





### CRC Cards

**Player:** tracks gold, level, damage, hp; handles upgrades and taking damage. 
Collaborator(s): *Boss*

**Boss:** stores hp, maxHp, reward, attack, stage; handles taking damage
Collaborator(s): *Player*

**GameUI:** manages GUI display and logic; responds to button clicks; updates HP bars and stage progression
Collaborator(s): *Player, Boss*



### **UML Diagram:** 









### **Object-Oriented Design**

- Aggregation: The GameUI has-a Player and a Boss

- Encapsulation: Each class manages its own state and behavior (damage, HP, gold).
- Polymorphism: future versions include subclasses such as a SlimeBoss and DragonBoss to demonstrate inheritance
- Event-Driven Programming: each click triggers ActionListener events, updating the GUI and progressing the game round-by-round



##### Video Link: 



### Learning Outcomes





### Planned Working Time





### TO-DO Timeline Plan

| Week | Goals                                                        |
| ---- | ------------------------------------------------------------ |
| 1    | Write proposal; design CRC cards and UML; set up project files. |
| 2    | Implement Player and Boss classes; test damage logic and gold system. |
| 3    | Finalize model logic; draw GUI layout plan.                  |
| 4    | Test and refine attack flow; prepare a simple non-functional GUI. |
| 5    | Build the GUI (view) and display live HP updates.            |
| 6    | Connect event handling (controller) for attack and upgrade buttons. |
| 7    | Debug and polish stage progression and death/reset behavior. |
| 8    | Record video demonstration; finalize all documentation and code. |



### GitHub Repository

