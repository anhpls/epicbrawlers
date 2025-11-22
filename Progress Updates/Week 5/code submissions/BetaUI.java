import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// main game window that holds everything together
public class BetaUI extends JFrame
{
	// keep track of what stage the player is on
	private int stage = 1;
	// make a new player
	private Player player = new Player();
	// create the first boss for stage 1
	private BaseBoss boss = createBossForStage(stage);

	// ui sections
	private BattlePanel battlePanel; // middle fight area
	private ControlPanel controlPanel; // bottom buttons
	private JTextArea logArea = new JTextArea(10, 36); // area that shows logs
	private JLabel lblStage = new JLabel("Stage 1", SwingConstants.CENTER); // top
																			// label
																			// for
																			// current
																			// stage

	// number formatter for gold
	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	// list of bosses that user has defeated
	private Set<String> defeatedBosses = new HashSet<>();

	// constructor builds the window
	public BetaUI()
	{
		// set the title of the window
		super("Epic Brawlers");
		// close the game when hitting X
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// make sure the window isn't too small
		setMinimumSize(new Dimension(1000, 640));

		// --- top: stage label ---
		lblStage.setFont(new Font("SansSerif", Font.BOLD, 20)); // make
																// it
																// big
																// and
																// bold
		JPanel top = new JPanel(new BorderLayout()); // create top panel
		top.add(lblStage, BorderLayout.CENTER); // put stage label in the middle

		// --- center: battle arena + log ---
		battlePanel = new BattlePanel(player, boss, this); // middle panel shows
															// fight
		JScrollPane logScroll = new JScrollPane(logArea); // scroll bar for log
		logArea.setEditable(false); // user can’t type in log

		JPanel centerWrap = new JPanel(new BorderLayout(10, 10)); // wrapper for
																	// middle
																	// layout
		centerWrap.add(battlePanel, BorderLayout.CENTER); // fight area in
															// center
		centerWrap.add(logScroll, BorderLayout.EAST); // logs on the right

		// --- bottom: controls ---
		controlPanel = new ControlPanel(this); // create button panel

		// set main layout of the frame
		getContentPane().setLayout(new BorderLayout(10, 10));
		getContentPane().add(top, BorderLayout.NORTH); // top = stage label
		getContentPane().add(centerWrap, BorderLayout.CENTER); // middle = fight
																// + logs
		getContentPane().add(controlPanel, BorderLayout.SOUTH); // bottom =
																// buttons

		// run setup methods
		appendHowToPlay(); // show how to play at the start
		refreshAll(); // update all ui elements
		pack(); // adjust window size automatically
		setLocationRelativeTo(null); // center window on screen
	}

	// === methods called from control panel buttons ===
	public void attackBoss()
	{
		battlePanel.handleAttack(); // run attack logic inside battle panel
	}

	public void usePotion()
	{
		battlePanel.handlePotion(); // try to use a potion
	}

	public void nextStage()
	{
		stage++; // go up one stage
		player.autoStageHPBump();
		player.healToFull(); // heal player to full before new boss
		boss = createBossForStage(stage); // create new boss for that stage
		battlePanel.setBoss(boss); // tell battle panel to update its boss

		// make sure this is a normal fight, not a rebattle
		battlePanel.setRebattleMode(false);

		refreshAll(); // update ui
		controlPanel.disableNextStage(); // lock next stage until new win
		log("Entering Stage " + stage); // add to log
	}

	public void openShop()
	{
		new Shop(this, player).setVisible(true);
	}

	// === helper / utility methods ===
	public void refreshAll()
	{
		lblStage.setText("Stage " + stage); // update top label
		battlePanel.refreshBars(); // update hp bars
		battlePanel.refreshStats(); // update stats labels
		controlPanel.setPotionButtonEnabled(player.getPotionCount() > 0);

	}

	public String[] getDefeatedBossArray()
	{
		return defeatedBosses.toArray(new String[0]);
	}

	public void markBossDefeated(String name)
	{
		defeatedBosses.add(name);
	}

	// unlock next stage button (called from battle panel)
	public void enableNextStage()
	{
		controlPanel.enableNextStage();
	}

	// restart everything after defeat
	public void resetGame()
	{
		stage = 1; // go back to stage 1
		player = new Player(); // reset player
		boss = createBossForStage(stage); // new boss for stage 1
		battlePanel.setPlayer(player); // send player to panel
		battlePanel.setBoss(boss); // send boss to panel
		refreshAll(); // refresh ui
		controlPanel.disableNextStage(); // lock next stage again
		log("---------------------------------------------");
		log("You have been defeated! Restarting Game."); // log message
	}

	// print messages to the right side log
	public void log(String msg)
	{
		logArea.append(msg + "\n"); // add message to log area
		logArea.setCaretPosition(logArea.getDocument().getLength()); // scroll
																		// to
																		// bottom
	}

	// show instructions when game starts
	private void appendHowToPlay()
	{
		log("Welcome to Epic Brawlers!");
		log("How to Play:");
		log(" • Click attack to damage the boss (weapon line shows dmg).");
		log(" • Each boss hits back. Every 3rd attack is a unique special move.");
		log(" • Use health potions to heal hp.");
		log(" • Bosses become more difficult as game progresses.");
		log(" • Defeat the boss to earn gold for upgrading weapons and stats.");
		log(" • Click next stage to continue.");
		log(" • Tip: Rebattling bosses allows you to earn gold for shop upgrades.");
		log("This is essential for being able to fight later bosses.");
		log(" • Enjoy!");
		log("---------------------------------------------");
	}

	// pick which boss appears for a given stage number
	private BaseBoss createBossForStage(int s)
	{
		if (s == 1) return new BattleBee(s); // easy starter boss
		if (s % 5 == 0) return new Dragon(s); // every 5th boss is a dragon

		// otherwise cycle through 3 boss types
		switch ((s - 1) % 3)
		{
			case 0:
				return new Ghoul(s);
			case 1:
				return new Mushroom(s);
			default:
				return new Slime(s);
		}
	}

	private int getBaseStageForBoss(String name)
	{
		return switch (name)
		{
			case "BattleBee" -> 1; // so if it's a battle bee, stage 1
			case "Ghoul" -> 2; // ghoul bosses start at stage 2
			case "Mushroom" -> 3; // mushroom bosses tied to stage 3
			case "Slime" -> 4; // slime bosses tied to stage 4
			default -> 1; // fallback just in case
		};
	}

	// handles rebattling
	public void replayBoss(String bossName)
	{
		int baseStage = getBaseStageForBoss(bossName); // figure out what stage
														// that boss originally
														// belongs to

		// create the right boss instance based on name
		BaseBoss rebattleBoss = switch (bossName)
		{
			case "BattleBee" -> new BattleBee(baseStage);
			case "Ghoul" -> new Ghoul(baseStage);
			case "Mushroom" -> new Mushroom(baseStage);
			case "Slime" -> new Slime(baseStage);
			default -> new BattleBee(baseStage); // default fallback again
		};

		boss = rebattleBoss; // set new rebattle boss
		battlePanel.setBoss(boss); // update battle panel so ui refreshes

		battlePanel.setRebattleMode(true); // flip flag so battle logic knows
											// it's a rebattle

		refreshAll(); // update ui (hp bars, labels, etc)
		log("---------------------------------------------");
		log("Re-battling " + bossName + " for more gold!"); // log for player to
															// see
	}

	public void saveGame()
	{
		try
		{
			// all player + game data into savedata object
			SaveData data = new SaveData(stage, player.getGold(),
					player.getPotionCount(), player.getWeapon().getName(),
					player.getWeapon().getLevel(), player.getMaxHP(),
					player.getHP());

			// save file called "save.dat"
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("save.dat"));
			out.writeObject(data); // write the whole object in one go
			out.close(); // close the save stream

			log("Game saved successfully!"); // log the success of the save
		}
		catch (Exception e)
		{
			log("Error saving game."); // if file cannot be written then log
										// error
		}
	}

	public void loadGame()
	{
		try
		{
			// open up save.dat to read in player progress
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream("save.dat"));
			SaveData data = (SaveData) in.readObject(); // cast saved object
														// correctly
			in.close(); // close stream

			// === RESTORE PLAYER STUFF ===
			stage = data.stage; // bring stage back
			player.setGold(data.gold); // restore gold amount
			player.setPotionCount(data.potions); // restore potions
			player.setMaxHPDirect(data.maxHP); // set max hp
			player.setHP(data.currentHP); // restore current hp

			// recreate the right weapon from its name
			BaseWeapon newWeapon = switch (data.weaponName)
			{
				case "Stick" -> new Stick();
				case "Bow" -> new Bow();
				case "Sword" -> new Sword();
				case "Magic Staff" -> new MagicStaff();
				default -> new Stick(); // fallback
			};

			newWeapon.setLevel(data.weaponLevel); // restore weapon level
			player.setWeapon(newWeapon); // give player the weapon

			// === RESTORE BOSS ===
			boss = createBossForStage(stage); // create fresh boss at same stage
			battlePanel.setBoss(boss); // update ui boss
			battlePanel.setPlayer(player); // update ui player reference

			refreshAll(); // refresh all labels + hp bars
			log("Game loaded successfully!"); // tell player that load worked
		}
		catch (Exception e)
		{
			log("No save file found."); // if file isn't there
		}
	}

	public void saveGameToSlot(int slot)
	{
		try
		{
			// same savedata object as before but save to a numbered slot
			SaveData data = new SaveData(stage, player.getGold(),
					player.getPotionCount(), player.getWeapon().getName(),
					player.getWeapon().getLevel(), player.getMaxHP(),
					player.getHP());

			// dynamic filename depending on slot chosen
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("save" + slot + ".dat"));

			out.writeObject(data); // save everything
			out.close(); // close stream

			log("Saved to Slot " + slot + "!"); // confirm in log
		}
		catch (Exception e)
		{
			log("Error saving to Slot " + slot); // error in log
		}
	}

	public void loadGameFromSlot(int slot)
	{
		try
		{
			// option selected slot file (save1.dat or save2.dat)
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream("save" + slot + ".dat"));

			SaveData data = (SaveData) in.readObject(); // read saved object
			in.close(); // close stream after reading

			// restore stage + player stats
			stage = data.stage;
			player.setGold(data.gold);
			player.setPotionCount(data.potions);
			player.setMaxHPDirect(data.maxHP);
			player.setHP(data.currentHP);

			// recreate weapon
			BaseWeapon newWeapon = switch (data.weaponName)
			{
				case "Stick" -> new Stick();
				case "Bow" -> new Bow();
				case "Sword" -> new Sword();
				case "Magic Staff" -> new MagicStaff();
				default -> new Stick(); // fallback/default weapon
			};

			newWeapon.setLevel(data.weaponLevel); // restore weapon level
			player.setWeapon(newWeapon);

			// recreate boss
			boss = createBossForStage(stage);
			battlePanel.setBoss(boss); // update ui reference
			battlePanel.setPlayer(player);

			refreshAll(); // update hp bars / stats
			log("Loaded Slot " + slot + "!"); // log success
		}
		catch (Exception e)
		{
			log("No save data found in Slot " + slot); // slot empty or
														// corrupted
		}
	}

	private String getSlotDescription(int slot)
	{
		java.io.File f = new java.io.File("save" + slot + ".dat"); // check slot
																	// file

		if (!f.exists()) return "Slot " + slot + " (Empty)"; // nothing saved
																// yet

		try (ObjectInputStream in = new ObjectInputStream(
				new FileInputStream(f)))
		{
			SaveData d = (SaveData) in.readObject(); // try reading savedata

			// return a formatted string:
			// "Slot 1 - Stage 6, Sword Lv2"
			return "Slot " + slot + " – Stage " + d.stage + ", " + d.weaponName
					+ " Lv" + d.weaponLevel;
		}
		catch (Exception e)
		{
			return "Slot " + slot + " (Corrupted)"; // error reading the file
		}
	}

	public void showSaveMenu()
	{
		String[] options = { "Save to Slot 1", "Save to Slot 2", "Cancel" }; // options
																				// shown
																				// in
																				// the
																				// popup
																				// menu

		int choice = JOptionPane.showOptionDialog(this, "Choose a save slot:", // title
																				// text
				"Save Game", // popup window title
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]); // default choice pre-selected

		if (choice == 0) saveGameToSlot(1); // user picked slot 1
		else if (choice == 1) saveGameToSlot(2); // user picked slot 2
	}

	public void showLoadMenu()
	{
		String slot1 = getSlotDescription(1); // get info or "empty"
		String slot2 = getSlotDescription(2); // same for slot 2

		String[] options = { slot1, slot2, "Cancel" }; // display as readable
														// menu

		int choice = JOptionPane.showOptionDialog(this,
				"Choose a save to load:", "Load Game",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]);

		// only load if slot contains valid save; check with the word "stage"
		if (choice == 0 && slot1.contains("Stage")) loadGameFromSlot(1);
		else if (choice == 1 && slot2.contains("Stage")) loadGameFromSlot(2);
	}

	// entry point to launch the whole game
	public static void main(String[] args)
	{
		// make sure ui runs safely on the swing thread
		SwingUtilities.invokeLater(() -> new BetaUI().setVisible(true));
	}
}