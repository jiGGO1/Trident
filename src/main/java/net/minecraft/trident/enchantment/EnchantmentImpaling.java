package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentImpaling extends Enchantment {

   public EnchantmentImpaling(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
      super(rarityIn, TridentEnchantments.TRIDENT, slots);
   }

   @Override
   public int getMinEnchantability(int enchantmentLevel) {
      return 1 + (enchantmentLevel - 1) * 8;
   }

   @Override
   public int getMaxEnchantability(int enchantmentLevel) {
      return this.getMinEnchantability(enchantmentLevel) + 20;
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

}