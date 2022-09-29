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

	public ChannelConnection getConnection(InputChannel in) {
		return connections.stream().filter(con -> con.getInput().equals(in)).findFirst().orElse(null);
	}

	public void removeConnection(InputChannel in) {
		connections.removeIf(con -> con.getInput().equals(in));
	}

}
