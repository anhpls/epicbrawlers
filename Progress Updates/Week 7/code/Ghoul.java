public class Ghoul extends BaseBoss
{
	public Ghoul(int stage)
	{
		// call the base class with scaling values for hp, attack, and gold
		super(stage, 150L + (stage * 25L), // decent hp that grows per
											// stage
				12 + (stage * 3), // attack gets stronger slowly
				25 + (stage * 70)); // gold reward increases too
	}

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