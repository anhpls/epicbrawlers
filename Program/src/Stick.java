/**
 * stick weapon class.
 * represents the starter weapon with low base damage
 * and small growth for early gameplay.
 */
public class Stick extends BaseWeapon
{
	/**
	 * constructs the stick weapon with default stats.
	 */
	public Stick()
	{
		super("Stick", 10, 1.25); // gentle growth
	}
}