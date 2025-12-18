import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * panel that contains all main gameplay control buttons.
 * handles user input for combat actions, shop access, saving/loading,
 * and rebattling previously defeated bosses.
 */
public class ControlPanel extends JPanel
{
	// buttons that need to be accessed later
	private final JButton btnNext;
	private final JButton btnPotion;
	private final JButton btnDragonPotion; // dragon slayer potion button

	/**
	 * constructs the control panel and wires buttons to ui actions.
	 *
	 * @param ui reference to the main game ui
	 */
	public ControlPanel(BetaUI ui)
	{
		// flow layout to line up buttons in the center
		setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));

		// create each button
		JButton btnAttack = new JButton("Attack");
		btnPotion = new JButton("Use Potion");
		btnDragonPotion = new JButton("Use Dragon Slayer Potion");
		btnNext = new JButton("Next Stage");
		JButton btnShop = new JButton("Open Shop");
		JButton btnRebattle = new JButton("Re-Battle Boss");
		JButton btnSave = new JButton("Save Game");
		JButton btnLoad = new JButton("Load Game");

		// add them all to the panel
		add(btnAttack);
		add(btnPotion);
		add(btnDragonPotion);
		add(btnNext);
		add(btnShop);
		add(btnRebattle);
		add(btnSave);
		add(btnLoad);

		// connect each button to main UI actions
		btnAttack.addActionListener(e -> ui.attackBoss());
		btnPotion.addActionListener(e -> ui.usePotion());
		btnDragonPotion.addActionListener(e -> ui.useDragonPotion());
		btnNext.addActionListener(e -> ui.nextStage());
		btnShop.addActionListener(e -> ui.openShop());
		btnSave.addActionListener(e -> ui.showSaveMenu());
		btnLoad.addActionListener(e -> ui.showLoadMenu());
		btnRebattle.addActionListener(e -> {
			// ask UI for list of defeated bosses
			String[] defeated = ui.getDefeatedBossArray();

			if (defeated.length == 0)
			{
				JOptionPane.showMessageDialog(ui,
						"You haven't defeated any bosses yet!", "Re-Battle",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			String chosen = (String) JOptionPane.showInputDialog(ui,
					"Choose a boss to re-battle:", "Re-Battle",
					JOptionPane.PLAIN_MESSAGE, null, defeated, defeated[0]);

			if (chosen != null)
			{
				ui.replayBoss(chosen);
			}
		});

		// next stage starts off locked until boss is defeated
		btnNext.setEnabled(false);

		// potion button starts disabled (player has none yet)
		btnPotion.setEnabled(false);

		btnDragonPotion.setEnabled(false); // no dragon potions at start

	}

	/**
	 * enables the next stage button after a boss is defeated.
	 */
	public void enableNextStage()
	{
		btnNext.setEnabled(true);
	}

	/**
	 * disables the next stage button when starting a new stage.
	 */
	public void disableNextStage()
	{
		btnNext.setEnabled(false);
	}

	/**
	 * toggles the potion button based on player potion count.
	 *
	 * @param enabled true if potions are available
	 */
	public void setPotionButtonEnabled(boolean enabled)
	{
		btnPotion.setEnabled(enabled);
	}

	/**
	 * toggles the dragon slayer potion button based on potion count.
	 *
	 * @param enabled true if dragon potions are available
	 */
	public void setDragonPotionButtonEnabled(boolean enabled)
	{
		btnDragonPotion.setEnabled(enabled);
	}
}