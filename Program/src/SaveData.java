import java.io.Serializable;

public class SaveData implements Serializable
{
	public int stage;
	public int gold;
	public int potions;
	public String weaponName;
	public int weaponLevel;
	public int maxHP;
	public int currentHP;

	public SaveData(int stage, int gold, int potions, String weaponName,
			int weaponLevel, int maxHP, int currentHP)
	{
		this.stage = stage;
		this.gold = gold;
		this.potions = potions;
		this.weaponName = weaponName;
		this.weaponLevel = weaponLevel;
		this.maxHP = maxHP;
		this.currentHP = currentHP;
	}
}