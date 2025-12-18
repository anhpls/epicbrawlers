/**
 * abstract base class for all weapons in the game.
 * defines shared weapon stats, upgrade logic, and damage scaling.
 */
public abstract class BaseWeapon
{
	// instance fields
	private final String name; // name of weapon (stick, bow, sword, etc)
	private int level; // current weapon level (starts at 1 and goes up when
						// upgraded)
	private final int baseDamage; // base damage before scaling
	private final double growth; // how much the weapon's damage multiplies per
									// level

	/**
	 * constructs a weapon with base stats.
	 *
	 * @param name the name of the weapon
	 * @param baseDamage the base damage value
	 * @param growth the growth multiplier per level
	 */
	public BaseWeapon(String name, int baseDamage, double growth)
	{
		this.name = name; // store weapon name
		this.baseDamage = baseDamage; // store base damage
		this.level = 1; // all weapons start at lvl 1
		this.growth = growth; // how fast it scales per level
	}

	// getters
	/**
	 * gets the name of the weapon.
	 *
	 * @return weapon name
	 */
	public String getName()
	{
		return name; // return name of weapon
	}

	/**
	 * gets the current weapon level.
	 *
	 * @return weapon level
	 */
	public int getLevel()
	{
		return level; // return current level
	}

	/**
	 * gets the base damage of the weapon.
	 *
	 * @return base damage value
	 */
	public int getBaseDamage()
	{
		return baseDamage; // return base damage
	}

	/**
	 * calculates the gold cost to upgrade the weapon.
	 *
	 * @return upgrade cost based on current level
	 */
	public int getUpgradeCost()
	{
		return 20 * level;
	}

	/**
	 * gets the growth multiplier for this weapon.
	 *
	 * @return growth multiplier
	 */
	public double getGrowth()
	{
		return growth; // return growth multiplier
	}

	/**
	 * upgrades the weapon by one level.
	 */
	public void upgrade()
	{
		level++; // just adds +1 to current level
	}

	/**
	 * sets the weapon level directly.
	 * used when loading save files.
	 *
	 * @param lvl the level to set
	 */
	public void setLevel(int lvl)
	{
		if (lvl > 0) this.level = lvl;
	}

	/**
	 * calculates the current damage dealt by the weapon.
	 * can be overridden by subclasses for custom behavior.
	 *
	 * @return calculated weapon damage
	 */
	public long getDamage()
	{
		// exponential scaling: dmg = base * growth^(level - 1)
		double scaled = baseDamage * Math.pow(growth, Math.max(0, level - 1));
		// make sure damage never drops below 1, and round the number
		return Math.max(1L, Math.round(scaled));
	}

	/**
	 * returns a string summary of the weapon.
	 *
	 * @return formatted weapon description
	 */
	@Override
	public String toString()
	{
		// show weapon name, level, and current damage as a string
		return name + " Lv. " + level + " (DMG " + getDamage() + ")";
	}

}