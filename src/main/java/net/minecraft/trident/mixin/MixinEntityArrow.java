package net.minecraft.trident.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2021/03/12
 */
@Mixin(EntityArrow.class)
public abstract class MixinEntityArrow extends Entity {

    @Shadow
    private int xTile;
    @Shadow
    private int yTile;
    @Shadow
    private int zTile;
    @Shadow
    private Block inTile;
    @Shadow
    private int inData;
    @Shadow
    protected boolean inGround;
    @Shadow
    protected int timeInGround;
    @Shadow
    public int arrowShake;
    @Shadow
    public Entity shootingEntity;
    @Shadow
    private int ticksInGround;
    @Shadow
    private int ticksInAir;

    public MixinEntityArrow(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onUpdate()V", at = @At(value = "HEAD"), cancellable = true)
    public void onTrident(CallbackInfo call) {
        if (((Object)this) instanceof EntityTrident) {
            EntityTrident trident = (EntityTrident)(Object)this;
            super.onUpdate();
            boolean flag = trident.getNoClip();
            if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
                float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
                this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
                this.prevRotationYaw = this.rotationYaw;
                this.prevRotationPitch = this.rotationPitch;
            }
            BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            if (iblockstate.getMaterial() != Material.AIR && !flag) {
                AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);
                if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                    this.inGround = true;
                }
            }
            if (this.arrowShake > 0) {
                --this.arrowShake;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            if (this.inGround && !flag) {
                int j = block.getMetaFromState(iblockstate);
                if ((block != this.inTile || j != this.inData) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.06D))) {
                    this.inGround = false;
                    this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                    this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                    this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                    this.ticksInGround = 0;
                    this.ticksInAir = 0;
                } else {
                    ++this.ticksInGround;
                    if (this.ticksInGround >= 1200) {
                        this.setDead();
                    }
                }
                ++this.timeInGround;
            } else {
                this.timeInGround = 0;
                ++this.ticksInAir;
                Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
                Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
                RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
                vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
                vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
                if (raytraceresult != null) {
                    vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
                }
                Entity entity = this.findEntityOnPath(vec3d1, vec3d);
                if (entity != null) {
                    raytraceresult = new RayTraceResult(entity);
                }
                if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
                    EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;
                    if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                        raytraceresult = null;
                    }
                }
                if (raytraceresult != null && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.isAirBorne = true;
                }
                if (this.getIsCritical()) {
                    for (int k = 0; k < 4; ++k) {
                        this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) k / 4.0D, this.posY + this.motionY * (double) k / 4.0D, this.posZ + this.motionZ * (double) k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                    }
                }
                this.posX += this.motionX;
                this.posY += this.motionY;
                this.posZ += this.motionZ;
                float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                if (flag) {
                    this.rotationYaw = (float)(MathHelper.atan2(-this.motionX, -this.motionZ) * (double)(180F / (float)Math.PI));
                } else {
                    this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (double)(180F / (float)Math.PI));
                }
                for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                    ;
                }
                while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                    this.prevRotationPitch += 360.0F;
                }
                while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                    this.prevRotationYaw -= 360.0F;
                }
                while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                    this.prevRotationYaw += 360.0F;
                }
                this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
                this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
                float f1 = 0.99F;
                float f2 = 0.05F;
                if (this.isInWater()) {
                    for (int i = 0; i < 4; ++i) {
                        float f3 = 0.25F;
                        this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                    }
                    f1 = trident.getWaterDrag();
                }
                this.motionX *= (double) f1;
                this.motionY *= (double) f1;
                this.motionZ *= (double) f1;
                if (!this.hasNoGravity()) {
                    this.motionY -= 0.05F;
                }
                this.setPosition(this.posX, this.posY, this.posZ);
                this.doBlockCollisions();
            }
            call.cancel();
        }
    }

    @Shadow
    protected abstract void onHit(RayTraceResult result);

    @Shadow
    protected abstract Entity findEntityOnPath(Vec3d start, Vec3d end);

    @Shadow
    public abstract boolean getIsCritical();

}