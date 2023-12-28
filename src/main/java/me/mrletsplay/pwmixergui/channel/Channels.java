package me.mrletsplay.pwmixergui.channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.pwmixer.PWMInput;
import me.mrletsplay.pwmixer.PWMOutput;
import me.mrletsplay.pwmixer.PWMixer;
import me.mrletsplay.pwmixergui.PWMixerGUI;
import me.mrletsplay.pwmixergui.exception.LoadException;
import me.mrletsplay.pwmixergui.exception.SaveException;

public class Channels {

	private static List<InputChannel> inputs = new ArrayList<>();
	private static List<OutputChannel> outputs = new ArrayList<>();

	public static void clear() {
		new ArrayList<>(outputs).forEach(o -> removeOutput(o));
		new ArrayList<>(inputs).forEach(i -> removeInput(i));
	}

	public static void createDefault() {
		Channels.createInput(0, "Microphone In", false);
		Channels.createOutput(1, "System Out", false);
	}

	public static InputChannel createInput(long id, String name, boolean isSink) {
		PWMInput input = PWMixer.ioCreateInput(name, isSink);
		InputChannel i = new InputChannel(input, id, name, isSink);
		inputs.add(i);
		PWMixerGUI.controller.addInput(i);
		return i;
	}

	public static OutputChannel createOutput(long id, String name, boolean isSource) {
		PWMOutput output = PWMixer.ioCreateOutput(name, isSource);
		OutputChannel o = new OutputChannel(output, id, name, isSource);
		outputs.add(o);
		PWMixerGUI.controller.addOutput(o);
		return o;
	}

	public static void removeInput(InputChannel input) {
		inputs.remove(input);
		outputs.forEach(o -> o.removeConnection(input));
		PWMixerGUI.controller.removeInput(input);
		PWMixer.ioDestroyInput(input.getInput());
	}

	public static void removeOutput(OutputChannel output) {
		outputs.remove(output);
		inputs.forEach(o -> o.removeConnection(output));
		PWMixerGUI.controller.removeOutput(output);
		PWMixer.ioDestroyOutput(output.getOutput());
	}

	public static ChannelConnection connect(InputChannel in, OutputChannel out) {
		PWMixer.ioConnect(in.getInput(), out.getOutput());
		ChannelConnection con = new ChannelConnection(in, out);
		in.addConnection(con);
		out.addConnection(con);
		return con;
	}

	public static void disconnect(InputChannel in, OutputChannel out) {
		PWMixer.ioDisconnect(in.getInput(), out.getOutput());
		in.removeConnection(out);
		out.removeConnection(in);
	}

	public static InputChannel getInputByID(long id) {
		return inputs.stream()
			.filter(i -> i.getID() == id)
			.findFirst().orElse(null);
	}

	public static List<InputChannel> getInputs() {
		return inputs;
	}

	public static List<OutputChannel> getOutputs() {
		return outputs;
	}

	public static void load(Path path) {
		clear();

		try {
			JSONObject obj = new JSONObject(Files.readString(path, StandardCharsets.UTF_8));

			for(Object o : obj.getJSONArray("inputs")) {
				JSONObject iObj = (JSONObject) o;
				createInput(iObj.getLong("id"), iObj.getString("name"), iObj.getBoolean("isDevice"));
			}

			for(Object o : obj.getJSONArray("outputs")) {
				JSONObject oObj = (JSONObject) o;
				OutputChannel out = createOutput(oObj.getLong("id"), oObj.getString("name"), oObj.getBoolean("isDevice"));

				for(Object c : oObj.getJSONArray("connections")) {
					JSONObject con = (JSONObject) c;
					ChannelConnection connection = connect(Channels.getInputByID(con.getLong("to")), out);
					connection.setVolume(con.getDouble("volume").floatValue());
				}
			}
		} catch (IOException e) {
			throw new LoadException("Failed to load configuration", e);
		}
	}

	public static void save(Path path) {
		JSONObject obj = new JSONObject();

		JSONArray ins = new JSONArray();
		for(InputChannel in : inputs) {
			JSONObject i = new JSONObject();
			i.put("id", in.getID());
			i.put("name", in.getName());
			i.put("isDevice", in.isDevice());
			ins.add(i);
		}
		obj.put("inputs", ins);

		JSONArray outs = new JSONArray();
		for(OutputChannel out : outputs) {
			JSONObject o = new JSONObject();
			o.put("id", out.getID());
			o.put("name", out.getName());
			o.put("isDevice", out.isDevice());
			o.put("volume", out.getVolume());

			JSONArray cons = new JSONArray();
			for(ChannelConnection con : out.getConnections()) {
				JSONObject c = new JSONObject();
				c.put("to", con.getInput().getID());
				c.put("volume", con.getVolume());
				cons.add(c);
			}
			o.put("connections", cons);
			outs.add(o);
		}
		obj.put("outputs", outs);

		try {
			Files.writeString(path, obj.toString(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new SaveException("Failed to save configuration", e);
		}
	}

}
