public class Dragon extends BaseBoss
{
	// dragon takes less damage overall (its tough scales reduce hits)
	private static final double DAMAGE_TAKEN = 0.80; // only takes 80% of
														// incoming dmg

	public Dragon(int stage)
	{
		// call the BaseBoss constructor with custom scaling values
		super(stage, (long) Math.round(600 * Math.pow(1.35, stage - 1)), // super
																			// high
																			// hp
																			// that
																			// grows
																			// fast
				(int) Math.round(35 * Math.pow(1.25, stage - 1)), // strong
																	// attack
																	// that
																	// scales
																	// too
				200 * stage); // gives a large gold reward when defeated
	}

	public void takeDamage(int dmg, Player player)
	{
		// if dragon potion is active, ignore armor
		if (player.consumeDragonPotionHit())
		{
			super.takeDamage(dmg); // full damage, no reduction
			return;
		}

		// apply passive dmg reduction before calling base class method
		int reduced = (int) Math.round(dmg * DAMAGE_TAKEN); // cut dmg to 80%
		super.takeDamage(reduced); // actually subtract hp
	}

	@Override
	// inferno breath — massive fire blast that hits 3x harder
	protected long specialAttack()
	{
		return Math.round(getBaseAttack() * 3.0);
	}
}