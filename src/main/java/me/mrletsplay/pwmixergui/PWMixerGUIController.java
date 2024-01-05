package me.mrletsplay.pwmixergui;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import me.mrletsplay.pwmixergui.channel.ChannelController;
import me.mrletsplay.pwmixergui.channel.Channels;
import me.mrletsplay.pwmixergui.channel.InputChannel;
import me.mrletsplay.pwmixergui.channel.OutputChannel;
import me.mrletsplay.pwmixergui.exception.LoadException;
import me.mrletsplay.pwmixergui.exception.SaveException;
import me.mrletsplay.pwmixergui.util.dialog.DialogData;
import me.mrletsplay.pwmixergui.util.dialog.DialogHelper;
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
			channel.setController(c);
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
			channel.setController(c);
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
		Channels.createInput(System.currentTimeMillis(), data.get("name"), data.get("isDevice"));
	}

	@FXML
	void menuAddOutput(ActionEvent event) {
		DialogData data = promptChannel();
		if(data == null) return;
		Channels.createOutput(System.currentTimeMillis(), data.get("name"), data.get("isDevice"));
	}

	@FXML
	void menuNew(ActionEvent event) {
		int c = DialogHelper.showYesNoCancel("Do you want to save your current configuration?");
		if(c == DialogHelper.CANCEL) return;

		if(c == DialogHelper.YES) {
			if(!promptSave()) return;
		}

		Channels.clear();
		Channels.createDefault();
	}

	@FXML
	void menuLoad(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File("."));
		chooser.getExtensionFilters().add(new ExtensionFilter("JSON Files", "*.json"));
		chooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*"));
		File f = chooser.showOpenDialog(PWMixerGUI.stage);
		if(f == null) return;

		try {
			Channels.load(f.toPath());
		}catch(LoadException e) {
			DialogHelper.showError("Failed to load", e);
		}
	}

	@FXML
	void menuSave(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File("."));
		chooser.getExtensionFilters().add(new ExtensionFilter("JSON Files", "*.json"));
		chooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*"));
		File f = chooser.showSaveDialog(PWMixerGUI.stage);
		if(f == null) return;

		try {
			Channels.save(f.toPath());
		}catch(LoadException e) {
			DialogHelper.showError("Failed to load", e);
		}
	}

	boolean promptSave() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File("."));
		chooser.getExtensionFilters().add(new ExtensionFilter("JSON Files", "*.json"));
		chooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*"));
		File f = chooser.showSaveDialog(PWMixerGUI.stage);
		if(f == null) return false;

		if(!f.getName().endsWith(".json")) {
			boolean yes = DialogHelper.showYesNo("The file's name doesn't end with a .json extension.\nDo you want to save it with that extension instead? (might override existing files)");
			if(yes) f = new File(f.getAbsolutePath() + ".json");
		}

		try {
			Channels.save(f.toPath());
			return true;
		}catch(SaveException e) {
			DialogHelper.showError("Failed to save", e);
			return false;
		}
	}

	@FXML
	void menuAbout() {
		DialogHelper.showInformation("PWMixerGUI & PWMixer were created by MrLetsplay2003\n\nSpecial thanks to: Palme11100");
	}

}
