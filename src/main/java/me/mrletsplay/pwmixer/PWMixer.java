package me.mrletsplay.pwmixer;

public class PWMixer {

	public static final int
		PWM_CHANNELS = 2,
		PWM_RATE = 44100;

	public static native void sysConnect(String[] argv);
	public static native void sysDisconnect();

	public static native boolean sysIsRunning();

	public static void ioConnect(PWMInput input, PWMOutput output) {
		ioConnect0(input.id, output.id);
	}

	public static native void ioConnect0(long input, long output);

	public static void ioDisconnect(PWMInput input, PWMOutput output) {
		ioDisconnect0(input.id, output.id);
	}

	public static native void ioDisconnect0(long input, long output);

	public static PWMInput ioCreateInput(String name, boolean isSink) {
		long id = ioCreateInput0(name, isSink);
		return new PWMInput(id);
	}

	private static native long ioCreateInput0(String name, boolean isSource);

	public static void ioDestroyInput(PWMInput input) {
		ioDestroyInput0(input.id);
	}

	private static native void ioDestroyInput0(long input);

	public static PWMOutput ioCreateOutput(String name, boolean isSource) {
		long id = ioCreateOutput0(name, isSource);
		return new PWMOutput(id);
	}

	private static native long ioCreateOutput0(String name, boolean isSource);

	public static void ioDestroyOutput(PWMOutput output) {
		ioDestroyOutput0(output.id);
	}

	private static native void ioDestroyOutput0(long output);

	public static void ioSetVolume(PWMInput input, float volume) {
		ioSetVolume0(input.id, volume);
	}

	public static void ioSetVolume(PWMOutput output, float volume) {
		ioSetVolume0(output.id, volume);
	}

	private static native void ioSetVolume0(long id, float volume);

	public static void ioSetConnectionVolume(PWMInput input, PWMOutput output, float volume) {
		ioSetConnectionVolume0(input.id, output.id, volume);
	}

	private static native void ioSetConnectionVolume0(long input, long output, float volume);

	public static float ioGetLastVolume(PWMInput input) {
		return ioGetLastVolume0(input.id);
	}

	public static float ioGetLastVolume(PWMOutput output) {
		return ioGetLastVolume0(output.id);
	}

	private static native float ioGetLastVolume0(long id);

	public static void ioSetFilterFunction(PWMInput input, PWMOutput output, PWMFilterFunction filterFunction) {
		ioSetFilterFunction0(input.id, output.id, filterFunction);
	}

	private static native void ioSetFilterFunction0(long input, long output, PWMFilterFunction filterFunction);

	public static native void debugEnableLog(boolean log);

	public static native boolean debugIsLogEnabled();

}
