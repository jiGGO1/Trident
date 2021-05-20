package net.minecraft.trident;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.trident.advancements.LightningTrigger;
import net.minecraft.trident.capabilities.CapabilityHandler;
import net.minecraft.trident.capabilities.CapabilityTrident;
import net.minecraft.trident.capabilities.ISpinAttackDuration;
import net.minecraft.trident.common.CommonProxy;
import net.minecraft.trident.config.TridentConfig;
import net.minecraft.trident.enchantment.TridentEnchantments;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.item.ItemTrident;
import net.minecraft.trident.network.PacketSpinAttack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @author ji_GGO
 * @date 2020/02/27
 */
@Mod(modid = Trident.MODID, name = Trident.NAME, version = Trident.VERSION,
    guiFactory = "net.minecraft.trident.config.TridentConfigFactory")
public class Trident {

    public static final String MODID = "trident";
    public static final String NAME = "Trident Mod";
    public static final String VERSION = "1.0.1";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @SidedProxy(clientSide = "net.minecraft.trident.client.ClientProxy",
            serverSide = "net.minecraft.trident.common.CommonProxy")
    public static CommonProxy proxy;

    public static final Item TRIDENT;
    public static final ResourceLocation GUI = new ResourceLocation(MODID, "trident_in_gui");
    public static final ModelResourceLocation MODEL = new ModelResourceLocation(GUI, "inventory");

    public static final EnumAction SPEAR = EnumHelper.addAction("SPEAR");

    public static final LightningTrigger LIGHTNING_TRIGGER = CriteriaTriggers.register(new LightningTrigger());
    public static final Map<EntityLightningBolt, EntityPlayer> LIGHTNING_BOLTS = Maps.newHashMap();

    static {
        TRIDENT = new ItemTrident().setRegistryName(MODID, "trident").setUnlocalizedName("minecraft.trident");
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(Trident.class);
        MinecraftForge.EVENT_BUS.register(this);
        CapabilityHandler.setupCapabilities();
        NETWORK.registerMessage(PacketSpinAttack.Handler.class, PacketSpinAttack.class, 0, Side.CLIENT);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        CreativeTabs combat = CreativeTabs.COMBAT;
        ArrayList<EnumEnchantmentType> types = Lists.newArrayList(combat.getRelevantEnchantmentTypes());
        types.add(TridentEnchantments.TRIDENT);
        combat.setRelevantEnchantmentTypes(types.toArray(new EnumEnchantmentType[types.size()]));
        proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event){
        proxy.postInit(event);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(TRIDENT);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(final ModelRegistryEvent event) {
        registerModel(TRIDENT);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        ModelLoader.registerItemVariants(item, item.getRegistryName(), GUI);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityTrident.class)
                .id(new ResourceLocation(MODID, "trident"), 1)
                .name("minecraft.trident")
                .tracker(32, 1, true)
                .build());
    }

    @SubscribeEvent
    public void onAttachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof EntityPlayer){
            ICapabilitySerializable<NBTTagCompound> provider = new CapabilityTrident.ProvidePlayer();
            event.addCapability(new ResourceLocation(MODID + ":trident"), provider);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(final PlayerEvent.Clone event){
        Capability<ISpinAttackDuration> capability = CapabilityHandler.capability;
        Capability.IStorage<ISpinAttackDuration> storage = capability.getStorage();
        if(event.getOriginal().hasCapability(capability, null) && event.getEntityPlayer().hasCapability(capability, null)){
            NBTBase nbt = storage.writeNBT(capability, event.getOriginal().getCapability(capability, null), null);
            storage.readNBT(capability, event.getEntityPlayer().getCapability(capability, null), null, nbt);
        }
    }

    @SubscribeEvent
    public void onTridentDamage(final LivingHurtEvent event){
        EntityLivingBase entity = event.getEntityLiving();
        if (!(event.getSource().getImmediateSource() instanceof EntityLivingBase)) return;
        EntityLivingBase attacker = (EntityLivingBase)event.getSource().getImmediateSource();
        if (entity == null || attacker == null || !entity.isWet()) return;
        ItemStack stack = attacker.getHeldItemMainhand();
        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemTrident) {
            int level = TridentEnchantments.getImpalingModifier(attacker);
            if (level > 0) {
                event.setAmount(event.getAmount() + (float)level * 2.5F);
            }
        }
    }

    @SubscribeEvent
    public void onZombieDrop(final LivingDropsEvent event){
        EntityLivingBase entity = event.getEntityLiving();
        if (TridentConfig.zombieDrop && !entity.world.isRemote && entity.isWet()) {
            if (entity instanceof EntityZombie) {
                Random random = entity.getRNG();
                ItemStack stack = null;
                if (entity.getHeldItemMainhand().getItem() == TRIDENT) {
                    stack = entity.getHeldItemMainhand().copy();
                } else if (entity.getHeldItemMainhand().isEmpty() &&
                        random.nextInt(100 + 1) <= TridentConfig.tridentDrop){
                    stack = new ItemStack(TRIDENT);
                }
                if (stack != null) {
                    stack.setItemDamage(random.nextInt(stack.getMaxDamage() + 1));
                    event.getDrops().add(entity.entityDropItem(stack, 0.0f));
                }
            }
        }
    }

    @SubscribeEvent
    public void onZombieSpawn(final LivingSpawnEvent event){
        World world = event.getWorld();
        EntityLivingBase entity = event.getEntityLiving();
        if (TridentConfig.tridentZombie && !entity.world.isRemote && world.isThundering() || world.isRaining() && entity.isWet()) {
            if (entity instanceof EntityZombie) {
                boolean flag = EntityList.getKey(entity).toString().equals("minecraft:zombie");
                if (flag && entity.getHeldItemMainhand().isEmpty()) {
                    Random random = entity.getRNG();
                    if ((double)random.nextFloat() < 0.005D) {
                        int i = random.nextInt(16);
                        if (i < 10) {
                            entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(TRIDENT));
                        }
                    }
                }
            }
        }
    }

}