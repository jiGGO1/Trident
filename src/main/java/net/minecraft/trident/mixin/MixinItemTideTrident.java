package net.minecraft.trident.mixin;

import com.github.alexthe666.iceandfire.item.ItemTideTrident;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.compat.iceandfire.EntityTideTrident;
import net.minecraft.trident.compat.iceandfire.IceAndFireInit;
import net.minecraft.trident.compat.iceandfire.RenderItemTideTrident;
import net.minecraft.trident.item.ITrident;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2022/11/26
 */
@Mixin(ItemTideTrident.class)
public class MixinItemTideTrident extends Item implements ITrident {

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(CallbackInfo info) {
        this.addPropertyOverride(new ResourceLocation("throwing"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 2.0F : 0.0F;
        });
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return !player.isCreative();
    }

    @Inject(method = "onPlayerStoppedUsing", at = @At(value = "HEAD"), cancellable = true)
    private void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft, CallbackInfo info) {
        ITrident.super.onPlayerStoppedUsing(stack, world, living, timeLeft);
        info.cancel();
    }

    @Override
    public EntityTrident getTrident(World world, EntityLivingBase thrower, ItemStack stack) {
        return new EntityTideTrident(world, thrower, stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return Trident.SPEAR;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        return ITrident.super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase living) {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, living);
        }
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 12.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.9F, 0));
        }

        return multimap;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public ModelResourceLocation getModel() {
        return IceAndFireInit.MODEL;
    }

    @Override
    public ModelResourceLocation getHandModel() {
        return IceAndFireInit.HAND_MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(ItemStack stack, ItemCameraTransforms.TransformType type) {
        RenderItemTideTrident.render(stack, type);
    }

}