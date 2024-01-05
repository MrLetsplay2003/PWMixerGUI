package me.mrletsplay.pwmixer;

public class PWMixerUtil {

	private static final double LOG10_2 = Math.log10(2);

	private static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

	/**
	 * Converts a change in perceived volume into a raw volume multiplier (e.g. 2 = "twice as loud")<br>
	 * The inverse of this method is {@link #convertVolumeMultiplierToPerceivedVolume(double)}<br>
	 * <br>
	 * Examples:
	 * <table>
	 * <thead>
	 * <tr><td>Linear Volume</td><td>Multiplier</td></tr>
	 * </thead>
	 * <tbody>
	 * <tr><td>0</td><td>0</td></tr>
	 * <tr><td>0.5</td><td>0.1</td></tr>
	 * <tr><td>1</td><td>1</td></tr>
	 * <tr><td>2</td><td>10</td></tr>
	 * </tbody>
	 * </table>
	 * @return A volume multiplier
	 */
	public static double convertPerceivedVolumeToVolumeMultiplier(double linear) {
		return Math.pow(10, log2(linear));
	}

	/**
	 * Converts a raw volume multiplier into a change in perceived volume<br>
	 * This is the inverse of {@link #convertPerceivedVolumeToVolumeMultiplier(double)}<br>
	 * <br>
	 * Examples:
	 * <table>
	 * <thead>
	 * <tr><td>Linear Volume</td><td>Multiplier</td></tr>
	 * </thead>
	 * <tbody>
	 * <tr><td>0</td><td>0</td></tr>
	 * <tr><td>0.1</td><td>0.5</td></tr>
	 * <tr><td>1</td><td>1</td></tr>
	 * <tr><td>10</td><td>2</td></tr>
	 * </tbody>
	 * </table>
	 * @return A volume multiplier
	 */
	public static double convertVolumeMultiplierToPerceivedVolume(double volumeMultiplier) {
		return Math.pow(volumeMultiplier, LOG10_2);
	}

}
