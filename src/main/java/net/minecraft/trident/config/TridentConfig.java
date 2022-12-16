package net.minecraft.trident.config;

import net.minecraft.trident.Trident;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ji_GGO
 * @date 2020/05/20
 */
@Config(modid = Trident.MODID)
@LangKey("config.trident.general")
public class TridentConfig {

    @Name(value = "ZombieDrop")
    @LangKey("config.trident.general.zombie_drop")
    public static boolean ZOMBIE_DROP = true;

    @Name(value = "TridentZombie")
    @LangKey("config.trident.general.trident_zombie")
    public static boolean TRIDENT_ZOMBIE = true;

    @Name(value = "TridentDrop")
    @RangeInt(min = 1, max = 100)
    @LangKey("config.trident.general.trident_drop")
    public static int TRIDENT_DROP = 2;

    @Name(value = "TridentRecipe")
    @RequiresMcRestart
    @LangKey("config.trident.general.trident_recipe")
    public static boolean TRIDENT_RECIPE = true;

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