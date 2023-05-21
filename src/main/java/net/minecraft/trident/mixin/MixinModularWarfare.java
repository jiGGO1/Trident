package net.minecraft.trident.mixin;

import com.modularwarfare.ModularWarfare;
import net.minecraft.trident.compat.modularwarfare.ModularWarfareInit;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author ji_GGO
 * @date 2023/03/27
 */
@Mixin(ModularWarfare.class)
public class MixinModularWarfare {

    static {
        ModularWarfareInit.init();
    }

}