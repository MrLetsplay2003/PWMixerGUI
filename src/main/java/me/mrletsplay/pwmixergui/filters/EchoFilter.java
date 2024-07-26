package me.mrletsplay.pwmixergui.filters;

import me.mrletsplay.pwmixer.PWMFilterFunction;
import me.mrletsplay.pwmixer.PWMixer;

public class EchoFilter implements PWMFilterFunction {

	private float strength;
	private float[] buffer;
	private int bufferIndex;

	public EchoFilter(int delayMilliseconds, float strength) {
		this.strength = strength;
		this.buffer = new float[(int) ((long) PWMixer.PWM_CHANNELS * PWMixer.PWM_RATE * delayMilliseconds / 1000)];
		this.bufferIndex = buffer.length;
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

}
