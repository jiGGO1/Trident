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
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItemDamage() >= stack.getMaxDamage() - 1) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        } else if (TridentEnchantments.getRiptideModifier(stack) > 0 && !this.isInWaterOrRain(player)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        } else {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
    }

    default EntityTrident getTrident(World world, EntityLivingBase thrower, ItemStack stack) {
        return new EntityTrident(world, thrower, stack);
    }

    default void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            int time = this.getItem().getMaxItemUseDuration(stack) - timeLeft;
            if (time >= 10) {
                int riptide = TridentEnchantments.getRiptideModifier(stack);
                if (riptide <= 0 || this.isInWaterOrRain(player)) {
                    if (!world.isRemote) {
                        stack.damageItem(1, player);
                        if (riptide == 0) {
                            EntityTrident trident = this.getTrident(world, player, stack);
                            trident.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F + (float) riptide * 0.5F, 1.0F);
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
                    if (riptide > 0) {
                        float yaw = player.rotationYaw;
                        float pitch = player.rotationPitch;
                        float motionX = -MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
                        float motionY = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
                        float motionZ = MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
                        float magnitude = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                        float power = 3.0F * ((1.0F + (float) riptide) / 4.0F);
                        motionX = motionX * (power / magnitude);
                        motionY = motionY * (power / magnitude);
                        motionZ = motionZ * (power / magnitude);
                        player.addVelocity((double) motionX, (double) motionY, (double) motionZ);
                        this.startSpinAttack(player, 20);
                        if (player.onGround) {
                            float riptideSpeed = 1.1999999F;
                            player.move(MoverType.SELF, 0.0D, (double) riptideSpeed, 0.0D);
                        }
                        SoundEvent sound;
                        if (riptide >= 3) {
                            sound = TridentSounds.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (riptide == 2) {
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

    default boolean canApplyTridentEnchantment() {
        return true;
    }

    default Item getItem() {
        return (Item) this;
    }

}