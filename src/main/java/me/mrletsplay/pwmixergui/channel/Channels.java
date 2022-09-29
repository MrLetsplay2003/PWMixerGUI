package me.mrletsplay.pwmixergui.channel;

import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.pwmixer.PWMInput;
import me.mrletsplay.pwmixer.PWMOutput;
import me.mrletsplay.pwmixer.PWMixer;
import me.mrletsplay.pwmixergui.PWMixerGUI;

public class Channels {

	private static List<InputChannel> inputs = new ArrayList<>();
	private static List<OutputChannel> outputs = new ArrayList<>();

	public static InputChannel createInput(String name, boolean isSink) {
		PWMInput input = PWMixer.ioCreateInput(name, isSink);
		InputChannel i = new InputChannel(input, name, isSink);
		inputs.add(i);
		PWMixerGUI.controller.addInput(i);
		return i;
	}

	public static OutputChannel createOutput(String name, boolean isSource) {
		PWMOutput output = PWMixer.ioCreateOutput(name, isSource);
		OutputChannel o = new OutputChannel(output, name, isSource);
		outputs.add(o);
		PWMixerGUI.controller.addOutput(o);
		return o;
	}

	public static void removeInput(InputChannel input) {
		inputs.remove(input);
		outputs.forEach(o -> o.removeConnection(input));
		PWMixerGUI.controller.removeInput(input);
	}

	public static void removeOutput(OutputChannel output) {
		outputs.remove(output);
		inputs.forEach(o -> o.removeConnection(output));
		PWMixerGUI.controller.removeOutput(output);
	}

	public static void connect(InputChannel in, OutputChannel out) {
		PWMixer.ioConnect(in.getInput(), out.getOutput());
		ChannelConnection con = new ChannelConnection(in, out);
		in.addConnection(con);
		out.addConnection(con);
	}

	public static void disconnect(InputChannel in, OutputChannel out) {
		PWMixer.ioDisconnect(in.getInput(), out.getOutput());
		in.removeConnection(out);
		out.removeConnection(in);
	}

	public static List<InputChannel> getInputs() {
		return inputs;
	}

	public static List<OutputChannel> getOutputs() {
		return outputs;
	}

}
