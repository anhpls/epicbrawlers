/**
 * a simple early-game boss used as an introductory enemy.
 * battlebee has low damage and slight health scaling.
 */
public class BattleBee extends BaseBoss
{
	// helper: an easy first fight with very low damage
	/**
	 * constructs a battlebee boss for a given stage.
	 *
	 * @param stage the stage this boss appears on
	 */
	public BattleBee(int stage)
	{

		super(stage, // remember what stage this bee is from
				100L + (stage * 15L), // modest hp that grows slowly each
										// stage
				3 + stage, // very low base attack (barely hurts)
				20 + stage * 30 // small gold reward so early game feels light
		);
	}

	/**
	 * performs the battlebee's special attack.
	 * triggers every third attack and deals slightly increased damage
	 * with a small self-damage recoil.
	 *
	 * @return damage dealt by the special attack
	 */
	@Override
	protected long specialAttack()
	{
		// "sting jab" — a small spike every 3rd hit
		int dmg = (int) Math.round(getBaseAttack() * 1.5); // 1.5x damage, still
															// mild
		selfDamage((int) Math.round(getMaxHP() * 0.05)); // tiny recoil (5% max
															// hp) for flavor
		return dmg; // return the actual damage dealt
	}
}