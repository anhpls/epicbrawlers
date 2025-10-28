```java
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class QuickBattleUI extends JFrame
{
	// --- game state (no separate model/controller) ---
	private int stage = 1;
	private final Player player = new Player();
	private BaseBoss boss = createBossForStage(stage);

	// number formatter for gold (commas)
	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	// --- ui pieces ---
	private final JLabel lblStage = new JLabel("Stage 1",
			SwingConstants.CENTER);

	// player side (stacked stats)
	private final JPanel playerStatsCol = new JPanel();
	private final JLabel lblWeaponLine = new JLabel("", SwingConstants.LEFT);
	private final JLabel lblPotionsLine = new JLabel("", SwingConstants.LEFT);
	private final JLabel lblGoldLine = new JLabel("", SwingConstants.LEFT);

	private final JProgressBar pbPlayerHP = new JProgressBar(0, 100);
	private final JLabel lblPlayerIcon = new JLabel("🙂",
			SwingConstants.CENTER);

	// boss side (name + special stacked)
	private final JPanel bossHeader = new JPanel();
	private final JLabel lblBossTitle = new JLabel("Boss: ?",
			SwingConstants.LEFT);
	private final JLabel lblBossSpecial = new JLabel("Special: —",
			SwingConstants.LEFT);
	private final JProgressBar pbBossHP = new JProgressBar(0, 100);
	private final JLabel lblBossIcon = new JLabel("👾", SwingConstants.CENTER);

	// hit flash (overlayed in the center)
	private final JLabel lblHit = new JLabel("💥", SwingConstants.CENTER);

	// log + controls
	private final JTextArea logArea = new JTextArea(10, 36);
	private final JButton btnAttack = new JButton("Attack");
	private final JButton btnUsePotion = new JButton("Use Potion");
	private final JButton btnNextStage = new JButton("Next Stage");
	private final JButton btnOpenShop = new JButton("Open Shop");

	public QuickBattleUI()
	{
		super("Epic Brawlers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1000, 640));

		// ----- top: stage only -----
		JPanel top = new JPanel(new BorderLayout());
		lblStage.setFont(lblStage.getFont().deriveFont(Font.BOLD, 20f));
		top.add(lblStage, BorderLayout.CENTER);

		// ===== center arena (player left vs boss right) =====
		JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));

		// --- player column ---
		JPanel playerCol = new JPanel(new BorderLayout(8, 8));

		// stacked stats (emoji lines) — left aligned, vertical
		playerStatsCol
				.setLayout(new BoxLayout(playerStatsCol, BoxLayout.Y_AXIS));
		leftAlign(playerStatsCol, lblWeaponLine, lblPotionsLine, lblGoldLine);

		lblWeaponLine.setFont(lblWeaponLine.getFont().deriveFont(14f));
		lblPotionsLine.setFont(lblPotionsLine.getFont().deriveFont(14f));
		lblGoldLine.setFont(lblGoldLine.getFont().deriveFont(14f));

		// player hp bar (thicker + green)
		styleHPBar(pbPlayerHP);

		JPanel playerTop = new JPanel(new BorderLayout(6, 6));
		playerTop.add(pad(playerStatsCol, 0, 0, 6, 0), BorderLayout.NORTH);
		playerTop.add(pbPlayerHP, BorderLayout.SOUTH);

		playerCol.add(playerTop, BorderLayout.NORTH);
		lblPlayerIcon.setPreferredSize(new Dimension(360, 280));
		lblPlayerIcon.setFont(lblPlayerIcon.getFont().deriveFont(72f));
		playerCol.add(lblPlayerIcon, BorderLayout.CENTER);

		// --- boss column ---
		JPanel bossCol = new JPanel(new BorderLayout(8, 8));

		// name + special stacked — left aligned, vertical
		bossHeader.setLayout(new BoxLayout(bossHeader, BoxLayout.Y_AXIS));
		leftAlign(bossHeader, lblBossTitle, lblBossSpecial);

		lblBossTitle.setFont(lblBossTitle.getFont().deriveFont(Font.BOLD, 16f));
		lblBossSpecial.setFont(lblBossSpecial.getFont().deriveFont(13f));

		// boss hp bar (thicker + green)
		styleHPBar(pbBossHP);

		JPanel bossTop = new JPanel(new BorderLayout(6, 6));
		bossTop.add(pad(bossHeader, 0, 0, 6, 0), BorderLayout.NORTH);
		bossTop.add(pbBossHP, BorderLayout.SOUTH);

		bossCol.add(bossTop, BorderLayout.NORTH);
		lblBossIcon.setPreferredSize(new Dimension(360, 280));
		lblBossIcon.setFont(lblBossIcon.getFont().deriveFont(72f));
		bossCol.add(lblBossIcon, BorderLayout.CENTER);

		arena.add(playerCol);
		arena.add(bossCol);

		// ----- overlay stack (arena + hit flash centered) -----
		JPanel arenaStack = new JPanel();
		arenaStack.setLayout(new OverlayLayout(arenaStack));

		JPanel hitLayer = new JPanel(new GridBagLayout());
		hitLayer.setOpaque(false);
		lblHit.setFont(lblHit.getFont().deriveFont(108f));
		lblHit.setVisible(false);
		hitLayer.add(lblHit);

		arenaStack.add(hitLayer); // on top
		arenaStack.add(arena); // underneath

		// ----- right: log -----
		logArea.setEditable(false);
		JScrollPane logScroll = new JScrollPane(logArea);

		// ----- bottom: controls -----
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
		controls.add(btnAttack);
		controls.add(btnUsePotion);
		controls.add(btnNextStage);
		controls.add(btnOpenShop);

		// ----- root layout -----
		JPanel centerWrap = new JPanel(new BorderLayout(10, 10));
		centerWrap.add(arenaStack, BorderLayout.CENTER);
		centerWrap.add(logScroll, BorderLayout.EAST);

		getContentPane().setLayout(new BorderLayout(10, 10));
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(centerWrap, BorderLayout.CENTER);
		getContentPane().add(controls, BorderLayout.SOUTH);

		// wire buttons
		btnAttack.addActionListener(e -> onAttack());
		btnUsePotion.addActionListener(e -> onUsePotion());
		btnNextStage.addActionListener(e -> onNextStage());
		btnOpenShop.addActionListener(e -> onOpenShop());

		// init ui
		btnNextStage.setEnabled(false); // locked until boss dies
		appendHowToPlay(); // instructions at start
		refreshAll();
		pack();
		setLocationRelativeTo(null);
	}

	// --- actions ---
	private void onAttack()
	{
		if (!player.isAlive())
		{
			log("you are down! retry the stage.");
			return;
		}
		if (!boss.isAlive())
		{
			log("boss already defeated. hit next stage when ready.");
			return;
		}

		int pd = player.attackDamage();
		boss.takeDamage(pd);
		flashHit("💥");
		log("you hit the " + bossName() + " for " + pd + " damage 💥");
		refreshHPBars();

		if (!boss.isAlive())
		{
			int g = boss.getRewardGold();
			player.addGold(g);
			log("boss defeated! +" + NF.format(g) + " gold 🎉");
			btnNextStage.setEnabled(true);
			updateButtons();
			refreshTop();
			return;
		}

		int bd = boss.dealDamage();
		player.takeDamage(bd);
		flashHit("⚔️");
		log(bossName() + " hits you for " + bd + " ⚔️");
		refreshHPBars();

		if (!player.isAlive())
		{
			updateButtons();
			showDeathDialog();
		}
	}

	private void onUsePotion()
	{
		if (!player.isAlive())
		{
			log("you can't use a potion while down.");
			return;
		}
		if (player.usePotionInBattle())
		{
			log("potion used! ❤️ restored health.");
			refreshAll();
		}
		else
		{
			log("no health potions available.");
		}
	}

	private void onNextStage()
	{
		if (boss.isAlive())
		{
			log("you must defeat the boss before advancing.");
			btnNextStage.setEnabled(false);
			return;
		}

		stage++;
		int inc = Math.max(1, (int) Math.round(player.getMaxHP() * 0.05));
		player.increaseMaxHP(inc);
		player.healToFull();

		boss = createBossForStage(stage);
		log("entering stage " + stage + "...");
		btnNextStage.setEnabled(false);
		refreshAll();
	}

	private void onOpenShop()
	{
		JOptionPane.showMessageDialog(this,
				"shop coming soon.\nfor now, defeat bosses and collect gold.",
				"shop", JOptionPane.INFORMATION_MESSAGE);
	}

	// --- death + retry ---
	private void showDeathDialog()
	{
		String[] options = { "Retry Stage", "Cancel" };
		int choice = JOptionPane.showOptionDialog(this,
				"you died.\nretry the current stage?", "defeated",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);
		if (choice == 0) retryStage();
	}

	private void retryStage()
	{
		player.healToFull();
		boss.healToFull();
		log("retrying stage " + stage + "...");
		refreshAll();
	}

	// --- helpers ---
	private void flashHit(String emoji)
	{
		lblHit.setText(emoji);
		lblHit.setVisible(true);
		Timer t = new Timer(240, e -> lblHit.setVisible(false));
		t.setRepeats(false);
		t.start();
	}

	private void styleHPBar(JProgressBar bar)
	{
		// force basic ui so foreground color shows on all plafs
		bar.setUI(new BasicProgressBarUI());
		bar.setStringPainted(true);
		bar.setForeground(new Color(46, 204, 64)); // green fill
		bar.setBackground(new Color(30, 30, 30)); // dark track
		bar.setPreferredSize(new Dimension(0, 28)); // thicker bar
		bar.setBorder(BorderFactory.createLineBorder(new Color(20, 20, 20)));
	}

	private void refreshAll()
	{
		refreshTop();
		refreshHPBars();
		updateButtons();
	}

	private void updateButtons()
	{
		boolean playerAlive = player.isAlive();
		boolean bossAlive = boss.isAlive();

		btnAttack.setEnabled(playerAlive && bossAlive);
		btnUsePotion.setEnabled(playerAlive && player.getPotionCount() > 0);
		btnNextStage.setEnabled(!bossAlive);
	}

	private void refreshTop()
	{
		lblStage.setText("Stage " + stage);
		lblBossTitle.setText("Boss: " + bossName());
		lblBossSpecial.setText("Special: " + getBossSpecialText(boss));

		// player stats — use HTML so long text renders cleanly; align left
		int dmg = player.getWeapon().getDamage();
		lblWeaponLine.setText(html(
				"⚔️  <b>Weapon:</b> " + player.getWeapon().getName() + " (Lv "
						+ player.getWeapon().getLevel() + ") — DMG " + dmg));
		lblPotionsLine.setText(
				html("🧪  <b>Health Potions:</b> x" + player.getPotionCount()));
		lblGoldLine.setText(
				html("🪙  <b>Gold:</b> " + NF.format(player.getGold())));

		// re-add (first time only) to ensure correct sizing and alignment
		if (playerStatsCol.getComponentCount() == 0
				|| playerStatsCol.getComponent(0) != lblWeaponLine)
		{
			playerStatsCol.removeAll();
			playerStatsCol.add(lblWeaponLine);
			playerStatsCol.add(Box.createVerticalStrut(2));
			playerStatsCol.add(lblPotionsLine);
			playerStatsCol.add(Box.createVerticalStrut(2));
			playerStatsCol.add(lblGoldLine);
		}

		if (bossHeader.getComponentCount() == 0
				|| bossHeader.getComponent(0) != lblBossTitle)
		{
			bossHeader.removeAll();
			bossHeader.add(lblBossTitle);
			bossHeader.add(Box.createVerticalStrut(2));
			bossHeader.add(lblBossSpecial);
		}
	}

	private void refreshHPBars()
	{
		pbPlayerHP.setMaximum(player.getMaxHP());
		pbPlayerHP.setValue(player.getHP());
		pbPlayerHP.setString(player.getHP() + " / " + player.getMaxHP());

		pbBossHP.setMaximum(boss.getMaxHP());
		pbBossHP.setValue(boss.getHP());
		pbBossHP.setString(boss.getHP() + " / " + boss.getMaxHP());
	}

	private static void leftAlign(JPanel parent, JComponent... comps)
	{
		parent.setAlignmentX(Component.LEFT_ALIGNMENT);
		for (JComponent c : comps)
		{
			c.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
	}

	private static JComponent pad(JComponent c, int top, int left, int bottom,
			int right)
	{
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		p.add(c, BorderLayout.CENTER);
		return p;
	}

	private static String html(String s)
	{
		return "<html>" + s + "</html>";
	}

	private String bossName()
	{
		return boss.getClass().getSimpleName();
	}

	private String getBossSpecialText(BaseBoss b)
	{
		String n = b.getClass().getSimpleName();
		switch (n)
		{
			case "Ghoul":
				return "Life Drain — deals +50% dmg; heals 20% of damage every 3rd hit";
			case "Mushroom":
				return "Spore Burst — deals 2× damage every 3rd hit";
			case "Slime":
				return "Triple Slam — 3× damage; self-damage 10% max HP every 3rd hit";
			case "Dragon":
				return "Inferno Breath — 3× damage every 3rd hit (passive: takes only 80% dmg)";
			default:
				return "—";
		}
	}

	private void appendHowToPlay()
	{
		log("welcome to epic brawlers!");
		log("how to play:");
		log(" • click Attack to damage the boss (weapon line shows exact dmg).");
		log(" • the boss hits back; every 3rd attack is a special move.");
		log(" • use Health Potions to heal 40% max HP.");
		log(" • defeat the boss to earn gold, then click Next Stage.");
		log(" • shop is coming soon for upgrades.");
		log("---------------------------------------------");
	}

	private void log(String msg)
	{
		logArea.append(msg + "\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	// simple inline boss creation (keeps this file standalone)
	private BaseBoss createBossForStage(int s)
	{
		if (s % 5 == 0) return new Dragon(s);
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

	// entry point
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new QuickBattleUI().setVisible(true));
	}
}
```

![image-20251027053753462](/Users/anhpls/Library/Application Support/typora-user-images/image-20251027053753462.png)





```
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class QuickBattleUI extends JFrame
{
	// --- game state (no separate model/controller) ---
	private int stage = 1;
	private final Player player = new Player();
	private BaseBoss boss = createBossForStage(stage);

	// --- ui pieces ---
	private final JLabel lblStage = new JLabel("Stage 1",
			SwingConstants.CENTER);

	// player side
	private final JLabel lblPlayerStats = new JLabel(
			"Gold: 0 | Weapon: Stick | Pots: 0", SwingConstants.CENTER);
	private final JProgressBar pbPlayerHP = new JProgressBar(0, 100);
	private final JLabel lblPlayerIcon = new JLabel("🙂",
			SwingConstants.CENTER);

	// boss side
	private final JLabel lblBossTitle = new JLabel("Boss: ?",
			SwingConstants.CENTER);
	private final JProgressBar pbBossHP = new JProgressBar(0, 100);
	private final JLabel lblBossIcon = new JLabel("👾", SwingConstants.CENTER);

	// log + controls
	private final JTextArea logArea = new JTextArea(10, 36);
	private final JButton btnAttack = new JButton("Attack");
	private final JButton btnUsePotion = new JButton("Use Potion");
	private final JButton btnNextStage = new JButton("Next Stage");
	private final JButton btnOpenShop = new JButton("Open Shop");

	public QuickBattleUI()
	{
		super("Epic Brawlers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(920, 580));

		// ----- top: stage only -----
		JPanel top = new JPanel(new BorderLayout());
		lblStage.setFont(lblStage.getFont().deriveFont(Font.BOLD, 18f));
		top.add(lblStage, BorderLayout.CENTER);

		// ----- center: arena (player left vs boss right) -----
		JPanel arena = new JPanel(new GridLayout(1, 2, 14, 0));

		// player column: stats -> hp -> image
		JPanel playerCol = new JPanel(new BorderLayout(6, 6));
		JPanel playerTop = new JPanel(new GridLayout(2, 1, 4, 4));
		playerTop.add(lblPlayerStats);
		pbPlayerHP.setStringPainted(true);
		playerTop.add(pbPlayerHP);
		playerCol.add(playerTop, BorderLayout.NORTH);
		lblPlayerIcon.setPreferredSize(new Dimension(300, 260));
		playerCol.add(lblPlayerIcon, BorderLayout.CENTER);

		// boss column: title -> hp -> image
		JPanel bossCol = new JPanel(new BorderLayout(6, 6));
		JPanel bossTop = new JPanel(new GridLayout(2, 1, 4, 4));
		bossTop.add(lblBossTitle);
		pbBossHP.setStringPainted(true);
		bossTop.add(pbBossHP);
		bossCol.add(bossTop, BorderLayout.NORTH);
		lblBossIcon.setPreferredSize(new Dimension(300, 260));
		bossCol.add(lblBossIcon, BorderLayout.CENTER);

		arena.add(playerCol);
		arena.add(bossCol);

		// ----- right: log -----
		logArea.setEditable(false);
		JScrollPane logScroll = new JScrollPane(logArea);

		// ----- bottom: controls -----
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		controls.add(btnAttack);
		controls.add(btnUsePotion);
		controls.add(btnNextStage);
		controls.add(btnOpenShop);

		// ----- root layout -----
		JPanel centerWrap = new JPanel(new BorderLayout(10, 10));
		centerWrap.add(arena, BorderLayout.CENTER);
		centerWrap.add(logScroll, BorderLayout.EAST);

		getContentPane().setLayout(new BorderLayout(10, 10));
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(centerWrap, BorderLayout.CENTER);
		getContentPane().add(controls, BorderLayout.SOUTH);

		// wire buttons
		btnAttack.addActionListener(e -> onAttack());
		btnUsePotion.addActionListener(e -> onUsePotion());
		btnNextStage.addActionListener(e -> onNextStage());
		btnOpenShop.addActionListener(e -> onOpenShop());

		// init ui
		refreshAll();
		pack();
		setLocationRelativeTo(null);
	}

	// --- actions ---
	private void onAttack()
	{
		if (!player.isAlive())
		{
			log("you are down! try a potion or advance after gearing up.");
			return;
		}
		if (!boss.isAlive())
		{
			log("boss already defeated. hit next stage when ready.");
			return;
		}

		// player -> boss
		int pd = player.attackDamage();
		boss.takeDamage(pd);
		log("you hit the " + bossName() + " for " + pd + " damage 💥");
		refreshHPBars();

		if (!boss.isAlive())
		{
			int g = boss.getRewardGold();
			player.addGold(g);
			log("boss defeated! +" + g + " gold 🎉");
			btnNextStage.setEnabled(true);
			refreshTop();
			return;
		}

		// boss -> player (special handled in dealDamage)
		int bd = boss.dealDamage();
		player.takeDamage(bd);
		log(bossName() + " hits you for " + bd + " ⚔️");
		refreshHPBars();

		if (!player.isAlive())
		{
			log("you were defeated... consider buying max hp or potions later.");
		}
	}

	private void onUsePotion()
	{
		if (player.usePotionInBattle())
		{
			log("potion used! ❤️ restored health.");
			refreshAll();
		}
		else
		{
			log("no potions available.");
		}
	}

	private void onNextStage()
	{
		stage++;
		// light autoscaling: small hp growth, heal to full
		int inc = Math.max(1, (int) Math.round(player.getMaxHP() * 0.05));
		player.increaseMaxHP(inc);
		player.healToFull();

		boss = createBossForStage(stage);
		log("entering stage " + stage + "...");
		btnNextStage.setEnabled(false);
		refreshAll();
	}

	private void onOpenShop()
	{
		JOptionPane.showMessageDialog(this,
				"shop coming soon.\nfor now, use gold to buy later upgrades.",
				"shop", JOptionPane.INFORMATION_MESSAGE);
	}

	// --- helpers ---
	private void refreshAll()
	{
		refreshTop();
		refreshHPBars();
	}

	private void refreshTop()
	{
		lblStage.setText("Stage " + stage);
		lblBossTitle.setText("Boss: " + bossName());
		lblPlayerStats.setText("Gold: " + player.getGold() + " | Weapon: "
				+ player.getWeapon().getName() + " | Pots: "
				+ player.getPotionCount());
	}

	private void refreshHPBars()
	{
		pbPlayerHP.setMaximum(player.getMaxHP());
		pbPlayerHP.setValue(player.getHP());
		pbPlayerHP.setString(player.getHP() + " / " + player.getMaxHP());

		pbBossHP.setMaximum(boss.getMaxHP());
		pbBossHP.setValue(boss.getHP());
		pbBossHP.setString(boss.getHP() + " / " + boss.getMaxHP());
	}

	private String bossName()
	{
		return boss.getClass().getSimpleName();
	}

	private void log(String msg)
	{
		logArea.append(msg + "\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	// simple inline boss creation (keeps this file standalone)
	private BaseBoss createBossForStage(int s)
	{
		if (s % 5 == 0) return new Dragon(s);
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

	// entry point
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new QuickBattleUI().setVisible(true));
	}
}
```

![image-20251027053846614](/Users/anhpls/Library/Application Support/typora-user-images/image-20251027053846614.png)





# **BEST SO FAR**

QuickBattleUI.java

```
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class QuickBattleUI extends JFrame
{
	// --- game state ---
	private int stage = 1;
	private Player player = new Player(); // mutable for hard restart
	private BaseBoss boss = createBossForStage(stage);

	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	// --- ui pieces ---
	private final JLabel lblStage = new JLabel("Stage 1",
			SwingConstants.CENTER);

	// player side
	private final JProgressBar pbPlayerHP = new JProgressBar(0, 100);
	private final JLabel lblPlayerIcon = new JLabel("🙂 Player",
			SwingConstants.CENTER);
	private final JPanel playerStatsCol = new JPanel();
	private final JLabel lblWeaponLine = new JLabel("", SwingConstants.LEFT);
	private final JLabel lblPotionsLine = new JLabel("", SwingConstants.LEFT);
	private final JLabel lblGoldLine = new JLabel("", SwingConstants.LEFT);

	// boss side
	private final JProgressBar pbBossHP = new JProgressBar(0, 100);
	private final JLabel lblBossIcon = new JLabel("👾 Boss",
			SwingConstants.CENTER);
	private final JPanel bossStatsCol = new JPanel();
	private final JLabel lblBossTitle = new JLabel("Boss: ?",
			SwingConstants.LEFT);
	private final JLabel lblBossSpecial = new JLabel("Special: —",
			SwingConstants.LEFT);

	// hit flash (overlayed in the center)
	private final JLabel lblHit = new JLabel("💥", SwingConstants.CENTER);

	// log + controls
	private final JTextArea logArea = new JTextArea(10, 36);
	private final JButton btnAttack = new JButton("Attack");
	private final JButton btnUsePotion = new JButton("Use Potion");
	private final JButton btnNextStage = new JButton("Next Stage"); // <-
																	// restored
	private final JButton btnOpenShop = new JButton("Open Shop"); // <- restored

	public QuickBattleUI()
	{
		super("Epic Brawlers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1000, 640));

		// ----- top: stage only -----
		JPanel top = new JPanel(new BorderLayout());
		lblStage.setFont(lblStage.getFont().deriveFont(Font.BOLD, 20f));
		top.add(lblStage, BorderLayout.CENTER);

		// ===== center arena (player left vs boss right) =====
		JPanel arena = new JPanel(new GridLayout(1, 2, 20, 0));

		// --- player column (NORTH bar, CENTER image, SOUTH stats) ---
		JPanel playerCol = new JPanel(new BorderLayout(8, 8));
		styleHPBar(pbPlayerHP);
		playerCol.add(pbPlayerHP, BorderLayout.NORTH);

		lblPlayerIcon.setPreferredSize(new Dimension(360, 280));
		lblPlayerIcon.setFont(lblPlayerIcon.getFont().deriveFont(72f));
		playerCol.add(wrapImage(lblPlayerIcon, new Color(230, 230, 230)),
				BorderLayout.CENTER);

		playerStatsCol
				.setLayout(new BoxLayout(playerStatsCol, BoxLayout.Y_AXIS));
		leftAlign(playerStatsCol, lblWeaponLine, lblPotionsLine, lblGoldLine);
		lblWeaponLine.setFont(lblWeaponLine.getFont().deriveFont(14f));
		lblPotionsLine.setFont(lblPotionsLine.getFont().deriveFont(14f));
		lblGoldLine.setFont(lblGoldLine.getFont().deriveFont(14f));
		JPanel playerStatsWrap = pad(playerStatsCol, 4, 6, 0, 6);
		playerCol.add(playerStatsWrap, BorderLayout.SOUTH);

		// --- boss column (NORTH bar, CENTER image, SOUTH stats) ---
		JPanel bossCol = new JPanel(new BorderLayout(8, 8));
		styleHPBar(pbBossHP);
		bossCol.add(pbBossHP, BorderLayout.NORTH);

		lblBossIcon.setPreferredSize(new Dimension(360, 280));
		lblBossIcon.setFont(lblBossIcon.getFont().deriveFont(72f));
		bossCol.add(wrapImage(lblBossIcon, new Color(245, 220, 220)),
				BorderLayout.CENTER);

		bossStatsCol.setLayout(new BoxLayout(bossStatsCol, BoxLayout.Y_AXIS));
		leftAlign(bossStatsCol, lblBossTitle, lblBossSpecial);
		lblBossTitle.setFont(lblBossTitle.getFont().deriveFont(Font.BOLD, 16f));
		lblBossSpecial.setFont(lblBossSpecial.getFont().deriveFont(13f));
		JPanel bossStatsWrap = pad(bossStatsCol, 4, 6, 0, 6);
		bossCol.add(bossStatsWrap, BorderLayout.SOUTH);

		arena.add(playerCol);
		arena.add(bossCol);

		// ----- overlay stack (arena + hit flash centered) -----
		JPanel arenaStack = new JPanel();
		arenaStack.setLayout(new OverlayLayout(arenaStack));

		JPanel hitLayer = new JPanel(new GridBagLayout());
		hitLayer.setOpaque(false);
		lblHit.setFont(lblHit.getFont().deriveFont(108f));
		lblHit.setVisible(false);
		hitLayer.add(lblHit);

		arenaStack.add(hitLayer); // on top
		arenaStack.add(arena); // underneath

		// ----- right: log -----
		logArea.setEditable(false);
		JScrollPane logScroll = new JScrollPane(logArea);

		// ----- bottom: controls (restored Next Stage + Open Shop) -----
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
		controls.add(btnAttack);
		controls.add(btnUsePotion);
		controls.add(btnNextStage); // restored
		controls.add(btnOpenShop); // restored

		// ----- root layout -----
		JPanel centerWrap = new JPanel(new BorderLayout(10, 10));
		centerWrap.add(arenaStack, BorderLayout.CENTER);
		centerWrap.add(logScroll, BorderLayout.EAST);

		getContentPane().setLayout(new BorderLayout(10, 10));
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(centerWrap, BorderLayout.CENTER);
		getContentPane().add(controls, BorderLayout.SOUTH);

		// wire buttons
		btnAttack.addActionListener(e -> onAttack());
		btnUsePotion.addActionListener(e -> onUsePotion());
		btnNextStage.addActionListener(e -> onNextStage()); // restored
		btnOpenShop.addActionListener(e -> onOpenShop()); // restored

		// init ui
		btnNextStage.setEnabled(false); // only enabled after a kill
		appendHowToPlay();
		refreshAll();
		pack();
		setLocationRelativeTo(null);
	}

	// --- actions ---
	private void onAttack()
	{
		if (!player.isAlive())
		{
			log("you are down!");
			handlePlayerDeath(); // hard reset
			return;
		}
		if (!boss.isAlive())
		{
			log("Boss already defeated. You can click Next Stage ");
			return;
		}

		// player -> boss
		int pd = player.attackDamage();
		boss.takeDamage(pd);
		flashHit("💥");
		log("you hit the " + bossName() + " for " + pd + " damage 💥");
		refreshHPBars();

		if (!boss.isAlive())
		{
			int g = boss.getRewardGold();
			player.addGold(g);
			log("Boss Defeated! +" + NF.format(g) + " Gold 🎉");
			refreshTop();
			btnNextStage.setEnabled(true); // enable the bottom button
			showVictoryPopup(g); // also offer popup option
			return;
		}

		// boss -> player
		int bd = boss.dealDamage();
		player.takeDamage(bd);
		flashHit("⚔️");
		log(bossName() + " hits you for " + bd + " ⚔️");
		refreshHPBars();

		if (!player.isAlive())
		{
			handlePlayerDeath(); // hard reset
		}
	}

	private void onUsePotion()
	{
		if (!player.isAlive())
		{
			log("You can't use a potion while down.");
			handlePlayerDeath();
			return;
		}
		if (player.usePotionInBattle())
		{
			log("Potion used! ❤️ Restored health.");
			refreshAll();
		}
		else
		{
			log("no health potions available.");
		}
	}

	private void onNextStage()
	{
		if (boss.isAlive())
		{
			log("You must defeat the boss before advancing.");
			btnNextStage.setEnabled(false);
			return;
		}
		advanceToNextStage();
	}

	private void onOpenShop()
	{
		JOptionPane.showMessageDialog(this,
				"Shop coming soon.\nfor now, defeat bosses and collect gold.",
				"Shop", JOptionPane.INFORMATION_MESSAGE);
	}

	// --- victory popup with Next Stage button (kept) ---
	private void showVictoryPopup(int goldEarned)
	{
		String msg = "<html><center>" + "<h2>🎉 Victory!</h2>"
				+ "<div>You defeated the " + bossName() + ".</div>"
				+ "<div style='margin-top:6px;'>Reward: <b>"
				+ NF.format(goldEarned) + " Gold</b></div>"
				+ "</center></html>";

		String[] options = { "Next Stage", "Stay" };
		int choice = JOptionPane.showOptionDialog(this, msg, "Congrats",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);

		if (choice == 0)
		{
			advanceToNextStage();
		}
		// if "Stay", bottom Next Stage remains enabled
	}

	private void advanceToNextStage()
	{
		stage++;
		// small auto-scaling for survivability
		int inc = Math.max(1, (int) Math.round(player.getMaxHP() * 0.05));
		player.increaseMaxHP(inc);
		player.healToFull();

		boss = createBossForStage(stage);
		log("Entering stage " + stage + "...");
		refreshAll();
		btnNextStage.setEnabled(false); // lock again until next kill
	}

	// --- death => hard reset ---
	private void handlePlayerDeath()
	{
		JOptionPane.showMessageDialog(this,
				"You died.\nRestarting from Stage 1.", "Defeated",
				JOptionPane.WARNING_MESSAGE);
		resetGame();
	}

	private void resetGame()
	{
		stage = 1;
		player = new Player(); // fresh player
		boss = createBossForStage(stage); // fresh boss

		logArea.setText(""); // clear battle log
		appendHowToPlay(); // show help again

		refreshAll();
		btnNextStage.setEnabled(false);
	}

	// --- helpers ---
	private void flashHit(String emoji)
	{
		lblHit.setText(emoji);
		lblHit.setVisible(true);
		Timer t = new Timer(240, e -> lblHit.setVisible(false));
		t.setRepeats(false);
		t.start();
	}

	private void styleHPBar(JProgressBar bar)
	{
		bar.setUI(new BasicProgressBarUI());
		bar.setStringPainted(true);
		bar.setForeground(new Color(46, 204, 64)); // green fill
		bar.setBackground(new Color(30, 30, 30)); // dark track
		bar.setPreferredSize(new Dimension(0, 28)); // thicker
		bar.setBorder(BorderFactory.createLineBorder(new Color(20, 20, 20)));
	}

	private JPanel wrapImage(JLabel iconLabel, Color bg)
	{
		JPanel p = new JPanel(new BorderLayout());
		iconLabel.setOpaque(true);
		iconLabel.setBackground(bg);
		iconLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		p.add(iconLabel, BorderLayout.CENTER);
		return p;
	}

	private void refreshAll()
	{
		refreshTop();
		refreshHPBars();
		updateButtons();
	}

	private void updateButtons()
	{
		boolean playerAlive = player.isAlive();
		boolean bossAlive = boss.isAlive();

		btnAttack.setEnabled(playerAlive && bossAlive);
		btnUsePotion.setEnabled(playerAlive && player.getPotionCount() > 0);
		btnNextStage.setEnabled(!bossAlive); // stays enabled if they chose
												// "Stay"
		btnOpenShop.setEnabled(true);
	}

	private void refreshTop()
	{
		lblStage.setText("Stage " + stage);
		lblBossTitle.setText("Boss: " + bossName());
		lblBossSpecial.setText("Special: " + getBossSpecialText(boss));

		int dmg = player.getWeapon().getDamage();
		lblWeaponLine.setText(nowrap(
				"⚔️  <b>Weapon:</b> " + player.getWeapon().getName() + " (Lv "
						+ player.getWeapon().getLevel() + ") — DMG " + dmg));
		lblPotionsLine.setText(nowrap(
				"🧪  <b>Health Potions:</b> x" + player.getPotionCount()));
		lblGoldLine.setText(
				nowrap("🪙  <b>Gold:</b> " + NF.format(player.getGold())));

		// build stacks first time
		if (playerStatsCol.getComponentCount() == 0)
		{
			playerStatsCol.add(lblWeaponLine);
			playerStatsCol.add(Box.createVerticalStrut(2));
			playerStatsCol.add(lblPotionsLine);
			playerStatsCol.add(Box.createVerticalStrut(2));
			playerStatsCol.add(lblGoldLine);
		}
		if (bossStatsCol.getComponentCount() == 0)
		{
			bossStatsCol.add(lblBossTitle);
			bossStatsCol.add(Box.createVerticalStrut(2));
			bossStatsCol.add(lblBossSpecial);
		}
	}

	private void refreshHPBars()
	{
		pbPlayerHP.setMaximum(player.getMaxHP());
		pbPlayerHP.setValue(player.getHP());
		pbPlayerHP.setString(player.getHP() + " / " + player.getMaxHP());

		pbBossHP.setMaximum(boss.getMaxHP());
		pbBossHP.setValue(boss.getHP());
		pbBossHP.setString(boss.getHP() + " / " + boss.getMaxHP());
	}

	private static void leftAlign(JPanel parent, JComponent... comps)
	{
		parent.setAlignmentX(Component.LEFT_ALIGNMENT);
		for (JComponent c : comps) c.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	private static JPanel pad(JPanel c, int top, int left, int bottom,
			int right)
	{
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		p.add(c, BorderLayout.CENTER);
		return p;
	}

	private static String nowrap(String s)
	{
		return "<html><span style='white-space:nowrap;'>" + s
				+ "</span></html>";
	}

	private String bossName()
	{
		return boss.getClass().getSimpleName();
	}

	private String getBossSpecialText(BaseBoss b)
	{
		String n = b.getClass().getSimpleName();
		switch (n)
		{
			case "BattleBee":
				return "Sting Jab — 1.5× dmg every 3rd hit; tiny self-dmg (5% max HP)";
			case "Ghoul":
				return "Life Drain — deals +50% dmg; heals 20% of damage every 3rd hit";
			case "Mushroom":
				return "Spore Burst — deals 2× damage every 3rd hit";
			case "Slime":
				return "Triple Slam — 3× damage; self-damage 10% max HP every 3rd hit";
			case "Dragon":
				return "Inferno Breath — 3× damage every 3rd hit (passive: takes only 80% dmg)";
			default:
				return "—";
		}
	}

	private void appendHowToPlay()
	{
		log("welcome to epic brawlers!");
		log("how to play:");
		log(" • click Attack to damage the boss (weapon line shows exact dmg).");
		log(" • the boss hits back; every 3rd attack is a special move.");
		log(" • use Health Potions to heal 40% max HP.");
		log(" • defeat the boss to earn gold, then choose Next Stage in the popup or the bottom button.");
		log("---------------------------------------------");
	}

	private void log(String msg)
	{
		logArea.append(msg + "\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	// simple inline boss creation (keeps this file standalone)
	private BaseBoss createBossForStage(int s)
	{
		// gentle, safe first fight if you added BattleBee.java
		if (s == 1) return new BattleBee(s);

		if (s % 5 == 0) return new Dragon(s);
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

	// entry point
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new QuickBattleUI().setVisible(true));
	}
}
```

