package me.mrletsplay.pwmixergui.util;

import java.io.InputStream;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.mrletsplay.pwmixer.PWMixer;

public class UIHelper {

	private UIHelper() {}

	public static void setButtonImages(Node n) {
		if(n instanceof Button) {
			Button b = (Button) n;
			if(b.getText() != null && b.getText().startsWith("@")) {
				String iconName = b.getText().substring(1);
				ImageView icon = loadIconView(iconName);
				if(icon != null) {
					b.setGraphic(icon);
					b.setText(null);
				}
			}
		}

		if(n instanceof Parent) {
			for(Node ch : ((Parent) n).getChildrenUnmodifiable()) {
				setButtonImages(ch);
			}
		}
	}

	public static InputStream loadIcon(String iconName) {
		return PWMixer.class.getResourceAsStream("/icon/" + iconName);
	}

	public static ImageView loadIconView(String iconName) {
		InputStream icon = loadIcon(iconName);
		if(icon == null) return null;
		return new ImageView(new Image(icon));
	}

}
