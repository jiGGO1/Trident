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

    protected ItemStack thrownStack = new ItemStack(Trident.TRIDENT);
    private boolean dealtDamage;
    public int returningTicks;

    public EntityTrident(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityTrident(World world, EntityLivingBase thrower, ItemStack thrownStack) {
        super(world, thrower);
        this.thrownStack = thrownStack.copy();
        this.dataManager.set(LOYALTY_LEVEL, (byte) TridentEnchantments.getLoyaltyModifier(thrownStack));
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
            int loyalty = this.dataManager.get(LOYALTY_LEVEL);
            if (loyalty > 0 && !this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }
                this.setDead();
            } else if (loyalty > 0) {
                this.setNoClip(true);
                Vec3d direction = new Vec3d(entity.posX - this.posX, entity.posY + (double) entity.getEyeHeight() - this.posY, entity.posZ - this.posZ);
                this.posY += direction.y * 0.015D * (double) loyalty;
                if (this.world.isRemote) {
                    this.lastTickPosY = this.posY;
                }
                direction = direction.normalize();
                double velocity = 0.05D * (double) loyalty;
                this.motionX += direction.x * velocity - this.motionX * 0.05D;
                this.motionY += direction.y * velocity - this.motionY * 0.05D;
                this.motionZ += direction.z * velocity - this.motionZ * 0.05D;
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
        float damage = (float) this.getDamage();
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            damage += EnchantmentHelper.getModifierForCreature(this.thrownStack, living.getCreatureAttribute());
            if (entity.isWet()) damage += TridentEnchantments.getImpalingModifier(this.getArrowStack()) * 2.5;
        }
        Entity shooter = this.shootingEntity;
        DamageSource source = EntityTrident.causeTridentDamage(this, shooter == null ? this : shooter);
        this.dealtDamage = true;
        SoundEvent sound = TridentSounds.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(source, damage) && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            if (shooter instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments(living, shooter);
                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) shooter, living);
            }
            this.arrowHit(living);
        }
        this.motionX *= (double) -0.01F;
        this.motionY *= (double) -0.1F;
        this.motionZ *= (double) -0.01F;
        float volume = 1.0F;
        if (this.world.isThundering() && TridentEnchantments.hasChanneling(this.thrownStack)) {
            BlockPos position = entity.getPosition();
            if (this.world.canSeeSky(position)) {
                EntityLightningBolt lightningBolt = new EntityLightningBolt(this.world,
                        position.getX(), position.getY(), position.getZ(), false);
                this.world.addWeatherEffect(lightningBolt);
                this.addLightning(lightningBolt);
                sound = TridentSounds.ITEM_TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }
        this.playSound(sound, volume, 1.0F);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        Entity entity = this.getShootingEntity();
        if (entity == null || entity.getUniqueID() == player.getUniqueID()) {
            if (!this.world.isRemote && (this.inGround || this.getNoClip()) && this.arrowShake <= 0) {
                boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && player.capabilities.isCreativeMode || this.getNoClip();
                if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !player.inventory.addItemStackToInventory(this.getArrowStack())) {
                    flag = false;
                }
                if (flag) {
                    player.onItemPickup(this, 1);
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

    public static DamageSource causeTridentDamage(Entity source, @Nullable Entity indirectEntity) {
        return new EntityDamageSourceIndirect("trident", source, indirectEntity).setProjectile();
    }

    public void setNoClip(boolean noClip){
        this.noClip = noClip;
    }

    public boolean getNoClip(){
        return this.noClip;
    }

    @Override
    public double getDamage() {
        return 8.0f;
    }

}