import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * container class for saving and loading game state.
 * stores player stats, stage progress, equipment, and defeated bosses.
 */
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
	public long maxHP;

	// player's current hp at the moment
	public long currentHP;

	// how many maxHP upgrades user has done
	public int hpUpgradeCount;

	// how many dragon slayer potions
	public int dragonPotionHits;

	// records bosses that user has already defeated
	public Set<String> defeatedBosses = new HashSet<>();

	/**
	 * constructs a savedata object containing all relevant game state.
	 *
	 * @param stage current stage
	 * @param gold current gold amount
	 * @param potions current potion count
	 * @param weaponName name of equipped weapon
	 * @param weaponLevel weapon level
	 * @param maxHP player max hp
	 * @param currentHP player current hp
	 * @param hpUpgradeCount number of hp upgrades purchased
	 * @param dragonPotionHits remaining dragon potion hits
	 * @param defeatedBosses set of defeated boss names
	 */
	public SaveData(int stage, int gold, int potions, String weaponName,
			int weaponLevel, long maxHP, long currentHP, int hpUpgradeCount,
			int dragonPotionHits, Set<String> defeatedBosses)
	{
		this.stage = stage; // save current stage
		this.gold = gold; // save current gold
		this.potions = potions; // save potion count
		this.weaponName = weaponName; // save weapon name
		this.weaponLevel = weaponLevel; // save weapon level
		this.maxHP = maxHP; // save max hp
		this.currentHP = currentHP; // save current hp
		this.hpUpgradeCount = hpUpgradeCount; // save hp upgrade count
		this.dragonPotionHits = dragonPotionHits; // save num of dragon potions
		this.defeatedBosses = new HashSet<>(defeatedBosses); // saves list of
																// bosses that
																// user has
																// defeated
	}
}