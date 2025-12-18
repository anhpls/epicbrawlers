/**
 * slime boss class.
 * represents a steadily scaling boss with consistent growth
 * and a high-risk, high-damage special attack.
 */
public class Slime extends BaseBoss
{
	/**
	 * constructs a slime boss for a given stage.
	 *
	 * @param stage the stage this slime appears on
	 */
	public Slime(int stage)
	{
		// slime scales smoother but has consistent growth
		super(stage, (long) Math.round(220 * Math.pow(1.25, stage - 1)), // hp
																			// scales
																			// exponentially
				(int) Math.round(10 * Math.pow(1.17, stage - 1)), // attack
																	// scales
																	// moderately
				40 * stage); // gold reward increases linearly
	}

	/**
	 * performs the slime's special attack.
	 * triple slam deals heavy damage but also causes self-damage.
	 *
	 * @return damage dealt by the special attack
	 */
	@Override
	// "triple slam" — hits 3x harder but damages itself
	protected long specialAttack()
	{
		long dmg = (long) getBaseAttack() * 3; // big triple slam hit
		selfDamage(Math.round(getMaxHP() * 0.10)); // hurts itself by 10%
													// of max hp
		return dmg; // send damage back
	}
}