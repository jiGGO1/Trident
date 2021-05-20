package net.minecraft.trident.common;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.config.TridentConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ji_GGO
 * @date 2021/03/13
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        if (TridentConfig.tridentRecipe) {
            String name = Trident.MODID;
            ResourceLocation group = new ResourceLocation(name);
            GameRegistry.addShapedRecipe(new ResourceLocation(name, Trident.MODID), group,
                    new ItemStack(Trident.TRIDENT), " QQ", " PQ", "P  ",
                    'Q', Items.QUARTZ, 'P', Items.PRISMARINE_SHARD);
        }
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}