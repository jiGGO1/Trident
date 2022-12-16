package net.minecraft.trident.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.capabilities.CapabilityHandler;
import net.minecraft.trident.capabilities.ISpinAttackDuration;
import net.minecraft.trident.util.EntityHelper;
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

    default void startSpinAttack(EntityPlayer player, int time){
        if(player.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = player.getCapability(CapabilityHandler.capability, null);
            trident.setSpinAttackDuration(time);
        }
        if (!player.world.isRemote) {
            EntityHelper.setLivingFlag(player, true);
        }
    }

    @SideOnly(value = Side.CLIENT)
    default void render(ItemStack stack) {

    }

}