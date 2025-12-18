/**
 * dragon boss class.
 * represents a late-game boss with very high health, strong attacks,
 * passive damage reduction, and a powerful special move.
 */
public class Dragon extends BaseBoss
{
	// dragon takes less damage overall (its tough scales reduce hits)
	private static final double DAMAGE_TAKEN = 0.80; // only takes 80% of
														// incoming dmg

	/**
	 * constructs a dragon boss for a given stage.
	 *
	 * @param stage the stage this dragon appears on
	 */
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

	/**
	 * applies damage to the dragon, accounting for armor reduction
	 * and dragon slayer potion effects.
	 *
	 * @param dmg the incoming damage
	 * @param player the player dealing the damage
	 */
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

	/**
	 * performs the dragon's special attack.
	 * inferno breath deals triple base attack damage.
	 *
	 * @return damage dealt by the special attack
	 */
	@Override
	// inferno breath — massive fire blast that hits 3x harder
	protected long specialAttack()
	{
		return Math.round(getBaseAttack() * 3.0);
	}
}