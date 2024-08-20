package me.mrletsplay.pwmixergui.filter;

import java.util.List;

import me.mrletsplay.pwmixer.PWMixer;

public class EchoFilter extends Filter {

	public static final FilterParameter<Integer> DELAY_MILLISECONDS = FilterParameter.ofInteger("delayMilliseconds", "Delay (Milliseconds)", 500, 100, 10000);
	public static final FilterParameter<Float> STRENGTH = FilterParameter.ofFloat("strength", "Echo Strength", 0.75f, 0.0f, 1.0f);

	public static final List<FilterParameter<?>> PARAMETERS = List.of(DELAY_MILLISECONDS, STRENGTH);

	private float strength;
	private float[] buffer;
	private int bufferIndex;

	public EchoFilter() {}

	public EchoFilter(int delayMilliseconds, float strength) {
		setParameter(DELAY_MILLISECONDS, delayMilliseconds);
		setParameter(STRENGTH, strength);
		applyParameters();
	}

	@Override
	public void filter(float[] samples) {
		int echoSamples = bufferIndex < samples.length ? bufferIndex : samples.length;
		for(int i = 0; i < echoSamples; i++) {
			samples[i] += buffer[i] * strength;
		}

		System.arraycopy(buffer, echoSamples, buffer, 0, bufferIndex - echoSamples);
		bufferIndex -= echoSamples;

		int freeSpace = buffer.length - bufferIndex;
		int toCopy = samples.length < freeSpace ? samples.length : freeSpace;
		System.arraycopy(samples, 0, buffer, bufferIndex, toCopy);
		bufferIndex += toCopy;
	}

	@Override
	public void applyParameters() {
		this.strength = getParameter(STRENGTH);
		this.buffer = new float[(int) ((long) PWMixer.PWM_CHANNELS * PWMixer.PWM_RATE * getParameter(DELAY_MILLISECONDS) / 1000)];
		this.bufferIndex = buffer.length;
	}

}
