package me.mrletsplay.pwmixergui.util.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import me.mrletsplay.pwmixergui.PWMixerGUI;

public class DialogHelper {

	public static final int
		YES = 1,
		NO = 2,
		CANCEL = 3;

	public static void showInformation(String warning) {
		Alert a = new Alert(AlertType.INFORMATION);
		a.initOwner(PWMixerGUI.stage);
		a.setContentText(warning);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a.showAndWait();
	}

	public static void showWarning(String warning) {
		Alert a = new Alert(AlertType.WARNING);
		a.initOwner(PWMixerGUI.stage);
		a.setContentText(warning);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a.showAndWait();
	}

	public static void showError(String error) {
		Alert a = new Alert(AlertType.ERROR);
		a.initOwner(PWMixerGUI.stage);
		a.setContentText(error);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a.showAndWait();
	}

	public static void showError(String error, Throwable r) {
		Alert a = new Alert(AlertType.ERROR);
		a.initOwner(PWMixerGUI.stage);
		a.setHeaderText(error);
		a.setResizable(true);

		StringWriter w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		r.printStackTrace(pw);
		String exceptionText = w.toString();

		Label label = new Label("Exception stacktrace:");
		TextArea area = new TextArea(exceptionText);
		area.setEditable(false);
		area.setWrapText(true);
		area.setMaxWidth(Double.MAX_VALUE);
		area.setMaxHeight(Double.MAX_VALUE);
		area.autosize();

		GridPane.setVgrow(area, Priority.ALWAYS);
		GridPane.setHgrow(area, Priority.ALWAYS);

		GridPane content = new GridPane();
		content.setMaxWidth(Double.MAX_VALUE);
		content.add(label, 0, 0);
		content.add(area, 0, 1);

		a.getDialogPane().setContent(content);

		a.showAndWait();
	}

	public static boolean showYesNo(String message) {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.initOwner(PWMixerGUI.stage);
		a.getButtonTypes().clear();
		a.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		a.setContentText(message);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		return a.showAndWait().orElse(null) == ButtonType.YES;
	}

	public static int showYesNoCancel(String message) {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.initOwner(PWMixerGUI.stage);
		a.getButtonTypes().clear();
		a.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		a.setContentText(message);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

		ButtonType clicked = a.showAndWait().orElse(null);
		if(clicked == null || clicked == ButtonType.CANCEL) return CANCEL;
		return clicked == ButtonType.YES ? YES : NO;
	}

	public static int showChoice(String title, String message, String... choices) {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.initOwner(PWMixerGUI.stage);
		a.getButtonTypes().clear();
		for(String ch : choices) {
			a.getButtonTypes().add(new ButtonType(ch));
		}
		a.setTitle(title);
		a.setHeaderText(title);
		a.setContentText(message);
		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		return Arrays.asList(choices).indexOf(a.showAndWait().orElse(null).getText());
	}

}
