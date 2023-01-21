package net.minecraft.trident.compat.iceandfire;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.world.World;

public class EntityTideTrident extends EntityTrident {

    public EntityTideTrident(World world) {
        super(world);
        this.setSize(0.85F, 0.5F);
        this.thrownStack = new ItemStack(IafItemRegistry.tide_trident);
    }

    public EntityTideTrident(World world, EntityLivingBase thrower, ItemStack thrownStackIn) {
        super(world, thrower, thrownStackIn);
        this.setSize(0.85F, 0.5F);
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.getItem() == Trident.TRIDENT ? new ItemStack(IafItemRegistry.tide_trident) : this.thrownStack.copy();
    }

    @Override
    public double getDamage() {
        return 12.0f;
    }

}