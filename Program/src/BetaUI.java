import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;

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
		player.healToFull(); // heal player to full before new boss
		boss = createBossForStage(stage); // create new boss for that stage
		battlePanel.setBoss(boss); // tell battle panel to update its boss
		refreshAll(); // update ui
		controlPanel.disableNextStage(); // lock next stage until new win
		log("Entering Stage " + stage); // add to log
	}

	public void openShop()
	{
		// popup message for shop placeholder
		JOptionPane.showMessageDialog(this,
				"Shop coming soon.\nFor now, defeat bosses and collect gold.",
				"Shop", JOptionPane.INFORMATION_MESSAGE);
	}

	// === helper / utility methods ===
	public void refreshAll()
	{
		lblStage.setText("Stage " + stage); // update top label
		battlePanel.refreshBars(); // update hp bars
		battlePanel.refreshStats(); // update stats labels
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

	public void replayBoss(String bossName)
	{
		BaseBoss rebattleBoss = switch (bossName)
		{
			case "BattleBee" -> new BattleBee(stage); // same scaling
			case "Ghoul" -> new Ghoul(stage);
			case "Mushroom" -> new Mushroom(stage);
			case "Slime" -> new Slime(stage);
			default -> new BattleBee(stage); // fallback
		};

		boss = rebattleBoss;
		battlePanel.setBoss(boss);
		refreshAll();
		log("---------------------------------------------");
		log("Re-battling " + bossName + " for more gold!");
	}

	// entry point to launch the whole game
	public static void main(String[] args)
	{
		// make sure ui runs safely on the swing thread
		SwingUtilities.invokeLater(() -> new BetaUI().setVisible(true));
	}
}