import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// a simple container for all player + stage data
public class SaveData implements Serializable
{
	// what stage the player is currently on
	public int stage;

	// how much gold they have
	public int gold;

	// how many potions they have
	public int potions;

	// name of the equipped weapon (stick, bow, etc)
	public String weaponName;

	// level of the weapon at save time
	public int weaponLevel;

	// player's max hp at the moment
	public int maxHP;

	// player's current hp at the moment
	public int currentHP;

	// how many maxHP upgrades user has done
	public int hpUpgradeCount;

	// records bosses that user has already defeated
	public Set<String> defeatedBosses = new HashSet<>();

	// constructor builds the savedata object with all needed values
	public SaveData(int stage, int gold, int potions, String weaponName,
			int weaponLevel, int maxHP, int currentHP, int hpUpgradeCount,
			Set<String> defeatedBosses)
	{
		this.stage = stage; // save current stage
		this.gold = gold; // save current gold
		this.potions = potions; // save potion count
		this.weaponName = weaponName; // save weapon name
		this.weaponLevel = weaponLevel; // save weapon level
		this.maxHP = maxHP; // save max hp
		this.currentHP = currentHP; // save current hp
		this.hpUpgradeCount = hpUpgradeCount;
		this.defeatedBosses = new HashSet<>(defeatedBosses); // saves list of
																// bosses that
																// user has
																// defeated
	}
}