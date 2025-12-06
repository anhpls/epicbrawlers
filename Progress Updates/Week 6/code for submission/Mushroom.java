public class Mushroom extends BaseBoss
{
	public Mushroom(int stage)
	{
		// this one’s tankier than ghoul but not as deadly
		super(stage, 200L + (stage * 30L), // solid hp that grows each
											// stage
				10 + (stage * 4), // average attack
				30 + (stage * 40)); // gives okay gold rewards
	}

	@Override
	// "spore burst" — doubles attack every 3rd hit
	protected int specialAttack()
	{
		return getBaseAttack() * 2; // simple but strong burst
	}
}