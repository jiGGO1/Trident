package net.minecraft.trident.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.config.TridentConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

/**
 * @author ji_GGO
 * @date 2022/12/14
 */
public class TridentRecipes {

    public static void init() {
        final String name = Trident.MODID;
        final ItemStack trident = new ItemStack(Trident.TRIDENT);
        if (TridentConfig.TRIDENT_RECIPE) {
            addShapedOreRecipe(new ResourceLocation(name, name),
                    trident, " QQ", " PQ", "P  ",
                    'Q', "gemQuartz",
                    'P', Items.PRISMARINE_SHARD);
        }

        if (OreDictionary.getOres(name).size() > 1) {
            register(new RecipeTridentItem().setRegistryName(new ResourceLocation(name, "convert")));
        }
    }

    private static <K extends IForgeRegistryEntry<K>> K register(K object) {
        return GameData.register_impl(object);
    }

    public static void addShapedOreRecipe(ResourceLocation name, ResourceLocation group, @Nonnull ItemStack result, Object... params) {
        register(new ShapedOreRecipe(group, result, params).setRegistryName(name));
    }

    public static void addShapedOreRecipe(ResourceLocation name, @Nonnull ItemStack result, Object... params) {
        addShapedOreRecipe(name, null, result, params);
    }

}