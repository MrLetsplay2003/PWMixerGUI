package me.mrletsplay.pwmixergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.mrletsplay.pwmixergui.channel.Channels;

public class PWMixerGUI extends Application {

	public static Stage stage;
	public static PWMixerGUIController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;

		FXMLLoader loader = new FXMLLoader(PWMixerGUI.class.getResource("/ui.fxml"));
		Parent p = loader.load();
		controller = loader.getController();

		Scene sc = new Scene(p, 720, 480);
		sc.getStylesheets().add("/ui.css");
		primaryStage.setTitle("PWMixerGUI");
		primaryStage.setMinWidth(720);
		primaryStage.setMinHeight(480);
		primaryStage.setScene(sc);
		primaryStage.show();

		Channels.createInput("Microphone In", false);
		Channels.createOutput("System Out", false);
	}

}
