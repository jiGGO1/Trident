package net.minecraft.trident.crafting;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.trident.Trident;
import net.minecraft.trident.enchantment.TridentEnchantments;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ji_GGO
 * @date 2022/12/15
 */
public class RecipeTridentItem extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        List<Item> items = OreDictionary.getOres("trident").stream()
                .map(ItemStack::getItem)
                .collect(Collectors.toList());

        List<Item> crafting = Lists.newArrayList();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                crafting.add(stack.getItem());
            }
        }

        return crafting.size() == 1 && crafting.stream().allMatch(item -> items.contains(item));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack trident = new ItemStack(Trident.TRIDENT);
        int index = -1;
        List<Item> items = OreDictionary.getOres("trident").stream()
                .map(ItemStack::getItem)
                .collect(Collectors.toList());
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (items.contains(stack.getItem())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            ItemStack crafting = inventory.getStackInSlot(index).copy();

            if (crafting.getItemDamage() != crafting.getMaxDamage()) {
                int damage = crafting.getItemDamage();
                trident.setItemDamage(damage);
            }

            if (crafting.hasTagCompound()) {
                NBTTagCompound tag = crafting.getTagCompound();
                Map<Enchantment, Integer> enchantments = this.getEnchantments(crafting);
                trident.setTagCompound(tag);
                EnchantmentHelper.setEnchantments(enchantments, trident);
            }
        }
        return trident;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    private Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        Map<Enchantment, Integer> maps = EnchantmentHelper.getEnchantments(stack);
        if (Loader.isModLoaded("futuremc")) {
            Enchantment loyalty = getEnchantment("loyalty"), impaling = getEnchantment("impaling"),
                    riptide = getEnchantment("riptide"), channeling = getEnchantment("channeling");
            for (Enchantment enchantment : enchantments.keySet()) {
                int level = enchantments.get(enchantment);
                boolean flag = false;
                if (enchantment == loyalty) {
                    maps.put(TridentEnchantments.LOYALTY, level);
                    flag = true;
                } else if (enchantment == impaling) {
                    maps.put(TridentEnchantments.IMPALING, level);
                    flag = true;
                } else if (enchantment == riptide) {
                    maps.put(TridentEnchantments.RIPTIDE, level);
                    flag = true;
                } else if (enchantment == channeling) {
                    maps.put(TridentEnchantments.CHANNELING, level);
                    flag = true;
                }

                if (flag) {
                    maps.remove(enchantment);
                }
            }
        }
        return maps;
    }

    private static Enchantment getEnchantment(String name) {
        return ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("futuremc", name));
    }

}