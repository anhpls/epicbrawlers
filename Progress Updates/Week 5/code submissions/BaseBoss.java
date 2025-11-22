public abstract class BaseBoss
{
	private final int stage; // each boss's stage
	private final int maxHP; // how much total health the boss has
	private int hp; // current health
	private final int baseAttack; // how hard the boss hits
	private final int rewardGold; // how much gold is rewarded after winning

	// track attack count for specialAttack every 3rd hit
	private int attackCount = 0;
	private boolean lastAttackWasSpecial = false;

	// constructor: main stats
	public BaseBoss(int stage, int maxHP, int baseAttack, int rewardGold)
	{
		this.stage = stage; // save which stage this boss belongs to
		this.maxHP = Math.max(1, maxHP); // make sure hp isn't below 1
		this.hp = this.maxHP; // start with full hp
		this.baseAttack = Math.max(1, baseAttack); // make sure the attack is
													// positive
		this.rewardGold = Math.max(0, rewardGold); // no negative gold rewards
	}

	// === getters ===
	public boolean wasSpecialAttack()
	{
		return lastAttackWasSpecial;
	}

	public int getStage()
	{
		return stage; // retrieve stage number
	}

	public int getMaxHP()
	{
		return maxHP; // retrieve max hp
	}

	public int getHP()
	{
		return hp; // retrieve current health
	}

	public int getBaseAttack()
	{
		return baseAttack; // retrieve attack power
	}

	public int getRewardGold()
	{
		return rewardGold; // retrieve gold reward
	}

	public boolean isAlive()
	{
		return hp > 0; // check if boss is still alive
	}

	public void takeDamage(int dmg)
	{
		// reduce hp only if damage is positive and boss is alive
		if (dmg > 0 && isAlive()) hp = Math.max(0, hp - dmg);
	}

	// full heal (when restarting or re-entering a new stage)
	public void healToFull()
	{
		hp = maxHP; // reset hp to full
	}

	// === attacks ===
	public int dealDamage()
	{
		attackCount++; // count how many times the boss has attacked
		// every 3rd triggers a specialAtt
		lastAttackWasSpecial = false; // reset flag

		if (attackCount % 3 == 0)
		{
			lastAttackWasSpecial = true;
			return specialAttack(); // use sbuclass's special move
		}
		return baseAttack; // otherwise normal hit
	}

	// === subclass helpers ===
	protected void heal(int amount)
	{
		// heal some hp, but not if dead or healing zero
		if (amount <= 0 || !isAlive()) return;
		hp = Math.min(maxHP, hp + amount); // cap at max hp
	}

	protected void selfDamage(int amount)
	{
		// boss hurts itself (some special moves might do this)
		if (amount <= 0 || !isAlive()) return;
		hp = Math.max(0, hp - amount); // lower hp but not below 0
	}

	// === special (implemented by subclasses) ===
	protected abstract int specialAttack(); // each boss has its own 3rd-attack
											// special move

	@Override
	public String toString()
	{
		// summary for logs / debugging
		return getClass().getSimpleName() + " (Stage " + stage + ")  HP " + hp
				+ "/" + maxHP + "  ATK " + baseAttack + "  Reward " + rewardGold
				+ "g";
	}

}