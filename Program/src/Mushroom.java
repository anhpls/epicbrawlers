/**
 * mushroom boss class.
 * represents a defensive boss with higher health and moderate damage,
 * featuring a burst-style special attack.
 */
public class Mushroom extends BaseBoss
{
	/**
	 * constructs a mushroom boss for a given stage.
	 *
	 * @param stage the stage this mushroom appears on
	 */
	public Mushroom(int stage)
	{
		// this one’s tankier than ghoul but not as deadly
		super(stage, 200L + (stage * 30L), // solid hp that grows each
											// stage
				10 + (stage * 4), // average attack
				30 + (stage * 40)); // gives okay gold rewards
	}

	/**
	 * performs the mushroom's special attack.
	 * spore burst doubles base attack damage every third hit.
	 *
	 * @return damage dealt by the special attack
	 */
	@Override
	// "spore burst" — doubles attack every 3rd hit
	protected long specialAttack()
	{
		return (long) getBaseAttack() * 2; // simple but strong burst
	}
}