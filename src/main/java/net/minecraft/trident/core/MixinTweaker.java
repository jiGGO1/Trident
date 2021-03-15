package net.minecraft.trident.core;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

/**
 * @author ji_GGO
 * @date 2021/02/27
 */
public class MixinTweaker implements ITweaker {
	
	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.trident.json");
		CodeSource source = getClass().getProtectionDomain().getCodeSource();
		if (source != null) {
			URL location = source.getLocation();
			try {
				File file = new File(location.toURI());
				if (file.isFile()) {
					CoreModManager.getIgnoredMods().remove(file.getName());
				}
				Logger logger = LogManager.getLogger();
				logger.warn("Trident Mixin Loaded");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			Logger logger = LogManager.getLogger();
			logger.warn("No CodeSource, if this is not a development environment we might run into problems!");
			logger.warn(this.getClass().getProtectionDomain());
		}
	}
	
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

	}
	
	@Override
	public String getLaunchTarget() { return null; }
	
	@Override
	public String[] getLaunchArguments() { return new String[0]; }
	
}
