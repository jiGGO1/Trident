package net.minecraft.trident.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.trident.crafting.RecipeTridentItem;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ji_GGO
 * @date 2022/12/15
 */
@JEIPlugin
public class JEITridentPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        if (OreDictionary.getOres("trident").size() > 1) {
            IJeiHelpers jeiHelpers = registry.getJeiHelpers();
            registry.handleRecipes(RecipeTridentItem.class, recipe -> new TridentRecipeWrapper(jeiHelpers, recipe), VanillaRecipeCategoryUid.CRAFTING);
        }
    }

}