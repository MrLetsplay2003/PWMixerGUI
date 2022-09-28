package me.mrletsplay.pwmixergui.channel;

import me.mrletsplay.pwmixer.PWMOutput;

public class OutputChannel extends Channel {

	private PWMOutput output;

	public OutputChannel(PWMOutput output, String name, boolean isSource) {
		super(name, isSource);
		this.output = output;
	}

	public PWMOutput getOutput() {
		return output;
	}

}
