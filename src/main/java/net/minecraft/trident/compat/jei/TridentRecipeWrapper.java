package net.minecraft.trident.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.trident.Trident;
import net.minecraft.trident.crafting.RecipeTridentItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * @author ji_GGO
 * @date 2022/12/15
 */
public class TridentRecipeWrapper extends ShapelessRecipeWrapper {

    public TridentRecipeWrapper(IJeiHelpers jeiHelpers, RecipeTridentItem recipe) {
        super(jeiHelpers, new ShapelessOreRecipe(null, Trident.TRIDENT,
                "trident").setRegistryName(new ResourceLocation(Trident.MODID, "convert")));
    }

}