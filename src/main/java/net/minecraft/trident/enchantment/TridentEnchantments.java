package net.minecraft.trident.enchantment;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.item.ITrident;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
@Mod.EventBusSubscriber(modid = Trident.MODID)
public class TridentEnchantments {

    public static final EnumEnchantmentType TRIDENT = EnumHelper.addEnchantmentType("TRIDENT",
            (item -> item instanceof ITrident));

    public static final List<Enchantment> ENCHANTMENTS = Lists.newArrayList();

    public static final Enchantment LOYALTY;
    public static final Enchantment IMPALING;
    public static final Enchantment RIPTIDE;
    public static final Enchantment CHANNELING;

    static {
        LOYALTY = register("loyalty", new EnchantmentLoyalty(Enchantment.Rarity.UNCOMMON, EntityEquipmentSlot.MAINHAND));
        IMPALING = register("impaling", new EnchantmentImpaling(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        RIPTIDE = register("riptide", new EnchantmentRiptide(Enchantment.Rarity.RARE, EntityEquipmentSlot.MAINHAND));
        CHANNELING = register("channeling", new EnchantmentChanneling(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.MAINHAND));
    }

    @SubscribeEvent
    public static void onRegisterEnchantment(final RegistryEvent.Register<Enchantment> event) {
        ENCHANTMENTS.forEach(e -> event.getRegistry().register(e));
    }

    public static Enchantment register(String key, Enchantment enchantment){
        enchantment.setName("minecraft." + key);
        enchantment.setRegistryName(Trident.MODID, key);
        ENCHANTMENTS.add(enchantment);
        return enchantment;
    }

    public static int getLoyaltyModifier(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(LOYALTY, stack);
    }

    public static int getImpalingModifier(EntityLivingBase player) {
        return EnchantmentHelper.getMaxEnchantmentLevel(IMPALING, player);
    }

    public static int getImpalingModifier(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(IMPALING, stack);
    }

    public static int getRiptideModifier(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(RIPTIDE, stack);
    }

    public static boolean hasChanneling(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(CHANNELING, stack) > 0;
    }

}