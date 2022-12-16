package net.minecraft.trident.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.trident.Trident;
import net.minecraft.trident.capabilities.CapabilityHandler;
import net.minecraft.trident.capabilities.ISpinAttackDuration;
import net.minecraft.trident.network.PacketSpinAttack;

/**
 * @author ji_GGO
 * @date 2021/03/08
 */
public final class EntityHelper {

    public static int getSpinAttackDuration(Entity entity) {
        if(entity.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = entity.getCapability(CapabilityHandler.capability, null);
            return trident.getSpinAttackDuration();
        }
        return 0;
    }

    public static void setSpinAttackDuration(Entity entity, int time) {
        if(entity.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = entity.getCapability(CapabilityHandler.capability, null);
            trident.setSpinAttackDuration(time);
        }
    }

    public static boolean getSpinAttack(Entity entity) {
        if(entity.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = entity.getCapability(CapabilityHandler.capability, null);
            return trident.getSpinAttack();
        }
        return false;
    }

    public static void setSpinAttack(Entity entity, boolean type) {
        if(entity.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = entity.getCapability(CapabilityHandler.capability, null);
            trident.setSpinAttack(type);
        }
    }

    public static void setLivingFlag(EntityPlayer player, boolean value) {
        Trident.NETWORK.sendTo(new PacketSpinAttack(player.getEntityId(), value), (EntityPlayerMP)player);
    }

    public static boolean isSpinAttacking(EntityPlayer player) {
        return EntityHelper.getSpinAttack(player);
    }

}