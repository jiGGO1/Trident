package net.minecraft.trident.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author ji_GGO
 * @date 2020/03/08
 */
public class CapabilityHandler {

    @CapabilityInject(ISpinAttackDuration.class)
    public static Capability<ISpinAttackDuration> capability;

    public static void setupCapabilities(){
        CapabilityManager.INSTANCE.register(ISpinAttackDuration.class, new CapabilityTrident.Storage(), CapabilityTrident.Implementation.class);
    }

}