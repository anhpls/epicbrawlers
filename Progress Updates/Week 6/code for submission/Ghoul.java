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
	protected int specialAttack()
	{
		// "drain life" — deals 1.5x dmg and heals a bit from it
		int damage = (int) (getBaseAttack() * 1.5); // 50% stronger hit
		int healAmount = (int) (damage * 0.2); // heals for 20% of that damage
		heal(healAmount); // restore some hp
		return damage; // send back damage dealt
	}
}