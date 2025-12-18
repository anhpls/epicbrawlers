/**
 * represents the player character.
 * stores stats, equipment, consumables, combat logic,
 * and save/load support for gameplay progression.
 */
public class Player
{
	// player stats
	private int gold; // how much money player has
	private long maxHP; // total hp player can have
	private long hp; // current hp

	// consumables
	private int potionCount; // how many health pots player owns
	private static final double POTION_HEAL = 0.40; // heal 40% of max HP
	private int hpUpgradeCount = 0;
	private int dragonPotionHits = 0; // how many armor breaking hits left
	private int dragonPotionCount = 0; // how many dragon slayer potions left

	// current weapon
	private BaseWeapon weapon;

	/**
	 * constructs a new player with default starting stats.
	 */
	public Player()
	{
		this.gold = 0; // start broke lol
		this.maxHP = 100; // base max hp
		this.hp = 100; // full health at start
		this.weapon = new Stick(); // starts with stick weapon
		this.potionCount = 0; // no potions yet
	}

	// === getters ===
	/**
	 * returns the player's current gold.
	 *
	 * @return gold amount
	 */
	public int getGold()
	{
		return gold; // return gold amount
	}

	/**
	 * returns the player's current hp.
	 *
	 * @return current hp
	 */
	public long getHP()
	{
		return hp; // return current hp
	}

	/**
	 * returns the player's maximum hp.
	 *
	 * @return max hp
	 */
	public long getMaxHP()
	{
		return maxHP; // return max hp
	}

	/**
	 * returns how many health potions the player has.
	 *
	 * @return potion count
	 */
	public int getPotionCount()
	{
		return potionCount; // return how many potions left
	}

	/**
	 * returns how many times max hp has been upgraded.
	 *
	 * @return hp upgrade count
	 */
	public int getHpUpgradeCount()
	{
		return hpUpgradeCount; // how many times the user has upgraded health
	}

	/**
	 * returns the currently equipped weapon.
	 *
	 * @return equipped weapon
	 */
	public BaseWeapon getWeapon()
	{
		return weapon; // return equipped weapon
	}

	/**
	 * returns remaining armor-breaking hits from dragon potions.
	 *
	 * @return dragon potion hits
	 */
	public int getDragonPotionHits()
	{
		return dragonPotionHits; // how many armor-breaking hits are left
	}

	/**
	 * returns how many dragon slayer potions the player has.
	 *
	 * @return dragon potion count
	 */
	public int getDragonPotionCount()
	{
		return dragonPotionCount; // how many dragon slayer potions we have
	}

	/**
	 * adds dragon slayer potions to the inventory.
	 *
	 * @param amount number of potions to add
	 */
	public void addDragonPotions(int amount)
	{
		if (amount > 0) // only add if positive
			dragonPotionCount += amount; // increase potion count
	}

	/**
	 * directly sets dragon potion count.
	 *
	 * @param count potion count
	 */
	public void setDragonPotionCount(int count)
	{
		dragonPotionCount = Math.max(0, count); // clamp
	}

	// === gold handling ===
	/**
	 * adds gold to the player.
	 *
	 * @param amount gold to add
	 */
	public void addGold(int amount)
	{
		// add gold if amount is positive
		if (amount > 0)
		{
			gold += amount;
		}
	}

	/**
	 * attempts to spend gold.
	 *
	 * @param amount gold to spend
	 * @return true if purchase succeeded
	 */
	public boolean spendGold(int amount)
	{
		if (amount <= 0) return false; // can't spend 0 or negative amounts
		// only spend if you have enough gold
		if (gold >= amount)
		{
			gold -= amount;
			return true; // purchase worked
		}
		return false; // not enough money
	}

	// === health handling ===
	/**
	 * checks whether the player is alive.
	 *
	 * @return true if hp is greater than zero
	 */
	public boolean isAlive()
	{
		return hp > 0; // check if player is still alive
	}

	/**
	 * applies damage to the player.
	 *
	 * @param dmg incoming damage
	 */
	public void takeDamage(int dmg)
	{
		// take damage but don't go below 0
		if (dmg > 0)
		{
			hp = Math.max(0L, hp - dmg);
		}
	}

	/**
	 * fully restores player hp.
	 */
	public void healToFull()
	{
		hp = maxHP; // heal completely
	}

	/**
	 * permanently increases max hp and heals by the same amount.
	 *
	 * @param increase hp increase amount
	 */
	public void increaseMaxHP(long increase)
	{
		if (increase <= 0) return;
		maxHP += increase; // bump total hp
		hp = Math.min(maxHP, hp + increase); // heal some too
	}

	/**
	 * purchases a permanent max hp upgrade.
	 *
	 * @param stage current stage
	 * @param gold unused parameter kept for compatibility
	 * @return true if upgrade was successful
	 */
	public boolean buyMaxHPUpgrade(int stage, int gold)
	{
		// cost grows based on how many times upgrade was used
		int cost = (int) (40 * Math.pow(1.20, hpUpgradeCount));

		// check if player can afford it
		if (this.gold < cost) return false;

		// pay the cost
		this.gold -= cost;

		// percent increase also scales with the number of purchases
		// starts at 10%, grows +1% per upgrade
		double percent = 0.10 + (hpUpgradeCount * 0.01);

		// NO cap on percent growth
		// HP will scale infinitely as upgrades continue

		int increase = (int) (maxHP * percent);

		// apply HP increase
		maxHP += increase;
		hp = Math.min(maxHP, hp + increase);

		// track how many HP upgrades player has bought
		hpUpgradeCount++;

		return true;
	}

	/**
	 * automatically increases max hp when advancing stages.
	 */
	// bosses scale faster so player still feels pressure to upgrade
	public void autoStageHPBump()
	{
		long increase = Math.max(1L, Math.round(maxHP * 0.15)); // +15%
		increaseMaxHP(increase);
	}

	// === potion logic===
	/**
	 * adds health potions to the inventory.
	 *
	 * @param pot number of potions to add
	 */
	public void addPotions(int pot)
	{
		// add potions if amount > 0
		if (pot > 0)
		{
			potionCount += pot;
		}
	}

	/**
	 * sets hp upgrade count directly.
	 *
	 * @param count upgrade count
	 */
	public void setHpUpgradeCount(int count)
	{
		this.hpUpgradeCount = Math.max(0, count);
	}

	/**
	 * uses a health potion during battle.
	 *
	 * @return true if potion was consumed
	 */
	public boolean usePotionInBattle()
	{
		// can't use potion if there's none left
		if (potionCount <= 0) return false;

		// if at full hp, cannot use hp potion
		if (hp >= maxHP) return false;

		potionCount--; // use one
		// heal 40%
		long heal = Math.max(1L, Math.round(maxHP * POTION_HEAL));

		hp = Math.min(maxHP, hp + heal); // don't go past max hp
		return true;
	}

	/**
	 * sets remaining dragon potion hits.
	 *
	 * @param hits hit count
	 */
	public void setDragonPotionHits(int hits)
	{
		this.dragonPotionHits = Math.max(0, hits);
	}

	/**
	 * consumes one armor-breaking hit if available.
	 *
	 * @return true if a hit was consumed
	 */
	public boolean consumeDragonPotionHit()
	{
		if (dragonPotionHits > 0)
		{
			dragonPotionHits--;
			return true;
		}
		return false;
	}

	/**
	 * activates a dragon slayer potion.
	 *
	 * @return true if potion was used
	 */
	public boolean useDragonPotion()
	{
		if (dragonPotionCount <= 0) // no potions left
			return false;

		dragonPotionCount--; // consume one potion
		dragonPotionHits += 10; // grant 10 armor-breaking hits
		return true; // success
	}

	// === combat & equipment ===
	
	/**
	 * calculates attack damage based on equipped weapon.
	 *
	 * @return attack damage
	 */
	public int attackDamage()
	{
		long dmg = weapon.getDamage();
		// player always has a weapon (starts with Stick, can upgrade later)
		if (dmg > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		if (dmg < 1) return 1;

		return (int) dmg;
	}

	/**
	 * equips a new weapon.
	 *
	 * @param newWeapon weapon to equip
	 */
	public void setWeapon(BaseWeapon newWeapon)
	{
		// swap weapon if not null
		if (newWeapon != null)
		{
			weapon = newWeapon;
		}
	}

	// === save/load ===
	/**
	 * directly sets current hp.
	 *
	 * @param hp hp value
	 */
	public void setHP(long hp)
	{
		this.hp = Math.max(0, Math.min(hp, maxHP));
	}

	/**
	 * directly sets max hp.
	 *
	 * @param newMax new max hp
	 */
	public void setMaxHPDirect(long newMax)
	{
		this.maxHP = newMax;
		this.hp = Math.min(hp, maxHP);
	}

	/**
	 * sets potion count.
	 *
	 * @param count potion count
	 */
	public void setPotionCount(int count)
	{
		this.potionCount = Math.max(0, count);
	}

	/**
	 * sets gold amount.
	 *
	 * @param amount gold value
	 */
	public void setGold(int amount)
	{
		this.gold = Math.max(0, amount);
	}
}