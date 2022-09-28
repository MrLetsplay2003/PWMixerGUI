package me.mrletsplay.pwmixergui.channel;

import me.mrletsplay.pwmixer.PWMixer;

public class ChannelConnection {

	private OutputChannel output;
	private InputChannel input;
	private float volume;

	public ChannelConnection(InputChannel input, OutputChannel output) {
		this.input = input;
		this.output = output;
		this.volume = 1.0f;
	}

	public OutputChannel getOutput() {
		return output;
	}

	public InputChannel getInput() {
		return input;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		PWMixer.ioSetConnectionVolume(input.getInput(), output.getOutput(), volume);
	}

}
