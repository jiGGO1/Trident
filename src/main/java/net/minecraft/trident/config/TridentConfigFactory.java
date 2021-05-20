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
    public void initialize(Minecraft mc) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new GuiConfig(parent, ConfigElement.from(TridentConfig.class).getChildElements(), Trident.MODID, false, false, getTitle(parent));
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }

    private static String getTitle(GuiScreen parent) {
        if (parent instanceof GuiModList) {
            Configuration config = new Configuration(new File("config/trident.cfg"));
            return GuiConfig.getAbridgedConfigPath(config.toString());
        }
        return Trident.NAME;
    }

}