/**
 * utility helper class.
 * provides common formatting used across the game.
 */
public class Utils
{

	// this formatter adds commas (ex: 12,300 or 4,500,000)
	public static final java.text.NumberFormat NF_COMMA = java.text.NumberFormat
			.getIntegerInstance();

	/**
	 * formats large numbers into short, readable strings.
	 * examples: 950 -> "950", 1200 -> "1.2K", 3_400_000 -> "3.4M"
	 *
	 * @param n the number to format
	 * @return formatted string representation
	 */
	public static String fmt(long n)
	{
		// if it's less than 1000, just show the number normally
		if (n < 1000) return String.valueOf(n);

		// if it's under a million, show it as K (thousands)
		// divide by 1000.0 so we get decimals like 1.2K instead of 1K
		if (n < 1_000_000) return String.format("%.1fK", n / 1000.0);

		// if it's under a billion, format it as M (millions)
		if (n < 1_000_000_000) return String.format("%.1fM", n / 1_000_000.0);

		// anything bigger becomes B (billions)
		return String.format("%.1fB", n / 1_000_000_000.0);
	}
}