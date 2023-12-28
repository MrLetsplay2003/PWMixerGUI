package me.mrletsplay.pwmixergui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import me.mrletsplay.fxloader.FXLoader;
import me.mrletsplay.pwmixer.PWMixer;

public class PWMixerGUILauncher {

	public static void main(String[] args) throws IOException, URISyntaxException {
		Path libDir = Paths.get(FXLoader.getLibDirectory().getAbsolutePath());
		Path pwmixerj = Paths.get(libDir.toString(), "libpwmixerj.so");
		if(!Files.exists(pwmixerj)) {
			try(InputStream libIn = PWMixerGUILauncher.class.getResourceAsStream("/libpwmixerj.so")) {
				Files.write(pwmixerj, libIn.readAllBytes());
			}
		}

		System.load(pwmixerj.toString());

		String[] copy = new String[args.length + 1];
		copy[0] = "pwmixerj";

		PWMixer.sysConnect(copy);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> PWMixer.sysDisconnect()));

		Application.launch(PWMixerGUI.class, args);
	}

}
