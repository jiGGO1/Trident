package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentChanneling extends Enchantment {

    public EnchantmentChanneling(Enchantment.Rarity rarity, EntityEquipmentSlot... slots) {
        super(rarity, TridentEnchantments.TRIDENT, slots);
    }

    @Override
    public int getMinEnchantability(int level) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench);
    }

}