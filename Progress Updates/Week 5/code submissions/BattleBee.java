public class BattleBee extends BaseBoss
{
	// tiny helper: an easy first fight with very low damage
	public BattleBee(int stage)
	{

		super(stage, // remember what stage this bee is from
				100 + (stage * 15), // modest hp that grows slowly each stage
				3 + stage, // very low base attack (barely hurts)
				20 + (stage * 30) // small gold reward so early game feels light
		);
	}

	@Override
	protected int specialAttack()
	{
		// "sting jab" — a small spike every 3rd hit
		int dmg = (int) Math.round(getBaseAttack() * 1.5); // 1.5x damage, still
															// mild
		selfDamage((int) Math.round(getMaxHP() * 0.05)); // tiny recoil (5% max
															// hp) for flavor
		return dmg; // return the actual damage dealt
	}
}