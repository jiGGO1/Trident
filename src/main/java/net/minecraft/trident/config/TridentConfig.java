package net.minecraft.trident.config;

import net.minecraft.trident.Trident;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ji_GGO
 * @date 2020/05/20
 */
@Config(modid = Trident.MODID)
@Config.LangKey("config.trident.general")
public class TridentConfig {

    @Config.LangKey("config.trident.general.zombie_drop")
    public static boolean zombieDrop = true;

    @Config.LangKey("config.trident.general.trident_zombie")
    public static boolean tridentZombie = true;

    @Config.RangeInt(min = 1, max = 100)
    @Config.LangKey("config.trident.general.trident_drop")
    public static int tridentDrop = 2;

    @Config.RequiresMcRestart
    @Config.LangKey("config.trident.general.trident_recipe")
    public static boolean tridentRecipe = true;

    @Mod.EventBusSubscriber(modid = Trident.MODID)
    private static class ConfigHandler {

        @SubscribeEvent
        public static void onConfigChanged(final OnConfigChangedEvent event) {
            if (event.getModID().equals(Trident.MODID)){
                ConfigManager.sync(Trident.MODID, Config.Type.INSTANCE);
            }
        }

    }

}