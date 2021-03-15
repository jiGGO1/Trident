package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentLoyalty extends Enchantment {

   public EnchantmentLoyalty(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
      super(rarityIn, TridentEnchantments.TRIDENT, slots);
   }

   @Override
   public int getMinEnchantability(int enchantmentLevel) {
      return 5 + enchantmentLevel * 7;
   }

   @Override
   public int getMaxEnchantability(int enchantmentLevel) {
      return 50;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   @Override
   public boolean canApplyTogether(Enchantment ench) {
      return super.canApplyTogether(ench);
   }

}
