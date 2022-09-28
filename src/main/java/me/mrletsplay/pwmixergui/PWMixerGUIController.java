package me.mrletsplay.pwmixergui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import me.mrletsplay.pwmixergui.channel.ChannelController;
import me.mrletsplay.pwmixergui.channel.Channels;
import me.mrletsplay.pwmixergui.channel.InputChannel;
import me.mrletsplay.pwmixergui.channel.OutputChannel;
import me.mrletsplay.pwmixergui.util.dialog.DialogData;
import me.mrletsplay.pwmixergui.util.dialog.SimpleInputDialog;

public class PWMixerGUIController {

	@FXML
	private HBox boxInputs;

	@FXML
	private HBox boxOutputs;

	public void addInput(InputChannel channel) {
		FXMLLoader loader = new FXMLLoader(PWMixerGUI.class.getResource("/channel.fxml"));
		try {
			Parent p = loader.load();
			p.setId("channel" + channel.getID());
			boxInputs.getChildren().add(p);
			ChannelController c = loader.getController();
			c.init(channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addOutput(OutputChannel channel) {
		FXMLLoader loader = new FXMLLoader(PWMixerGUI.class.getResource("/channel.fxml"));
		try {
			Parent p = loader.load();
			p.setId("channel" + channel.getID());
			boxOutputs.getChildren().add(p);
			ChannelController c = loader.getController();
			c.init(channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeInput(InputChannel channel) {
		Node n = boxInputs.lookup("#channel" + channel.getID());
		if(n == null) return;
		boxInputs.getChildren().remove(n);
	}

	public void removeOutput(OutputChannel channel) {
		Node n = boxOutputs.lookup("#channel" + channel.getID());
		if(n == null) return;
		boxOutputs.getChildren().remove(n);
	}

	@FXML
	void menuClose(ActionEvent event) {
		System.exit(0);
	}

	private static DialogData promptChannel() {
		return new SimpleInputDialog()
			.addString("name", "Name", "Name")
			.addBoolean("isDevice", "Is Device", true)
			.setVerifier(d -> {
				if(d.<String>get("name") == null) return "Name cannot be blank";
				return null;
			})
			.show("Create Channel", "Select Channel Parameters");
	}

	@FXML
	void menuAddInput(ActionEvent event) {
		DialogData data = promptChannel();
		if(data == null) return;
		Channels.createInput(data.get("name"), data.get("isDevice"));
	}

	@FXML
	void menuAddOutput(ActionEvent event) {
		DialogData data = promptChannel();
		if(data == null) return;
		Channels.createOutput(data.get("name"), data.get("isDevice"));
	}

}
