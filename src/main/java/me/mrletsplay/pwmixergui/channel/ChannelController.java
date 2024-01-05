package me.mrletsplay.pwmixergui.channel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import me.mrletsplay.pwmixer.PWMixer;
import me.mrletsplay.pwmixer.PWMixerUtil;
import me.mrletsplay.pwmixergui.PWMixerGUI;

public class ChannelController {

	private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

	private InputChannel input;
	private OutputChannel output;

	@FXML
	private AnchorPane paneBackground;

	@FXML
	private Button buttonSelect;

	@FXML
	private Label labelChannelName;

	@FXML
	private Slider sliderVolume;

	@FXML
	private Slider sliderVolumeOut;

	public void init(InputChannel channel) {
		this.input = channel;
		setName(channel.getName());

		buttonSelect.textProperty().bind(Bindings.createStringBinding(() -> PWMixerGUI.selectedChannelConnections.stream().anyMatch(c -> c.getInput().equals(input)) ? "Active" : "Inactive", PWMixerGUI.selectedChannelConnections));

		BooleanBinding noChannelSelected = Bindings.createBooleanBinding(() -> PWMixerGUI.selectedChannel.getValue() == null, PWMixerGUI.selectedChannel);
		BooleanBinding inputNotConnected = Bindings.createBooleanBinding(() -> PWMixerGUI.selectedChannelConnections.stream().noneMatch(c -> c.getInput().equals(input)), PWMixerGUI.selectedChannelConnections);

		buttonSelect.disableProperty().bind(noChannelSelected);
		sliderVolume.disableProperty().bind(inputNotConnected);
		sliderVolume.valueProperty().addListener(v -> {
			OutputChannel out = PWMixerGUI.selectedChannel.get();
			if(out == null) return;
			ChannelConnection con = out.getConnection(input);
			if(con == null) return;

			con.setVolume((float) PWMixerUtil.convertPerceivedVolumeToVolumeMultiplier(sliderVolume.getValue() / 100));
		});
	}

	public void init(OutputChannel channel) {
		this.output = channel;
		setName(channel.getName());

		sliderVolume.valueProperty().addListener(v -> {
			PWMixer.ioSetVolume(output.getOutput(), (float) Math.pow(sliderVolume.getValue() / 100, 2));
		});
	}

	private void setName(String name) {
		labelChannelName.setText(name);
	}

	private void setSelected(boolean selected) {
		paneBackground.pseudoClassStateChanged(SELECTED, selected);
	}

	public void setVolumeDisplay(float val) {
		double newVal = PWMixerUtil.convertVolumeMultiplierToPerceivedVolume(val) * 100;
		double oldVal = sliderVolumeOut.getValue();
		if(newVal < oldVal) {
			sliderVolumeOut.setValue(oldVal - Math.min(-(newVal - oldVal), 1));
		}else {
			sliderVolumeOut.setValue(newVal);
		}
	}

	@FXML
	void select(ActionEvent event) {
		if(input != null) {
			OutputChannel ch = PWMixerGUI.selectedChannel.get();

			if(ch.getConnection(input) == null) {
				Channels.connect(input, ch);
			}else {
				Channels.disconnect(input, ch);
			}
		}else {
			Channels.getOutputs().forEach(o -> o.getController().setSelected(false));
			setSelected(true);
			PWMixerGUI.selectedChannel.set(output);
			PWMixerGUI.selectedChannelConnections.set(output.getConnections());
			Channels.getInputs().forEach(in -> {
				ChannelConnection con = output.getConnection(in);
				if(con == null) {
					in.getController().sliderVolume.setValue(100);
				}else {
					in.getController().sliderVolume.setValue(Math.sqrt(con.getVolume()) * 100);
				}
			});
		}
	}

	@FXML
	void remove(ActionEvent event) {
		if(input != null) {
			Channels.removeInput(input);
		}else{
			Channels.removeOutput(output);
		}
	}

}
