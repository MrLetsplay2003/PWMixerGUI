package me.mrletsplay.pwmixergui;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
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
			try(FileSystem selfFS = FileSystems.newFileSystem(Paths.get(PWMixerGUILauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()), (ClassLoader) null)) {
				Files.copy(selfFS.getPath("/libpwmixerj.so"), pwmixerj);
			}
		}

		System.load(pwmixerj.toString());

		Color c = new Color(0);

		String[] copy = new String[args.length + 1];
		copy[0] = "pwmixerj";

		PWMixer.sysConnect(copy);
		PWMixer.debugEnableLog(true);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> PWMixer.sysDisconnect()));

//		PWMInput in = PWMixer.ioCreateInput("To Out + Out2", false);
//		PWMInput in2 = PWMixer.ioCreateInput("To Out2 only", true);
//		PWMOutput out = PWMixer.ioCreateOutput("Out", true);
//		PWMOutput out2 = PWMixer.ioCreateOutput("Out2", false);
//
//		PWMixer.ioConnect(in, out);
//		PWMixer.ioConnect(in, out2);
//		PWMixer.ioConnect(in2, out2);

		Application.launch(PWMixerGUI.class, args);
	}

}
