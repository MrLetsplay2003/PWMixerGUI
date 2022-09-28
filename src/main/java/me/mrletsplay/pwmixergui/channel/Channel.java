package me.mrletsplay.pwmixergui.channel;

import java.util.ArrayList;
import java.util.List;

public class Channel {

	private long id;
	private String name;
	private boolean isDevice;
	private ChannelController controller;

	private List<ChannelConnection> connections;

	public Channel(String name, boolean isDevice) {
		this.name = name;
		this.isDevice = isDevice;
		this.connections = new ArrayList<>();
	}

	public long getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isDevice() {
		return isDevice;
	}

	public void setController(ChannelController controller) {
		this.controller = controller;
	}

	public ChannelController getController() {
		return controller;
	}

	public void addConnection(ChannelConnection connection) {
		connections.add(connection);
	}

	public void removeInput(InputChannel in) {
		connections.removeIf(con -> con.getInput().equals(in));
	}

	public void removeOutput(OutputChannel out) {
		connections.removeIf(con -> con.getOutput().equals(out));
	}

}
