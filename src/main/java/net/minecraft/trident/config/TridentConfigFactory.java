package net.minecraft.trident.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.trident.Trident;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * @author ji_GGO
 * @date 2020/05/20
 */
public class TridentConfigFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraft) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen screen) {
        return new GuiConfig(screen, ConfigElement.from(TridentConfig.class).getChildElements(), Trident.MODID, false, false, getTitle(screen));
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }

    private static String getTitle(GuiScreen screen) {
        if (screen instanceof GuiModList) {
            Configuration config = new Configuration(new File("config/trident.cfg"));
            return GuiConfig.getAbridgedConfigPath(config.toString());
        }
        return Trident.NAME;
    }

}