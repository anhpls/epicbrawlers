import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// this panel holds all the bottom buttons (attack, potion, next, shop,
// rebattle)
public class ControlPanel extends JPanel
{
	// buttons that need to be accessed later
	private final JButton btnNext;
	private final JButton btnPotion;

	// constructor builds the row of buttons
	public ControlPanel(BetaUI ui)
	{
		// simple flow layout to line up buttons in the center
		setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));

		// create each button
		JButton btnAttack = new JButton("Attack");
		btnPotion = new JButton("Use Potion");
		btnNext = new JButton("Next Stage");
		JButton btnShop = new JButton("Open Shop");
		JButton btnRebattle = new JButton("Re-Battle Boss"); // 👈 added here

		// add them all to the panel
		add(btnAttack);
		add(btnPotion);
		add(btnNext);
		add(btnShop);
		add(btnRebattle);

		// connect each button to main UI actions
		btnAttack.addActionListener(e -> ui.attackBoss());
		btnPotion.addActionListener(e -> ui.usePotion());
		btnNext.addActionListener(e -> ui.nextStage());
		btnShop.addActionListener(e -> ui.openShop());
		btnRebattle.addActionListener(e -> {
			String[] bosses = { "BattleBee", "Mushroom", "Ghoul", "Slime" };
			String chosen = (String) JOptionPane.showInputDialog(ui,
					"Choose a boss to re-battle:", "Re-Battle",
					JOptionPane.PLAIN_MESSAGE, null, bosses, bosses[0]);

			if (chosen != null) ui.replayBoss(chosen);
		});

		// next stage starts off locked until boss is defeated
		btnNext.setEnabled(false);

		// potion button starts disabled (player has none yet)
		btnPotion.setEnabled(false);
	}

	// enable the next stage button after winning
	public void enableNextStage()
	{
		btnNext.setEnabled(true);
	}

	// disable next stage again when starting a new round
	public void disableNextStage()
	{
		btnNext.setEnabled(false);
	}

	// used by the ui to toggle potion button based on potion count
	public void setPotionButtonEnabled(boolean enabled)
	{
		btnPotion.setEnabled(enabled);
	}
}