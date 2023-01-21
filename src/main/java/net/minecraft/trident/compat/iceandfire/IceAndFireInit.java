package net.minecraft.trident.compat.iceandfire;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.trident.Trident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2022/11/26
 */
@Mod.EventBusSubscriber(modid = Trident.MODID)
public class IceAndFireInit {

    public static final String MODID = "iceandfire";

    public static final ResourceLocation GUI = new ResourceLocation(MODID, "tide_trident");
    public static final ResourceLocation HAND = new ResourceLocation(MODID, "tide_trident_in_hand");
    public static final ModelResourceLocation MODEL = new ModelResourceLocation(GUI, "inventory");
    public static final ModelResourceLocation HAND_MODEL = new ModelResourceLocation(HAND, "inventory");

    @Method(modid = IceAndFireInit.MODID)
    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(EntityEntryBuilder.create()
                .entity(EntityTideTrident.class)
                .id(new ResourceLocation(Trident.MODID, "tide_trident"), 1)
                .name("tide_trident")
                .tracker(32, 1, true)
                .build());
    }

    @Method(modid = IceAndFireInit.MODID)
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(final ModelRegistryEvent event) {
        final Item trident = IafItemRegistry.tide_trident;
        RenderingRegistry.registerEntityRenderingHandler(EntityTideTrident.class, RenderTideTrident::new);
        ModelLoader.registerItemVariants(trident, trident.getRegistryName(), HAND);
    }

}