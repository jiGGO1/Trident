package net.minecraft.trident.mixin;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.trident.Trident;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2021/03/09
 */
@Mixin(EntityVillager.class)
public abstract class MixinEntityVillager extends EntityAgeable {

    public MixinEntityVillager(World world) {
        super(world);
    }

    @Inject(method = "onStruckByLightning(Lnet/minecraft/entity/effect/EntityLightningBolt;)V",
            at = @At(value = "HEAD"))
    private void trigger(EntityLightningBolt lightningBolt, CallbackInfo info){
        if (!this.world.isRemote && !this.isDead){
            if (Trident.LIGHTNING_BOLTS.containsKey(lightningBolt)) {
                Trident.LIGHTNING_TRIGGER.trigger((EntityPlayerMP)Trident.LIGHTNING_BOLTS.get(lightningBolt));
                Trident.LIGHTNING_BOLTS.clear();
            }
        }
    }

}