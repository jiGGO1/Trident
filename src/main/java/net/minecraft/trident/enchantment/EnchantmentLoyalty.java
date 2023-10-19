package net.minecraft.trident.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
public class EnchantmentLoyalty extends Enchantment {

   public EnchantmentLoyalty(Enchantment.Rarity rarity, EntityEquipmentSlot... slots) {
      super(rarity, TridentEnchantments.TRIDENT, slots);
   }

   @Override
   public int getMinEnchantability(int level) {
      return 5 + level * 7;
   }

   @Override
   public int getMaxEnchantability(int level) {
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
