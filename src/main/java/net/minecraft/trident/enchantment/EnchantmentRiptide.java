package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentRiptide extends Enchantment {

   public EnchantmentRiptide(Enchantment.Rarity rarity, EntityEquipmentSlot... slots) {
      super(rarity, TridentEnchantments.TRIDENT, slots);
   }

   @Override
   public int getMinEnchantability(int enchantmentLevel) {
      return 10 + enchantmentLevel * 7;
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
      return super.canApplyTogether(ench) && ench != TridentEnchantments.LOYALTY && ench != TridentEnchantments.CHANNELING;
   }

}