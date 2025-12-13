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

	// default player stats: 100 HP, 0 gold, stick, 0 health pots
	public Player()
	{
		this.gold = 0; // start broke lol
		this.maxHP = 100; // base max hp
		this.hp = 100; // full health at start
		this.weapon = new Stick(); // starts with stick weapon
		this.potionCount = 0; // no potions yet
	}

	// === getters ===
	public int getGold()
	{
		return gold; // return gold amount
	}

	public long getHP()
	{
		return hp; // return current hp
	}

	public long getMaxHP()
	{
		return maxHP; // return max hp
	}

	public int getPotionCount()
	{
		return potionCount; // return how many potions left
	}

	public int getHpUpgradeCount()
	{
		return hpUpgradeCount; // how many times the user has upgraded health
	}

	public BaseWeapon getWeapon()
	{
		return weapon; // return equipped weapon
	}

	public int getDragonPotionHits()
	{
		return dragonPotionHits; // how many armor-breaking hits are left
	}

	public int getDragonPotionCount()
	{
		return dragonPotionCount; // how many dragon slayer potions we have
	}

	public void addDragonPotions(int amount)
	{
		if (amount > 0) // only add if positive
			dragonPotionCount += amount; // increase potion count
	}

	public void setDragonPotionCount(int count)
	{
		dragonPotionCount = Math.max(0, count); // clamp
	}

	// === gold handling ===
	public void addGold(int amount)
	{
		// add gold if amount is positive
		if (amount > 0)
		{
			gold += amount;
		}
	}

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
	public boolean isAlive()
	{
		return hp > 0; // check if player is still alive
	}

	public void takeDamage(int dmg)
	{
		// take damage but don't go below 0
		if (dmg > 0)
		{
			hp = Math.max(0L, hp - dmg);
		}
	}

	public void healToFull()
	{
		hp = maxHP; // heal completely
	}

	// permanent max HP increase (via shop)
	// gives a mini heal for same amount
	public void increaseMaxHP(long increase)
	{
		if (increase <= 0) return;
		maxHP += increase; // bump total hp
		hp = Math.min(maxHP, hp + increase); // heal some too
	}

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

	// auto hp boost per stage (mild scaling)
	// bosses scale faster so player still feels pressure to upgrade
	public void autoStageHPBump()
	{
		long increase = Math.max(1L, Math.round(maxHP * 0.15)); // +15%
		increaseMaxHP(increase);
	}

	// === potion logic===
	public void addPotions(int pot)
	{
		// add potions if amount > 0
		if (pot > 0)
		{
			potionCount += pot;
		}
	}

	public void setHpUpgradeCount(int count)
	{
		this.hpUpgradeCount = Math.max(0, count);
	}

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

	public void setDragonPotionHits(int hits)
	{
		this.dragonPotionHits = Math.max(0, hits);
	}

	public boolean consumeDragonPotionHit()
	{
		if (dragonPotionHits > 0)
		{
			dragonPotionHits--;
			return true;
		}
		return false;
	}

	// activate the dragon slayer potion
	public boolean useDragonPotion()
	{
		if (dragonPotionCount <= 0) // no potions left
			return false;

		dragonPotionCount--; // consume one potion
		dragonPotionHits += 10; // grant 10 armor-breaking hits
		return true; // success
	}

	// === combat & equipment ===
	public int attackDamage()
	{
		long dmg = weapon.getDamage();
		// player always has a weapon (starts with Stick, can upgrade later)
		if (dmg > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		if (dmg < 1) return 1;

		return (int) dmg;
	}

	public void setWeapon(BaseWeapon newWeapon)
	{
		// swap weapon if not null
		if (newWeapon != null)
		{
			weapon = newWeapon;
		}
	}

	// === save/load ===
	// directly set current HP (used when loading a save file)
	public void setHP(long hp)
	{
		this.hp = Math.max(0, Math.min(hp, maxHP));
	}

	// directly set max HP (used when loading a save file)
	public void setMaxHPDirect(long newMax)
	{
		this.maxHP = newMax;
		this.hp = Math.min(hp, maxHP);
	}

	// set potion count (used for loading)
	public void setPotionCount(int count)
	{
		this.potionCount = Math.max(0, count);
	}

	// set gold (used for loading)
	public void setGold(int amount)
	{
		this.gold = Math.max(0, amount);
	}
}