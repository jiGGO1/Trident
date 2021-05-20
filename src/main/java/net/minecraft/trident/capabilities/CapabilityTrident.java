package net.minecraft.trident.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author ji_GGO
 * @date 2020/03/08
 */
public class CapabilityTrident {

    public static class Storage implements Capability.IStorage<ISpinAttackDuration>{

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ISpinAttackDuration> capability, ISpinAttackDuration instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("duration", instance.getSpinAttackDuration());
            compound.setBoolean("spin_attack", instance.getSpinAttack());
            return compound;
        }

        @Override
        public void readNBT(Capability<ISpinAttackDuration> capability, ISpinAttackDuration instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound =(NBTTagCompound) nbt;
            instance.setSpinAttackDuration(compound.getInteger("duration"));
            instance.setSpinAttack(compound.getBoolean("spin_attack"));
        }

    }

    public static class Implementation implements ISpinAttackDuration {

        Integer spinAttackDuration = 0;

        Boolean spinAttack = false;

        @Override
        public int getSpinAttackDuration() {
            return this.spinAttackDuration;
        }

        @Override
        public void setSpinAttackDuration(int time) {
            this.spinAttackDuration = time;
        }

        @Override
        public boolean getSpinAttack() {
            return this.spinAttack;
        }

        @Override
        public void setSpinAttack(boolean spinAttack) {
            this.spinAttack = spinAttack;
        }

    }

    public static class ProvidePlayer implements ICapabilitySerializable<NBTTagCompound>, ICapabilityProvider {

        private ISpinAttackDuration trident = new Implementation();

        private Capability.IStorage<ISpinAttackDuration> storage = CapabilityHandler.capability.getStorage();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return CapabilityHandler.capability.equals(capability);
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if(CapabilityHandler.capability.equals(capability)){
                return (T) trident;
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) storage.writeNBT(CapabilityHandler.capability, trident, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            storage.readNBT(CapabilityHandler.capability, trident, null, nbt);
        }

    }

}