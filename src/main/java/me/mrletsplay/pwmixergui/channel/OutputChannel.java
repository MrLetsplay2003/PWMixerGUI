package me.mrletsplay.pwmixergui.channel;

import me.mrletsplay.pwmixer.PWMOutput;
import me.mrletsplay.pwmixer.PWMixer;

public class OutputChannel extends Channel {

	private PWMOutput output;
	private float volume;

	public OutputChannel(PWMOutput output, long id, String name, boolean isSource) {
		super(id, name, isSource);
		this.output = output;
	}

	public PWMOutput getOutput() {
		return output;
	}

	public ChannelConnection getConnection(InputChannel in) {
		return connections.stream().filter(con -> con.getInput().equals(in)).findFirst().orElse(null);
	}

	public void removeConnection(InputChannel in) {
		connections.removeIf(con -> con.getInput().equals(in));
	}

	public void setVolume(float volume) {
		this.volume = volume;
		PWMixer.ioSetVolume(output, volume);
	}

	public float getVolume() {
		return volume;
	}

}
