package me.mrletsplay.pwmixergui.channel;

import me.mrletsplay.pwmixer.PWMInput;

public class InputChannel extends Channel {

	private PWMInput input;

	public InputChannel(PWMInput input, long id, String name, boolean isSink) {
		super(id, name, isSink);
		this.input = input;
	}

	public PWMInput getInput() {
		return input;
	}

	public ChannelConnection getConnection(OutputChannel out) {
		return connections.stream().filter(con -> con.getOutput().equals(out)).findFirst().orElse(null);
	}

	public void removeConnection(OutputChannel out) {
		connections.removeIf(con -> con.getOutput().equals(out));
	}

}
