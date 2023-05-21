package net.minecraft.trident.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author ji_GGO
 * @date 2021/03/06
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    public int getSpinAttackDuration() {
        return EntityHelper.getSpinAttackDuration(this);
    }

    public void setSpinAttackDuration(int time) {
        EntityHelper.setSpinAttackDuration(this, time);
    }

    @Inject(method = "onLivingUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;collideWithNearbyEntities()V"))
    private void livingTick(CallbackInfo info) {
        if ((Object)this instanceof EntityPlayer) {
            if (this.getSpinAttackDuration() > 0) {
                this.setSpinAttackDuration(this.getSpinAttackDuration() - 1);
                this.updateSpinAttack(this.getEntityBoundingBox(), this.getEntityBoundingBox());
            }
        }
    }

    protected void updateSpinAttack(AxisAlignedBB before, AxisAlignedBB after) {
        AxisAlignedBB box = before.union(after);
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, box);
        if (!entities.isEmpty()) {
            for (int i = 0; i < entities.size(); ++i) {
                Entity entity = entities.get(i);
                if (entity instanceof EntityLivingBase) {
                    this.spinAttack((EntityLivingBase) entity);
                    this.setSpinAttackDuration(0);
                    this.scale(-0.2D);
                    break;
                }
            }
        } else if (this.collidedHorizontally) {
            this.setSpinAttackDuration(0);
        }
        if (!this.world.isRemote && this.getSpinAttackDuration() <= 0) {
            this.setLivingFlag(false);
        }
    }

    public void scale(double factor){
        this.motionX = this.motionX * factor;
        this.motionY = this.motionY * factor;
        this.motionX = this.motionZ * factor;
    }

    protected void setLivingFlag(boolean value) {
        EntityHelper.setLivingFlag((EntityPlayer)((Object)this), value);
    }

    protected void spinAttack(EntityLivingBase target) {
        EntityPlayer player = (EntityPlayer)((Object)this);
        player.attackTargetEntityWithCurrentItem(target);
    }

}