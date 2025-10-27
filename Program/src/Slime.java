public class Slime extends BaseBoss
{
	public Slime(int stage)
	{
		// slime scales smoother but has consistent growth
		super(stage, (int) Math.round(220 * Math.pow(1.25, stage - 1)), // hp
																		// scales
																		// exponentially
				(int) Math.round(10 * Math.pow(1.17, stage - 1)), // attack
																	// scales
																	// moderately
				40 * stage); // gold reward increases linearly
	}

	@Override
	// "triple slam" — hits 3x harder but damages itself
	protected int specialAttack()
	{
		int dmg = getBaseAttack() * 3; // big triple slam hit
		selfDamage((int) Math.round(getMaxHP() * 0.10)); // hurts itself by 10%
															// of max hp
		return dmg; // send damage back
	}
}