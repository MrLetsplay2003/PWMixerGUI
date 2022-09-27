package me.mrletsplay.pwmixer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.mrletsplay.fxloader.FXLoader;

public class PWMixerMain {

	public static void main(String[] args) throws IOException, URISyntaxException {
		Path libDir = Paths.get(FXLoader.getLibDirectory().getAbsolutePath());
		Path pwmixerj = Paths.get(libDir.toString(), "libpwmixerj.so");
		if(!Files.exists(pwmixerj)) {
			try(FileSystem selfFS = FileSystems.newFileSystem(Paths.get(PWMixerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()), (ClassLoader) null)) {
				Files.copy(selfFS.getPath("/libpwmixerj.so"), pwmixerj);
			}
		}

		System.load(pwmixerj.toString());

		String[] copy = new String[args.length + 1];
		copy[0] = "pwmixerj";

		PWMixer.sysConnect(copy);
		PWMixer.debugEnableLog(true);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> PWMixer.sysDisconnect()));

		PWMInput in = PWMixer.ioCreateInput("To Out + Out2", false);
		PWMInput in2 = PWMixer.ioCreateInput("To Out2 only", true);
		PWMOutput out = PWMixer.ioCreateOutput("Out", true);
		PWMOutput out2 = PWMixer.ioCreateOutput("Out2", false);

		PWMixer.ioConnect(in, out);
		PWMixer.ioConnect(in, out2);
		PWMixer.ioConnect(in2, out2);

		while(PWMixer.sysIsRunning()) {
			System.out.println("Sleeping");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
