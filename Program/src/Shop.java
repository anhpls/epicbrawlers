import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Shop extends JDialog
{

	private Player player; // store player so stats can be modified
	private BetaUI ui; // reference back to main ui so refresh can happen

	// all shop buttons
	private JButton btnBuyPotion;
	private JButton btnLevelUpWeapon;
	private JButton btnUpgradeWeaponTier;
	private JButton btnIncreaseHP;
	private JButton btnClose;

	// formatting gold
	private static final NumberFormat NF = NumberFormat.getIntegerInstance();

	public Shop(BetaUI ui, Player player)
	{
		super(ui, "Shop", true); // modal popup shop so user can't click behind
									// it
		this.ui = ui; // store ui
		this.player = player; // store player

		setLayout(new BorderLayout(30, 30)); // spacing around edges
		setSize(620, 420); // window size
		setLocationRelativeTo(ui); // center on the main ui window

		// title at top
		JLabel title = new JLabel("Buy Upgrades", SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 18));

		// creating buttons
		btnBuyPotion = new JButton();
		btnLevelUpWeapon = new JButton();
		btnUpgradeWeaponTier = new JButton();
		btnIncreaseHP = new JButton();
		btnClose = new JButton("Close");

		// linking buttons to functions
		btnBuyPotion.addActionListener(e -> buyPotion());
		btnLevelUpWeapon.addActionListener(e -> levelUpWeapon());
		btnUpgradeWeaponTier.addActionListener(e -> upgradeWeaponTier());
		btnIncreaseHP.addActionListener(e -> upgradeMaxHP());
		btnClose.addActionListener(e -> dispose()); // close shop

		// putting buttons in a list
		JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
		panel.add(btnBuyPotion);
		panel.add(btnLevelUpWeapon);
		panel.add(btnUpgradeWeaponTier);
		panel.add(btnIncreaseHP);
		panel.add(btnClose);

		// title and button panel to shop
		add(title, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);

		// update all prices + button states when shop opens
		refreshShop();
	}

	// updates all button text and enabled/disabled states
	public void refreshShop()
	{
		int potionCost = 25; // cost to buy potion
		int hpCost = 40; // cost to boost hp

		// potion button text + enable/disable
		btnBuyPotion.setText("Buy Potion (" + potionCost + "g)");
		btnBuyPotion.setIcon(loadIcon("images/shop/potion.png", 32));
		btnBuyPotion.setEnabled(player.getGold() >= potionCost);

		// weapon level up info
		BaseWeapon weapon = player.getWeapon(); // get their weapon
		int levelUpCost = weapon.getUpgradeCost(); // cost scales per level

		btnLevelUpWeapon.setText("Level Up " + weapon.getName() + " to LV "
				+ (weapon.getLevel() + 1) + " (" + levelUpCost + "g)");
		btnLevelUpWeapon.setIcon(loadIcon("images/shop/weapon.png", 32));
		btnLevelUpWeapon.setEnabled(player.getGold() >= levelUpCost);

		// weapon tier upgrade
		String nextTier = getNextWeaponTier(); // next weapon
		int nextTierCost = getNextTierCost(); // how much next weapon costs

		if (nextTier == null) // if already on best weapon
		{
			btnUpgradeWeaponTier.setText("Weapon Tier Maxed"); // display text
			btnUpgradeWeaponTier.setIcon(loadIcon("images/shop/max.png", 32));
			btnUpgradeWeaponTier.setEnabled(false); // can't click on button if
													// maxed
		}
		else
		{
			btnUpgradeWeaponTier.setText("Upgrade Weapon → " + nextTier + " ("
					+ nextTierCost + "g)"); // set upgrade weapon text to show
											// next weapon + cost
			btnUpgradeWeaponTier
					.setIcon(loadIcon("images/shop/weapon.png", 32)); // weapon
																		// upgrade
																		// icon
			btnUpgradeWeaponTier.setEnabled(player.getGold() >= nextTierCost); // clickable
																				// if
																				// enough
																				// gold
		}

		// hp upgrade button text + enable
		btnIncreaseHP.setText("Increase Max HP (40g)");
		btnIncreaseHP.setIcon(loadIcon("images/shop/hp.png", 32));
		btnIncreaseHP.setEnabled(player.getGold() >= hpCost);
	}

	// speicific icon sizes
	private javax.swing.ImageIcon loadIcon(String path, int size)
	{
		java.awt.Image img = new javax.swing.ImageIcon(path).getImage(); // load
																			// normal
																			// img
		java.awt.Image scaled = img.getScaledInstance(size, size,
				java.awt.Image.SCALE_SMOOTH); // then shrink
		return new javax.swing.ImageIcon(scaled); // return shrunk icon
	}

	// weapon tier order
	private String getNextWeaponTier()
	{
		return switch (player.getWeapon().getName())
		{
			case "Stick" -> "Bow"; // if stick then bow is next
			case "Bow" -> "Sword"; // bow -> sword
			case "Sword" -> "Magic Staff"; // sword -> staff
			default -> null; // magic staff = maxed
		};
	}

	// prices for upgrading to the next weapon tier
	private int getNextTierCost()
	{
		return switch (String.valueOf(getNextWeaponTier()))
		{
			case "Bow" -> 500;
			case "Sword" -> 1200;
			case "Magic Staff" -> 2000;
			default -> -1;
		};
	}

	// buying a potion
	private void buyPotion()
	{
		int cost = 25;

		if (!player.spendGold(cost)) // if broke then cannot buy
		{
			ui.log("Not enough gold to buy potion.");
			return;
		}

		player.addPotions(1); // give player the potion
		ui.log("Bought a potion!"); // log

		ui.refreshAll(); // refresh the gameplay ui
		refreshShop(); // update prices / buttons in shop window
	}

	// level up current weapon
	private void levelUpWeapon()
	{
		BaseWeapon weapon = player.getWeapon(); // get current weapon
		int cost = weapon.getUpgradeCost(); // cost to level up

		if (!player.spendGold(cost))
		{
			ui.log("Not enough gold to level up weapon.");
			return;
		}

		weapon.upgrade(); // increase weapon level
		ui.log("Your " + weapon.getName() + " leveled up!");

		ui.refreshAll(); // refresh gameplay
		refreshShop(); // refresh shop
	}

	// upgrade to the next weapon tier
	private void upgradeWeaponTier()
	{
		String next = getNextWeaponTier(); // what is the next weapon
		int cost = getNextTierCost(); // cost of the upgrade

		if (next == null || cost < 0) return;

		if (!player.spendGold(cost)) // not enough gold then no upgrade
		{
			ui.log("Not enough gold to upgrade weapon tier.");
			return;
		}

		// swap to the new weapon class
		BaseWeapon newW = switch (next)
		{
			case "Bow" -> new Bow();
			case "Sword" -> new Sword();
			case "Magic Staff" -> new MagicStaff();
			default -> null;
		};

		player.setWeapon(newW); // equip new weapon
		ui.log("Weapon upgraded to " + next + "!");

		ui.refreshAll(); // refresh gameplay ui
		refreshShop(); // refresh shop items
	}

	// permanently upgrade player's max hp
	private void upgradeMaxHP()
	{
		int cost = 40;

		if (!player.spendGold(cost)) // not enough gold
		{
			ui.log("Not enough gold.");
			return;
		}

		player.increaseMaxHP(40); // adds 40 max hp
		ui.log("Max HP increased!");

		ui.refreshAll(); // update hp bar
		refreshShop(); // refresh the gold + buttons
	}
}