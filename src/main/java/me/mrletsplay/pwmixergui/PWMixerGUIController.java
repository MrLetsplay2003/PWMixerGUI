package me.mrletsplay.pwmixergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import me.mrletsplay.pwmixer.PWMixer;

public class PWMixerGUIController {

	@FXML
	void menuClose(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void menuAddInput(ActionEvent event) {
		PWMixer.ioCreateInput("Out" + Math.random(), false);
	}

	@FXML
	void menuAddOutput(ActionEvent event) {
		PWMixer.ioCreateOutput("Out" + Math.random(), false);
	}

}
