/**
 * abstract base class for all boss enemies in the * game.
 * defines shared stats, health logic, and attack * behavior.
 */
public abstract class BaseBoss
{
	private final int stage; // each boss's stage
	private final long maxHP; // how much total health the boss has
	private long hp; // current health
	private final int baseAttack; // how hard the boss hits
	private final int rewardGold; // how much gold is rewarded after winning

	// track attack count for specialAttack every 3rd hit
	private int attackCount = 0;
	private boolean lastAttackWasSpecial = false;

	/**
	 * constructs a boss with base stats.
	 *
	 * @param stage the stage this boss belongs to
	 * @param maxHP the maximum health of the boss
	 * @param baseAttack the base attack damage
	 * @param rewardGold gold rewarded when the boss is defeated
	 */
	public BaseBoss(int stage, long maxHP, int baseAttack, int rewardGold)
	{
		this.stage = stage; // save which stage this boss belongs to
		this.maxHP = Math.max(1L, maxHP); // make sure hp isn't below 1
		this.hp = this.maxHP; // start with full hp
		this.baseAttack = Math.max(1, baseAttack); // make sure the attack is
													// positive
		this.rewardGold = Math.max(0, rewardGold); // no negative gold rewards
	}

	// === getters ===
	/**
	 * checks whether the last attack performed was a special attack.
	 *
	 * @return true if the last attack was special, false otherwise
	 */
	public boolean wasSpecialAttack()
	{
		return lastAttackWasSpecial;
	}

	/**
	 * gets the stage number for this boss.
	 *
	 * @return the stage value
	 */
	public int getStage()
	{
		return stage; // retrieve stage number
	}

	/**
	 * gets the maximum health of the boss.
	 *
	 * @return max hp
	 */
	public long getMaxHP()
	{
		return maxHP; // retrieve max hp
	}

	/**
	 * gets the current health of the boss.
	 *
	 * @return current hp
	 */
	public long getHP()
	{
		return hp; // retrieve current health
	}

	/**
	 * gets the base attack damage of the boss.
	 *
	 * @return base attack value
	 */
	public int getBaseAttack()
	{
		return baseAttack; // retrieve attack power
	}

	/**
	 * gets the amount of gold rewarded for defeating the boss.
	 *
	 * @return reward gold
	 */
	public int getRewardGold()
	{
		return rewardGold; // retrieve gold reward
	}

	/**
	 * checks if the boss is still alive.
	 *
	 * @return true if hp is greater than zero
	 */
	public boolean isAlive()
	{
		return hp > 0; // check if boss is still alive
	}

	/**
	 * applies damage to the boss.
	 *
	 * @param dmg the amount of damage dealt
	 */
	public void takeDamage(int dmg)
	{
		// reduce hp only if damage is positive and boss is alive
		if (dmg > 0 && isAlive()) hp = Math.max(0, hp - dmg);
	}

	/**
	 * fully restores the boss's health.
	 */
	public void healToFull()
	{
		hp = maxHP; // reset hp to full
	}

	// === attacks ===
	/**
	 * calculates and returns the damage dealt by the boss.
	 * every third attack triggers a special attack.
	 *
	 * @return damage dealt by this attack
	 */
	public long dealDamage()
	{
		attackCount++; // count how many times the boss has attacked
		lastAttackWasSpecial = false; // reset flag

		if (attackCount % 3 == 0)
		{
			lastAttackWasSpecial = true;
			return specialAttack(); // subclass special move
		}
		return baseAttack; // normal hit
	}

	// === subclass helpers ===
	/**
	 * heals the boss by a given amount without exceeding max hp.
	 *
	 * @param amount amount of hp to heal
	 */
	protected void heal(int amount)
	{
		// heal some hp, but not if dead or healing zero
		if (amount <= 0 || !isAlive()) return;
		hp = Math.min(maxHP, hp + amount); // cap at max hp
	}

	/**
	 * applies self-inflicted damage to the boss.
	 *
	 * @param l amount of damage dealt to itself
	 */
	protected void selfDamage(long l)
	{
		// boss hurts itself (some special moves might do this)
		if (l <= 0 || !isAlive()) return;
		hp = Math.max(0, hp - l); // lower hp but not below 0
	}

	// === special (implemented by subclasses) ===
	/**
	 * performs the boss's special attack.
	 * triggered every third attack.
	 *
	 * @return damage dealt by the special attack
	 */
	protected abstract long specialAttack(); // each boss has its own 3rd-attack
												// special move

												/**
	 * returns a string summary of the boss for debugging.
	 *
	 * @return formatted boss description
	 */
	@Override
	public String toString()
	{
		// summary for logs / debugging
		return getClass().getSimpleName() + " (Stage " + stage + ")  HP " + hp
				+ "/" + maxHP + "  ATK " + baseAttack + "  Reward " + rewardGold
				+ "g";
	}

}