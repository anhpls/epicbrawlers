# Week 1: Project Proposal

Anh Huynh



### Project Pitch

**Game Title:** Epic Brawlers

**Description:** 
*Epic Brawlers* is a click-to-fight boss game built with Java Swing. The player faces one enemy boss at a time. Clicking **attack** inflicts damage; defeating a boss awards gold, which the player can spend on **weapon upgrades** to increase damage per click. Selecting to move onto the **Next Boss** spawns a tougher opponent with higher health and larger rewards. 

All gameplay happens through GUI components — buttons, labels, and a progress bar. There's no player movement; instead, progression focuses on stages, upgrades, and incremental strength.

**Gameplay each round:**
When the player clicks Attack, the player deals damage to the boss, and immediately after, the boss attacks back. This ensures that both sides lose HP each round. When the boss's HP reaches 0, the player earns gold and has the option to upgrade their weapon (upgrades unavailable if funds are insufficient) or advance to the next stage, where the next boss has higher HP, greater attack damage, and better rewards.

**User actions:**

- Clicks **Attack** —> Boss HP decreases. If HP <= 0, the boss dies, the player earns gold, and a new boss appears.
- Clicks **Upgrade Weapon** —> if enough gold, the player's weapon level increases, raising it's attack damage
- Clicks **Next Boss** —> Generates the next-tier boss.
- **Stage Progression** —> defeating a boss allows to advance to the next stage
- **Death/Reset** —> if HP <= 0, the player restarts at Stage 1, fully healed.



### GUI Preview

![image-20251022212033373](/Users/anhpls/Desktop/SWE/CISC 191 Intermediate Java/Epic Brawlers/Progress Updates/Week 1/assets/image-20251022212033373.png)



### CRC Cards

**Player:** tracks gold, level, damage, hp; handles upgrades and taking damage. 
**Collaborator(s):** *Boss*

**Boss:** stores hp, maxHp, reward, attack, stage; handles taking damage
**Collaborator(s):** *Player*

**GameUI:** manages GUI display and logic; responds to button clicks; updates HP bars and stage progression
**Collaborator(s):** *Player, Boss*

***BaseWeapon:** base class for all weapons; defines common fields (name, damage, level) and methods for attacking and upgrading

**Collaborator(s):** *Player, Boss*

*= possibly added in future 

### **UML Diagram:** 



![EpicBrawlersUML.drawio](/Users/anhpls/Desktop/SWE/CISC 191 Intermediate Java/Epic Brawlers/Progress Updates/Week 1/assets/EpicBrawlersUML.drawio-1261124.png)



### **Object-Oriented Design**

- Aggregation: The GameUI has-a Player and a Boss

- Encapsulation: Each class manages its own state and behavior (damage, HP, gold).
- Polymorphism: future versions include subclasses such as a SlimeBoss and DragonBoss to demonstrate inheritance
- Event-Driven Programming: each click triggers ActionListener events, updating the GUI and progressing the game round-by-round



### Video Link: https://youtu.be/fAck9a8_tyI



### Learning Outcomes

| LO                                                           | Demonstrated in Epic Brawlers                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| LO1: Object Oriented Design Principles                       | Clear separation of classes; encapsulated Player/Boss classes; GUI/controller in GameUI class |
| LO2: ·  LO2: Construct programs utilizing single and multidimensional arrays (optional) | Optional                                                     |
| LO3: Objects & Aggregation                                   | GameUI aggregates Player and Boss and coordinates their interactions |
| LO4: Inheritance & polymorphism                              | Planned boss hierarchy (e.g., BaseBoss —> SlimeBoss, DragonBoss) without changing controller code |
| LO6: GUI & Event-Driven Programming                          | Swing (JFrame, JButton, JLabel, JProgressBar) with ActionListener callbacks for attacks/upgrades |
| LO7: Exception handling                                      | Handling for insufficient funds on upgrade; safe HP bounds; death/reset flow |
| LO8: Text File I/O                                           | Save/load player progress (gold, level) to a simple text file |



### Planned Working Time

| Week   | Hours (outside class)                        |
| ------ | -------------------------------------------- |
| Week 1 | Thurs 2-5 PM + Fri 6-9 PM                    |
| Week 2 | Thurs 2-5 PM + Fri 6-9 PM                    |
| Week 3 | Thurs 2-5 PM + Fri 6-9 PM / Sat 11 AM - 2 PM |
| Week 4 | Thurs 2-5 PM + Fri 6-9 PM / Sat 11 AM - 2 PM |
| "…"    | "…"                                          |



### TO-DO Timeline Plan

| Week | Goals                                                        |
| ---- | ------------------------------------------------------------ |
| 1    | Write proposal; design CRC cards and UML; set up project files. |
| 2    | Implement Player and Boss classes; test damage logic and gold system. |
| 3    | Finalize model logic; create beta GUI layout. Implement rough codes for all other classes |
| 4    | Test and refine attack flow; prepare a simple non-functional GUI. |
| 5    | Test the GUI (view) and display live HP updates.             |
| 6    | Connect event handling (controller) for attack and upgrade buttons. |
| 7    | Debug and polish stage progression and death/reset behavior. |
| 8    | Finalize all documentation and code; thorough testing        |



### GitHub Repository

https://github.com/anhpls/epicbrawlers



### Week 1 Additional Deliverables:

·  *Deliverable 2 (optional, on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* If you have started writing code, submit the code you have written so far, even if it is not complete and/or has compiler errors. 

 

## Week 2: Updates

Revise anything that is needed in the Week 1 section based on instructor feedback. Make sure your design is viable before working on your Week 2 goals from your project timeline.

### Week 2 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Week 1:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

 

## Week 3: Updates

Work on your Week 3 goals from your project timeline.

### Week 3 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Week 1 or Week 2:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

## Week 4: Updates

Work on your Week 4 goals from your project timeline.

### Week 4 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Weeks 1-3:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

## Week 5: Updates

Work on your Week 5 goals from your project timeline.

### Week 5 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Weeks 1-4:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

## Week 6: Updates

Work on your Week 6 goals from your project timeline.

### Week 6 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Weeks 1-5:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

## Week 7: Updates

Work on your Week 4 goals from your project timeline.

### Week 7 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Weeks 1-6:

<span style="color: red">·  Share a screenshot of the GUI. </span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes have you decided to make, and why did you make them? </span>

​	<span style="color: red">·   What challenges have you encountered? </span>

<span style="color: red">	·   What do you still need to do to complete the project?</span>

<span style="color: red">·  Update your timeline goals, if needed. </span>

## Week 8: Project Wrap-up

Finish writing any other necessary code and debug (although hopefully you were debugging as you wrote the code!).  

### Week 8 Deliverables

*Deliverable 1 (on the Canvas* [*Project submission*](https://sdccd.instructure.com/courses/2441328/assignments/19806962)*):* Submit the code you have written so far, even if it is not complete and/or has compiler errors. 

*Deliverable 2 (here):* Update your Canvas Project page from Week 1. Please add to the page - do not delete any content from Weeks 1-7:

<span style="color: red">·  Share a screenshot of the text interaction with the user.</span>

<span style="color: red">·  Add a journal-like entry about your experience writing code this week to your Canvas project page: </span>

​	<span style="color: red">·   What design changes did you make during the project, and why did you make them? </span>

<span style="color: red">	·   What challenges did you encounter? </span>

<span style="color: red">	·   What could you do to expand on and improve your project?</span>

<span style="color: red">	·   If you were to start the project from scratch, what would you do differently? </span>

<span style="color: red">·  Optionally, you may choose to share your code with your classmates by linking to your Eclipse project here.</span>

*Deliverable 3 (here):* Share your video demonstration of your project and explanation of how and why the project utilized the concepts from the Learning Objectives; you only need to address the LOs for which you have not yet demonstrated Senior Developer proficiency. Aim for your video to be around 5 minutes, and do not exceed 10 minutes in length. Each partner must create their own video to demonstrate Senior Developer proficiency. 

If you have not previously demonstrated Middle Developer proficiency for a LO, use your project code to make a video for that LO. Senior Developer proficiency will not be evaluated until Middle Developer proficiency is demonstrated.

