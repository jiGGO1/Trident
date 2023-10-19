package net.minecraft.trident.compat.oe;

import com.sirsquidly.oe.entity.EntityDrowned;
import com.sirsquidly.oe.entity.EntityPickled;
import com.sirsquidly.oe.entity.EntityTrident;
import com.sirsquidly.oe.init.OEItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.trident.Trident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author ji_GGO
 * @date 2023/08/31
 */
@Mod.EventBusSubscriber(modid = Trident.MODID)
public class OceanicExpanse {

    public static final String MODID = "oe";

    public static final ResourceLocation GUI = new ResourceLocation(MODID, "oe_trident");
    public static final ResourceLocation HAND = new ResourceLocation(MODID, "oe_trident_in_hand");
    public static final ModelResourceLocation MODEL = new ModelResourceLocation(GUI, "inventory");
    public static final ModelResourceLocation HAND_MODEL = new ModelResourceLocation(HAND, "inventory");

    @Method(modid = OceanicExpanse.MODID)
    public static void init() {
        OEItems.TRIDENT_ORIG = new ItemOeTrident();
    }

    @Method(modid = OceanicExpanse.MODID)
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(final ModelRegistryEvent event) {
        final Item trident = OEItems.TRIDENT_ORIG;
        ModelLoader.registerItemVariants(trident, trident.getRegistryName(), GUI);
        ModelLoader.registerItemVariants(trident, trident.getRegistryName(), HAND);

        RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderOeTrident::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDrowned.class, RenderOeDrowned::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPickled.class, RenderOePickled::new);
    }

    @SideOnly(Side.CLIENT)
    public static <E extends EntityLiving> void addVanillaLayerHeldItem(RenderLiving<?> render, List<LayerRenderer<E>> layers) {
        for (LayerRenderer<?> layer : layers) {
            if (layer instanceof LayerHeldItem) {
                layers.remove(layer);
                break;
            }
        }
        render.addLayer(new LayerHeldItem(render));
    }

}