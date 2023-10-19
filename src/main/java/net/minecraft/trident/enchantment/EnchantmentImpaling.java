package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentImpaling extends Enchantment {

   public EnchantmentImpaling(Enchantment.Rarity rarity, EntityEquipmentSlot... slots) {
      super(rarity, TridentEnchantments.TRIDENT, slots);
   }

   @Override
   public int getMinEnchantability(int level) {
      return 1 + (level - 1) * 8;
   }

   @Override
   public int getMaxEnchantability(int level) {
      return this.getMinEnchantability(level) + 20;
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

}