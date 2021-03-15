package net.minecraft.trident.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.trident.Trident;
import net.minecraft.trident.enchantment.TridentEnchantments;
import net.minecraft.trident.sound.TridentSounds;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityTrident extends EntityArrow {

    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> EFFECT = EntityDataManager.createKey(EntityTrident.class, DataSerializers.BOOLEAN);

    private ItemStack thrownStack = new ItemStack(Trident.TRIDENT);
    private boolean dealtDamage;
    public int returningTicks;

    public EntityTrident(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityTrident(World world, EntityLivingBase thrower, ItemStack thrownStackIn) {
        super(world, thrower);
        this.thrownStack = thrownStackIn.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte) TridentEnchantments.getLoyaltyModifier(thrownStackIn));
        this.dataManager.set(EFFECT, hasEffect());
        this.setSize(0.5F, 0.5F);
    }

    @SideOnly(Side.CLIENT)
    public EntityTrident(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.setSize(0.5F, 0.5F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOYALTY_LEVEL, (byte)0);
        this.dataManager.register(EFFECT, false);
    }

    @Override
    public void onUpdate() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.shootingEntity;
        if ((this.dealtDamage || this.getNoClip()) && entity != null) {
            int i = this.dataManager.get(LOYALTY_LEVEL);
            if (i > 0 && !this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }
                this.setDead();
            } else if (i > 0) {
                this.setNoClip(true);
                Vec3d vec3d = new Vec3d(entity.posX - this.posX, entity.posY + (double)entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
                this.posY += vec3d.y * 0.015D * (double)i;
                if (this.world.isRemote) {
                    this.lastTickPosY = this.posY;
                }
                vec3d = vec3d.normalize();
                double d0 = 0.05D * (double)i;
                this.motionX += vec3d.x * d0 - this.motionX * 0.05D;
                this.motionY += vec3d.y * d0 - this.motionY * 0.05D;
                this.motionZ += vec3d.z * d0 - this.motionZ * 0.05D;
                if (this.returningTicks == 0) {
                    this.playSound(TridentSounds.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }
        super.onUpdate();
    }

    @Override
    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @SideOnly(Side.CLIENT)
    public boolean effect() {
        return this.dataManager.get(EFFECT);
    }

    @Nullable
    @Override
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        return this.dealtDamage ? null : super.findEntityOnPath(start, end);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if (result.entityHit != null) {
            this.onHitEntity(result);
        } else {
            super.onHit(result);
        }
    }

    protected void onHitEntity(RayTraceResult result) {
        Entity entity = result.entityHit;
        float f = 8.0F;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, entitylivingbase.getCreatureAttribute());
            if (entity.isWet()) f += TridentEnchantments.getImpalingModifier(this.getArrowStack()) * 2.5;
        }
        Entity entity1 = this.shootingEntity;
        DamageSource damagesource = EntityTrident.causeTridentDamage(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent sound = TridentSounds.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(damagesource, f) && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            if (entity1 instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(living, entity1);
                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)entity1, living);
            }
            this.arrowHit(living);
        }
        this.motionX *= (double)-0.01F;
        this.motionY *= (double)-0.1F;
        this.motionZ *= (double)-0.01F;
        float f1 = 1.0F;
        if (this.world.isThundering() && TridentEnchantments.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.getPosition();
            if (this.world.canSeeSky(blockpos)) {
                EntityLightningBolt lightningBolt = new EntityLightningBolt(this.world,
                        blockpos.getX(), blockpos.getY(), blockpos.getZ(), false);
                this.world.addWeatherEffect(lightningBolt);
                this.addLightning(lightningBolt);
                sound = TridentSounds.ITEM_TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }
        this.playSound(sound, f1, 1.0F);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        Entity entity = this.getShootingEntity();
        if (entity == null || entity.getUniqueID() == entityIn.getUniqueID()) {
            if (!this.world.isRemote && (this.inGround || this.getNoClip()) && this.arrowShake <= 0) {
                boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode || this.getNoClip();
                if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
                    flag = false;
                }
                if (flag) {
                    entityIn.onItemPickup(this, 1);
                    this.setDead();
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        int i = compound.getTagId("Trident");
        if (i != 10) {
            this.thrownStack = new ItemStack((NBTTagCompound)compound.getTag("Trident"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.dataManager.set(LOYALTY_LEVEL, (byte)TridentEnchantments.getLoyaltyModifier(this.thrownStack));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound){
        super.writeEntityToNBT(compound);
        compound.setTag("Trident", this.thrownStack.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound == SoundEvents.ENTITY_ARROW_HIT) {
            super.playSound(this.getHitEntitySound(), volume, pitch);
        } else {
            super.playSound(sound, volume, pitch);
        }
    }

    protected SoundEvent getHitEntitySound() {
        return TridentSounds.ITEM_TRIDENT_HIT_GROUND;
    }

    public float getWaterDrag() {
        return 0.99F;
    }

    @Override
    protected int getFireImmuneTicks() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    public void addLightning(EntityLightningBolt lightning){
        if (this.shootingEntity instanceof EntityPlayer){
            Trident.LIGHTNING_BOLTS.clear();
            Trident.LIGHTNING_BOLTS.put(lightning, (EntityPlayer)shootingEntity);
        }
    }

    public boolean hasEffect(){
        return this.thrownStack.isItemEnchanted();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getShootingEntity();
        if (entity != null && entity.isEntityAlive()) {
            return !(entity instanceof EntityPlayerMP) || !((EntityPlayerMP)entity).isSpectator();
        } else {
            return false;
        }
    }

    @Nullable
    public Entity getShootingEntity() {
        return this.shootingEntity != null && this.world instanceof WorldServer ? ((WorldServer)this.world).getEntityFromUuid(this.shootingEntity.getUniqueID()) : null;
    }

    public static DamageSource causeTridentDamage(Entity source, @Nullable Entity indirectEntityIn) {
        return (new EntityDamageSourceIndirect("trident", source, indirectEntityIn)).setProjectile();
    }

    public void setNoClip(boolean noClip){
        this.noClip = noClip;
    }

    public boolean getNoClip(){
        return this.noClip;
    }

}