package me.mrletsplay.pwmixergui.channel;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ChannelController {

	private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

	private Channel channel;

	@FXML
	private AnchorPane paneBackground;

	@FXML
	private Button buttonSelect;

	@FXML
	private Label labelChannelName;

	public void init(Channel channel) {
		this.channel = channel;
		setName(channel.getName());
	}

	public void setName(String name) {
		labelChannelName.setText(name);
	}

	public void setSelectable(boolean selectable) {
		buttonSelect.setDisable(!selectable);
	}

	public void setSelected(boolean selected) {
		paneBackground.pseudoClassStateChanged(SELECTED, selected);
	}

	@FXML
	void select(ActionEvent event) {
		setSelected(true);
	}

	@FXML
	void remove(ActionEvent event) {
		if(channel instanceof InputChannel) {
			Channels.removeInput((InputChannel) channel);
		}else if(channel instanceof OutputChannel) {
			Channels.removeOutput((OutputChannel) channel);
		}
	}

}
