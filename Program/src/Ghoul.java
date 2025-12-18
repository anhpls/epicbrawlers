/**
 * ghoul boss class.
 * represents a mid-game boss that can heal itself through life drain attacks.
 */
public class Ghoul extends BaseBoss
{
	/**
	 * constructs a ghoul boss for a given stage.
	 *
	 * @param stage the stage this ghoul appears on
	 */
	public Ghoul(int stage)
	{
		// call the base class with scaling values for hp, attack, and gold
		super(stage, 150L + (stage * 25L), // decent hp that grows per
											// stage
				12 + (stage * 3), // attack gets stronger slowly
				25 + (stage * 70)); // gold reward increases too
	}

	/**
	 * performs the ghoul's special attack.
	 * drain life deals increased damage and heals the ghoul
	 * for a portion of the damage dealt.
	 *
	 * @return damage dealt by the special attack
	 */
	@Override
	protected long specialAttack()
	{
		// "drain life" — deals 1.5x dmg and heals a bit from it
		// 50% stronger hit
		long damage = Math.round(getBaseAttack() * 1.5);
		// heals for 20% of that damage
		long healAmount = Math.round(damage * 0.2);
		heal((int) healAmount); // restore some hp
		return damage; // send back damage dealt
	}
}