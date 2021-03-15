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

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    public int getSpinAttackDuration() {
        return EntityHelper.getSpinAttackDuration(this);
    }

    public void setSpinAttackDuration(int time) {
        EntityHelper.setSpinAttackDuration(this, time);
    }

    @Inject(method = "onLivingUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;collideWithNearbyEntities()V"))
    public void livingTick(CallbackInfo call) {
        if ((Object)this instanceof EntityPlayer) {
            if (this.getSpinAttackDuration() > 0) {
                this.setSpinAttackDuration(this.getSpinAttackDuration()-1);
                this.updateSpinAttack(this.getEntityBoundingBox(), this.getEntityBoundingBox());
            }
        }
    }

    protected void updateSpinAttack(AxisAlignedBB aabb1, AxisAlignedBB aabb2) {
        AxisAlignedBB axisalignedbb = aabb1.union(aabb2);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); ++i) {
                Entity entity = list.get(i);
                if (entity instanceof EntityLivingBase) {
                    this.spinAttack((EntityLivingBase)entity);
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

    protected void spinAttack(EntityLivingBase entity) {
        EntityPlayer player = (EntityPlayer)((Object)this);
        player.attackTargetEntityWithCurrentItem(entity);
    }

}