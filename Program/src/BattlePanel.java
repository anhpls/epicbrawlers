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

// this panel handles all battle visuals and combat logic
public class BattlePanel extends JPanel
{
	private Player player; // current player
	private BaseBoss boss; // current boss
	private BetaUI ui; // main ui

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

	// number formatter for gold
	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	// constructor for battle panel
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

	// builds a character panel with hp bar + icon
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

	// style for hp bars
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

	// vertical list of player stats
	private JPanel createPlayerStats()
	{
		JPanel stats = new JPanel(); // panel for stats
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS)); // vertical
		stats.add(lblWeapon); // weapon info
		stats.add(lblPotions); // potion count
		stats.add(lblGold); // gold
		return stats; // return panel
	}

	// vertical list of boss stats
	private JPanel createBossStats()
	{
		JPanel stats = new JPanel(); // panel for stats
		stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS)); // vertical
		stats.add(lblBossTitle); // boss name
		stats.add(lblBossSpecial); // boss special attack description
		return stats; // return panel
	}

	// popup for when player wins a battle
	private void showVictoryPopup(int goldEarned)
	{
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

	// shows popup when player loses
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

	// handles logic when attack button is pressed
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
		boss.takeDamage(dmg); // subtract from boss hp
		flashHit("💥"); // show hit emoji
		ui.log("You hit the boss for " + dmg + " 💥"); // log message
		refreshBars(); // update hp display

		// check if boss is dead
		if (!boss.isAlive())
		{
			int gold = boss.getRewardGold(); // gold reward
			player.addGold(gold); // add gold
			ui.log("Boss defeated! +" + NF.format(gold) + " G"); // log
			ui.enableNextStage(); // unlock next button
			refreshStats(); // update stats
			showVictoryPopup(gold); // show win popup
			return; // done
		}

		// boss attacks back
		int bd = boss.dealDamage(); // boss damage
		player.takeDamage(bd); // player loses hp

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

	// handles potion button click
	public void handlePotion()
	{
		if (!player.isAlive())
		{
			ui.log("You can’t use a potion while down."); // can't heal if dead
			return;
		}

		if (player.usePotionInBattle()) // if potion exists
		{
			ui.log("Potion used! ❤️ restored health."); // log heal
			refreshBars(); // update hp bar
		}
		else
		{
			ui.log("No health potions left."); // log fail
		}
	}

	// shows quick emoji animation when hit happens
	private void flashHit(String emoji)
	{
		lblHit.setText(emoji); // set emoji
		lblHit.setVisible(true); // show it
		Timer t = new Timer(240, e -> lblHit.setVisible(false)); // hide soon
		t.setRepeats(false); // only once
		t.start(); // start timer
	}

	// update the current boss
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

	// update the current player
	public void setPlayer(Player newPlayer)
	{
		this.player = newPlayer; // assign new player
		refreshBars(); // refresh health
		refreshStats(); // refresh info
	}

	// update both hp bars to match live data
	public void refreshBars()
	{
		pbPlayerHP.setMaximum(player.getMaxHP()); // max hp for player
		pbPlayerHP.setValue(player.getHP()); // current hp for player
		pbPlayerHP.setString(player.getHP() + "/" + player.getMaxHP()); // text

		pbBossHP.setMaximum(boss.getMaxHP()); // max hp for boss
		pbBossHP.setValue(boss.getHP()); // current hp for boss
		pbBossHP.setString(boss.getHP() + "/" + boss.getMaxHP()); // text
	}

	// update stats labels
	public void refreshStats()
	{
		lblWeapon.setText(" Weapon: " + player.getWeapon().getName() + " (Lv "
				+ player.getWeapon().getLevel() + ") dmg "
				+ player.getWeapon().getDamage()); // weapon info
		lblPotions.setText(" Potions: x" + player.getPotionCount()); // potions
		lblGold.setText(" Gold: " + NF.format(player.getGold())); // gold
		lblBossTitle.setText(" Boss: " + boss.getClass().getSimpleName()); // name
		lblBossSpecial.setText(" Special: " + getBossSpecialText()); // special
																		// move
	}

	// resize images
	private javax.swing.ImageIcon loadScaledIcon(String path, int width,
			int height)
	{
		java.awt.Image img = new javax.swing.ImageIcon(path).getImage();
		java.awt.Image scaled = img.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		return new javax.swing.ImageIcon(scaled);
	}

	// get different image for each boss
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

	// returns short text describing boss's special ability
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