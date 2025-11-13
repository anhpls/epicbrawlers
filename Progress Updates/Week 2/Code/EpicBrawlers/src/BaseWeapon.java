public abstract class BaseWeapon
{
	// instance fields
	private final String name; // name of weapon (stick, bow, sword, etc)
	private int level; // current weapon level (starts at 1 and goes up when
						// upgraded)
	private final int baseDamage; // base damage before scaling
	private final double growth; // how much the weapon's damage multiplies per
									// level

	// constructor: set main weapon info
	public BaseWeapon(String name, int baseDamage, double growth)
	{
		this.name = name; // store weapon name
		this.baseDamage = baseDamage; // store base damage
		this.level = 1; // all weapons start at lvl 1
		this.growth = growth; // how fast it scales per level
	}

	// getters
	public String getName()
	{
		return name; // return name of weapon
	}

	public int getLevel()
	{
		return level; // return current level
	}

	public int getBaseDamage()
	{
		return baseDamage; // return base damage
	}

	public double getGrowth()
	{
		return growth; // return growth multiplier
	}

	// upgrade weapon by one level
	public void upgrade()
	{
		level++; // just adds +1 to current level
	}

	// main damage formula (will overridden by subclasses)
	public int getDamage()
	{
		// exponential scaling: dmg = base * growth^(level - 1)
		double scaled = baseDamage * Math.pow(growth, Math.max(0, level - 1));
		// make sure damage never drops below 1, and round the number
		return Math.max(1, (int) Math.round(scaled));
	}

	@Override
	public String toString()
	{
		// show weapon name, level, and current damage as a string
		return name + " Lv. " + level + " (DMG " + getDamage() + ")";
	}

}