package net.minecraft.trident.common;

import net.minecraft.item.Item;
import net.minecraft.trident.crafting.TridentRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ji_GGO
 * @date 2021/03/13
 */
public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent event) {

    }

    public void init(final FMLInitializationEvent event) {
        registerOre("trident");
        registerOre("futuremc");
        registerOre("futureminecraf");
        TridentRecipes.init();
    }

    public void postInit(final FMLPostInitializationEvent event) {

    }

    private static void registerOre(String modid) {
        if (Loader.isModLoaded(modid)) {
            Item trident = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, "trident"));
            OreDictionary.registerOre("trident", trident);
        }
    }

}