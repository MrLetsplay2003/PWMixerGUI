package me.mrletsplay.pwmixergui.channel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Channel {

	private long id;
	private String name;
	private boolean isDevice;
	private ChannelController controller;

	protected ObservableList<ChannelConnection> connections;

	public Channel(long id, String name, boolean isDevice) {
		this.id = id;
		this.name = name;
		this.isDevice = isDevice;
		this.connections = FXCollections.observableArrayList();
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

	public ObservableList<ChannelConnection> getConnections() {
		return connections;
	}

	public void addConnection(ChannelConnection connection) {
		connections.add(connection);
	}

}
