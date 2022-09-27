package me.mrletsplay.pwmixergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PWMixerGUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(PWMixerGUI.class.getResource("/ui.fxml"));
		Parent p = loader.load();

		Scene sc = new Scene(p, 720, 480);
		primaryStage.setTitle("PWMixerGUI");
		primaryStage.setMinWidth(720);
		primaryStage.setMinHeight(480);
		primaryStage.setScene(sc);
		primaryStage.show();
	}

}
