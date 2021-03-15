package net.minecraft.trident.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.trident.Trident;
import net.minecraft.trident.capabilities.CapabilityHandler;
import net.minecraft.trident.capabilities.ISpinAttackDuration;
import net.minecraft.trident.enchantment.TridentEnchantments;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.sound.TridentSounds;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author ji_GGO
 * @date 2021/03/04
 */
public class ItemTrident extends Item {

    public ItemTrident() {
        this.setMaxDamage(250);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.addPropertyOverride(new ResourceLocation("throwing"), (stack, world, entity) -> {
            return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 2.0F : 0.0F;
        });
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return Trident.SPEAR;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if (i >= 10) {
                int j = TridentEnchantments.getRiptideModifier(stack);
                if (j <= 0 || player.isWet()) {
                    if (!world.isRemote) {
                        stack.damageItem(1, player);
                        if (j == 0) {
                            EntityTrident trident = new EntityTrident(world, player, stack);
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
                            player.move(MoverType.SELF, 0.0D, (double) 1.1999999F, 0.0D);
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

    public void startSpinAttack(EntityPlayer player, int time){
        if(player.hasCapability(CapabilityHandler.capability, null)) {
            ISpinAttackDuration trident = (ISpinAttackDuration)player.getCapability(CapabilityHandler.capability, null);
            trident.setSpinAttackDuration(time);
        }
        if (!player.world.isRemote) {
            EntityHelper.setLivingFlag(player, true);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItemDamage() >= itemstack.getMaxDamage() - 1) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        } else if (TridentEnchantments.getRiptideModifier(itemstack) > 0 && !player.isWet()) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        } else {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)8.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-2.9F, 0));
        }

        return multimap;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

}