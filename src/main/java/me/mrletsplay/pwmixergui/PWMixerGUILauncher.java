package me.mrletsplay.pwmixergui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import me.mrletsplay.fxloader.FXLoader;
import me.mrletsplay.pwmixer.PWMInput;
import me.mrletsplay.pwmixer.PWMOutput;
import me.mrletsplay.pwmixer.PWMixer;

public class PWMixerGUILauncher {

	public static void main(String[] args) throws IOException, URISyntaxException {
		Path libDir = Paths.get(FXLoader.getLibDirectory().getAbsolutePath());
		Path pwmixerj = Paths.get(libDir.toString(), "libpwmixerj.so");
		if(!Files.exists(pwmixerj)) {
			try(FileSystem selfFS = FileSystems.newFileSystem(Paths.get(PWMixerGUILauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()), (ClassLoader) null)) {
				Files.copy(selfFS.getPath("/libpwmixerj.so"), pwmixerj);
			}
		}

		System.load(pwmixerj.toString());

		String[] copy = new String[args.length + 1];
		copy[0] = "pwmixerj";

		PWMixer.sysConnect(copy);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> PWMixer.sysDisconnect()));

		PWMInput in = PWMixer.ioCreateInput("in", true);
		PWMOutput out = PWMixer.ioCreateOutput("out", false);
		PWMixer.ioConnect(in, out);

		Application.launch(PWMixerGUI.class, args);
	}

}
