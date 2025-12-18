import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * panel responsible for rendering the battle screen and handling combat logic.
 * manages player/boss visuals, hp bars, attack flow, and battle popups.
 */
public class BattlePanel extends JPanel
{
	private Player player; // current player
	private BaseBoss boss; // current boss
	private BetaUI ui; // main ui

	// rebattle boss mode toggle
	private boolean rebattleMode = false;

	// health bars
	private JProgressBar pbPlayerHP = new JProgressBar(0, 100);
	private JProgressBar pbBossHP = new JProgressBar(0, 100);

	// labels for player and boss icons
	private JLabel lblPlayer = new JLabel();
	private JLabel lblBoss = new JLabel();

	// label for hit effect emoji
	private JLabel lblHit = new JLabel("💥", SwingConstants.CENTER);

	// stat labels shown under player and boss
	private JLabel lblWeapon = new JLabel();
	private JLabel lblPotions = new JLabel();
	private JLabel lblGold = new JLabel();
	private JLabel lblBossTitle = new JLabel();
	private JLabel lblBossSpecial = new JLabel();

	// dragon slayer potion UI
	private JLabel lblDragonPotion = new JLabel(); // shows dragon slayer hits
	private JLabel lblDragonPotionCount = new JLabel(); // shows potion count

	// number formatter for gold
	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	/**
	 * constructs the battle panel with a player, boss, and ui reference.
	 *
	 * @param player the active player
	 * @param boss the active boss
	 * @param ui reference to the main ui controller
	 */
	public BattlePanel(Player player, BaseBoss boss, BetaUI ui)
	{
		this.player = player;
		this.boss = boss;
		this.ui = ui;

		setLayout(new OverlayLayout(this));
		JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));

		// player column
		JPanel playerCol = createCharacterPanel(lblPlayer, pbPlayerHP, null);
		JPanel playerStats = createPlayerStats();
		playerCol.add(playerStats, BorderLayout.SOUTH);

		// boss column
		JPanel bossCol = createCharacterPanel(lblBoss, pbBossHP, null);
		JPanel bossStats = createBossStats();
		bossCol.add(bossStats, BorderLayout.SOUTH);

		arena.add(playerCol);
		arena.add(bossCol);

		// set images for player and boss
		lblPlayer.setIcon(loadScaledIcon("images/player.png", 240, 240));
		lblBoss.setIcon(loadScaledIcon("images/"
				+ boss.getClass().getSimpleName().toLowerCase() + ".png", 240,
				240));

		// overlay hit effect
		lblHit.setFont(new Font("SansSerif", Font.BOLD, 108));
		lblHit.setVisible(false);
		JPanel hitLayer = new JPanel(new GridBagLayout());
		hitLayer.setOpaque(false);
		hitLayer.add(lblHit);

		add(hitLayer);
		add(arena);
	}

	/**
	 * builds a character column containing an hp bar and icon.
	 *
	 * @param icon the character icon label
	 * @param bar the health bar
	 * @param bg optional background color
	 * @return configured character panel
	 */
	private JPanel createCharacterPanel(JLabel icon, JProgressBar bar, Color bg)
	{
		JPanel col = new JPanel(new BorderLayout(8, 8)); // layout for each side
		styleHPBar(bar); // apply custom bar style
		icon.setFont(new Font("SansSerif", Font.BOLD, 72)); // big player/boss

		// text
		icon.setHorizontalAlignment(SwingConstants.CENTER);
		icon.setVerticalAlignment(SwingConstants.CENTER);
		icon.setPreferredSize(new Dimension(300, 250)); // give label space

		// to show the image
		col.add(bar, BorderLayout.NORTH); // hp bar on top
		col.add(icon, BorderLayout.CENTER); // icon in center
		return col; // return built column
	}

	/**
	 * applies consistent styling to an hp progress bar.
	 *
	 * @param bar the progress bar to style
	 */
	private void styleHPBar(JProgressBar bar)
	{
		bar.setUI(new BasicProgressBarUI()
		{
			protected Color getSelectionBackground()
			{
				return Color.BLACK; // text stays black even when bar fills
			}

			protected Color getSelectionForeground()
			{
				return Color.BLACK; // black when bar is empty
			}
		});

		bar.setStringPainted(true); // numbers on bar
		bar.setForeground(new Color(46, 204, 64)); // green fill color
		bar.setPreferredSize(new Dimension(0, 32)); // taller bar for numbers
		bar.setFont(new Font("SansSerif", Font.BOLD, 14)); // custom font
	}

	/**
	 * creates the vertical list of player stat labels.
	 *
	 * @return player stats panel
	 */
	private JPanel createPlayerStats()
	{
		JPanel stats = new JPanel(); // panel for stats
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS)); // vertical
																	// stack

		stats.add(lblWeapon); // weapon info
		stats.add(lblPotions); // health potion count
		stats.add(lblDragonPotionCount); // dragon potion count
		stats.add(lblDragonPotion); // dragon slayer hits
		stats.add(lblGold); // gold display

		return stats; // return finished panel
	}

	/**
	 * creates the vertical list of boss stat labels.
	 *
	 * @return boss stats panel
	 */
	private JPanel createBossStats()
	{
		JPanel stats = new JPanel(); // panel for stats
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS)); // vertical
		stats.add(lblBossTitle); // boss name
		stats.add(lblBossSpecial); // boss special attack description
		return stats; // return panel
	}

	/**
	 * shows a victory popup after defeating a boss.
	 *
	 * @param goldEarned gold awarded from the fight
	 */
	private void showVictoryPopup(int goldEarned)
	{

		ui.markBossDefeated(boss.getClass().getSimpleName());

		if (rebattleMode)
		{
			ui.log("Re-battle victory! Earned " + goldEarned + " gold.");
			rebattleMode = false;
			return;
		}

		String msg = "Victory!\n\nYou defeated the "
				+ boss.getClass().getSimpleName() + ".\nReward: " + goldEarned
				+ " Gold";

		String[] options = { "Next Stage", "Stay" }; // popup button options
		int choice = JOptionPane.showOptionDialog(this, msg, "Congrats",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]); // create dialog

		if (choice == 0)
		{
			ui.nextStage(); // move to next stage
		}
		// if player stays, they can click next stage manually
	}

	/**
	 * enables or disables rebattle mode.
	 *
	 * @param rebattleMode true if rebattle mode is active
	 */
	public void setRebattleMode(boolean rebattleMode)
	{
		this.rebattleMode = rebattleMode;
	}

	/**
	 * shows the defeat popup and resets the game.
	 */
	private void showDefeatPopup()
	{
		JOptionPane optionPane = new JOptionPane(
				"<html><center><h2>You have been defeated!</h2>"
						+ "<div>Restarting from Stage 1</div></center></html>",
				JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION); // defeat
																			// text

		JDialog dialog = optionPane.createDialog(this, "Defeated"); // make
																	// dialog
		dialog.setModal(true); // block game until closed

		// center the ok button in the dialog
		((JPanel) optionPane.getComponent(1))
				.setLayout(new FlowLayout(FlowLayout.CENTER));

		dialog.setVisible(true); // show popup
		ui.resetGame(); // restart game after closing
	}

	/**
	 * handles combat logic when the attack button is pressed.
	 */
	public void handleAttack()
	{
		if (!player.isAlive())
		{
			ui.log("You are down!"); // can't attack when dead
			return;
		}
		if (!boss.isAlive())
		{
			ui.log("Boss already defeated!"); // can't attack dead boss
			return;
		}

		// player attacks boss
		int dmg = player.attackDamage(); // get weapon damage
		if (boss instanceof Dragon)
		{
			((Dragon) boss).takeDamage(dmg, player);
		}
		else
		{
			boss.takeDamage(dmg);
		} // subtract from boss hp

		// if boss is dead then enable next stage button.
		if (!boss.isAlive())
		{
			ui.enableNextStage();
		}

		flashHit("💥"); // show hit emoji
		ui.log("You hit the boss for " + Utils.fmt(dmg) + " 💥"); // log message
		refreshBars(); // update hp display
		refreshStats();

		// check if boss is dead
		if (!boss.isAlive())
		{
			int gold = boss.getRewardGold();
			player.addGold(gold);
			ui.log("Boss defeated! +" + NF.format(gold) + " G");
			ui.markBossDefeated(boss.getClass().getSimpleName());
			refreshStats();

			if (!rebattleMode)
			{
				// normal boss fight
				ui.enableNextStage();
				showVictoryPopup(gold);
			}
			else
			{
				// rebattle fight — no popup, but allow normal gold + allow
				// progressing normally
				ui.log("Re-battle complete! You earned extra gold.");
				ui.enableNextStage();
				rebattleMode = false; // reset mode
			}

			return;
		}

		// boss attacks back
		long bd = boss.dealDamage(); // boss damage
		player.takeDamage((int) Math.min(Integer.MAX_VALUE, bd));

		// check if it was a special attack
		if (boss.wasSpecialAttack())
		{
			flashHit("🔥"); // fire effect
			ui.log("⚡ " + boss.getClass().getSimpleName()
					+ " used a special attack! (" + getBossSpecialText() + ")");
			ui.log("Dealt " + bd + " damage! ");
		}
		else
		{
			flashHit("⚔️"); // normal attack icon
			ui.log("Boss hits you for " + bd);
		}

		refreshBars(); // update both hp bars

		if (!player.isAlive())
		{
			ui.log("You have been defeated..."); // log defeat
			showDefeatPopup(); // show lose popup
		}
	}

	/**
	 * handles using a health potion during battle.
	 */
	public void handlePotion()
	{
		if (!player.isAlive())
		{
			ui.log("You can’t use a potion while down.");
			return;
		}

		// Case: potion not used
		if (!player.usePotionInBattle())
		{
			if (player.getHP() >= player.getMaxHP())
				ui.log("HP already full — potion not used.");
			else ui.log("No health potions left.");

			return; // stop here
		}

		// Case: potion was used
		ui.log("Potion used! ❤️ Restored health.");
		refreshBars();
		ui.refreshAll();
	}

	/**
	 * handles activating a dragon slayer potion.
	 */
	public void handleDragonPotion()
	{
		if (!player.isAlive()) // can’t use while dead
		{
			ui.log("You can’t use potions while down."); // feedback
			return;
		}

		if (!player.useDragonPotion()) // try to consume potion
		{
			ui.log("No Dragon Slayer potions left."); // failed
			return;
		}

		ui.log("🔥 Used Dragon Slayer Potion! (+10 hits)"); // success log
		refreshStats(); // update UI immediately
	}

	/**
	 * displays an emoji hit effect.
	 *
	 * @param emoji the emoji to display
	 */
	private void flashHit(String emoji)
	{
		lblHit.setText(emoji); // set emoji
		lblHit.setVisible(true); // show it
		Timer t = new Timer(240, e -> lblHit.setVisible(false)); // hide soon
		t.setRepeats(false); // only once
		t.start(); // start timer
	}

	/**
	 * updates the current boss and refreshes the ui.
	 *
	 * @param newBoss the new boss instance
	 */
	public void setBoss(BaseBoss newBoss)
	{
		this.boss = newBoss; // assign new boss
		lblBoss.setIcon(loadScaledIcon("images/"
				+ boss.getClass().getSimpleName().toLowerCase() + ".png", 240,
				240)); // update
		// image
		refreshBars(); // refresh health
		refreshStats(); // refresh info
	}

	/**
	 * updates the current player and refreshes the ui.
	 *
	 * @param newPlayer the new player instance
	 */
	public void setPlayer(Player newPlayer)
	{
		this.player = newPlayer; // assign new player
		refreshBars(); // refresh health
		refreshStats(); // refresh info
	}

	/**
	 * refreshes both hp bars to match current values.
	 */
	public void refreshBars()
	{
		// max hp for player
		pbPlayerHP.setMaximum(
				(int) Math.min(Integer.MAX_VALUE, player.getMaxHP()));
		// current hp for player
		pbPlayerHP.setValue((int) Math.min(Integer.MAX_VALUE, player.getHP()));
		// text
		pbPlayerHP.setString(
				Utils.fmt(player.getHP()) + "/" + Utils.fmt(player.getMaxHP()));

		// max hp for boss
		pbBossHP.setMaximum((int) Math.min(Integer.MAX_VALUE, boss.getMaxHP()));
		// current hp for boss
		pbBossHP.setValue((int) Math.min(Integer.MAX_VALUE, boss.getHP()));
		// text
		pbBossHP.setString(
				Utils.fmt(boss.getHP()) + "/" + Utils.fmt(boss.getMaxHP()));
	}

	/**
	 * refreshes all stat labels shown in the panel.
	 */
	public void refreshStats()
	{
		// show current weapon name, level, and damage
		lblWeapon.setText(" Weapon: " + player.getWeapon().getName() + " (Lv "
				+ player.getWeapon().getLevel() + ") dmg "
				+ Utils.fmt(player.getWeapon().getDamage()));

		// show how many health potions the player has
		lblPotions.setText(" Potions: x" + Utils.fmt(player.getPotionCount()));

		// show how many dragon slayer potions are in inventory
		lblDragonPotionCount.setText(
				" Dragon Slayer Potions: x" + player.getDragonPotionCount());

		// show how many armor-breaking hits are left
		lblDragonPotion.setText(
				" Dragon Slayer Hits: " + player.getDragonPotionHits());

		// display current gold amount
		lblGold.setText(" Gold: " + Utils.fmt(player.getGold()));

		// show the current boss name
		lblBossTitle.setText(" Boss: " + boss.getClass().getSimpleName());

		// show the boss's special ability description
		lblBossSpecial.setText(" Special: " + getBossSpecialText());
	}

	/**
	 * loads and scales an image icon.
	 *
	 * @param path image path
	 * @param width target width
	 * @param height target height
	 * @return scaled image icon
	 */
	private javax.swing.ImageIcon loadScaledIcon(String path, int width,
			int height)
	{
		java.awt.Image img = new javax.swing.ImageIcon(path).getImage();
		java.awt.Image scaled = img.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		return new javax.swing.ImageIcon(scaled);
	}

	/**
	 * returns a fallback icon for a given boss name.
	 *
	 * @param bossName the boss class name
	 * @return boss image icon
	 */
	private javax.swing.ImageIcon getBossIcon(String bossName)
	{
		switch (bossName)
		{
			case "BattleBee":
				return new javax.swing.ImageIcon("images/battlebee.png");
			case "Ghoul":
				return new javax.swing.ImageIcon("images/ghoul.png");
			case "Mushroom":
				return new javax.swing.ImageIcon("images/mushroom.png");
			case "Slime":
				return new javax.swing.ImageIcon("images/slime.png");
			case "Dragon":
				return new javax.swing.ImageIcon("images/dragon.png");
			default:
				return new javax.swing.ImageIcon("images/bee.png"); // fallback
		}
	}

	/**
	 * returns a short description of the boss's special attack.
	 *
	 * @return special attack description text
	 */
	private String getBossSpecialText()
	{
		String n = boss.getClass().getSimpleName(); // boss type
		switch (n)
		{
			case "BattleBee":
				return "Sting jab — 1.5× dmg every 3rd hit; small self dmg";
			case "Ghoul":
				return "Life drain — heals 20% of dmg every 3rd hit";
			case "Mushroom":
				return "Spore burst — 2× dmg every 3rd hit";
			case "Slime":
				return "Triple slam — 3× dmg every 3rd hit";
			case "Dragon":
				return "Inferno breath — 3× dmg, takes 80% dmg";
			default:
				return "—"; // default placeholder
		}
	}
}