package net.minecraft.trident.item;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.capabilities.CapabilityHandler;
import net.minecraft.trident.capabilities.ISpinAttackDuration;
import net.minecraft.trident.enchantment.TridentEnchantments;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.sound.TridentSounds;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2022/12/02
 */
public interface ITrident {

    default ModelResourceLocation getModel() {
        return Trident.MODEL;
    }

    default ModelResourceLocation getHandModel() {
        return Trident.HAND_MODEL;
    }

    @SideOnly(Side.CLIENT)
    default void render(ItemStack stack, ItemCameraTransforms.TransformType type) {

    }

    default void startSpinAttack(EntityPlayer player, int time){
        if(player.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = player.getCapability(CapabilityHandler.capability, null);
            trident.setSpinAttackDuration(time);
        }
        if (!player.world.isRemote) {
            EntityHelper.setLivingFlag(player, true);
        }
    }

    default ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItemDamage() >= itemstack.getMaxDamage() - 1) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        } else if (TridentEnchantments.getRiptideModifier(itemstack) > 0 && !this.isInWaterOrRain(player)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        } else {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    default EntityTrident getTrident(World world, EntityLivingBase thrower, ItemStack stack) {
        return new EntityTrident(world, thrower, stack);
    }

    default void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            int i = this.getItem().getMaxItemUseDuration(stack) - timeLeft;
            if (i >= 10) {
                int j = TridentEnchantments.getRiptideModifier(stack);
                if (j <= 0 || this.isInWaterOrRain(player)) {
                    if (!world.isRemote) {
                        stack.damageItem(1, player);
                        if (j == 0) {
                            EntityTrident trident = this.getTrident(world, player, stack);
                            trident.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
                            if (player.isCreative()) {
                                trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                            }
                            world.spawnEntity(trident);
                            world.playSound(null, trident.getPosition(), TridentSounds.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!player.isCreative()) {
                                player.inventory.deleteStack(stack);
                            }
                        }
                    }
                    if (j > 0) {
                        float f7 = player.rotationYaw;
                        float f = player.rotationPitch;
                        float f1 = -MathHelper.sin(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
                        float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F));
                        float f3 = MathHelper.cos(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
                        float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
                        f1 = f1 * (f5 / f4);
                        f2 = f2 * (f5 / f4);
                        f3 = f3 * (f5 / f4);
                        player.addVelocity((double) f1, (double) f2, (double) f3);
                        this.startSpinAttack(player, 20);
                        if (player.onGround) {
                            float f6 = 1.1999999F;
                            player.move(MoverType.SELF, 0.0D, (double) f6, 0.0D);
                        }
                        SoundEvent sound;
                        if (j >= 3) {
                            sound = TridentSounds.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (j == 2) {
                            sound = TridentSounds.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            sound = TridentSounds.ITEM_TRIDENT_RIPTIDE_1;
                        }

                        world.playSound(null, player.getPosition(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    default boolean isInWaterOrRain(EntityPlayer player) {
        if (player.isWet()) {
            return true;
        } else if (Trident.getRiptide().test(player)) {
            return true;
        } else {
            return false;
        }
    }

    default Item getItem() {
        return (Item) this;
    }

}